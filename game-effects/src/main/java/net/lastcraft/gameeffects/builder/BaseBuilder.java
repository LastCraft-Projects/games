package net.lastcraft.gameeffects.builder;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.manager.CosmeticManager;
import net.lastcraft.gameeffects.lobby.CosmeticBox;
import net.lastcraft.gameeffects.lobby.CosmeticLevelReward;
import org.bukkit.inventory.ItemStack;

public abstract class BaseBuilder<T extends Cosmetic> {

    private static final CosmeticManager COSMETIC_MANAGER = GameEffectsAPI.getCosmeticManager();

    protected final int id;
    protected final ItemStack item;

    protected BaseBuilder(int id, ItemStack item){
        this.id = id;
        this.item = item;
    }

    protected Rarity rarity = Rarity.NONE;
    protected int minLevel = 15;
    protected int rewardLevel = 0;
    protected PurchaseType purchaseType = PurchaseType.MYSTERY_DUST;
    protected int price = 10000000;
    protected boolean shulkerFree = false;

    public BaseBuilder setMinLevel(int minLevel) {
        this.minLevel = minLevel;
        return this;
    }

    public BaseBuilder setRewardLevel(int rewardLevel) {
        this.rewardLevel = rewardLevel;
        return this;
    }

    public BaseBuilder setPurchaseType(PurchaseType purchaseType, int price) {
        this.purchaseType = purchaseType;
        this.price = price;
        return this;
    }

    public BaseBuilder setRarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public BaseBuilder setShulkerFree(boolean shulkerFree) {
        this.shulkerFree = shulkerFree;
        return this;
    }

    protected abstract T init();

    public void build() {
        Cosmetic cosmetic = init();

        if (cosmetic == null) {
            throw new NullPointerException("cosmetic не может быть null");
        }

        if (LastCraft.isLobby() || LastCraft.isHub()) {
            if (cosmetic.getRarity() != Rarity.NONE) {
                new CosmeticBox(cosmetic);
            }

            if (rewardLevel != 0) {
                new CosmeticLevelReward(rewardLevel, cosmetic);
            }
        }


        COSMETIC_MANAGER.addCosmetic(cosmetic);
    }
}
