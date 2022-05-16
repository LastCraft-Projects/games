package net.lastcraft.luckywars.board;

import lombok.RequiredArgsConstructor;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.gameapi.server.game.board.api.GameBoard;
import net.lastcraft.gameapi.server.game.settings.GameSettings;
import net.lastcraft.gameapi.users.GameUser;
import net.lastcraft.gameapi.users.GameUserRegistry;
import net.lastcraft.gameapi.utils.CoreUtil;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class LuckyWarsBoard implements GameBoard {

    private final LuckyWarsGame game;

    public void create(Player player) {
        GameUser user = GameUserRegistry.getUser(player.getName());
        BukkitGamer gamer = user.getGamer();

        Language lang = gamer.getLanguage();

        Board board = BOARD_API.createBoard();

        int playersPerTeam = game.getSetting(GameSettings.PLAYERS_PER_TEAM);

        board.setDynamicDisplayName(game.getType().getGameType().getName());

        board.setLine(9, "§7" + this.game.getType().getTypeName() + " " + StringUtil.getDate());
        board.setLine(8, StringUtil.getLineCode(8));
        board.setLine(4, StringUtil.getLineCode(4));
        board.setLine(3, lang.getMessage("BOARD_MAPS") + ": §a" + game.getWorldName());
        board.setLine(2, lang.getMessage("BOARD_SERVER") + ": §c" + CoreUtil.getServerName());
        board.setLine(1, StringUtil.getLineCode(1));
        board.setLine(0, "§ewww.lastcraft.net");

        board.updater(() -> board.setDynamicLine(5,
                lang.getMessage("BOARD_KILLS") + ": §a",
                String.valueOf(user.getCachedStat("Kills"))), 40L);
        board.updater(() -> board.setDynamicLine(6,
                lang.getMessage("SW_" + (playersPerTeam > 1 ? "TEAMS" : "PLAYERS") + "_ALIVE"),
                String.valueOf(playersPerTeam > 1 ? game.getAliveTeams().size() : game.getAlivePlayers().size())));
        board.updater(() -> board.setDynamicLine(7,
                lang.getMessage("BOARD_TOTAL_LUCKYBLOCKS") + ": §a",
                Integer.toString(user.getCachedStat("LBBreaked"))));

        board.showTo(player);
    }

}