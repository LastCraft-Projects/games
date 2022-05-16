package net.lastcraft.luckywarslobby.listeners;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.locale.Language;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.lobby.api.profile.BoardLobby;
import net.lastcraft.luckywarslobby.LWBoard;

/**
 * Created by Kambet on 30.04.2018
 */
public class LWBoardListener implements BoardLobby
{

    @Override
    public void showBoard(BukkitGamer gamer, Language language) {
        BukkitUtil.runTaskAsync(() -> LWBoard.createLWBoard(gamer, language));
    }
}