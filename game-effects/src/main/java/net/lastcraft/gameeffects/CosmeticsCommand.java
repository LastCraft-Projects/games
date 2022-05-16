package net.lastcraft.gameeffects;

import net.lastcraft.api.command.CommandInterface;
import net.lastcraft.api.command.SpigotCommand;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerEntity;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.gui.guis.CosmeticGui;
import net.lastcraft.gameeffects.gui.guis.MainGui;
import org.bukkit.entity.Player;

public final class CosmeticsCommand implements CommandInterface {

    private final GuiManager<CosmeticGui> guiManager = GameEffectsAPI.getGuiManager();

    CosmeticsCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("cosmetics", this);
        spigotCommand.setOnlyPlayers(true);
        spigotCommand.setOnlyGame(false);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String s, String[] strings) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        Player player = gamer.getPlayer();

        MainGui mainGui = guiManager.getGui(MainGui.class, player);
        if (mainGui == null) {
            return;
        }

        mainGui.onOpenGui();

    }
}
