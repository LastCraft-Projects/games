package net.lastcraft.luckywars.listener;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import lombok.RequiredArgsConstructor;
import net.lastcraft.api.LastCraft;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameapi.server.game.event.player.PlayerKillEvent;
import net.lastcraft.gameapi.users.GameUser;
import net.lastcraft.gameapi.users.GameUserRegistry;
import net.lastcraft.gameapi.utils.PlayerUtil;
import net.lastcraft.luckywars.converter.MaterialToDropConverter;
import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ActionExecutorQueue;
import net.lastcraft.luckywars.executor.ExecutorContext;
import net.lastcraft.luckywars.executor.SpinningBlockTask;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import net.lastcraft.packetlib.nms.util.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final LuckyWarsGame game;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        Entity damager = PlayerUtil.getLastDamager(player);

        if (damager == null || !damager.hasMetadata("damage_handler")) {
            return;
        }

        MetadataValue value = damager.getMetadata("damage_handler").iterator().next();

        ((Consumer<Player>) value.value()).accept(player);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeft(PlayerKillEvent event) {
        World world = event.getPlayer().getWorld();

        for (Creature creature : world.getEntitiesByClass(Creature.class)) {
            if (creature.getTarget() != null && Objects.equal(creature.getTarget(), event.getPlayer())) {
                creature.remove();
            }
        }

    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (!entity.hasMetadata("drops")) {
            return;
        }

        Collection<ItemStack> drops = (Collection<ItemStack>) entity.getMetadata("drops").iterator().next().value();

        event.getDrops().clear();
        event.getDrops().addAll(drops);

    }

    @EventHandler
    public void onLuckyBlockInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block.getType() != Material.SKULL) {
            return;
        }

        Skull skull = (Skull) block.getState();

        GameProfile profile = ReflectionUtils.getFieldValue(skull, "profile");

        if (profile == null) {
            return;
        }

        GameUser user = GameUserRegistry.getUser(event.getPlayer());

        ActionExecutorQueue queue = game.getActionExecutorQueue();
        ActionExecutor executor = queue.nextExecutor();

        block.setType(Material.AIR);

        Location location = block.getLocation().clone();

        SpinningBlockTask task = new SpinningBlockTask(location
                .add(location.getX() > 0 ? 0.5D : -0.5D, 1.5D,
                        location.getZ() > 0 ? 0.5D : 0.5D)
                .subtract(0, 1.5, 0),

                45);

        task.prepareEntities(profile);

        task.getPostListeners().add((dropLoc) -> {

            if (!event.getPlayer().isOnline() || game.isSpectator(event.getPlayer())) {
                return;
            }

            ExecutorContext context = queue.createContext(event.getPlayer());
            context.getLocals().put("drop_loc", dropLoc);
            context.getLocals().put("start_loc", task.getBlockLocation());

            user.addStat("LBBreaked", 1);

            try {
                BukkitUtil.runTask(() -> executor.execute(game, context));
            } catch (Throwable throwable) {
                event.getPlayer().sendMessage(game.getPrefix() + "Произошла ошибка открытия лакиблока, сообщите на форум §c(" + executor.toString() + ")");
                throwable.printStackTrace();
            }
        });

        task.runTaskTimerAsynchronously(game.getOwner(), 0L, 1);

        event.setCancelled(true);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();

        if (block.getType() == Material.SKULL) {
            return;
        }

        MaterialToDropConverter converter = game.converterOf(material);

        if (converter == null || !converter.predicate().test(event)) {
            return;
        }

        event.setDropItems(false);

        ItemStack item = converter.supplier().apply(block);

        Map<Integer, ItemStack> map = event.getPlayer().getInventory().addItem(item);

        if (map.containsValue(item)) {
            block.getLocation().getWorld().dropItem(block.getLocation(), item);
        }

        block.setType(Material.COBBLESTONE);

        LastCraft.getSoundAPI().play(event.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5f, 2.5f);
    }
}
