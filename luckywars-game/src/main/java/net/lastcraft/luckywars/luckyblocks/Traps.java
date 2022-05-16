package net.lastcraft.luckywars.luckyblocks;

import net.lastcraft.api.LastCraft;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.luckywars.LuckyWars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Traps {

    public static void send(Player player, Block block){
        new BukkitRunnable(){
            @Override
            public void run() {
                switch (LuckyWars.getInstance().getRandom().nextInt(6)){
                    case 0:
                        lavaCage(player);
                        break;
                    case 1:
                        waterCage(player);
                        break;
                    case 2:
                        TNTCage(player);
                        break;
                    case 3:
                        TNT(player);
                        break;
                    case 4:
                        slime(player);
                        break;
                    case 5:
                        damage(player);
                        break;
                }
            }
        }.runTaskLater(LuckyWars.getInstance(), 1);
    }

    private static void lavaCage(Player player){
        for (int y = -1; y<=2; y++){
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block blockAtLocation = player.getWorld().getBlockAt(player.getLocation().add(x, y, z));
                    if (y == -1){
                        blockAtLocation.setType(Material.SMOOTH_BRICK);
                    } else if (y == 0 || y == 1){
                        if (x != 0 || z != 0){
                            blockAtLocation.setType(Material.IRON_FENCE);
                        } else {
                            blockAtLocation.setType(Material.AIR);
                        }
                    } else {
                        if (x != 0 || z != 0){
                            blockAtLocation.setType(Material.IRON_FENCE);
                        } else {
                            blockAtLocation.setType(Material.LAVA);
                        }
                    }
                }
            }
        }
    }

    private static void waterCage(Player player) {
        for (int y = -1; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block blockAtLocation = player.getWorld().getBlockAt(player.getLocation().add(x, y, z));
                    if (y == -1) {
                        blockAtLocation.setType(Material.OBSIDIAN);
                    } else if (y == 0 || y == 1) {
                        if (x != 0 || z != 0) {
                            if (Math.abs(x) == 1 && z == 0 && y == 1 || x == 0 && y == 1){
                                blockAtLocation.setType(Material.THIN_GLASS);
                            } else {
                                blockAtLocation.setType(Material.OBSIDIAN);
                            }
                        } else {
                            blockAtLocation.setType(Material.WATER);
                        }
                    } else {
                        blockAtLocation.setType(Material.OBSIDIAN);
                    }
                }
            }
        }
    }

    private static void TNTCage(Player player) {
        for (int y = -1; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block blockAtLocation = player.getWorld().getBlockAt(player.getLocation().add(x, y, z));
                    if (y == -1) {
                        blockAtLocation.setType(Material.RED_SANDSTONE);
                    } else {
                        if (x != 0 || z != 0) {
                            if (Math.abs(x) == 1 && z == 0 && (y == 1 || y == 2) || x == 0 && (y == 1 || y == 2)) {
                                blockAtLocation.setType(Material.STONE_SLAB2);
                                blockAtLocation.setData((byte) 8);
                            } else {
                                blockAtLocation.setType(Material.RED_SANDSTONE);
                            }
                        } else {
                            blockAtLocation.setType(Material.AIR);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            Location location = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ() + 0.5);
            TNTPrimed tnt = player.getWorld().spawn(location, TNTPrimed.class);
            tnt.setVelocity(new Vector());
        }
    }

    private static void TNT(Player player) {
        for (int y = -1; y >= -4; y--) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block blockAtLocation = player.getWorld().getBlockAt(player.getLocation().add(x, y, z));
                    if (y == -1) {
                        if (x == 0 && z == 0){
                            blockAtLocation.setType(Material.STONE_PLATE);
                        } else {
                            blockAtLocation.setType(Material.AIR);
                        }
                    } else if (y == -2 || y == -4) {
                        blockAtLocation.setType(Material.SMOOTH_BRICK);
                    } else {
                        blockAtLocation.setType(Material.TNT);
                    }
                }
            }
        }
    }

    private static void slime(Player player){
        player.setVelocity(new Vector(0.0, 1.8, 0.0));
        LastCraft.getSoundAPI().play(player, SoundType.SLIME);
    }

    private static void damage(Player player){
        player.damage(player.getMaxHealth()/2);
        PlayerUtil.addPotionEffect(player, PotionEffectType.BLINDNESS, 1, 5);
        PlayerUtil.addPotionEffect(player, PotionEffectType.SLOW, 1, 5);
        LastCraft.getSoundAPI().play(player, SoundType.NEGATIVE);
    }
}
