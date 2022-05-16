package net.lastcraft.eggwars.generator;

import net.lastcraft.dartaapi.utils.core.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private String cost;
    private ItemStack updateItem;
    private ItemStack currentItem;
    private int level;
    private int period;

    public LevelData(String cost, int level, int period, String lore, GeneratorData generatorData){
        this.cost = cost;
        this.level = level;
        this.period = period;

        ItemMeta updateItemMeta;
        List<String> updateItemLore = new ArrayList<>();
        if (level != 5){
            updateItem = new ItemStack(Material.ANVIL, level);
            updateItemMeta = updateItem.getItemMeta();
            updateItemMeta.setDisplayName("§aУлучшить до уровня " + level + " ↑");
            updateItemLore.add("§7Нажмите, чтобы улучшить");
            updateItemLore.add("§7генератор " + generatorData.getName() + " до§c уровня " + level);
            updateItemLore.add("");
            updateItemLore.add("§7Интервал спавна ресурсов");
            updateItemLore.add("§7будет §e" + period + " " +  StringUtil.getCorrectWord(period, "секунд", "а", "ы", ""));
            updateItemLore.add("");
            updateItemLore.add(lore);
        } else {
            updateItem = new ItemStack(Material.ANVIL);
            updateItemMeta = updateItem.getItemMeta();
            updateItemMeta.setDisplayName("§cМаксимальный уровень");
            updateItemLore.add("§7Генератор " + generatorData.getName() + " имеет");
            updateItemLore.add("§7максимальный уровень прокачки.");
        }
        updateItemMeta.setLore(updateItemLore);
        updateItem.setItemMeta(updateItemMeta);


        ItemMeta currentItemMeta;
        List<String> currentItemLore = new ArrayList<>();
        if (level != 0){
            currentItem = new ItemStack(generatorData.getItemSpawn().getType(), level);
            currentItemMeta = currentItem.getItemMeta();
            currentItemMeta.setDisplayName("§aГенератор " + generatorData.getName() + " - Уровень " + level);
            currentItemLore.add("§7Генератора " + generatorData.getName() + "§c уровнем " + level);
            currentItemLore.add("§7Интервал спавна ресурсов §e" + period + " " +  StringUtil.getCorrectWord(period, "секунд", "а", "ы", ""));
        } else {
            currentItem = new ItemStack(generatorData.getItemSpawn().getType());
            currentItemMeta = currentItem.getItemMeta();
            currentItemMeta.setDisplayName("§cГенератор " + generatorData.getName() + " - Сломан");
            currentItemLore.add("§7Генератор " + generatorData.getName() + " сломан,");
            currentItemLore.add("§7улучшите его, чтобы получать ресурсы.");

        }
        currentItemMeta.setLore(currentItemLore);
        currentItem.setItemMeta(currentItemMeta);
    }

    public int getLevel(){
        return level;
    }

    public int getPeriod(){
        return period;
    }

    public ItemStack getUpdateItem(){
        return updateItem;
    }

    public ItemStack getCurrentItem(){
        return currentItem;
    }

    public String getCost(){
        return cost;
    }
}