package net.lastcraft.kitwarssolo;

import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.kitwarssolo.bonuses.Bonus;
import net.lastcraft.kitwarssolo.game.Game;
import net.lastcraft.kitwarssolo.game.GameFactory;
import net.lastcraft.kitwarssolo.kits.KitsMenu;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class KitWars extends JavaPlugin {

    private static KitWars instance;
    private List<Location> playerSpawns = new ArrayList<>();
    private List<Location> villagerSpawns = new ArrayList<>();

    public static KitWars getInstance(){
        return instance;
    }

    public List<Location> getPlayerSpawns(){
        return playerSpawns;
    }

    public List<Location> getVillagerSpawns(){
        return villagerSpawns;
    }

    public FileConfiguration getConfig(){
        return Game.config;
    }

    public void onLoad(){
        instance = this;
    }

    public void onEnable(){
        new Game();
        new GameFactory();
        new KitsMenu();

        createBonuses();

        CoreUtil.registerGame(CoreUtil.getGameWorld());
    }

    private void createBonuses(){
        new Bonus("Ослепление", false, PotionEffectType.BLINDNESS, 5, 1);
        new Bonus("Скорость", true, PotionEffectType.SPEED, 5, 1);
        new Bonus("Регенерация", true, PotionEffectType.REGENERATION, 5, 1);
        new Bonus("Огнестойкость", true, PotionEffectType.FIRE_RESISTANCE, 5, 1);
        new Bonus("Поглощение", true, PotionEffectType.DAMAGE_RESISTANCE, 5, 1);
        new Bonus("Прыгучесть", true, PotionEffectType.JUMP, 5, 1);
        new Bonus("Замедление", false, PotionEffectType.SLOW, 5, 1);
        new Bonus("Сила", true, PotionEffectType.INCREASE_DAMAGE, 5, 1);
        new Bonus("Моментальный урон", false, 1);
        new Bonus("Исцеление", true, 2);
        new Bonus("Слаймовый разряд", false, 3);
        new Bonus("Очищение", false, 4);
        new Bonus("Рандомный набор", true, 5);
        new Bonus("Зелье исцеления", true, 6);
        new Bonus("Смерть", false, 7);
    }
}
