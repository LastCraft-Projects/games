package net.lastcraft.eggwarslobby.listeners;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.locale.Language;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.eggwarslobby.EWBoard;
import net.lastcraft.lobby.api.profile.BoardLobby;

/**
 * Created by Kambet on 30.04.2018
 */
public class EWBoardListener implements BoardLobby
{

    @Override
    public void showBoard(BukkitGamer gamer, Language language) {
        BukkitUtil.runTaskAsync(() -> EWBoard.createEWBoard(gamer, language));
    }
}