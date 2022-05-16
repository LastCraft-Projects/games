package net.lastcraft.gameeffects.cosmetics.colorable;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.IBaseGamer;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.cosmetics.BaseCosmetic;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;
import java.util.List;

public final class ColorableCosmetic extends BaseCosmetic {

    private final DyeColor color;
    private final List<Pattern> patterns;

    public ColorableCosmetic(int id, ItemStack itemStack, Rarity rarity, DyeColor color, List<Pattern> patterns,
                             int minLevel, int rewardLevel,
                             PurchaseType purchaseType, int price, boolean shulkerFree) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree);
        this.color = color;
        this.patterns = patterns;
    }

    @Override
    public GameCosmeticType getType() {
        return GameCosmeticType.COLORABLE;
    }

    public final ItemStack getNewItem(ItemStack itemStack, IBaseGamer gamer) {
        ItemStack newItemStack = itemStack.clone();
        if (gamer == null) {
            return newItemStack;
        }

        List<String> lore = Collections.singletonList("§6Exclusive by §f" + gamer.getPrefix() + gamer.getName());
        ItemMeta meta = newItemStack.getItemMeta();
        switch (newItemStack.getType()) {
            case SHIELD:
                BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                blockStateMeta.setLore(lore);

                Banner banner = (Banner) blockStateMeta.getBlockState();
                banner.setBaseColor(color);
                banner.setPatterns(patterns);
                banner.update();

                blockStateMeta.setBlockState(banner);
                break;
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                armorMeta.setColor(color.getColor());
                armorMeta.setLore(lore);
                break;
        }
        newItemStack.setItemMeta(meta);

        return newItemStack;
    }
}
