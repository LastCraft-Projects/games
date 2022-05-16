package net.lastcraft.kitwarssolo.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class GameBoard {

    private static final GamerManager gamerManager = LastCraft.getGamerManager();

    GameBoard(Player player) {
        BukkitGamer gamerMain = gamerManager.getGamer(player);
        if (gamerMain == null) return;
        int lang = gamerMain.getLanguage().getId();

        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.setLine(10, "§7Solo " + StringUtil.getDate());
        board.setLine(9, StringUtil.getLineCode(9));
        board.setLine(7, StringUtil.getLineCode(7));
        board.setLine(6, "Лидеры:");
        board.setLine(2, StringUtil.getLineCode(2));
        board.updater(() -> {
            board.setDynamicLine(8, "Время: §e", StringUtil.getCompleteTime(GameTime.getGameTime()));
            Map<Player, Integer> top = GameManager.getInstance().getStats().getPlayersTop("Kills");
            Iterator<Map.Entry<Player, Integer>> iterator = top.entrySet().iterator();
            for (int i = 1; i <= 3; i++) {
                if (iterator.hasNext()) {
                    Map.Entry<Player, Integer> entry = iterator.next();
                    if (!PlayerUtil.isAlive(entry.getKey())) {
                        i--;
                        continue;
                    }
                    BukkitGamer gamer = gamerManager.getGamer(entry.getKey());
                    if (entry.getValue() != 0) {
                        board.setDynamicLine(6 - i, StringUtil.getLineCode(6 - i) + " §c" + entry.getValue() + " ⚔ " + gamer.getPrefix().substring(gamer.getPrefix().length() - 2), entry.getKey().getName());
                        continue;
                    }
                }
                board.setDynamicLine(6 - i, " §7" + i + " место", "");
            }
            board.setDynamicLine(1, "Убийств: §c" + GameManager.getInstance().getStats().getPlayerStats(player, "Kills"), StringUtil.getLineCode(1));
        });

        board.showTo(player);
    }
}

