package net.lastcraft.gameeffects.gui.guis.type;

import net.lastcraft.base.locale.Language;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.gui.guis.CategoryGui;
import org.bukkit.entity.Player;

public final class KillEffectGui extends CategoryGui {

    public KillEffectGui(Player player, Language lang) {
        super(player, lang, "GAME_COSMETIC_GUI_" + GameCosmeticType.KILL_EFFECT + "_NAME");
    }

    @Override
    protected GameCosmeticType getType() {
        return GameCosmeticType.KILL_EFFECT;
    }
}
