package net.lastcraft.luckywars.game;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.NonNull;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.util.LocationUtil;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.dartaapi.utils.WorldTime;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.gameapi.items.ItemCategory;
import net.lastcraft.gameapi.items.registry.GameItemRegistry;
import net.lastcraft.gameapi.server.game.Game;
import net.lastcraft.gameapi.server.game.board.EndBoard;
import net.lastcraft.gameapi.server.game.board.api.GameBoard;
import net.lastcraft.gameapi.server.game.config.GameConfig;
import net.lastcraft.gameapi.server.game.listeners.game.DeathListener;
import net.lastcraft.gameapi.server.game.listeners.game.TNTListener;
import net.lastcraft.gameapi.server.game.settings.GameSettings;
import net.lastcraft.gameapi.server.game.state.end.EndGameTop;
import net.lastcraft.gameapi.server.game.state.end.EndHoloInfo;
import net.lastcraft.gameapi.server.game.teams.GameTeam;
import net.lastcraft.gameapi.settings.ModeSettings;
import net.lastcraft.gameapi.users.GameUser;
import net.lastcraft.gameapi.users.GameUserRegistry;
import net.lastcraft.gameapi.utils.PlayerUtil;
import net.lastcraft.luckywars.LuckyWars;
import net.lastcraft.luckywars.board.LuckyWarsBoard;
import net.lastcraft.luckywars.cage.LuckyWarsCage;
import net.lastcraft.luckywars.cage.LuckyWarsCageList;
import net.lastcraft.luckywars.converter.MaterialToDropConverter;
import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ActionExecutorCategory;
import net.lastcraft.luckywars.executor.ActionExecutorQueue;
import net.lastcraft.luckywars.executor.LootDropActionExecutor;
import net.lastcraft.luckywars.executor.trap.MobSpawners;
import net.lastcraft.luckywars.executor.trap.TrapCage;
import net.lastcraft.luckywars.executor.trap.TrapLighting;
import net.lastcraft.luckywars.executor.trap.TrapLightingAround;
import net.lastcraft.luckywars.listener.GameListener;
import net.lastcraft.luckywars.task.StartGameTask;
import net.lastcraft.luckywars.util.RandomizedQueue;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LuckyWarsGame extends Game {

    private final GameBoard gameBoard = new LuckyWarsBoard(this);
    private GameProfile midBlockProfile;

    private Map<Material, MaterialToDropConverter> converters;

    @Getter
    private ActionExecutorQueue actionExecutorQueue;

    public LuckyWarsGame(Plugin owner) {
        super(owner);
    }

    public MaterialToDropConverter converterOf(@NonNull Material material) {
        return converters.get(material);
    }

    @Override
    public void onGameLoad(ModeSettings settings) {
        LuckyWars.loadGameItems();

        setWorldName(System.getProperty("mapName"));

        generateWorld(getWorldName());

        WorldTime.freezeTime(getWorldName(), 2100, false);

        converters = new EnumMap<>(Material.class);
        actionExecutorQueue = new ActionExecutorQueue(ThreadLocalRandom.current());
        midBlockProfile = new GameProfile(UUID.randomUUID(), null);

        RandomizedQueue<ActionExecutor> traps = new RandomizedQueue<>();
        traps.enqueue(new TrapCage(Material.IRON_FENCE, Material.AIR, Material.OBSIDIAN));
        traps.enqueue(new TrapCage(Material.IRON_FENCE, Material.WATER, Material.OBSIDIAN));
        traps.enqueue(new TrapLighting());
        traps.enqueue(new TrapLightingAround());
        traps.enqueue(MobSpawners.MILK_COW);
        traps.enqueue(MobSpawners.ANGRY_BLAZE);
        traps.enqueue(MobSpawners.ANGRY_CREEPER);
        traps.enqueue(MobSpawners.ANGRY_GHAST);
        traps.enqueue(MobSpawners.ANGRY_WITCH);
        traps.enqueue(MobSpawners.SKELETON);
        traps.enqueue(MobSpawners.ANGRY_GOLEM);
        traps.enqueue(MobSpawners.SMALL_ZOMBIE);
        traps.enqueue(MobSpawners.ZOMBIE_WITH_PLAYER_HEAD);
        traps.enqueue(MobSpawners.ZOMBIE_WARRIOR);

        //======================================================================================= //
        FileConfiguration itemConfig = new GameConfig("items.yml").getConfiguration();

        RandomizedQueue<ActionExecutor> lowLoot = new RandomizedQueue<>();
        RandomizedQueue<ActionExecutor> highLoot = new RandomizedQueue<>();

        lowLoot.enqueue(new LootDropActionExecutor(
                itemConfig.getStringList("Items.Low")
                        .stream()
                        .map(ItemUtil::stringToItem)
                        .collect(Collectors.toList()),
                5
        ));
        highLoot.enqueue(new LootDropActionExecutor(
                itemConfig.getStringList("Items.High")
                        .stream()
                        .map(ItemUtil::stringToItem)
                        .collect(Collectors.toList()),
                3
        ));
        //======================================================================================= //

        actionExecutorQueue.getExecutors().put(ActionExecutorCategory.TRAP, traps);
        actionExecutorQueue.getExecutors().put(ActionExecutorCategory.LOOT_HIGH, highLoot);
        actionExecutorQueue.getExecutors().put(ActionExecutorCategory.LOOT_LOW, lowLoot);
        actionExecutorQueue.getExecutors().put(ActionExecutorCategory.MID, lowLoot);

        GameConfig gameConfig = new GameConfig("settings.yml");

        FileConfiguration config = gameConfig.getConfiguration();

        ConfigurationSection worldSection = config.getConfigurationSection("Worlds." + getWorldName());

        List<String> spawnList = worldSection.getStringList("Spawns");

        int playersInTeam = worldSection.getInt("PlayersInTeam", 1);
        int maxTeams = spawnList.size() / playersInTeam;

        //Some settings
        settings.setSetting(GameSettings.PLAYERS_PER_TEAM, playersInTeam);
        settings.setSetting(GameSettings.MAX_TEAMS, maxTeams);
        settings.setSetting(GameSettings.MAX_SLOTS, spawnList.size());
        settings.setSetting(GameSettings.TO_START, spawnList.size() / 2);
        settings.setSetting(GameSettings.SHOW_TEAM_LETTERS, playersInTeam > 1);
        settings.setSetting(GameSettings.AUTO_APPLY_GAME_ITEMS, false);
        settings.setSetting(GameSettings.ALLOW_BLOCK_PLACE, true);
        settings.setSetting(GameSettings.ALLOW_BLOCK_FORM, true);
        settings.setSetting(GameSettings.ALLOW_BLOCK_FROM_TO, true);
        settings.setSetting(GameSettings.ALLOW_BLOCK_FADE, true);
        settings.setSetting(GameSettings.ALLOW_BLOCK_BURN, true);
        settings.setSetting(GameSettings.ALLOW_BLOCK_SPREAD, true);
        settings.setSetting(GameSettings.ALLOW_LEAVES_DECAY, true);

        converters.put(Material.IRON_ORE, MaterialToDropConverter.alwaysDrop(() -> new ItemStack(Material.IRON_INGOT)));
        converters.put(Material.GOLD_ORE, MaterialToDropConverter.alwaysDrop(() -> new ItemStack(Material.GOLD_INGOT)));
        converters.put(Material.DIAMOND_ORE, MaterialToDropConverter.alwaysDrop(() -> new ItemStack(Material.DIAMOND)));
        converters.put(Material.CROPS, new MaterialToDropConverter((event -> event.getBlock().getData() == 7), (block) -> new ItemStack(Material.BREAD)));
        converters.put(Material.POTATO, new MaterialToDropConverter((event -> event.getBlock().getData() == 7), (block) -> new ItemStack(Material.BAKED_POTATO)));

        //Locations
        List<Location> spawns = spawnList.stream()
                .map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true))
                .collect(Collectors.toList());
        List<Location> topPositions = config.getStringList("EndTop")
                .stream()
                .map(spawnLoc -> LocationUtil.stringToLocation(spawnLoc, true))
                .collect(Collectors.toList());

        settings.setSetting(GameSettings.END_TOP_POSITIONS,
                topPositions);

        settings.setSetting(LuckyWarsSettings.MID_LUCKY_BLOCK_LOC,
                LocationUtil.stringToLocation(worldSection.getString("SuperBlock"), false)
                        .clone()
                        .subtract(0, 1, 0));
        settings.setSetting(LuckyWarsSettings.SPAWN_LOCATIONS,
                Iterators.cycle(spawns));

        settings.setSetting(GameSettings.LOBBY_LOCATION, Bukkit.getWorld("lobby").getSpawnLocation().add(0, 80, 0));


/*        settings.setSetting(GameSettings.LOBBY_LOCATION,
                LocationUtil.stringToLocation(config.getString("Lobby"), true));
        settings.setSetting(GameSettings.END_LOCATION,
                LocationUtil.stringToLocation(config.getString("EndLobby"), true));
        settings.setSetting(GameSettings.END_HOLOGRAM_POSITION,
                LocationUtil.stringToLocation(config.getString("EndHolo"), false));*/
    }


    @Override
    public void onGameStart() {
        prepareTeams();

        addListener(new DeathListener());
        addListener(new TNTListener());
        addListener(new GameListener(this));

        for (Player player : getAlivePlayers()) {
            GameUser user = GameUserRegistry.getUser(player.getName());
            user.addStat("Games", 1);

            PlayerUtil.resetPlayer(player);

            player.setGameMode(GameMode.ADVENTURE);

            gameBoard.create(player);
        }

        LastCraft.getCommandsAPI().disableCommand("fireworks");

        StartGameTask startGameTask = new StartGameTask(this, createCagesAndTeleport());
        startGameTask.runTaskTimer(getOwner(), 20L, 20L);

        addTask(startGameTask);
    }

    @Override
    public void onGameEnd(Object winner) {
        GameTeam team = ((GameTeam) winner);

        int winnersCount = team.getAlivePlayers().size();

        List<String> winners = team.getAlivePlayers()
                .stream()
                .map(Player::getDisplayName)
                .collect(Collectors.toList());

        String playersWon = Joiner.on("§f, ").join(winners);

        EndBoard endBoard = new EndBoard(winners);

        EndGameTop.Builder playerTopBuilder = EndGameTop.newBuilder().setField("Kills");

        for (Location location : (List<Location>) getSetting(GameSettings.END_TOP_POSITIONS)) {
            playerTopBuilder.addLocation(location);
        }

        playerTopBuilder.build().create();

        for (Player player : Bukkit.getOnlinePlayers()) {
            endBoard.create(player);

            GameUser user = GameUserRegistry.getUser(player.getName());

            Language lang = user.getGamer().getLanguage();

            player.sendMessage(StringUtil.stringToCenter("§6§lLuckyWars"));
            player.sendMessage(" ");
            player.sendMessage(StringUtil.stringToCenter(
                    lang.getMessage("GAME_WINNER" + (winnersCount > 1 ? "S" : ""), playersWon)
            ));

            if (team.getAlivePlayers().contains(player)) {
                LastCraft.getTitlesAPI().sendTitle(player,
                        lang.getMessage("GAME_TITLE_WIN"),
                        lang.getMessage(winnersCount > 1 ? "GAME_TITLE_TEAM_WIN" : "GAME_TITLE_SOLO_WIN"));

                user.addStat("Wins", 1);
                user.addReward(10, 70, 1);
            } else {
                LastCraft.getTitlesAPI().sendTitle(player,
                        lang.getMessage("GAME_TITLE_OVER"),
                        lang.getMessage("GAME_TITLE_NOT_WINNER"));
            }
        }

        EndHoloInfo.getBuilder(getSetting(GameSettings.END_HOLOGRAM_POSITION))
                .addField("Kills")
                .addField("")
                .addField(GameUser.COINS_EARNED_FIELD)
                .create()
                .show();
    }

    private Stack<Location> createCagesAndTeleport() {
        Stack<Location> blocksToRemove = new Stack<>();

        Iterator<Location> spawns = getSetting(LuckyWarsSettings.SPAWN_LOCATIONS);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (GameTeam gameTeam : getAliveTeams()) {
            Set<Player> players = gameTeam.getAlivePlayers();

            if (!spawns.hasNext()) {
                players.forEach(player -> player.kickPlayer("§cПроизошла ошибка\n\n§fНе удалось вычислить место появления\n§fСообщите на форум"));
                continue;
            }

            Location spawn = spawns.next();

            LuckyWarsCage playerCage = players
                    .stream()
                    .map(player -> {
                        LuckyWarsCage candidate = null;

                        Integer selectedItem = GameUserRegistry.getUser(player).getSelectedItem(getType(), ItemCategory.CAGE);

                        if (selectedItem != null) {
                            candidate = (LuckyWarsCage) GameItemRegistry.getById(selectedItem);
                        }
                        return candidate;
                    })
                    .filter(Objects::nonNull).min((__, ___) -> random.nextInt(-1, 1))
                    .orElse(LuckyWarsCageList.DEFAULT_CAGE);


            blocksToRemove.addAll(playerCage.create(this, spawn));

            for (Player player : players) {
                player.teleport(spawn);
            }
        }

        return blocksToRemove;
    }
}
