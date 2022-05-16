package net.lastcraft.gameeffects.cosmetics.sounds.start;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.cosmetics.sounds.SoundCosmetic;
import org.bukkit.inventory.ItemStack;

public final class StartSoundCosmetic extends SoundCosmetic {

    public StartSoundCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                              PurchaseType purchaseType, int price, boolean shulkerFree,
                              SoundType soundType) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree, soundType);
    }

    @Override
    public GameCosmeticType getType() {
        return GameCosmeticType.START_SOUND;
    }
}
