package net.lastcraft.gameeffects.cosmetics.sounds;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameapi.server.game.event.player.GamerTeleportArenaEvent;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import net.lastcraft.gameeffects.cosmetics.sounds.start.StartSoundCosmetic;
import org.bukkit.event.EventHandler;

public final class SoundListener extends DListener<GameEffects> {

    private final CosmeticPlayerManager cosmeticPlayerManager = GameEffectsAPI.getCosmeticPlayerManager();

    public SoundListener(GameEffects javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler
    public void onStartGame(GamerTeleportArenaEvent e) {
        BukkitGamer gamer = e.getGamer();

        CosmeticPlayer cosmeticPlayer = cosmeticPlayerManager.getCosmeticPlayer(gamer.getName());
        if (cosmeticPlayer == null) {
            return;
        }

        StartSoundCosmetic cosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.START_SOUND);
        if (cosmetic == null) {
            return;
        }

        if (gamer.isPlayer() && gamer.getLevelNetwork() < cosmetic.getMinLevel()) {
            return;
        }

        e.setSoundType(cosmetic.getSoundType());
    }
}
