package net.lastcraft.luckywars.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.TypeGame;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.luckywars.LWTeam;
import net.lastcraft.luckywars.luckyblocks.Items;
import net.lastcraft.luckywars.luckyblocks.Mobs;
import net.lastcraft.luckywars.luckyblocks.PseudoRandom;
import net.lastcraft.luckywars.luckyblocks.Traps;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class GameListener extends DListener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        LWTeam playerTeam = LWTeam.getPlayerTeam(e.getPlayer());
        if (playerTeam != null)
            playerTeam.removePlayer(e.getPlayer());

        GameFactory.check();
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (PlayerUtil.isAlive(player)) {
            LWTeam ewTeam = LWTeam.getPlayerTeam(player);
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
                        for (Player team : ewTeam.getPlayersInTeam()) {
                            e.getRecipients().add(team);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        ProjectileSource shooter;
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        LWTeam damager = null;
        LWTeam target = LWTeam.getPlayerTeam((Player) e.getEntity());
        if (e.getDamager() instanceof Player) {
            damager = LWTeam.getPlayerTeam((Player) e.getDamager());
        }
        if (e.getDamager() instanceof Projectile && (shooter = ((Projectile) e.getDamager()).getShooter()) != null && shooter instanceof Player) {
            damager = LWTeam.getPlayerTeam((Player) shooter);
        }
        if (damager == null || target == null) {
            return;
        }
        if (damager == target) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType() == Material.SKULL || e.getEntity().getItemStack().getType() == Material.SKULL_ITEM) {
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.SKULL) {
            Player player = e.getPlayer();
            Block block = e.getBlock();
            GameFactory.getStats().addPlayerStats(player, "LuckyBlocks", 1);

            ParticleAPI particleAPI = LastCraft.getParticleAPI();
            particleAPI.launchInstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.YELLOW, Color.RED).build(), block.getLocation());
            switch (PseudoRandom.getChance(player)){
                case 0:
                    Items.send(block, true);
                    break;
                case 1:
                    Items.send(block, false);
                    break;
                case 2:
                    Mobs.send(player, block);
                    break;
                default:
                    Traps.send(player, block);
            }
        }
    }
}

