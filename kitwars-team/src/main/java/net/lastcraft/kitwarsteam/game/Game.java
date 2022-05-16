package net.lastcraft.kitwarsteam.game;

import net.lastcraft.api.game.GameModeType;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.MiniGameType;
import net.lastcraft.api.game.TeamManager;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.kitwarsteam.KWTeam;
import net.lastcraft.kitwarsteam.KitWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends GameManager {

    public static FileConfiguration config;

    @Override
    protected String getTable() {
        return "KitWarsTeam";
    }

    @Override
    protected List<String> getColumns() {
        return Arrays.asList("Wins", "Kills");
    }

    @Override
    protected void loadConfig() {
        KitWars main = KitWars.getInstance();

        GameSettings.channel = "kwt";
        GameSettings.displayName = "KitWars";
        GameSettings.prefix = "§6KitWars §8| §f";

        GameSettings.blockPlace = false;
        GameSettings.blockBreak = false;
        GameSettings.fallDamage = false;
        GameSettings.food = false;
        GameSettings.drop = false;

        GameSettings.gamemode = GameModeType.ADVENTURE;

        GameSettings.hubPrefix = "kwlobby";
        GameSettings.minigame = MiniGameType.KW_TEAM;

        ConfigManager configManager = new ConfigManager(new File("/home/lastcraft/create/" + GameSettings.channel + "/config/"), "settings.yml");
        config = configManager.getConfig();
        GameSettings.lobbyLoc = LocationUtil.stringToLocation(config.getString("Lobby"), true);
        String world = CoreUtil.getGameWorld();
        Bukkit.createWorld(WorldCreator.name(world).generator("DartaAPI").generateStructures(false));
        String path = "Worlds." + world + ".";

        List<Location> teamLocations = config.getStringList(path + "TeamLocations").stream().map(location -> LocationUtil.stringToLocation(location, true)).collect(Collectors.toList());

        GameSettings.spectatorLoc = LocationUtil.stringToLocation(config.getString(path + "Spectator"), true);

        GameSettings.teamMode = true;
        GameSettings.playersInTeam = config.getInt(path + "PlayersInTeam");
        GameSettings.numberOfTeams = teamLocations.size();

        TeamManager.getTeams().clear();
        new TeamManager();

        Iterator<Location> iterator = teamLocations.iterator();
        for(TeamManager team : TeamManager.getTeams().values()){
            new KWTeam(team.getTeam(), team.getName(), team.getChatColor(), team.getShortName(), iterator.next());
        }

        GameSettings.slots = GameSettings.numberOfTeams * GameSettings.playersInTeam;
        GameSettings.toStart = GameSettings.slots - (GameSettings.slots/3);

        main.getVillagerSpawns().clear();
        main.getVillagerSpawns().addAll(config.getStringList(path + "Villagers").stream().map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true)).collect(Collectors.toList()));

    }
}
