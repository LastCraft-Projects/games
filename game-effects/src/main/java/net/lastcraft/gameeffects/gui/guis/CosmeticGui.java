package net.lastcraft.gameeffects.gui.guis;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.inventory.InventoryAPI;
import net.lastcraft.api.inventory.type.DInventory;
import net.lastcraft.api.inventory.type.MultiInventory;
import net.lastcraft.api.manager.GuiManager;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.dartaapi.guis.CustomItems;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.manager.CosmeticManager;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;

public abstract class CosmeticGui {
    protected static final InventoryAPI INVENTORY_API = LastCraft.getInventoryAPI();
    protected static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();
    protected static final SoundAPI SOUND_API = LastCraft.getSoundAPI();
    protected static final GuiManager<CosmeticGui> GUI_MANAGER = GameEffectsAPI.getGuiManager();
    protected static final CosmeticManager COSMETIC_MANAGER = GameEffectsAPI.getCosmeticManager();

    private static final CosmeticPlayerManager COSMETIC_PLAYER_MANAGER = GameEffectsAPI.getCosmeticPlayerManager();

    protected final BukkitGamer gamer;
    protected final Language lang;

    protected DInventory inventory;
    protected MultiInventory multiInventory;
    protected CosmeticPlayer cosmeticPlayer;

    protected CosmeticGui(Player player, Language lang, boolean small, String name) {
        this.gamer = GAMER_MANAGER.getGamer(player);
        this.lang = lang;

        if (small) {
            inventory = INVENTORY_API.createInventory(player, name, 5);
        } else {
            multiInventory = INVENTORY_API.createMultiInventory(player, name, 5);
        }
    }

    public final void setItems() {
        if (inventory == null && multiInventory == null && gamer == null) {
            return;
        }

        CosmeticPlayer cosmeticPlayer = COSMETIC_PLAYER_MANAGER.getCosmeticPlayer(gamer.getName());
        if (cosmeticPlayer != null) {
            this.cosmeticPlayer = cosmeticPlayer;
            setItems(cosmeticPlayer, lang);
            return;
        }

        BukkitUtil.runTaskAsync(() -> {
            CosmeticPlayer loadCosmeticPlayer = COSMETIC_PLAYER_MANAGER.getOrCreateCosmeticPlayer(gamer);
            if (loadCosmeticPlayer == null) {
                return;
            }

            this.cosmeticPlayer = loadCosmeticPlayer;
            setItems(loadCosmeticPlayer, lang);
        });
    }

    protected abstract void setItems(CosmeticPlayer cosmeticPlayer, Language language);

    public final void onOpenGui() {
        if (gamer == null) {
            return;
        }

        if (inventory != null) {
            inventory.openInventory(gamer);
            return;
        }

        multiInventory.openInventory(gamer);
    }

    protected final void setItemBack() {
        if (inventory != null) {
            setBackKey(inventory);
        }

        if (multiInventory != null) {
            multiInventory.getInventories().forEach(this::setBackKey);
        }
    }

    private void setBackKey(DInventory inventory) {
        if (inventory == null) {
            return;
        }

        inventory.setItem(40, new DItem(CustomItems.getBack(lang), (player, clickType, i) -> {
            SOUND_API.play(player, SoundType.PICKUP);
            MainGui mainGui = GUI_MANAGER.getGui(MainGui.class, player);
            if (mainGui == null) {
                return;
            }

            mainGui.onOpenGui();
        }));
    }
}
