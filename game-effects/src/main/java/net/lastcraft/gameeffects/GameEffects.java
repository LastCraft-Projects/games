package net.lastcraft.gameeffects;

import net.lastcraft.api.LastCraft;
import net.lastcraft.gameeffects.cosmetics.CosmeticLoader;
import net.lastcraft.gameeffects.cosmetics.arrow.ArrowListener;
import net.lastcraft.gameeffects.cosmetics.colorable.ColorableListener;
import net.lastcraft.gameeffects.cosmetics.colorable.GameColorableListener;
import net.lastcraft.gameeffects.cosmetics.killeffect.KillEffectListener;
import net.lastcraft.gameeffects.cosmetics.sounds.SoundListener;
import net.lastcraft.gameeffects.gui.GuiListener;
import net.lastcraft.gameeffects.lobby.CosmeticBox;
import net.lastcraft.gameeffects.manager.SqlManager;
import net.lastcraft.gameeffects.user.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class GameEffects extends JavaPlugin {

    @Override
    public void onEnable() {
        SqlManager.initTable();

        CosmeticLoader.initAll(this);

        new PlayerListener(this);

        if (LastCraft.isHub() || LastCraft.isLobby()) {
            new GuiListener(this);
            new CosmeticsCommand();
            CosmeticBox.initKeys();
        }

        if (LastCraft.isMisc() || LastCraft.isGame()) {
            new KillEffectListener(this);
            new ArrowListener(this);
            new ColorableListener(this);

            if (LastCraft.isGame()) {
                new GameColorableListener(this);
            }
        }

        if (LastCraft.isGame()) {
            new SoundListener(this);
        }

        //new TombListener(this); //todo пишу еще
    }

    @Override
    public void onDisable() {
        SqlManager.close();
    }
}
