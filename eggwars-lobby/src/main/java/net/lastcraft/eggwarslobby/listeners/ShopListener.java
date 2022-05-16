package net.lastcraft.eggwarslobby.listeners;

import com.google.common.collect.Maps;
import net.lastcraft.eggwarslobby.EggWarsLobby;
import net.lastcraft.eggwarslobby.shop.MainShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

/**
 * Created by Kambet on 30.04.2018
 */
public class ShopListener implements Listener {

    public static Map<String, MainShop> SHOPS = Maps.newHashMap();

    public ShopListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, EggWarsLobby.getInstance());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        SHOPS.remove(player.getName());
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (e.getMessage().equalsIgnoreCase("/event")) {
            e.setCancelled(true);

            MainShop shop = SHOPS.computeIfAbsent(player.getName(), k -> new MainShop(player));
            shop.getInventory().openInventory(player);
        }
    }

}
