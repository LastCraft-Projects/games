package net.lastcraft.gameeffects.user;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.gamer.async.AsyncGamerPreLoginEvent;
import net.lastcraft.api.event.gamer.async.AsyncGamerQuitEvent;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.lobby.CosmeticBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerListener extends DListener<GameEffects> {

    private final CosmeticPlayerManager cosmeticPlayerManager = GameEffectsAPI.getCosmeticPlayerManager();

    public PlayerListener(GameEffects gameEffects) {
        super(gameEffects);
    }

    @EventHandler
    public void onQuit(AsyncGamerQuitEvent e) {
        BukkitGamer gamer = e.getGamer();
        cosmeticPlayerManager.removeCosmeticPlayer(gamer.getName());
        if (LastCraft.isHub() || LastCraft.isLobby()) {
            CosmeticBox.MONEY_PLAYERS.remove(gamer.getPlayerID());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLoad(AsyncGamerPreLoginEvent e) {
        if (LastCraft.isHub() || LastCraft.isLobby()) {
            return;
        }

        cosmeticPlayerManager.getOrCreateCosmeticPlayer(e.getGamer());
    }
}
