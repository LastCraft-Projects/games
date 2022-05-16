package net.lastcraft.eggwars.shop;

import net.lastcraft.api.util.Head;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherMenu {

    private Player player;
    private Inventory inventory;
    private OtherMenuData otherMenuData;

    private static HashMap<Player, Map<Integer, OtherMenu>> otherMenus = new HashMap<>();

    public static OtherMenu getOtherMenu(Player player, int slot){
        return otherMenus.get(player).get(slot);
    }

    public static void updateOtherMenu(Player player, String name) {
        for (OtherMenu menu : otherMenus.get(player).values()) {
            if (menu.getInventory().getName().equals(name)) {
                menu.updateInventory();
                break;
            }
        }
    }

    public static void resetOtherMenus(){
        otherMenus.clear();
    }

    public OtherMenu(Player player){

        HashMap<Integer, OtherMenu> menus = new HashMap<>();
        for (OtherMenuData omd : OtherMenuData.getOtherMenuDatas().values()){
            menus.put(omd.getSlot(), new OtherMenu(player, omd));
        }

        otherMenus.put(player, menus);
    }

    private OtherMenu(Player player, OtherMenuData otherMenuData){
        this.player = player;
        this.otherMenuData = otherMenuData;

        inventory = Bukkit.createInventory(player, 27, otherMenuData.getInventoryName());
        inventory.setItem(22, ItemUtil.setItemMeta(Head.BACK.getHead(), "§cНазад"));
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void openInventory(){
        updateInventory();
        player.openInventory(inventory);
    }

    public void updateInventory(){
        EWTeam ewTeam = EWTeam.getPlayerTeam(player);
        for (Map.Entry<Integer, ShopItemData> sid : otherMenuData.getShopItems().entrySet()){

            boolean canBuy = player.getInventory().contains(sid.getValue().getCostMaterial(), sid.getValue().getCostAmount());

            ItemStack icon = sid.getValue().getIcon();
            String name = (canBuy ? "§a" : "§c") + sid.getValue().getName();
            List<String> lore = new ArrayList<>();
            lore.addAll(sid.getValue().getLore());
            lore.add("");
            if (canBuy){
                lore.add("§aНажмите, чтобы купить");
            } else {
                lore.add("§cУ вас недостаточно ресурсов");
            }

            if (icon.getType() == Material.WOOL){
                icon.setDurability(ewTeam.getSubID());
            }

            inventory.setItem(sid.getKey(), ItemUtil.setItemMeta(icon, name, lore));

        }
    }
}
