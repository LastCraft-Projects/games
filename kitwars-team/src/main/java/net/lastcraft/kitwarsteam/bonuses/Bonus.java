package net.lastcraft.kitwarsteam.bonuses;

import net.lastcraft.api.LastCraft;
import net.lastcraft.base.SoundType;
import net.lastcraft.kitwarsteam.KWPlayer;
import net.lastcraft.kitwarsteam.kits.Kit;
import net.lastcraft.kitwarsteam.kits.KitsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bonus {

    private String title;
    private boolean type;

    private PotionEffectType effectType;
    private int time;
    private int level;

    private int id;

    private static List<Bonus> bonuses = new ArrayList<>();

    public static Bonus getBonus() {
        Random random = new Random();
        return bonuses.get(random.nextInt(bonuses.size()));
    }

    public Bonus(String title, boolean type, PotionEffectType effectType, int time, int level) {
        this.title = title;
        this.type = type;
        this.effectType = effectType;
        this.time = time;
        this.level = level;

        bonuses.add(this);
    }

    public Bonus(String title, boolean type, int id) {
        this.title = title;
        this.type = type;
        this.id = id;

        bonuses.add(this);
    }

    public String getName(){
        return (type ? "§a" : "§c") + title;
    }

    public void giveBonus(Player player) {
        LastCraft.getTitlesAPI().sendTitle(player, "§eБонус", (type ? "§a" : "§c") + title, 20, 2 * 20, 20);
        if (type) {
            LastCraft.getSoundAPI().play(player, SoundType.POSITIVE);
        } else {
            LastCraft.getSoundAPI().play(player, SoundType.NEGATIVE);
        }
        if (effectType != null) {
            player.addPotionEffect(new PotionEffect(effectType, time*20, level));
        } else {
            switch (id) {
                case 1:
                    player.damage(player.getMaxHealth()/3);
                    break;
                case 2:
                    player.setHealth(player.getMaxHealth());
                    break;
                case 3:
                    player.setVelocity(new Vector(0.0, 0.9, 0.0));
                    break;
                case 4:
                    for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                    break;
                case 5:
                    Random random = new Random();
                    List<Kit> kits = new ArrayList<>();
                    kits.addAll(KitsMenu.getKits().values());
                    KWPlayer.getKWPlayer(player).setKit(kits.get(random.nextInt(kits.size())));
                    break;
                case 6:
                    player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                    break;
                case 7:
                    player.damage(player.getMaxHealth());
                    break;
            }
        }
    }
}
