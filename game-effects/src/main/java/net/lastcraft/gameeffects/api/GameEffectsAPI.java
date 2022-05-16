package net.lastcraft.gameeffects.api;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.gameeffects.api.manager.CosmeticManager;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.gui.guis.CosmeticGui;
import net.lastcraft.gameeffects.manager.CosmeticManagerImpl;
import net.lastcraft.gameeffects.manager.CosmeticPlayerManagerImpl;
import net.lastcraft.gameeffects.manager.GuiManagerImpl;
import org.bukkit.Material;

@UtilityClass
public class GameEffectsAPI {

    public final ImmutableSet<Material> COLORABLE_ITEMS = ImmutableSet.of(
            Material.SHIELD, Material.LEATHER_CHESTPLATE, Material.LEATHER_BOOTS,
            Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS
    );

    private CosmeticManager cosmeticManager;
    private CosmeticPlayerManager cosmeticPlayerManager;
    private GuiManager<CosmeticGui> guiManager;

    public CosmeticManager getCosmeticManager() {
        if (cosmeticManager == null)
            cosmeticManager = new CosmeticManagerImpl();
        return cosmeticManager;
    }

    public CosmeticPlayerManager getCosmeticPlayerManager() {
        if (cosmeticPlayerManager == null)
            cosmeticPlayerManager = new CosmeticPlayerManagerImpl();
        return cosmeticPlayerManager;
    }

    public GuiManager<CosmeticGui> getGuiManager() {
        if (guiManager == null)
            guiManager = new GuiManagerImpl();
        return guiManager;
    }
}
