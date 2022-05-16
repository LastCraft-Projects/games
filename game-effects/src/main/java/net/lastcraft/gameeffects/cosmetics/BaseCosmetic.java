package net.lastcraft.gameeffects.cosmetics;

import lombok.Getter;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.event.PlayerBuyCosmeticEvent;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public abstract class BaseCosmetic implements Cosmetic {

    protected static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();

    private final int id;
    private final ItemStack itemStack;
    private final Rarity rarity;
    private final int minLevel;
    private final int rewardLevel;
    private final PurchaseType purchaseType;
    private final int price;
    private final boolean shulkerFree;

    protected BaseCosmetic(int id, ItemStack itemStack, Rarity rarity, int minLevel, int rewardLevel,
                           PurchaseType purchaseType, int price, boolean shulkerFree) {
        this.id = id;
        this.itemStack = itemStack;
        this.rarity = rarity;
        this.minLevel = minLevel;
        this.rewardLevel = rewardLevel;
        this.purchaseType = purchaseType;
        this.price = price;
        this.shulkerFree = shulkerFree;
    }

    @Override
    public final ItemStack getItemStack() {
        return itemStack.clone();
    }

    @Override
    public final String getName(Language lang) {
        return lang.getMessage("GAME_COSMETIC_" + id + "_NAME");
    }

    @Override
    public final List<String> getLore(Language lang) {
        return lang.getList("GAME_COSMETIC_" + id + "_LORE");
    }

    @Override
    public final boolean onPurchase(CosmeticPlayer cosmeticPlayer) {
        if (cosmeticPlayer == null) {
            return false;
        }

        BukkitGamer gamer = GAMER_MANAGER.getGamer(cosmeticPlayer.getName());
        if (gamer == null) {
            return false;
        }

        boolean result = gamer.changeMoney(purchaseType, -price);

        Player player = gamer.getPlayer();
        if (result && player != null) {
            PlayerBuyCosmeticEvent event = new PlayerBuyCosmeticEvent(player, this);
            BukkitUtil.callEvent(event);

            if (event.isCancelled()) {
                gamer.changeMoney(purchaseType, price);
                return false;
            }

            Cosmetic finalCosmetic = event.getCosmetic();
            cosmeticPlayer.buyCosmetic(finalCosmetic);
        }

        return result;
    }

}
