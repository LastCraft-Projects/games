package net.lastcraft.eggwars.game;

import net.lastcraft.api.game.*;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.connector.Core;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.EggWars;
import net.lastcraft.eggwars.generator.Generator;
import net.lastcraft.eggwars.generator.GeneratorData;
import net.lastcraft.eggwars.generator.LevelData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        return "EggWars" + GameSettings.typeGame.getType();
    }

    @Override
    protected List<String> getColumns() {
        return Arrays.asList("Wins", "Kills", "Eggs", "tKills", "Generator");
    }

    @Override
    protected void loadConfig() {
        EggWars main = EggWars.getInstance();

        GameSettings.displayName = "EggWars";
        GameSettings.prefix = "§6EggWars §8| §f";

        GameSettings.waterFlows = true;
        GameSettings.TNTPrimed = true;
        GameSettings.blockPhysics = true;
        GameSettings.food = false;

        GameSettings.canDropOnDeath = true;
        GameSettings.gamemode = GameModeType.SURVIVAL;

        GameSettings.minigame = MiniGameType.EW;

        GameSettings.hubPrefix = "ewlobby";

        ConfigManager cmSettings = new ConfigManager(new File("/home/lastcraft/create/" + Core.getUsername().substring(0, 3) + "/config/"), "settings.yml");
        FileConfiguration configSettings = cmSettings.getConfig();
        GameSettings.lobbyLoc = LocationUtil.stringToLocation(configSettings.getString("Lobby"), true);
        String world = CoreUtil.getGameWorld();
        Bukkit.createWorld(WorldCreator.name(world).generator("DartaAPI").generateStructures(false));

        String path = "Worlds." + world + ".";

        GameSettings.spectatorLoc = LocationUtil.stringToLocation(configSettings.getString(path + "Spectator"), true);

        List<Location> teamSpawns = configSettings.getStringList(path + "Locations").stream().map(location -> LocationUtil.stringToLocation(location, true)).collect(Collectors.toList());
        List<Location> teamEggs = configSettings.getStringList(path + "Eggs").stream().map(location -> LocationUtil.stringToLocation(location, true)).collect(Collectors.toList());

        GameSettings.playersInTeam = configSettings.getInt(path + "PlayersInTeam");
        GameSettings.numberOfTeams = teamSpawns.size();

        TeamManager.getTeams().clear();
        new TeamManager();

        Iterator<Location> iteratorSpawn = teamSpawns.iterator();
        Iterator<Location> iteratorEgg = teamEggs.iterator();

        for (TeamManager team : TeamManager.getTeams().values()){
            new EWTeam(team.getTeam(), team.getName(), team.getChatColor(), team.getColor(), team.getSubID(), iteratorSpawn.next(), iteratorEgg.next(), team.getShortName());
        }

        GameSettings.slots = GameSettings.numberOfTeams * GameSettings.playersInTeam;
        GameSettings.toStart = GameSettings.slots - (GameSettings.slots / 3);

        ConfigManager cmGenerators = new ConfigManager(new File("/home/lastcraft/create/" + Core.getUsername().substring(0, 3) + "/config/"), "generators.yml");
        FileConfiguration configGenerators = cmGenerators.getConfig();

        for (String type : configGenerators.getConfigurationSection("Generators").getKeys(false)){
            GeneratorData generatorData = new GeneratorData(Material.getMaterial(type), Material.getMaterial(configGenerators.getString("Generators." + type + ".ItemSpawn")), configGenerators.getString("Generators." + type + ".ItemSpawnName"), configGenerators.getString("Generators." + type + ".Name"), configGenerators.getString("Generators." + type + ".Color"));
            for (String level : configGenerators.getConfigurationSection("Generators." + type + ".Levels").getKeys(false)){
                generatorData.addLevelData(new LevelData(configGenerators.getString("Generators." + type + ".Levels." + level + ".Cost"), Integer.parseInt(level), configGenerators.getInt("Generators." + type + ".Levels." + level + ".Period"), configGenerators.getString("Generators." + type + ".Levels." + level + ".Lore"), generatorData));
            }
        }

        for (String type : configSettings.getConfigurationSection(path + "Generators").getKeys(false)) {
            for (String location : configSettings.getStringList(path + "Generators." + type)) {
                new Generator(Material.getMaterial(type), LocationUtil.stringToLocation(location, false));
            }
        }

        main.getVillagerShopSpawns().clear();
        main.getVillagerUpgradeSpawns().clear();
        main.getVillagerShopSpawns().addAll(configSettings.getStringList(path + "Villagers.Shop").stream().map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true)).collect(Collectors.toList()));
        main.getVillagerUpgradeSpawns().addAll(configSettings.getStringList(path + "Villagers.Upgrade").stream().map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true)).collect(Collectors.toList()));

        setSettings();
    }

    private void setSettings() {
        if (GameSettings.playersInTeam != 1) {
            GameSettings.teamMode = true;
            GameSettings.channel = "ewt";
            GameSettings.typeGame = TypeGame.TEAM;
            if (GameSettings.playersInTeam == 2) {
                GameSettings.channel = "ewd";
                GameSettings.typeGame = TypeGame.DOUBLES;
            }
        } else {
            GameSettings.teamMode = false;
            GameSettings.channel = "ews";
            GameSettings.typeGame = TypeGame.SOLO;
        }
    }
}
