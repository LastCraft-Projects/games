package net.lastcraft.gameeffects.manager;

import lombok.Getter;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.base.locale.Language;
import net.lastcraft.gameeffects.gui.guis.CosmeticGui;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GuiManagerImpl implements GuiManager<CosmeticGui> {

    private final GamerManager gamerManager = LastCraft.getGamerManager();
    @Getter
    private final Map<String, Map<String, CosmeticGui>> playerGuis = new ConcurrentHashMap<>();
    private final Set<String> guis = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void createGui(Class<? extends CosmeticGui> clazz) {
        String name = clazz.getSimpleName().toLowerCase();
        if (guis.contains(name)) {
            return;
        }

        guis.add(name);
    }

    @Override
    public void removeGui(Class<? extends CosmeticGui> clazz) {
        String nameClazz = clazz.getSimpleName().toLowerCase();
        for (String name : playerGuis.keySet()) {
            Map<String, CosmeticGui> guis = playerGuis.get(name);
            for (String guiName : guis.keySet()) {
                if (guiName.equalsIgnoreCase(nameClazz) ) {
                    guis.remove(guiName);
                }
            }
        }
    }

    @Override
    public <T extends CosmeticGui> T getGui(Class<T> clazz, Player player) {
        if (player == null) {
            return null;
        }
        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null) {
            return null;
        }

        T gui = null;

        String guiName = clazz.getSimpleName().toLowerCase();
        String name = player.getName().toLowerCase();

        if (guis.contains(guiName)) {
            Map<String, CosmeticGui> guis = playerGuis.get(name);
            if (guis == null) {
                guis = new ConcurrentHashMap<>();
                playerGuis.put(name, guis);
            }
            gui = (T) guis.get(guiName);

            if (gui == null) {
                try {
                    gui = clazz.getConstructor(Player.class, Language.class).newInstance(player, gamer.getLanguage());
                    gui.setItems();
                    guis.put(guiName, gui);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gui.setItems();
            }
        }

        return gui;
    }

    @Override
    public void removeALL(Player player) {
        if (player == null) {
            return;
        }
        playerGuis.remove(player.getName().toLowerCase());
    }
}
