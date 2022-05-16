package net.lastcraft.gameeffects.manager;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticPlayerManagerImpl implements CosmeticPlayerManager {

    private final Map<String, CosmeticPlayer> cosmeticPlayers = new ConcurrentHashMap<>();

    @Override
    public CosmeticPlayer getCosmeticPlayer(Player player) {
        return getCosmeticPlayer(player.getName());
    }

    @Override
    public CosmeticPlayer getCosmeticPlayer(String name) {
        return cosmeticPlayers.get(name.toLowerCase());
    }

    @Override
    public CosmeticPlayer getOrCreateCosmeticPlayer(BukkitGamer gamer) {
        if (gamer == null) {
            return null;
        }

        CosmeticPlayer cosmeticPlayer = getCosmeticPlayer(gamer.getName());
        if (cosmeticPlayer != null) {
            return cosmeticPlayer;
        }

        cosmeticPlayer = SqlManager.loadCosmeticPlayer(gamer);
        addCosmeticPlayer(cosmeticPlayer);
        return cosmeticPlayer;
    }

    @Override
    public Map<String, CosmeticPlayer> getCosmeticPlayers() {
        return new HashMap<>(cosmeticPlayers);
    }

    @Override
    public void addCosmeticPlayer(CosmeticPlayer cosmeticPlayer) {
        if (cosmeticPlayer == null) {
            return;
        }
        cosmeticPlayers.putIfAbsent(cosmeticPlayer.getName().toLowerCase(), cosmeticPlayer);
    }

    @Override
    public void removeCosmeticPlayer(CosmeticPlayer cosmeticPlayer) {
        if (cosmeticPlayer == null) {
            return;
        }

        removeCosmeticPlayer(cosmeticPlayer.getName());
    }

    @Override
    public void removeCosmeticPlayer(String name) {
        cosmeticPlayers.remove(name.toLowerCase());
    }
}
