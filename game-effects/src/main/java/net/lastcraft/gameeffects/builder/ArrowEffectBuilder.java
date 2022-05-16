package net.lastcraft.gameeffects.builder;

import net.lastcraft.api.effect.ParticleEffect;
import net.lastcraft.gameeffects.cosmetics.arrow.ArrowEffectCosmetic;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArrowEffectBuilder extends BaseBuilder<ArrowEffectCosmetic> {

    private ParticleEffect effect = ParticleEffect.REDSTONE;
    private int delay = 2;
    private int period = 2;
    private int amount = 2;

    public ArrowEffectBuilder(int id, ItemStack item) {
        super(id, item);
        this.minLevel = 30;
    }

    public ArrowEffectBuilder(int id, Material material) {
        this(id, new ItemStack(material));
    }

    public ArrowEffectBuilder setEffect(ParticleEffect effect) {
        this.effect = effect;
        return this;
    }

    public ArrowEffectBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ArrowEffectBuilder setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public ArrowEffectBuilder setPeriod(int period) {
        this.period = period;
        return this;
    }

    @Override
    protected ArrowEffectCosmetic init() {
        return new ArrowEffectCosmetic(id, item, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree, effect, delay, period, amount);
    }
}
