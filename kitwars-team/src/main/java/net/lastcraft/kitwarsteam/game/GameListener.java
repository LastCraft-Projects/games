package net.lastcraft.kitwarsteam.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.gamer.GamerInteractNPCEvent;
import net.lastcraft.api.event.gamer.async.AsyncGamerJoinEvent;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.kitwarsteam.KWPlayer;
import net.lastcraft.kitwarsteam.KWTeam;
import net.lastcraft.kitwarsteam.ShopNPC;
import net.lastcraft.kitwarsteam.bonuses.Bonus;
import net.lastcraft.kitwarsteam.kits.Kit;
import net.lastcraft.kitwarsteam.kits.KitsMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

public class GameListener extends DListener {

    private static SoundAPI soundAPI;

    @EventHandler
    public void onTramplin(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getLocation().subtract(0.0, 0.4, 0.0).getBlock().getType() == Material.SLIME_BLOCK) {
            player.setVelocity(player.getLocation().getDirection().multiply(0.7).setY(0.7));
            player.playSound(player.getLocation(), Sound.BLOCK_SLIME_STEP, 10.0f, 10.0f);
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (PlayerUtil.isAlive(player)){
            if (message.startsWith("!")){
                String suffix = GAMER_MANAGER.getGamer(player).getGroup().getSuffix();
                message = message.replaceFirst("!", "");
                e.setFormat(" §8[§6Всем§8] " + player.getDisplayName() + suffix + " %2$s");
                e.setMessage(message);
            } else {
                e.getRecipients().clear();
                KWTeam kwTeam = KWPlayer.getKWPlayer(player).getKwTeam();
                e.setFormat(" §8[" + kwTeam.getChatColor() + StringUtil.changeEnding(kwTeam.getName(), 'м') + "§8]" + e.getFormat());
                for (Player team : KWPlayer.getKWPlayer(player).getKwTeam().getPlayers()) {
                    e.getRecipients().add(team);
                }
            }
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        switch (block.getType()) {
            case HOPPER:
            case ANVIL:
                e.setCancelled(true);
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        ProjectileSource shooter;
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        KWPlayer damager = null;
        KWPlayer target = KWPlayer.getKWPlayer((Player) e.getEntity());
        if (e.getDamager() instanceof Player) {
            damager = KWPlayer.getKWPlayer((Player) e.getDamager());
        }
        if (e.getDamager() instanceof Projectile && (shooter = ((Projectile) e.getDamager()).getShooter()) != null && shooter instanceof Player) {
            damager = KWPlayer.getKWPlayer((Player) shooter);
        }
        if (damager == null || target == null) {
            return;
        }
        if (damager.getKwTeam() == target.getKwTeam()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(AsyncGamerJoinEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }
        if (PlayerUtil.isSpectator(player)) {
            for (ShopNPC shopNPC : ShopNPC.getShopsNPC()) {
                shopNPC.getNpc().showTo(player);
            }

            for (KWTeam team : KWTeam.getTeams().values()) {
                PlayerTag teamTag = LastCraft.getScoreBoardAPI().createTag("§c§l" + team.getShortName() + " §c");
                teamTag.setPrefix("§c§l" + team.getShortName() + " §c");
                teamTag.addPlayersToTeam(team.getPlayers());
                teamTag.sendTo(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent e){
        e.setCancelled(true);
        Player player = e.getPlayer();
        Item item = e.getItem();
        if (PlayerUtil.isAlive(player)){
            if (item.getItemStack().getType() == Material.SKULL_ITEM){
                if (item.getItemStack().getItemMeta() != null){
                    if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("Бонус")){
                        item.remove();
                        Bonus bonus = Bonus.getBonus();
                        bonus.giveBonus(player);
                        MessageUtil.broadcast(player.getDisplayName() + " §fподобрал бонус " + bonus.getName());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(GamerInteractNPCEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }
        if (PlayerUtil.isSpectator(player)) return;
        KitsMenu.openMenu(player);
    }

    @EventHandler
    public void onInventoryClickSelect(InventoryClickEvent e) {
        if (e.getInventory().getName().equals("Выбор набора")) {
            e.setCancelled(true);

            int slot = e.getRawSlot();
            Player player = (Player) e.getWhoClicked();
            HashMap<Integer, Kit> kits = KitsMenu.getKits();
            if (kits.containsKey(slot)) {
                if (kits.get(slot) == KWPlayer.getKWPlayer(player).getKit()) {
                    MessageUtil.sendMessage(player, "§cНабор " + kits.get(slot).getName() + " §cуже выбран!");
                    soundAPI.play(player, SoundType.NO);
                } else {
                    if (kits.get(slot).getEndKit()) {
                        if (GameTime.getGameTime() <= 180) {
                            KWPlayer.getKWPlayer(player).setKit(kits.get(slot));
                            soundAPI.play(player, SoundType.SELECTED);
                        } else {
                            MessageUtil.sendMessage(player, "§cНабор " + kits.get(slot).getName() + " §cбудет доступен после 4 минуты игры");
                            soundAPI.play(player, SoundType.NO);
                        }
                    } else {
                        KWPlayer.getKWPlayer(player).setKit(kits.get(slot));
                        soundAPI.play(player, SoundType.SELECTED);
                    }
                }

                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClickPlayer(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == player.getInventory()) {
            int slot = e.getRawSlot();
            if (slot == 8 || slot == 7 || slot == 6 || slot == 5) {
                e.setCancelled(true);
            }
        }
    }

    static {
        soundAPI = LastCraft.getSoundAPI();
    }
}
