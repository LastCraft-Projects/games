package net.lastcraft.eggwarslobby.shop;

import lombok.Getter;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.inventory.action.InventoryAction;
import net.lastcraft.api.inventory.type.DInventory;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwarslobby.shop.perks.PerkShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class MainShop
{
    private Player player;
    private DInventory inventory;

    private final PerkShop perkShop;
    
    public MainShop(Player player) {
        this.player = player;

        inventory = LastCraft.getInventoryAPI().createInventory(player, "магазин", 5);
        inventory.createInventoryAction(new InventoryAction() {
            @Override
            public void onOpen(Player player) {
                fillShopPages();
            }
        });

        perkShop = new PerkShop(player);
    }

    private void fillShopPages() {
        int perks = 0;
        for (Perk perk : Perk.perks) {
            perks += (perk.has(player) ? 1 : 0);
        }

        ItemStack perkItem = ItemUtil.getBuilder(Material.BLAZE_POWDER)
                                .setName("§aПерки")
                                .setLore(
                                        "§7Покупка различных возможностей,",
                                        "§7использовать которые вы сможете",
                                        "§7прямо в игре.",
                                        "",
                                        "§7§oЗа игру можно использовать",
                                        "§7§oтолько §c§o1 §7§oумение.",
                                        "",
                                        "§7Открыто: §a" + perks + "/" + Perk.perks.size() + " §6" + StringUtil.onPercent(perks, Perk.perks.size()) + "%",
                                        "",
                                        "§e▸ Открыть меню умений")
                                .build();

        inventory.setItem(22, new DItem(perkItem,
                (player, clickType, slot) -> perkShop.getShopPages().get(0).openInventory(player)));
    }
}
