package net.lastcraft.gameeffects.gui.guis;

import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.dartaapi.guis.CustomItems;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import net.lastcraft.lobby.api.LobbyAPI;
import net.lastcraft.lobby.api.profile.ProfileGui;
import net.lastcraft.lobby.profile.gui.guis.ProfileMainPage;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class MainGui extends CosmeticGui {

    private final static GuiManager<ProfileGui> PROFILE_GUI_MANAGER = LobbyAPI.getProfileGuiManager();

    private final String notFound = lang.getMessage( "GADGETS_NOT_ACTIVATED");

    public MainGui(Player player, Language lang) {
        super(player, lang, true,
                lang.getMessage("PROFILE_MAIN_GUI_NAME") + " ► "
                + lang.getMessage("GAME_COSMETIC_MAIN_GUI_NAME"));
    }

    @Override
    protected void setItems(CosmeticPlayer cosmeticPlayer, Language language) {
        inventory.setItem(5, 5, new DItem(CustomItems.getBack2(lang),
                (player, clickType, i) -> {
                    ProfileMainPage mainPage = PROFILE_GUI_MANAGER.getGui(ProfileMainPage.class, player);
                    if (mainPage == null) {
                        return;
                    }

                    mainPage.open();
                    SOUND_API.play(player, SoundType.PICKUP);
                    player.chat("/profile");
                }));
        Arrays.stream(GameCosmeticType.values()).forEach(this::setItemGui);
    }

    private void setItemGui(GameCosmeticType cosmeticsType) {
        if (cosmeticPlayer == null) {
            return;
        }

        int cosmetics = cosmeticPlayer.getCosmetics(cosmeticsType).size();
        int allCosmetics = COSMETIC_MANAGER.getCosmetics(cosmeticsType).size();
        Cosmetic active = cosmeticPlayer.getActiveCosmetic(cosmeticsType);
        inventory.setItem(cosmeticsType.getSlot(), new DItem(ItemUtil.getBuilder(cosmeticsType.getItemStack())
                .removeFlags()
                .setName("§a" + lang.getMessage("GAME_COSMETIC_GUI_" + cosmeticsType.name() + "_NAME"))
                .setLore(lang.getList("GAME_COSMETIC_GUI_" + cosmeticsType.name() + "_LORE",
                        String.valueOf(cosmetics), //сколько открыто
                        String.valueOf(allCosmetics), //из скольки открыто
                        StringUtil.onPercent(cosmetics, allCosmetics) + "%", //сколько % открыто
                        " " + (active == null ? notFound : "§a" + active.getName(lang)) //какой активен сейчас
                )).build(), (player, clickType, i) -> {
            SOUND_API.play(player, SoundType.PICKUP);
            CosmeticGui gui = GUI_MANAGER.getGui(cosmeticsType.getClazz(), player);
            if (gui == null) {
                return;
            }

            gui.onOpenGui();
        }));
    }
}
