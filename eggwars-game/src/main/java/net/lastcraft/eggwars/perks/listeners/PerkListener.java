package net.lastcraft.eggwars.perks.listeners;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.GameState;
import net.lastcraft.api.game.MiniGameType;
import net.lastcraft.dartaapi.game.module.WaitModule;
import net.lastcraft.dartaapi.game.perk.PerkSaveEvent;
import net.lastcraft.dartaapi.game.perk.PerksGui;
import net.lastcraft.dartaapi.loader.DartaAPI;
import net.lastcraft.eggwars.managers.data.EWData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Kambet on 30.04.2018
 */
public class PerkListener implements Listener {

    public PerkListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DartaAPI.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (GameSettings.minigame != MiniGameType.DEFAULT) {
            if (GameState.getCurrent() == GameState.GAME || GameState.getCurrent() == GameState.END) {
                return;
            }
            WaitModule.perkgui.putIfAbsent(player, new PerksGui(player));
            (WaitModule.perkgui.get(player)).setPerk(EWData.getDataPlayer(player).getLastPerk());
        }
    }


    @EventHandler
    public void onSavePerk(PerkSaveEvent e) {
        EWData.choosePerk(e.getPlayer().getName(), e.getPerk());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        EWData.removeDataPlayer(player);
    }

}
