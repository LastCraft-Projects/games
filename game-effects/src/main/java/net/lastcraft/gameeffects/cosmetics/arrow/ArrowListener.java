package net.lastcraft.gameeffects.cosmetics.arrow;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public final class ArrowListener extends DListener<GameEffects> {

    private final CosmeticPlayerManager cosmeticPlayerManager = GameEffectsAPI.getCosmeticPlayerManager();

    public ArrowListener(GameEffects gameEffects) {
        super(gameEffects);
    }

    /*
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerShootBowEvent(EntityShootBowEvent e) {
        spawnArrowEffect(e.getEntity(), e.getProjectile());
    }
    */

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onThrow(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        ProjectileSource entity = projectile.getShooter();

        spawnArrowEffect(entity, projectile);
    }

    private void spawnArrowEffect(ProjectileSource playerEntity, Entity projectileEntity) {
        if (!(playerEntity instanceof Player)) {
            return;
        }

        Player player = (Player) playerEntity;
        if (!(projectileEntity instanceof Projectile) || projectileEntity instanceof FishHook) {
            return;
        }

        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        Projectile projectile = (Projectile) projectileEntity;
        CosmeticPlayer cosmeticPlayer = cosmeticPlayerManager.getCosmeticPlayer(player);
        if (cosmeticPlayer == null)
            return;

        ArrowEffectCosmetic cosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.ARROW);
        if (cosmetic == null)
            return;

        if (gamer.isPlayer() && gamer.getLevelNetwork() < cosmetic.getMinLevel()) {
            return;
        }

        cosmetic.run(javaPlugin, projectile);
    }
}
