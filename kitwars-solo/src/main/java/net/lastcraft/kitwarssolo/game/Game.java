package net.lastcraft.kitwarssolo.game;

import net.lastcraft.api.game.GameModeType;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.MiniGameType;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.kitwarssolo.KitWars;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends GameManager {

    public static FileConfiguration config;

    @Override
    protected String getTable() {
        return "KitWarsSolo";
    }

    @Override
    protected List<String> getColumns() {
        return Arrays.asList("Wins", "Kills");
    }

    @Override
    protected void loadConfig() {
        KitWars main = KitWars.getInstance();
        main.getVillagerSpawns().clear();
        main.getPlayerSpawns().clear();

        GameSettings.channel = "kws";
        GameSettings.displayName = "KitWars";
        GameSettings.prefix = "§6KitWars §8| §f";

        GameSettings.blockPlace = false;
        GameSettings.blockBreak = false;
        GameSettings.fallDamage = false;
        GameSettings.food = false;
        GameSettings.drop = false;

        GameSettings.gamemode = GameModeType.ADVENTURE;

        GameSettings.hubPrefix = "kwlobby";
        GameSettings.minigame = MiniGameType.KW_SOLO;

        ConfigManager configManager = new ConfigManager(new File("/home/lastcraft/create/" + GameSettings.channel + "/config/"), "settings.yml");
        config = configManager.getConfig();
        GameSettings.lobbyLoc = LocationUtil.stringToLocation(config.getString("Lobby"), true);
        String world = CoreUtil.getGameWorld();
        Bukkit.createWorld(WorldCreator.name(world).generator("DartaAPI").generateStructures(false));
        String path = "Worlds." + world + ".";

        GameSettings.spectatorLoc = LocationUtil.stringToLocation(config.getString(path + "Spectator"), true);

        main.getPlayerSpawns().addAll(config.getStringList(path + "Players").stream().map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true)).collect(Collectors.toList()));
        GameSettings.slots = main.getPlayerSpawns().size();
        GameSettings.toStart = GameSettings.slots - (GameSettings.slots/3);
        main.getVillagerSpawns().addAll(config.getStringList(path + "Villagers").stream().map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true)).collect(Collectors.toList()));
    }
}
