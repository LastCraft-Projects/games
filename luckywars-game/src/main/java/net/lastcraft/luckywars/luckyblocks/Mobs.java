package net.lastcraft.luckywars.luckyblocks;

import net.lastcraft.api.util.Head;
import net.lastcraft.luckywars.LuckyWars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class Mobs {

    public static void send(Player player, Block block) {
        switch (LuckyWars.getInstance().getRandom().nextInt(6)) {
            case 0:
                playerZombie(player, block.getLocation());
                break;
            case 1:
                skeleton(block.getLocation());
                break;
            case 2:
                zombie(block.getLocation());
                break;
            case 3:
                golem(player, block.getLocation());
                break;
            case 4:
                pigZombie(player, block.getLocation());
                break;
            case 5:
                witch(block.getLocation());
                break;
        }
    }

    private static void playerZombie(Player player, Location location) {
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setCustomName(player.getDisplayName());
        zombie.setCustomNameVisible(true);
        zombie.setBaby(false);
        zombie.getEquipment().setArmorContents(player.getEquipment().getArmorContents());
        zombie.getEquipment().setHelmet(Head.getHeadByPlayerName(player.getName()));
        zombie.getEquipment().setItemInHand(player.getItemInHand());
        zombie.setTarget(player);
    }


    private static void skeleton(Location location) {
        Spider spider = (Spider) location.getWorld().spawnEntity(location, EntityType.SPIDER);
        Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
        spider.setPassenger(skeleton);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 2);
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 3);
        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);

        skeleton.getEquipment().setItemInHand(bow);
        skeleton.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }

    private static void zombie(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setMaxHealth(25.0);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);

        zombie.getEquipment().setItemInHand(sword);
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }

    private static void golem(Player player, Location location) {
        IronGolem golem = (IronGolem) location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);
        golem.setTarget(player);
    }

    private static void pigZombie(Player player, Location location) {
        PigZombie pigZombie = (PigZombie) location.getWorld().spawnEntity(location, EntityType.PIG_ZOMBIE);
        pigZombie.setBaby(true);

        ItemStack sword = new ItemStack(Material.STICK);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        pigZombie.getEquipment().setItemInHand(sword);
        pigZombie.setTarget(player);
    }

    private static void witch(Location location) {
        location.getWorld().spawnEntity(location, EntityType.BAT);
        location.getWorld().spawnEntity(location, EntityType.BAT);
        location.getWorld().spawnEntity(location, EntityType.BAT);
        location.getWorld().spawnEntity(location, EntityType.WITCH);
        location.getWorld().spawnEntity(location, EntityType.BAT);
        location.getWorld().spawnEntity(location, EntityType.BAT);
        location.getWorld().spawnEntity(location, EntityType.BAT);
    }
}
