package net.lastcraft.luckywars.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.game.EndGameEvent;
import net.lastcraft.api.event.game.PlayerKillEvent;
import net.lastcraft.api.event.game.RestartGameEvent;
import net.lastcraft.api.event.game.StartGameEvent;
import net.lastcraft.api.game.*;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.functions.EnchantingListener;
import net.lastcraft.dartaapi.functions.FurnaceListener;
import net.lastcraft.dartaapi.functions.WorkBenchListener;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.game.module.EndModule;
import net.lastcraft.dartaapi.game.team.SelectionTeam;
import net.lastcraft.dartaapi.stats.Stats;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.games.TitleUtil;
import net.lastcraft.luckywars.LWTeam;
import net.lastcraft.luckywars.LuckyWars;
import net.lastcraft.luckywars.luckyblocks.PseudoRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameFactory extends DListener {
    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        Player player = e.getPlayer();
        Player killer = (Player) e.getKiller();

        if (killer != null && killer != player) {
            getStats().addPlayerStats(killer, "Kills", 1);
            LastCraft.getScoreBoardAPI().setScoreTab(killer, getStats().getPlayerStats(killer, "Kills"));

            BukkitGamer gamerKiller = GAMER_MANAGER.getGamer(killer);
            gamerKiller.addExpLocal(10);
            gamerKiller.addMoneyLocal(1);

            double money = 1 * gamerKiller.getMultiple();
            LastCraft.getActionBarAPI().sendBar(killer, "§6+" + money + " " + StringUtil.getCorrectWord((int) money, "монет", "а", "ы", "") + " §a+10 XP");
        }

        StringUtil.sendDamageCause(player);

        GAMER_MANAGER.getGamer(player).setGameMode(GameModeType.SPECTATOR);

        LWTeam playerTeam = LWTeam.getPlayerTeam(player);
        if (playerTeam != null)
            playerTeam.removePlayer(player);

        GameFactory.check();
    }

    static Stats getStats() {
        return GameManager.getInstance().getStats();
    }

    @EventHandler
    public void onStartGame(StartGameEvent e) {
        GameManager.getInstance().addGameListener(new GameListener());
        GameManager.getInstance().addGameListener(new EnchantingListener());
        GameManager.getInstance().addGameListener(new WorkBenchListener());
        GameManager.getInstance().addGameListener(new FurnaceListener());

        if (GameSettings.teamMode) {
            Map<Player, TeamManager> selectedTeams = SelectionTeam.getSelectedTeams();
            Map<String, LWTeam> teams = LWTeam.getTeams();

            for (LWTeam lwTeam : teams.values()){
                LocationUtil.loadChunk(lwTeam.getSpawn());
            }

            for (Map.Entry<Player, TeamManager> selected : selectedTeams.entrySet()){
                teams.get(selected.getValue().getTeam()).addPlayer(selected.getKey());
            }


            LWTeam lwTeam = teams.values().stream().findFirst().orElse(null);
            for (Player player : PlayerUtil.getAlivePlayers()){
                for (LWTeam team : teams.values()){
                    if (team.size() < GameSettings.playersInTeam){
                        lwTeam = team;
                        break;
                    }
                }
                if (!selectedTeams.containsKey(player)){
                    assert lwTeam != null;
                    lwTeam.addPlayer(player);
                }
            }

        } else {
            List<Location> spawns = LuckyWars.getInstance().getSpawns();
            int spawnId = 0;
            for (Player player : PlayerUtil.getAlivePlayers()) {
                if (spawnId >= spawns.size()) {
                    continue;
                }

                new LWTeam(player, spawns.get(spawnId++));
            }
        }

        LWTeam.getTeams().values().forEach(LWTeam::setTags);

        for (Player player : PlayerUtil.getAlivePlayers()) {
            TitleUtil.StartGameTitle(player, "Выживите и не попадитесь в ловушку");
            new GameBoard(player);
        }

        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§6§lLuckyWars"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§7" + GameSettings.typeGame.getType()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eВаша задача ломать лакиблоки и убивать игроков"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eВас будет преследовать множество ловушек,"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eтак что будьте очень осторожны!"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");

        LastCraft.getScoreBoardAPI().createGameObjective(true, true);
    }

    @EventHandler
    public void onEndGame(final EndGameEvent e) {
        e.setHoloTop(Collections.singletonList("§eТоп убийств"));
        e.setTopValue("Kills");
        e.setTopValueSuffix("убийств;о;а");
        e.setHoloInfo(Arrays.asList("Убийств: §c@Kills", "Сломано ЛакиБлоков: §b@LuckyBlocks"));

        LWTeam winner = null;
        for (LWTeam lwTeam : LWTeam.getTeams().values()) {
            if (lwTeam.isAlive()) {
                winner = lwTeam;
                break;
            }
        }

        for (BukkitGamer bukkitGamer : LastCraft.getGamerManager().getGamers().values()) {
            Player player = bukkitGamer.getPlayer();

            if (getStats().getStatsPlayers().containsKey(player) && getStats().getPlayerStats(player, "LuckyBlocks") >= 5) {
                int moneyLucky = getStats().getPlayerStats(player, "LuckyBlocks") / 5;
                bukkitGamer.addMoneyLocal(moneyLucky);
            }
        }

        if (winner != null) {
            Player winPlayer = winner.getPlayersInTeam().iterator().next();
            boolean isSolo = GameSettings.typeGame == TypeGame.SOLO;

            if (!isSolo) {
                e.setTeamWin(winner.getDisplayName());
                e.setWinMsg("§eПобедила команда §8- " + winner.getDisplayName());
            } else {
                e.setWinMsg("§eПобедил игрок §8- " + winPlayer.getDisplayName());
            }

            for (Player player : winner.getPlayersInTeam()) {
                BukkitGamer targetGamer = GAMER_MANAGER.getGamer(player);

                if (targetGamer == null) {
                    continue;
                }

                if (GameSettings.typeGame == TypeGame.SOLO) {
                    targetGamer.addExpLocal(80);
                    targetGamer.addMoneyLocal(10);
                } else if (GameSettings.typeGame == TypeGame.TEAM) {
                    targetGamer.addExpLocal(50);
                    targetGamer.addMoneyLocal(25);
                }

                getStats().addPlayerStats(player, "Wins", 1);
                e.addWinner(player);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                String stats = "";

                if (!getStats().getStatsPlayers().containsKey(player)) {
                    stats = stats + "@Kills:" + 0;
                    stats = stats + ";@LuckyBlocks:" + 0;
                } else {
                    stats = stats + "@Kills:" + getStats().getPlayerStats(player, "Kills");
                    stats = stats + ";@LuckyBlocks:" + getStats().getPlayerStats(player, "LuckyBlocks");
                }

                e.getHoloPlayerInfo().put(player, stats);

                if (winner.playerInTeam(player)) {
                    LastCraft.getTitlesAPI().sendTitle(player, isSolo ? "Победа" : "§6Ваша команда", isSolo ? "§6Вы остались последним выжившим" : "победила", 20L, 60L, 20L);
                } else {
                    LastCraft.getTitlesAPI().sendTitle(player, isSolo ? "§6Победил игрок" : "§6Победила команда", isSolo ? winPlayer.getDisplayName() : winner.getDisplayName(), 20L, 60L, 20L);
                }
            }
        }
    }

    @EventHandler
    public void onRestartGame(final RestartGameEvent e) {
        PseudoRandom.reset();
        LWTeam.getTeams().clear();
    }

    static void check() {
        BukkitUtil.runTaskLater(1, ()-> {
            int aliveTeam = 0;

            for (LWTeam ewTeam : LWTeam.getTeams().values()){
                if (ewTeam.isAlive()){
                    aliveTeam++;
                }
            }
            if (aliveTeam <= 1){
                if (GameState.END == GameState.getCurrent())
                    return;

                new EndModule();
            }
        });
    }
}
