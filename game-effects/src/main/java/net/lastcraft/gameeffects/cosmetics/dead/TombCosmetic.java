package net.lastcraft.gameeffects.cosmetics.dead;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.cosmetics.BaseCosmetic;
import org.bukkit.inventory.ItemStack;

public class TombCosmetic extends BaseCosmetic {

    public TombCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                        PurchaseType purchaseType, int price, boolean shulkerFree) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree);
    }

    @Override
    public GameCosmeticType getType() {
        return null;
    }
}
