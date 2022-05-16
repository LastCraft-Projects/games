package net.lastcraft.gameeffects.gui.guis.type;

import net.lastcraft.base.locale.Language;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.gui.guis.CategoryGui;
import org.bukkit.entity.Player;

public final class StartSoundGui extends CategoryGui {

    public StartSoundGui(Player player, Language lang) {
        super(player, lang, "GAME_COSMETIC_GUI_" + GameCosmeticType.START_SOUND + "_NAME");
    }

    @Override
    protected GameCosmeticType getType() {
        return GameCosmeticType.START_SOUND;
    }
}
