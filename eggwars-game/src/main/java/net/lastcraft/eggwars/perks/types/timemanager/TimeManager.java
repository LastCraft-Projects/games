package net.lastcraft.eggwars.perks.types.timemanager;

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
public class TimeManager extends EWPerk {

    private final String name = "§eПовелитель времени";

    public TimeManager(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    public boolean canAfford(Player player) {
        return GAMER_MANAGER.getGamer(player.getName()).getMoney(PurchaseType.MYSTERY_DUST) >= this.cost;
    }

    public ItemStack getItem(Player player) {
        return ItemUtil.createItemStack(Material.WATCH, name, Arrays.asList("§7При убийстве игрока", "§7время его возрождения будет", "§7увеличено на 1 секунду"));
    }

    public ItemStack getItemPublic() {
        return new ItemStack(Material.WATCH);
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
