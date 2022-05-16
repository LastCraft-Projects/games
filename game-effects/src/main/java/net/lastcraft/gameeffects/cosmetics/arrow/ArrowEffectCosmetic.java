package net.lastcraft.gameeffects.cosmetics.arrow;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.effect.ParticleEffect;
import net.lastcraft.api.effect.ParticleProperty;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.cosmetics.BaseCosmetic;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class ArrowEffectCosmetic extends BaseCosmetic {

    private static final ParticleAPI PARTICLE_API = LastCraft.getParticleAPI();

    private final ParticleEffect effect;
    private final int delay, period, amount;

    public ArrowEffectCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                               PurchaseType purchaseType, int price, boolean shulkerFree,
                               ParticleEffect effect, int delay, int period, int amount) {
        super(id, itemStack, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree);

        this.effect = effect;
        this.delay = delay;
        this.period = period;
        this.amount = amount;
    }

    public GameCosmeticType getType() {
        return GameCosmeticType.ARROW;
    }

    void run(GameEffects gameEffects, Projectile projectile) {
        if (projectile == null || projectile.isDead()) {
            return;
        }

        boolean color = effect.getProperties().contains(ParticleProperty.COLORABLE);
        new BukkitRunnable() {
            int timer = 0;
            int allTime = 0;
            @Override
            public void run() {
                if (projectile.isDead() || (projectile.isOnGround() && timer++ >= 2)) {
                    cancel();
                }
                if (allTime++ >= 200) {
                    cancel();
                }

                PARTICLE_API.sendEffect(effect, projectile.getLocation(), (color ? 1F : 0.001F), amount);
            }
        }.runTaskTimer(gameEffects, delay, period);
    }
}
