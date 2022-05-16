package net.lastcraft.kitwarsteam.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.kitwarsteam.KWPlayer;
import net.lastcraft.kitwarsteam.KWTeam;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;

public class GameBoard {

    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();

    GameBoard(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null) return;
        int lang = gamer.getLanguage().getId();

        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.setLine(7 + GameSettings.numberOfTeams,"§7Team " + StringUtil.getDate());
        board.setLine(6 + GameSettings.numberOfTeams, StringUtil.getLineCode(6 + GameSettings.numberOfTeams));
        board.setLine(4 + GameSettings.numberOfTeams, StringUtil.getLineCode(4 + GameSettings.numberOfTeams));
        board.setLine(3, StringUtil.getLineCode(3));
        board.setLine(2, "Ваша команда: " + KWPlayer.getKWPlayer(player).getKwTeam().getDisplayName());
        board.updater(() -> {
            board.setDynamicLine(5 + GameSettings.numberOfTeams, "Время: §e", StringUtil.getCompleteTime(GameTime.getGameTime()));

            Collection<KWTeam> kwTeams = KWTeam.getTeams().values();
            Iterator<KWTeam> iterator = kwTeams.iterator();
            for (int i = 3 + GameSettings.numberOfTeams; i != 3; i--) {
                KWTeam kwTeam = iterator.next();
                if (kwTeam.size() > 0) {
                    board.setDynamicLine(i, StringUtil.getLineCode(i) + "§c" + kwTeam.getKills() + " ⚔ " + StringUtil.getLineCode(i) + kwTeam.getChatColor() + kwTeam.getName() + StringUtil.getLineCode(i) + "§7: §e", String.valueOf(kwTeam.size()));
                } else {
                    board.setDynamicLine(i, StringUtil.getLineCode(i), StringUtil.getLineCode(i));
                }
            }

            board.setDynamicLine(1, "Убийств: §c" + String.valueOf(Game.getInstance().getStats().getPlayerStats(player, "Kills")), StringUtil.getLineCode(1));
        });

        board.showTo(player);
    }

}

