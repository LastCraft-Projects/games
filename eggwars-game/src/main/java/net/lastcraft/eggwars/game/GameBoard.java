package net.lastcraft.eggwars.game;

import com.google.common.collect.Iterables;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.stats.Stats;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class GameBoard {

    public GameBoard(Player player) {
        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        EWTeam ewTeamPlayer = EWTeam.getPlayerTeam(player);

        board.setLine(15, "§7" + GameSettings.typeGame.getType() + " " + StringUtil.getDate());
        board.setLine(14, StringUtil.getLineCode(6));
        board.setLine(13, "§7Команды:");
        board.setLine(11, StringUtil.getLineCode(5));
        board.setLine(10, "§7Ваша команда:");
        board.setLine(5, StringUtil.getLineCode(4));
        board.setLine(4, "§7Статистика:");
        board.updater(() -> {
            Iterator<EWTeam> iterator = Iterables.cycle(EWTeam.getTeams().values()).iterator();
            String colors = null;
            for (int i = 4 + GameSettings.numberOfTeams; i != 4; i--) {
                EWTeam ewTeam = iterator.next();
                String team = ewTeam.size() <= 0 ? "" : (ewTeam.isCanRespawn() ? ewTeam.getChatColor() + "▉" : ewTeam.getChatColor() + StringUtil.getUTFNumber(ewTeam.getPlayers().size())); //✖
                if (colors != null) {
                    colors = colors + team;
                } else {
                    colors = team;
                }
                if (colors.length() > 32) {
                    colors = colors.substring(0, 30);
                }
                board.setDynamicLine(12, colors, (colors.length() > 32 ? colors.substring(32) : ""));
            }

            if (ewTeamPlayer != null) {
                board.setDynamicLine(9, " Цвет: §c", ewTeamPlayer.getDisplayName());
                board.setDynamicLine(8, " Яйцо: §c", ewTeamPlayer.isCanRespawn() ? "§aЕсть" : "§cНет");
                board.setDynamicLine(7, " Улучшений: §c", ewTeamPlayer.getUpgradeTeam().getPercent() + "%");
                board.setDynamicLine(6, " Игроков: §e", String.valueOf(ewTeamPlayer.size()));

                board.setDynamicLine(3, " Убийств: §c", String.valueOf(getStats().getPlayerStats(player, "tKills")));
                board.setDynamicLine(2, " Финальных убийств: §c", String.valueOf(getStats().getPlayerStats(player, "Kills")));
                board.setDynamicLine(1, " Сломано яиц: §b", String.valueOf(getStats().getPlayerStats(player, "Eggs")));
            }
        }, 60);

        board.showTo(player);
    }

    private Stats getStats() {
        return GameManager.getInstance().getStats();
    }

}

