package net.lastcraft.gameeffects.cosmetics.colorable;

import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public final class ColorableListener extends DListener<GameEffects> {

    private final CosmeticPlayerManager cosmeticPlayerManager = GameEffectsAPI.getCosmeticPlayerManager();

    public ColorableListener(GameEffects javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraft(CraftItemEvent e) {
        ItemStack currentItem = e.getCurrentItem();
        if (!GameEffectsAPI.COLORABLE_ITEMS.contains(currentItem.getType()) || !(e.getWhoClicked() instanceof Player)) {
            return;
        }

        CosmeticPlayer cosmeticPlayer = cosmeticPlayerManager.getCosmeticPlayer((Player) e.getWhoClicked());
        if (cosmeticPlayer == null || cosmeticPlayer.getGamer() == null) {
            return;
        }

        ColorableCosmetic colorableCosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.COLORABLE);
        if (colorableCosmetic == null) {
            return;
        }

        e.setCurrentItem(colorableCosmetic.getNewItem(currentItem, cosmeticPlayer.getGamer()));
    }
}
