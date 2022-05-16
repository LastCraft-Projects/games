package net.lastcraft.eggwars.shop;

import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopItemData {

    private String name;
    private ItemStack icon;
    private int slot;
    private List<String> lore;
    private Material costMaterial;
    private int costAmount;
    private List<ItemStack> giveItems;

    public ShopItemData(String name, String icon, int slot, List<String> lore, String cost, List<String> giveItems){
        this.name = name;
        this.icon = ItemUtil.stringToItem(icon);
        this.slot = slot;
        this.lore = lore;

        String[] costArray = cost.split(":");
        this.costMaterial = Material.getMaterial(costArray[0]);
        this.costAmount = Integer.parseInt(costArray[1]);

        this.giveItems = new ArrayList<>();
        this.giveItems.addAll(giveItems.stream().map(ItemUtil::stringToItem).collect(Collectors.toList()));
    }

    public String getName(){
        return name;
    }

    public ItemStack getIcon(){
        return icon;
    }

    public int getSlot(){
        return slot;
    }

    public List<String> getLore(){
        return lore;
    }

    public Material getCostMaterial(){
        return costMaterial;
    }

    public int getCostAmount(){
        return costAmount;
    }

    public List<ItemStack> getGiveItems(){
        return giveItems;
    }
}
