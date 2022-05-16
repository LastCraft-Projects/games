package net.lastcraft.gameeffects.cosmetics.killeffect;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.cosmetics.BaseCosmetic;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class KillEffectCosmetic extends BaseCosmetic {

    private final BiConsumer<Location, BukkitGamer> consumer;

    public KillEffectCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                              PurchaseType purchaseType, int price, boolean shulkerFree, BiConsumer<Location, BukkitGamer> consumer) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree);
        this.consumer = consumer;
    }

    public void apply(Location location, BukkitGamer gamer) {
        consumer.accept(location, gamer);
    }

    @Override
    public GameCosmeticType getType() {
        return GameCosmeticType.KILL_EFFECT;
    }
}
