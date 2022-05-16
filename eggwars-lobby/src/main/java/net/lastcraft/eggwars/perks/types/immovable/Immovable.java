package net.lastcraft.eggwars.perks.types.immovable;

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
public class Immovable extends EWPerk {

    private final String name = "§eНепоколебимый";

    public Immovable(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    public boolean canAfford(Player player) {
        return GAMER_MANAGER.getGamer(player.getName()).getMoney(PurchaseType.MYSTERY_DUST) >= this.cost;
    }

    public ItemStack getItem(final Player player) {
        return ItemUtil.createItemStack(Material.DRAGON_EGG, name, Arrays.asList("§7Если Вам сломали яйцо,", "§7Вы получаете Скорость I,", "§7Регенерацию I, Сопротивление I", "§7на 2 минуты"));
    }

    public ItemStack getItemPublic() {
        return new ItemStack(Material.DRAGON_EGG);
    }

    public void onUse(Player player) {

    }

    @Override
    public boolean has(final Player player) {
        return EWData.getDataPlayer(player).hasPerk(this);
    }

    public String getName() {
        return name;
    }

    public String getErrorMessage() {
        return Perk.getPrefix() + "§cУ вас нет данного умения!";
    }

}
