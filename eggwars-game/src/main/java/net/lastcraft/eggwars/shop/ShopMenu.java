package net.lastcraft.eggwars.shop;

import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopMenu {

    private static Inventory inventory;

    public ShopMenu(){
        inventory = Bukkit.createInventory(null, 27, "Магазин");
        for (OtherMenuData omd : OtherMenuData.getOtherMenuDatas().values()){
            List<String> lore = new ArrayList<>();
            lore.add("§8Доступно:");
            lore.addAll(omd.getShopItems().values().stream().map(sid -> " §8▪ §7" + sid.getName()).collect(Collectors.toList()));
            lore.add("");
            lore.add("§e▸ Нажмите, чтобы открыть меню");
            inventory.setItem(omd.getSlot(), ItemUtil.setItemMeta(omd.getIcon(), "§b" + omd.getName(), lore));
        }
    }

    public static Inventory getInventory(){
        return inventory;
    }

    public static void openInventory(Player player){
        player.openInventory(inventory);
    }
}
