package net.lastcraft.gameeffects.cosmetics.killeffect;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillEffectListener extends DListener<GameEffects> {

    private final GamerManager gamerManager = LastCraft.getGamerManager();
    private final CosmeticPlayerManager manager = GameEffectsAPI.getCosmeticPlayerManager();

    public KillEffectListener(GameEffects javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = player.getKiller();
        if (killer == null
                || !killer.isOnline()
                || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        CosmeticPlayer cosmeticPlayer = manager.getCosmeticPlayer(killer);
        if (cosmeticPlayer == null) {
            return;
        }

        KillEffectCosmetic effectCosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.KILL_EFFECT);
        if (effectCosmetic == null) {
            return;
        }

        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null) {
            return;
        }
        effectCosmetic.apply(player.getLocation(), gamer);
    }
}
