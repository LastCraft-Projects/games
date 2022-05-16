package net.lastcraft.gameeffects.builder;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.gameeffects.cosmetics.killeffect.KillEffectCosmetic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class KillEffectBuilder extends BaseBuilder<KillEffectCosmetic> {

    private final BiConsumer<Location, BukkitGamer> consumer;

    public KillEffectBuilder(int id, Material material, BiConsumer<Location, BukkitGamer> consumer) {
        this(id, new ItemStack(material), consumer);
    }

    public KillEffectBuilder(int id, ItemStack item, BiConsumer<Location, BukkitGamer> consumer) {
        super(id, item);
        this.consumer = consumer;
    }

    @Override
    protected KillEffectCosmetic init() {
        return new KillEffectCosmetic(id, item, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree, consumer);
    }
}
