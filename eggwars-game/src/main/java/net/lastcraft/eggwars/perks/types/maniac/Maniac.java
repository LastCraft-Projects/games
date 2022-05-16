package net.lastcraft.eggwars.perks.types.maniac;

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
public class Maniac extends EWPerk {

    private final String name = "§eМаньяк";

    public Maniac(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    public boolean canAfford(Player player) {
        return GAMER_MANAGER.getGamer(player.getName()).getMoney(PurchaseType.MYSTERY_DUST) >= this.cost;
    }

    public ItemStack getItem(final Player player) {
        return ItemUtil.createItemStack(Material.IRON_SWORD, name, Arrays.asList("§7После того, как Высломаете", "§7яйцо вражеской команды,", "§7Вы получите Силу I на 15 секунд"));
    }

    public ItemStack getItemPublic() {
        return new ItemStack(Material.IRON_SWORD);
    }

    public void onUse(Player player) {
        new ManiacListener(player);
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
