package net.lastcraft.luckywarslobby;

import lombok.Getter;
import net.lastcraft.lobby.api.LobbyAPI;
import net.lastcraft.luckywarslobby.listeners.LWBoardListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Kambet on 30.04.2018
 */
@Getter
public class LuckyWarsLobby extends JavaPlugin implements Listener
{
    @Getter
    private static LuckyWarsLobby instance;

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        LobbyAPI.setBoardLobby(new LWBoardListener());
    }
}