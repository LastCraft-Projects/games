package net.lastcraft.eggwars.shop;

import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherMenuData {

    private String inventoryName;
    private String name;
    private ItemStack icon;
    private int slot;

    private static Map<String, OtherMenuData> otherMenuDatas = new HashMap<>();
    private Map<Integer, ShopItemData> shopItems = new HashMap<>();

    public static Map<String, OtherMenuData> getOtherMenuDatas(){
        return otherMenuDatas;
    }

    public OtherMenuData(String name, String icon, int slot){
        this.name = name;
        this.icon = ItemUtil.stringToItem(icon);
        this.slot = slot;
        this.inventoryName = "Магазин ▸ " + name;

        otherMenuDatas.put(inventoryName, this);
    }

    public void addShopItem(String name, String icon, int slot, List<String> lore, String cost, List<String> giveItems){
        shopItems.put(slot, new ShopItemData(name, icon, slot, lore, cost, giveItems));
    }

    public Map<Integer, ShopItemData> getShopItems(){
        return shopItems;
    }

    public String getInventoryName(){
        return inventoryName;
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
}
