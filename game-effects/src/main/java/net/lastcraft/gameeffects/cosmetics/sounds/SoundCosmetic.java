package net.lastcraft.gameeffects.cosmetics.sounds;

import lombok.Getter;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.cosmetics.BaseCosmetic;
import org.bukkit.inventory.ItemStack;

public abstract class SoundCosmetic extends BaseCosmetic {

    @Getter
    private final SoundType soundType;

    public SoundCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                         PurchaseType purchaseType, int price, boolean shulkerFree,
                         SoundType soundType) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree);

        this.soundType = soundType;
    }
}
