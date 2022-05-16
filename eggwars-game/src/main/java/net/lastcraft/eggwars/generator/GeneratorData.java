package net.lastcraft.eggwars.generator;

import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GeneratorData {

    private String name;
    private ChatColor color;
    private ItemStack itemSpawn;

    private static HashMap<Material, GeneratorData> generators = new HashMap<>();
    private HashMap<Integer, LevelData> levels = new HashMap<>();

    static GeneratorData getGeneratorData(Material material){
        return generators.get(material);
    }

    LevelData getLevelData(int level){
        return levels.get(level);
    }

    ItemStack getItemSpawn(){
        return itemSpawn;
    }

    public GeneratorData(Material type, Material itemSpawn, String itemSpawnName, String name, String color) {
        this.itemSpawn = ItemUtil.createItemStack(itemSpawn, itemSpawnName);
        this.name = name;
        this.color = ChatColor.getByChar(color);

        generators.put(type, this);
    }

    public void addLevelData(LevelData levelData){
        levels.put(levelData.getLevel(), levelData);
    }

    ChatColor getColor(){
        return color;
    }

    public String getName(){
        return name;
    }

}
