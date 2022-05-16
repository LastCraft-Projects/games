package net.lastcraft.eggwars.perks.types.scrooge;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwars.managers.data.EWData;
import net.lastcraft.eggwars.perks.EWPerk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Kambet on 30.04.2018
 */
public class Scrooge extends EWPerk {

    private final String name = "§eСкряга";

    public Scrooge(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    public boolean canAfford(Player player) {
        return GAMER_MANAGER.getGamer(player.getName()).getMoney(PurchaseType.MYSTERY_DUST) >= this.cost;
    }

    public ItemStack getItem(Player player) {
        return ItemUtil.createItemStack(Material.DIAMOND_HELMET, name, Arrays.asList("§7При смерти есть шанс 20%,", "§7что ваши вещи не выпадут"));
    }

    public ItemStack getItemPublic() {
        return new ItemStack(Material.GLASS_BOTTLE);
    }

    public void onUse(Player player) {

    }

    @Override
    public boolean has(Player player) {
        return EWData.getDataPlayer(player).hasPerk(this);
    }

    public String getName() {
        return name;
    }

    public String getErrorMessage() {
        return Perk.getPrefix() + "§cУ вас нет данного умения!";
    }

}
