package net.lastcraft.gameeffects.gui.guis;

import com.google.common.base.Strings;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.gamer.constans.Group;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class CategoryGui extends CosmeticGui {

    private static final ItemStack RED = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
            .setDurability((short) 8)
            .build();
    private static final ItemStack NOT_USE = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
            .setDurability((short) 15)
            .build();
    private static final ItemStack MONEY = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
            .setDurability((short) 1)
            .build();
    private static final ItemStack GOLD = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
            .setDurability((short) 4)
            .build();

    protected CategoryGui(Player player, Language lang, String key) {
        super(player, lang, false, lang.getMessage("GAME_COSMETIC_MAIN_GUI_NAME")
                + " ► " + lang.getMessage(key));
    }

    protected abstract GameCosmeticType getType();

    @Override
    protected void setItems(CosmeticPlayer cosmeticPlayer, Language language) {
        int slot = 10;
        int page = 0;

        Cosmetic active = cosmeticPlayer.getActiveCosmetics().get(getType());
        for (Cosmetic cosmetic : COSMETIC_MANAGER.getCosmetics(getType())) {
            boolean available = cosmeticPlayer.hasCosmetic(cosmetic);
            boolean enable =  active != null && active.getId() == cosmetic.getId();
            boolean canUse = !gamer.isPlayer() || gamer.getLevelNetwork() >= cosmetic.getMinLevel();

            multiInventory.setItem(page, slot++, new DItem(createItem(cosmetic, available, enable, canUse),
                    (clicker, clickType, i) -> {
                if (updateGui(cosmeticPlayer, gamer, cosmetic, available, enable, canUse)) {
                    setItems(cosmeticPlayer, language);
                }
            }));

            if ((slot - 8) % 9 == 0) {
                slot += 2;
            }

            if (slot >= 35) {
                slot = 10;
                page++;
            }
        }

        setItemBack();

        INVENTORY_API.pageButton(lang, page + 1, multiInventory, 38, 42);
    }

    private boolean updateGui(CosmeticPlayer cosmeticPlayer, BukkitGamer gamer, Cosmetic cosmetic,
                              boolean available, boolean enable, boolean canUse) {
        if (available) {
            MainGui mainGui = GUI_MANAGER.getGui(MainGui.class, gamer.getPlayer());
            if (enable) {
                gamer.playSound(SoundType.CLICK);
                cosmeticPlayer.disableCosmetic(cosmetic);
                if (mainGui != null) {
                    mainGui.setItems(cosmeticPlayer, lang);
                }
                return true;
            } else if (canUse) {
                gamer.playSound(SoundType.CLICK);
                cosmeticPlayer.enableCosmetic(cosmetic);
                if (mainGui != null) {
                    mainGui.setItems(cosmeticPlayer, lang);
                }
                return true;
            } else {
                gamer.sendMessageLocale("GAME_COSMETIC_NO_LEVEL", cosmetic.getMinLevel());
            }
        } else {
            if (gamer.changeMoney(cosmetic.getPurchaseType(), -cosmetic.getPrice())) {
                cosmeticPlayer.buyCosmetic(cosmetic);
                gamer.playSound(SoundType.LEVEL_UP);
                return true;
            }
        }
        gamer.playSound(SoundType.NO);
        return false;
    }

    private ItemStack createItem(Cosmetic cosmetic, boolean available, boolean enable, boolean canUse) {
        char color = (enable ? 'b' : (canUse && available ? 'a' : 'c'));
        ItemStack item = !available ? getNotAvailableItem(cosmetic) : (!canUse ? NOT_USE.clone() : cosmetic.getItemStack());

        Rarity rarity = cosmetic.getRarity();
        String rarityString = rarity != Rarity.NONE ? "§8" + lang.getMessage("RARITY") + ": "
                + rarity.getColor() + Strings.repeat("☆", rarity.ordinal()) : "§8"
                + rarity.getName(lang);

        List<String> lore = new ArrayList<>();
        if (available) {
            if (enable) {
                lore.addAll(lang.getList("SPECTATOR_SETTING_FLY_LORE"));
            } else if (canUse) {
                lore.addAll(lang.getList("SPECTATOR_SETTING_NOFLY_LORE"));
            } else {
                lore.add("");
                lore.addAll(lang.getList("GAME_COSMETIC_LEVEL_ERROR", String.valueOf(cosmetic.getMinLevel())));
            }
        } else {
            lore.add("");
            if (cosmetic.isShulkerFree() || cosmetic.getRarity() != Rarity.NONE || cosmetic.getRewardLevel() != 0) {
                lore.add("§7" + lang.getMessage("GADGETS_CAN_AVAILABLE"));
                if (cosmetic.isShulkerFree()) {
                    lore.add("§8• " + lang.getMessage("GADGETS_GIVE_DONATE_ONLY", Group.SHULKER.getNameEn()));
                }
                if (cosmetic.getRarity() != Rarity.NONE) {
                    lore.add("§8• " + lang.getMessage("GADGETS_GIVE_BOX", cosmetic.getRarity().getName(lang)));
                }
                if (cosmetic.getRewardLevel() != 0) {
                    lore.add("§8• " + lang.getMessage("GADGETS_GIVE_LEVEL", cosmetic.getRewardLevel()));
                }
                lore.add("");
            }
            if (cosmetic.getPurchaseType() == PurchaseType.MYSTERY_DUST) {
                lore.add(lang.getMessage("GADGETS_GIVE_MONEY", StringUtil.getNumberFormat(cosmetic.getPrice())));
                lore.add("");
            } else {
                lore.add(lang.getMessage("GADGETS_GIVE_GOLD", StringUtil.getNumberFormat(cosmetic.getPrice())));
                lore.add("");
            }
            lore.addAll(lang.getList("GAME_COSMETIC_LEVEL_ERROR", String.valueOf(cosmetic.getMinLevel())));
        }

        return ItemUtil.getBuilder(item)
                .setName("§" + color + cosmetic.getName(lang))
                .setLore(rarityString)
                .addLore("")
                .addLore(cosmetic.getLore(lang))
                .addLore(lore)
                .removeFlags()
                .glowing(enable)
                .build();
    }

    private ItemStack getNotAvailableItem(Cosmetic cosmetic) {
        if (cosmetic.getPurchaseType() == PurchaseType.GOLD
                && gamer.getMoney(PurchaseType.GOLD) >= cosmetic.getPrice()) {
            return GOLD;
        } else if (cosmetic.getPurchaseType() == PurchaseType.MYSTERY_DUST
                && gamer.getMoney(PurchaseType.MYSTERY_DUST) >= cosmetic.getPrice()) {
            return MONEY;
        }
        return RED;
    }


}
