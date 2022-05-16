package net.lastcraft.eggwars.game;

import net.lastcraft.api.ActionBarAPI;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.gamer.GamerInteractCustomStandEvent;
import net.lastcraft.api.event.gamer.GamerInteractHologramEvent;
import net.lastcraft.api.event.gamer.GamerInteractNPCEvent;
import net.lastcraft.api.event.gamer.async.AsyncGamerJoinEvent;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.TypeGame;
import net.lastcraft.api.hologram.Hologram;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.EggWars;
import net.lastcraft.eggwars.generator.Generator;
import net.lastcraft.eggwars.shop.*;
import net.lastcraft.eggwars.upgrade.UpgradeMenu;
import net.lastcraft.eggwars.upgrade.UpgradeNPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameListener extends DListener {

    private final SoundAPI soundAPI = LastCraft.getSoundAPI();
    private final ActionBarAPI actionBarAPI = LastCraft.getActionBarAPI();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (PlayerUtil.isAlive(player)) {
            EWTeam ewTeam = EWTeam.getPlayerTeam(player);
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (ewTeam != null && gamer != null) {
                String suffix = gamer.getGroup().getSuffix();
                String prefix = gamer.getPrefix();
                if (GameSettings.typeGame != TypeGame.SOLO) {
                    if (e.getMessage().startsWith("!")) {
                        message = message.replaceFirst("!", "");
                        e.setFormat(" §8[" + ewTeam.getChatColor() + "Всем§8] §r" + prefix + player.getName() + suffix + " %2$s");
                        e.setMessage(message);
                    } else {
                        e.getRecipients().clear();
                        e.setFormat(" §8[" + ewTeam.getChatColor() + StringUtil.changeEnding(ewTeam.getName(), 'м') + "§8]" + e.getFormat());
                        for (Player team : ewTeam.getPlayers()) {
                            e.getRecipients().add(team);
                        }
                    }
                } else {
                    e.setFormat(" §8[" + ewTeam.getChatColor() + "Всем§8] " + e.getFormat());
                }
            }
        }
    }

    @EventHandler
    public void onShopInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        if (e.getInventory().getName().equals(ShopMenu.getInventory().getName())) {
            e.setCancelled(true);

            OtherMenu menu = OtherMenu.getOtherMenu(player, slot);
            if (menu != null) {
                menu.openInventory();
            }

            return;
        }

        Map<String, OtherMenuData> menus = OtherMenuData.getOtherMenuDatas();

        if (menus.containsKey(e.getInventory().getName())) {
            e.setCancelled(true);

            Map<Integer, ShopItemData> sid = menus.get(e.getInventory().getName()).getShopItems();

            if (sid.containsKey(slot)) {
                ShopItemData shopItemData = sid.get(slot);
                if (player.getInventory().contains(shopItemData.getCostMaterial(), shopItemData.getCostAmount())) {
                    EWTeam ewTeam = EWTeam.getPlayerTeam(player);
                    List<ItemStack> giveItems = new ArrayList<>();
                    for (ItemStack itemStack : shopItemData.getGiveItems()) {
                        ItemStack giveItem = new ItemStack(itemStack);
                        giveItem.setAmount(itemStack.getAmount());
                        if (itemStack.getType() == Material.WOOL) {
                            if (ewTeam != null) {
                                giveItem.setDurability(ewTeam.getSubID());
                            }
                        }
                        giveItems.add(giveItem);
                    }

                    for (ItemStack itemStack : giveItems) {
                        if (!InventoryUtil.canAdd(player.getInventory(), itemStack)) {
                            player.sendMessage("§cУ вас недостаточно места в инвенторе");
                            soundAPI.play(player, SoundType.NO);
                            return;
                        }
                    }

                    InventoryUtil.removeItemByMaterial(player.getInventory(), shopItemData.getCostMaterial(), shopItemData.getCostAmount());
                    for (ItemStack itemStack : giveItems) {
                        player.getInventory().addItem(itemStack);
                    }
                    OtherMenu.updateOtherMenu(player, e.getInventory().getName());
                } else {
                    MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
                    soundAPI.play(player, SoundType.NO);
                }
            }

            if (slot == 22) {
                ShopMenu.openInventory(player);
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.CRAFTING) {
            if (e.getClick() == ClickType.NUMBER_KEY){
                e.setCancelled(true);
                return;
            }
            ItemStack itemStack = e.getCurrentItem();
            if (itemStack != null) {
                Material item = itemStack.getType();
                if (item == Material.LEATHER_HELMET || item == Material.LEATHER_CHESTPLATE || item == Material.LEATHER_LEGGINGS || item == Material.LEATHER_BOOTS || item == Material.WOOD_PICKAXE) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        Material item = e.getItemDrop().getItemStack().getType();
        if (item == Material.LEATHER_HELMET || item == Material.LEATHER_CHESTPLATE || item == Material.LEATHER_LEGGINGS || item == Material.LEATHER_BOOTS || item == Material.WOOD_PICKAXE) {
            e.getItemDrop().remove();
            soundAPI.play(e.getPlayer(), SoundType.BREAK);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        ProjectileSource shooter;
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        EWTeam damager = null;
        EWTeam target = EWTeam.getPlayerTeam((Player) e.getEntity());
        if (e.getDamager() instanceof Player) {
            damager = EWTeam.getPlayerTeam((Player) e.getDamager());
        }
        if (e.getDamager() instanceof Projectile && (shooter = ((Projectile) e.getDamager()).getShooter()) != null && shooter instanceof Player) {
            damager = EWTeam.getPlayerTeam((Player) shooter);
        }
        if (damager == null || target == null) {
            return;
        }
        if (damager == target) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        EWTeam ewTeamByEgg = EWTeam.getEWTeamByEgg(e.getBlock().getLocation());
        if (ewTeamByEgg != null) {
            e.setCancelled(true);
            return;
        }
        e.getBlockPlaced().setMetadata("owner", new FixedMetadataValue(EggWars.getInstance(), e.getPlayer().getName()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.DRAGON_EGG) {
            Player player = e.getPlayer();
            EWTeam ewTeamByEgg = EWTeam.getEWTeamByEgg(e.getBlock().getLocation());
            EWTeam ewTeamBreaker = EWTeam.getPlayerTeam(player);

            if (ewTeamByEgg != null && ewTeamBreaker != null) {
                if (ewTeamByEgg == ewTeamBreaker) {
                    e.setCancelled(true);
                } else {
                    ewTeamByEgg.brokeEgg(player);
                    GameFactory.getStats().addPlayerStats(player, "Eggs", 1);
                    MessageUtil.broadcast("Яйцо команды " + StringUtil.changeEnding(ewTeamByEgg.getDisplayName(), 'х') + "§f было сломано игроком " + ewTeamBreaker.getChatColor() + player.getName());

                    BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
                    gamer.addMoneyLocal(4);
                    int egg = 25;
                    if (GameSettings.typeGame == TypeGame.DOUBLES) {
                        egg = 30;
                    }
                    if (GameSettings.typeGame == TypeGame.TEAM) {
                        egg = 35;
                    }
                    gamer.addExpLocal(egg);
                    double money = 4 * gamer.getMultiple();
                    actionBarAPI.sendBar(player, "§6+" + money + " " + StringUtil.getCorrectWord((int) money, "монет", "а", "ы", "") + " §a+" + egg + " XP");

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (ewTeamByEgg.playerInTeam(all)) {
                            LastCraft.getTitlesAPI().sendTitle(all, "§cВаше яйцо разрушено", "§7Вы не сможете возрождаться", 0, 3 * 20, 0);
                        }
                        soundAPI.play(player, SoundType.DESTROY);
                    }

                    GameFactory.check();
                }
                return;
            }
        }
        if (e.getBlock().getMetadata("owner").isEmpty()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(AsyncGamerJoinEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }


        EWTeam rejoinTeam = EWTeam.getPlayerTeam(player);

        if (rejoinTeam != null && rejoinTeam.isCanRespawn()) {
            LastCraft.getTitlesAPI().sendTitle(player, "§a", "§fВы перезашли в игру");
            BukkitUtil.runTaskLater(20L, () -> PlayerUtil.setRespawn(player, 5, () -> rejoinTeam.rejoin(player)));
            return;
        }

        if (PlayerUtil.isSpectator(player)) {
            for (ShopNPC shopNPC : ShopNPC.getShopsNPC()) {
                shopNPC.getNpc().showTo(player);
                shopNPC.getHologram().showTo(player);
            }
            for (UpgradeNPC upgradeNPC : UpgradeNPC.getUpgradeNPCs()) {
                upgradeNPC.getNpc().showTo(player);
                upgradeNPC.getHologram().showTo(player);
            }
            for (Generator generator : Generator.getGenerators()) {
                generator.getHologram().showTo(player);
            }
            for (EWTeam team : EWTeam.getTeams().values()) {
                PlayerTag enemyTags = LastCraft.getScoreBoardAPI().createTag(1 + "§c§l" + team.getShortName() + " §c");
                enemyTags.addPlayersToTeam(team.getPlayers());
                enemyTags.setPrefix(team.getChatColor().toString() + "§l" + team.getShortName() + " " + team.getChatColor().toString());
                enemyTags.sendTo(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        GameFactory.check();

        String uuid = e.getPlayer().getUniqueId().toString();

        Bukkit.getScheduler().runTaskLater(EggWars.getInstance(), () -> {
            try {
                File file = new File(Bukkit.getWorldContainer().getCanonicalFile().getPath() + "/lobby/playerdata/" + uuid + ".dat");
                file.delete();
            } catch (IOException io) {
                System.out.println("ERROR WHILE REMOVING DATA FILE...");
            }
        }, 10L);
    }

    @EventHandler
    public void onShopUse(GamerInteractNPCEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }
        if (PlayerUtil.isSpectator(player)) return;
        if (e.getAction() == GamerInteractNPCEvent.Action.LEFT_CLICK) return;
        for (ShopNPC shopNPC : ShopNPC.getShopsNPC() ) {
            if (e.getNpc() == shopNPC.getNpc()) {
                ShopMenu.openInventory(player);
                break;
            }
        }
        for (UpgradeNPC upgradeNPC : UpgradeNPC.getUpgradeNPCs() ) {
            if (e.getNpc() == upgradeNPC.getNpc()) {
                UpgradeMenu.getOrCreate(player).openInv();
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent e) {
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());
        if (block.getMetadata("owner").isEmpty()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        for (EWTeam team : EWTeam.getTeams().values()) {
            if (team.getSpawn().distance(event.getBlock().getLocation()) <= 3) {
                event.getPlayer().sendMessage("§cСтавить блоки рядом с местом респавна запрещено");
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onGaneratorClick(GamerInteractHologramEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }
        Hologram hologram = e.getHologram();
        if (PlayerUtil.isSpectator(player)) return;
        if (e.getAction() == GamerInteractCustomStandEvent.Action.LEFT_CLICK) return;
        for (Generator generator : Generator.getGenerators()) {
            if (generator.getHologram().equals(hologram)) {
                generator.openInventory(player);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());
        if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
            e.setCancelled(true);
        } else {
            block.setMetadata("owner", new FixedMetadataValue(EggWars.getInstance(), e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        List<Block> destroyed = e.blockList();
        destroyed.removeIf(block -> block.getMetadata("owner").isEmpty());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e){
        Material item = e.getEntity().getItemStack().getType();
        if (item == Material.LEATHER_HELMET || item == Material.LEATHER_CHESTPLATE || item == Material.LEATHER_LEGGINGS || item == Material.LEATHER_BOOTS || item == Material.WOOD_PICKAXE || item == Material.DRAGON_EGG) {
            e.getEntity().remove();
        }
    }
}
