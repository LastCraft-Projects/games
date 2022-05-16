package net.lastcraft.luckywars.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.TitleAPI;
import net.lastcraft.api.types.GameState;
import net.lastcraft.dartaapi.commands.FwCommand;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameapi.server.game.settings.GameSettings;
import net.lastcraft.gameapi.utils.GameUtil;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Kambet on 24.07.2018
 */
@RequiredArgsConstructor
public class StartGameTask extends BukkitRunnable {

    private static final TitleAPI TITLE_API = LastCraft.getTitlesAPI();

    private final LuckyWarsGame game;
    private final Stack<Location> blocksToRemove;

    @Getter
    private int startTime = 3;

    @Override
    public void run() {
        if (startTime == 0) {
            game.applyItems();

            for (Player player : game.getAlivePlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.setFoodLevel(20);
                player.setHealth(20f);
            }

            game.setState(GameState.GAME);

            game.removeTask(StartGameTask.class);

            game.setSetting(GameSettings.DISABLED_DAMAGE_REASONS, Arrays.asList(EntityDamageEvent.DamageCause.values()));
            BukkitUtil.runTaskLater(60L, () -> game.setSetting(GameSettings.DISABLED_DAMAGE_REASONS, null));

            while (!blocksToRemove.isEmpty()) {
                blocksToRemove.pop().getBlock().setType(Material.AIR);
            }

            new FwCommand();

            GameUtil.sendAnimationTitle("SG_GAME_STARTED_SUBTITLE");

/*            Location mapCenter = game.getSetting(GameSettings.MAP_CENTER_LOCATION);

            WorldBorder worldBorder = mapCenter.getWorld().getWorldBorder();
            worldBorder.setCenter(mapCenter);
            worldBorder.setSize(600D);
            worldBorder.setSize(3.0, 20 * 60);*/

            return;
        }

        ChatColor color = ChatColor.GREEN;
        if (startTime <= 5)
            color = ChatColor.YELLOW;
        if (startTime <= 3)
            color = ChatColor.RED;

        TITLE_API.sendTitleAll(color.toString() + startTime, "", 0, 30, 0);

        startTime--;
    }
}