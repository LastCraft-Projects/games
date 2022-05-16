package net.lastcraft.luckywars;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.luckywars.game.CreateGame;
import net.lastcraft.luckywars.game.GameFactory;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LuckyWars extends JavaPlugin{

    private static LuckyWars instance;
    private List<ItemStack> lowItems = new ArrayList<>();
    private List<ItemStack> highItems = new ArrayList<>();
    private List<Location> spawns = new ArrayList<>();
    private Random random = new Random();

    public List<ItemStack> getLowItems(){
        return lowItems;
    }

    public List<ItemStack> getHighItems(){
        return highItems;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public void setSpawns(List<Location> spawns) {
        this.spawns = spawns;
    }

    public Random getRandom() {
        return random;
    }

    public static LuckyWars getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        new CreateGame();
        loadItems();
        new GameFactory();
        CoreUtil.registerGame(CoreUtil.getGameWorld());
    }

    private void loadItems(){
        ConfigManager cmItems = new ConfigManager(new File("/home/lastcraft/create/" + GameSettings.channel + "/config/"), "items.yml");
        FileConfiguration configItems = cmItems.getConfig();
        lowItems.addAll(configItems.getStringList("Items.Low").stream().map(ItemUtil::stringToItem).collect(Collectors.toList()));
        highItems.addAll(configItems.getStringList("Items.High").stream().map(ItemUtil::stringToItem).collect(Collectors.toList()));
    }
}
