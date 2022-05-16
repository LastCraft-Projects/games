package net.lastcraft.eggwarslobby.shop.perks;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.guis.shop.ItemShop;
import net.lastcraft.eggwars.managers.data.EWData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class PerkItem implements ItemShop
{

    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();

    private static final List<PerkItem> PERK_ITEMS = Lists.newArrayList();

    private final Perk perk;
    private final ItemStack icon;
    
    public PerkItem(Perk perk) {
        this.perk = perk;
        this.icon = ItemUtil.getBuilder(Material.BARRIER)
                            .setName(perk.getName())
                            .build();

        PERK_ITEMS.add(this);
    }
    
    public static List<PerkItem> getPerkItems() {
        return PERK_ITEMS;
    }

    public PerkItem getByIcon(ItemStack icon) {
        for (PerkItem perkItem : PERK_ITEMS) {
            if (perkItem.getIcon().getItemMeta().getDisplayName().equals(icon.getItemMeta().getDisplayName().substring(2)))
                return perkItem;
        }

        return null;
    }

    public void choose(Player player) {
        EWData.choosePerk(player.getName(), perk);
    }

    public void giveToPlayer(Player player, boolean buy) {
        EWData.addPerk(player.getName(), perk);

        if (buy) {
            GAMER_MANAGER.getGamer(player.getName()).changeMoney(PurchaseType.MYSTERY_DUST, -perk.getPrice());
        }
    }
    
    public boolean canBuy(final Player player) {
        return perk.canAfford(player);
    }
    
    public boolean have(Player player) {
        return perk.has(player);
    }
}