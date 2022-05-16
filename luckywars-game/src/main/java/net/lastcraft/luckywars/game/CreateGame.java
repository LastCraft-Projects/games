package net.lastcraft.luckywars.game;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.MiniGameType;
import net.lastcraft.api.game.TeamManager;
import net.lastcraft.api.game.TypeGame;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.connector.Core;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.luckywars.LWTeam;
import net.lastcraft.luckywars.LuckyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CreateGame extends GameManager {

    @Override
    protected String getTable() {
        return "LuckyWars" + GameSettings.typeGame.getType();
    }

    @Override
    protected List<String> getColumns() {
        return Arrays.asList("Wins", "Kills", "LuckyBlocks");
    }

    @Override
    protected void loadConfig() {
        GameSettings.displayName = "LuckyWars";
        GameSettings.prefix = "§6LuckyWars §8| §f";

        GameSettings.blockPhysics = true;
        GameSettings.physical = true;
        GameSettings.canDropOnDeath = true;
        GameSettings.TNTPrimed = true;
        GameSettings.hubPrefix = "lwlobby";

        GameSettings.minigame = MiniGameType.LUCKYWARS;

        ConfigManager cmSettings = new ConfigManager(new File("/home/lastcraft/create/" + Core.getUsername().substring(0, 3) + "/config/"), "settings.yml");
        FileConfiguration configSettings = cmSettings.getConfig();
        GameSettings.lobbyLoc = LocationUtil.stringToLocation(configSettings.getString("Lobby"), true);

        String world = CoreUtil.getGameWorld();
        Bukkit.createWorld(WorldCreator.name(world).generator("DartaAPI").generateStructures(false));

        String path = "Worlds." + world + ".";

        GameSettings.spectatorLoc = LocationUtil.stringToLocation(configSettings.getString(path + "Spectator"), true);

        List<Location> teamSpawns = configSettings.getStringList(path + "Spawns").stream().map(location -> LocationUtil.stringToLocation(location, true)).collect(Collectors.toList());

        LuckyWars.getInstance().setSpawns(teamSpawns);

        GameSettings.playersInTeam = configSettings.getInt(path + "PlayersInTeam", 1);
        GameSettings.numberOfTeams = teamSpawns.size();

        if (GameSettings.playersInTeam > 1) {
            TeamManager.getTeams().clear();
            new TeamManager();

            Iterator<Location> iteratorSpawn = teamSpawns.iterator();

            for (TeamManager team : TeamManager.getTeams().values()){
                new LWTeam(team.getTeam(), team.getName(), team.getChatColor(), team.getColor(), team.getSubID(), iteratorSpawn.next(), team.getShortName());
            }
        }

        GameSettings.slots = GameSettings.numberOfTeams * GameSettings.playersInTeam;
        GameSettings.toStart = GameSettings.slots - (GameSettings.slots/3);

        setSettings();
    }

    private void setSettings() {
        if (GameSettings.playersInTeam > 1) {
            GameSettings.teamMode = true;
            GameSettings.channel = "lwt";
            GameSettings.typeGame = TypeGame.TEAM;
        } else {
            GameSettings.teamMode = false;
            GameSettings.channel = "lws";
            GameSettings.typeGame = TypeGame.SOLO;
        }
    }
}
