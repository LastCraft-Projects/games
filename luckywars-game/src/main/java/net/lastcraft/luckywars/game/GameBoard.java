package net.lastcraft.luckywars.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.scoreboard.Board;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.dartaapi.game.module.WaitModule;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.game.perk.PerksGui;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.luckywars.LWTeam;
import org.bukkit.entity.Player;

public class GameBoard {

    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();

    GameBoard(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        PerksGui perksGui = WaitModule.perkgui.get(player);
        Perk perk = perksGui == null ? null : perksGui.getPerk();

        Board board = LastCraft.getScoreBoardAPI().createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.setLine(8, "§7" + GameSettings.typeGame.getType() + " " + StringUtil.getDate());
        board.setLine(7, StringUtil.getLineCode(7));
        board.setLine(4, StringUtil.getLineCode(4));
        board.setLine(2, StringUtil.getLineCode(2));
        board.updater(() -> {
            board.setDynamicLine(6, StringUtil.getLineCode(6) + "§fУбийств: §c", String.valueOf(GameFactory.getStats().getPlayerStats(player, "Kills")));
            board.setDynamicLine(5, StringUtil.getLineCode(5) + "§fЛакиБлоков: §e", String.valueOf(GameFactory.getStats().getPlayerStats(player, "LuckyBlocks")));
            if (GameSettings.teamMode) {
                int aliveTeams = 0;

                for (LWTeam team : LWTeam.getTeams().values())
                    if (team.isAlive())
                        aliveTeams++;

                board.setDynamicLine(3, StringUtil.getLineCode(3) + "§fКоманд: §e", String.valueOf(aliveTeams));
            } else {
                board.setDynamicLine(3, StringUtil.getLineCode(3) + "§fИгроков: §e", String.valueOf(PlayerUtil.getAlivePlayers().size()));
            }
        });
        if (perk == null)
            board.setLine(1, StringUtil.getLineCode(1) + "§fУмение: §cНе выбрано");
        else
            board.setLine(1, StringUtil.getLineCode(1) + "§fУмение: §e" + perk.getName());

        board.showTo(player);
    }
}
