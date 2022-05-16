package net.lastcraft.eggwars.perks.listeners;

import net.lastcraft.api.event.gamer.async.AsyncGamerJoinEvent;
import net.lastcraft.dartaapi.loader.DartaAPI;
import net.lastcraft.eggwars.managers.data.EWData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Kambet on 30.04.2018
 */
public class PerkListener implements Listener {

    public PerkListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DartaAPI.getInstance());
    }

    @EventHandler
    public void onLogin(AsyncGamerJoinEvent event) {
        EWData.getDataPlayer(event.getGamer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        EWData.removeDataPlayer(e.getPlayer());
    }

}
