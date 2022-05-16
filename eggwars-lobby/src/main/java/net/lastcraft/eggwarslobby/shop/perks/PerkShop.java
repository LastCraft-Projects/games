package net.lastcraft.eggwarslobby.shop.perks;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.inventory.action.InventoryAction;
import net.lastcraft.api.inventory.type.DInventory;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.Cooldown;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.guis.CustomItems;
import net.lastcraft.dartaapi.guis.shop.ShopInventory;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwarslobby.listeners.ShopListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PerkShop extends ShopInventory
{
    public PerkShop(Player player) {
        super(player);

        this.player = player;

        DInventory dInventory = LastCraft.getInventoryAPI().createInventory(player, "Магазин ▸ Перки", 5);
        dInventory.createInventoryAction(new InventoryAction() {
            @Override
            public void onOpen(Player player) {
                fillShopPages(0);
            }
        });

        shopPages.add(dInventory);

        for (int i = 0; i < (Perk.perks.size() + 20) / 21 - 1; ++i) {
            int curI = i + 1;
            dInventory = LastCraft.getInventoryAPI().createInventory(player, "Магазин ▸ Перки | " + (i + 2), 5);
            dInventory.createInventoryAction(new InventoryAction() {
                @Override
                public void onOpen(Player player) {
                    fillShopPages(curI);
                }
            });

            shopPages.add(dInventory);
        }

        inventoryAPI.pageButton(Language.RUSSIAN, shopPages.size(), shopPages, 38, 42);
    }
    
    protected Material getTrigger() {
        return null;
    }
    
    public void fillShopPages(final int page) {
        int curPerk = 21 * page;

        for (int i = 1; i <= 3; ++i) {
            for (int j = 1; j <= 7 && curPerk < Perk.perks.size(); ++j) {
                PerkItem perkToSet = PerkItem.getPerkItems().get(curPerk++);
                ItemStack itemToSet = perkToSet.getPerk().getItem(player);

                List<String> lore = itemToSet.getItemMeta().getLore();

                if (!perkToSet.getPerk().has(player)) {
                    itemToSet = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);

                    if (perkToSet.getPerk().getRarity() != Rarity.NONE) {
                        lore.addAll(Arrays.asList("", "§7Можно получить:", "§8• §7Выпадает из §bСундука Удачи §7(" + perkToSet.getPerk().getRarity().getName(Language.RUSSIAN) + "§7)"));
                    }
                    if (perkToSet.getPerk().getPrice() >= 1) {
                        lore.addAll(Arrays.asList("", "§7Цена: §6" + StringUtil.getNumberFormat(perkToSet.getPerk().getPrice()) + " §7" + StringUtil.getCorrectWord(perkToSet.getPerk().getPrice(), "монет", "а", "s", "")));
                    }

                    lore.addAll(Arrays.asList("", perkToSet.canBuy(this.player) ? "§eНажмите, чтобы купить умение" : "§cДанное умение вам недоступно"));
                }
                else {
                    lore.addAll(Arrays.asList("", "§eКуплено"));
                }

                itemToSet = ItemUtil.setItemMeta(itemToSet, (perkToSet.getPerk().has(this.player) ? "§e" : "§c") + perkToSet.getPerk().getName(), lore);

                shopPages.get(page).setItem(i * 9 + j, new DItem(itemToSet, (player, clickType, slot) -> {
                    if (Cooldown.hasCooldown(player.getName())) {
                        return;
                    }

                    Cooldown.addCooldown(player.getName(), 20L);

                    if (!perkToSet.have(player)) {
                        if (perkToSet.canBuy(player)) {
                            perkToSet.giveToPlayer(player, true);
                            List<String> loreList = perkToSet.getPerk().getItem(player).getItemMeta().getLore();
                            loreList.addAll(Arrays.asList("", "§eКуплено"));

                            DInventory dInventory = this.shopPages.get(page);
                            ItemStack itemStack = ItemUtil.setItemMeta(perkToSet.getPerk().getItem(player), "§e" + perkToSet.getPerk().getName(), loreList);
                            DItem dItem = dInventory.getItems().get(slot);
                            dItem.setItem(itemStack);
                            dInventory.setItem(slot, dItem);

                            soundAPI.play(player, SoundType.SELECTED);
                        }
                        else {
                            soundAPI.play(player, SoundType.NO);
                        }
                    }
                    else {
                        player.sendMessage(Perk.getPrefix() + "§eВы выбираете умения во время игры!");
                        player.closeInventory();
                    }
                }));
            }
        }
        shopPages.get(page).setItem(40, new DItem(CustomItems.getBack(Language.RUSSIAN), (player, clickType, slot) -> {
            DInventory dInventory = ShopListener.SHOPS.get(player.getName()).getInventory();
            dInventory.openInventory(player);

            soundAPI.play(player, SoundType.PICKUP);
        }));
    }
}