package net.lastcraft.eggwarslobby;

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
public class EWBoard
{

    public static void createEWBoard(BukkitGamer gamer, Language language) {

        int[] ews = StatsUtil.loadStatsTable(gamer.getPlayerID(), "EggWarsSolo");
        int[] ewd = StatsUtil.loadStatsTable(gamer.getPlayerID(), "EggWarsDoubles");
        int[] ewt = StatsUtil.loadStatsTable(gamer.getPlayerID(), "EggWarsTeam");

        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName("EggWars");
        board.setLine(15, StringUtil.getLineCode(15));

        board.updater(() -> {
            board.setDynamicLine(14, language.getMessage( "BOARD_GOLD") + ": §6", StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.GOLD)));
            board.setDynamicLine(13, language.getMessage( "BOARD_MONEY") + ": §b", StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.MYSTERY_DUST)));
        });

        board.setLine(12, StringUtil.getLineCode(12));
        board.setLine(11, StringUtil.getLineCode(11) + "§f" + language.getMessage("BOARD_BUSTER") + ":");
        board.setLine(10, StringUtil.getLineCode(10) + " §7" + language.getMessage("BOARD_NO_BUSTER"));
        board.setLine(9, StringUtil.getLineCode(9));
        board.setLine(8, StringUtil.getLineCode(8) + "§f" + language.getMessage("BOARD_TOTAL_WINS") + ": §a" + StringUtil.getNumberFormat(ewd[1] + ews[1] + ewt[1]));
        board.setLine(7, StringUtil.getLineCode(7) + "§f" + language.getMessage("BOARD_TOTAL_EGG") + ": §a" + StringUtil.getNumberFormat(ewd[3] + ews[3] + ewt[3]));
        board.setLine(6, StringUtil.getLineCode(6) + "§f" + language.getMessage("BOARD_TOTAL_KILLS") + ": §a" + StringUtil.getNumberFormat(ewd[2] + ews[2] + ewt[2]));
        board.setLine(5, StringUtil.getLineCode(5) + "§f" + language.getMessage("BOARD_TOTAL_GEN") + ": §a" + StringUtil.getNumberFormat(ewd[5] + ews[5] + ewt[5]));
        board.setLine(4, StringUtil.getLineCode(4) + "§f" + language.getMessage("BOARD_TOTAL_GAMES") + ": §a" + StringUtil.getNumberFormat(ewd[0] + ews[0] + ewt[0]));
        board.setLine(3, StringUtil.getLineCode(3));
        board.setLine(2, StringUtil.getLineCode(2) + "§f" + language.getMessage("BOARD_STATS") + ":");
        board.setLine(1, StringUtil.getLineCode(1) + "§7• §e/stats");
        board.showTo(gamer);
    }
}
