package net.lastcraft.gameeffects.gui.guis.type;

import net.lastcraft.base.locale.Language;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.gui.guis.CategoryGui;
import org.bukkit.entity.Player;

public final class ColorableGui extends CategoryGui {

    public ColorableGui(Player player, Language lang) {
        super(player, lang, "GAME_COSMETIC_GUI_" + GameCosmeticType.COLORABLE + "_NAME");
    }

    @Override
    protected GameCosmeticType getType() {
        return GameCosmeticType.COLORABLE;
    }
}

