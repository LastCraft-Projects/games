package net.lastcraft.gameeffects.cosmetics.colorable;

import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameapi.items.event.GamerKitApplyEvent;
import net.lastcraft.gameapi.server.game.loot.event.LootChestFillEvent;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class GameColorableListener extends DListener<GameEffects> {

    private final CosmeticPlayerManager cosmeticPlayerManager = GameEffectsAPI.getCosmeticPlayerManager();

    public GameColorableListener(GameEffects javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler
    public void onKitApply(GamerKitApplyEvent e) {
        CosmeticPlayer cosmeticPlayer = cosmeticPlayerManager.getCosmeticPlayer(e.getGamer().getName());
        if (cosmeticPlayer == null || cosmeticPlayer.getGamer() == null) {
            return;
        }

        ColorableCosmetic colorableCosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.COLORABLE);
        if (colorableCosmetic == null) {
            return;
        }

        Map<EquipmentSlot, ItemStack> newArmor = new HashMap<>(e.getArmor());
        for (Map.Entry<EquipmentSlot, ItemStack> armor : e.getArmor().entrySet()) {
            ItemStack itemStack = armor.getValue();
            if (GameEffectsAPI.COLORABLE_ITEMS.contains(itemStack.getType())) {
                newArmor.put(armor.getKey(), colorableCosmetic.getNewItem(itemStack, cosmeticPlayer.getGamer()));
            }
        }
        e.setArmor(newArmor);

        List<ItemStack> newItems = new ArrayList<>(e.getItems());
        for (ItemStack itemStack : e.getItems()) {
            if (GameEffectsAPI.COLORABLE_ITEMS.contains(itemStack.getType())) {
                newItems.remove(itemStack);
                newItems.add(colorableCosmetic.getNewItem(itemStack, cosmeticPlayer.getGamer()));
            }
        }
        e.setItems(newItems);
    }

    @EventHandler
    public void onOpenChestSkyWars(LootChestFillEvent e) {
        CosmeticPlayer cosmeticPlayer = cosmeticPlayerManager.getCosmeticPlayer(e.getPlayer());
        if (cosmeticPlayer == null || cosmeticPlayer.getGamer() == null) {
            return;
        }
        ColorableCosmetic colorableCosmetic = cosmeticPlayer.getActiveCosmetic(GameCosmeticType.COLORABLE);
        if (colorableCosmetic == null) {
            return;
        }

        Set<ItemStack> newItems = new HashSet<>(e.getItems());
        for (ItemStack itemStack : e.getItems()) {
            if (GameEffectsAPI.COLORABLE_ITEMS.contains(itemStack.getType())) {
                newItems.remove(itemStack);
                newItems.add(colorableCosmetic.getNewItem(itemStack, cosmeticPlayer.getGamer()));
            }
        }
        e.setItems(newItems);
    }
}
