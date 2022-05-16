package net.lastcraft.gameeffects.gui;

import net.lastcraft.api.event.gamer.GamerChangeLanguageEvent;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.gui.guis.CosmeticGui;
import net.lastcraft.gameeffects.gui.guis.MainGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public final class GuiListener extends DListener<GameEffects> {

    private final GuiManager<CosmeticGui> manager = GameEffectsAPI.getGuiManager();

    public GuiListener(GameEffects javaPlugin) {
        super(javaPlugin);

        manager.createGui(MainGui.class);
        Arrays.stream(GameCosmeticType.values())
                .forEach(gameCosmeticType -> manager.createGui(gameCosmeticType.getClazz()));
    }

    @EventHandler
    public void onChangeLang(GamerChangeLanguageEvent e) {
        manager.removeALL(e.getGamer().getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        manager.removeALL(e.getPlayer());
    }
}
