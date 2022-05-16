package net.lastcraft.luckywarslobby;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.lobby.game.old.stats.StatsUtil;

/**
 * Created by Kambet on 30.04.2018
 */
public class LWBoard
{

    public static void createLWBoard(BukkitGamer gamer, Language language) {

        int[] lws = StatsUtil.loadStatsTable(gamer.getPlayerID(), "LuckyWarsSolo");
        int[] lwt = StatsUtil.loadStatsTable(gamer.getPlayerID(), "LuckyWarsTeam");

        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName("LuckyWars");
        board.setLine(15, StringUtil.getLineCode(15));

        board.updater(() -> {
            board.setDynamicLine(13, language.getMessage("BOARD_GOLD") + ": §6", StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.GOLD)));
            board.setDynamicLine(12, language.getMessage("BOARD_MONEY") + ": §b", StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.MYSTERY_DUST)));
        });

        board.setLine(11, StringUtil.getLineCode(11));
        board.setLine(10, StringUtil.getLineCode(10) + "§f" + language.getMessage("BOARD_BUSTER") + ":");
        board.setLine(9, StringUtil.getLineCode(9) + " §7" + language.getMessage("BOARD_NO_BUSTER"));
        board.setLine(8, StringUtil.getLineCode(8));
        board.setLine(7, StringUtil.getLineCode(7) + "§f" + language.getMessage("BOARD_TOTAL_WINS") + ": §a" + StringUtil.getNumberFormat(lws[1] + lwt[1]));
        board.setLine(6, StringUtil.getLineCode(6) + "§f" + language.getMessage("BOARD_TOTAL_LUCKYBLOCKS") + ": §a" + StringUtil.getNumberFormat(lws[3] + lwt[3]));
        board.setLine(5, StringUtil.getLineCode(5) + "§f" + language.getMessage("BOARD_TOTAL_KILLS") + ": §a" + StringUtil.getNumberFormat(lws[2] + lwt[2]));
        board.setLine(4, StringUtil.getLineCode(4) + "§f" + language.getMessage("BOARD_TOTAL_GAMES") + ": §a" + StringUtil.getNumberFormat(lws[0] + lwt[0]));
        board.setLine(3, StringUtil.getLineCode(3));
        board.setLine(2, StringUtil.getLineCode(2) + "§f" + language.getMessage("BOARD_STATS") + ":");
        board.setLine(1, StringUtil.getLineCode(1) + "§7• §e/stats");

        board.showTo(gamer);
    }
}
