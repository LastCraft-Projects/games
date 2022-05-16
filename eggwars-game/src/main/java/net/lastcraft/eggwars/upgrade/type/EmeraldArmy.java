package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmeraldArmy implements TypeUpgrade {

    private int level = 0;
    private int price = 10;

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public void setLevel() {
        if (level != 3) {
            level ++;
            price = (level + 1) * 10;
        }
    }

    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        if (getLevel() != 3) {
            lore.addAll(Arrays.asList(
                    "§7После улучшения,",
                    "§7Ваша команда будет получать",
                    "§7по " + (getLevel() + 1) + " " + StringUtil.getCorrectWord(getLevel() + 1, "изумруд", "у", "а", "ов"),
                    "§7За каждое убийство любого игрока",
                    "",
                    "§7Стоимость: §2" + getPrice() + " изумрудов",
                    ""
            ));
            if (player.getInventory().contains(getItem(), getPrice())) {
                lore.add("§aНажмите, чтобы улучшить");
            } else {
                lore.add("§cУ вас недостаточно ресурсов");
            }
        } else {
            lore.addAll(Arrays.asList(
                    "§7Ваша команда будет получать",
                    "§7по " + getLevel() + " " + StringUtil.getCorrectWord(getLevel() + 1, "изумруд", "у", "а", "ов"),
                    "§7За каждое убийство любого игрока",
                    "",
                    "§eМаксимальный уровень"
            ));
        }
        return lore;
    }

    @Override
    public String getName(Player player) {
        return (getLevel() > 0 ? "§aИзумрудное войско " + StringUtil.getRomanNumber(getLevel()) : "§cИзумрудное войско");
    }

    @Override
    public Material getItem() {
        return Material.EMERALD;
    }

    @Override
    public boolean click(Player player) {
        if (getLevel() == 3) {
            MessageUtil.sendMessage(player, "§cУ вас максимальный уровень прокачки!");
            return false;
        }
        if (player.getInventory().contains(getItem(), getPrice())) {
            InventoryUtil.removeItemByMaterial(player.getInventory(), getItem(), getPrice());
            setLevel();
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }
}
