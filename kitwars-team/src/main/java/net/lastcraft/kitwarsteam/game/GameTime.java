package net.lastcraft.kitwarsteam.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.dartaapi.game.module.EndModule;
import net.lastcraft.kitwarsteam.KitWars;
import net.lastcraft.kitwarsteam.kits.KitsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTime extends BukkitRunnable {

    private static int gameTime = 420;
    private static GameTime bukkitRunnable;

    @Override
    public void run() {
        if (gameTime == 0){
            new EndModule();
            bukkitRunnable.cancel();
            gameTime = 420;
        }
        if (gameTime == 180){

            for (Player all : Bukkit.getOnlinePlayers()){
                all.playSound(all.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
            }

            LastCraft.getTitlesAPI().sendTitleAll("§cВнимание", "§eОткрыты новые наборы!", 20, 3 * 20, 20);

            KitsMenu.updateIcons();
        }
        gameTime--;
    }

    public static int getGameTime(){
        return gameTime;
    }

    public static void start() {
        bukkitRunnable = new GameTime();
        bukkitRunnable.runTaskTimer(KitWars.getInstance(), 0L, 20L);
        KitsMenu.updateIcons();
    }
}
