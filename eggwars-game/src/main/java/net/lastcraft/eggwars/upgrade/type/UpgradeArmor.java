package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.upgrade.UpgradeTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpgradeArmor implements TypeUpgrade {

    private int level = 0;
    private int price = 10;

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public void setLevel(UpgradeTeam upgradeTeam) {
        if (level != 4) {
            level ++;
            if (level == 1)  upgradeTeam.setBoots(new ItemStack(Material.IRON_BOOTS));
            if (level == 2)  upgradeTeam.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            if (level == 3)  upgradeTeam.setChestPlate(new ItemStack(Material.IRON_CHESTPLATE));
            if (level == 4)  upgradeTeam.setHelmet(new ItemStack(Material.IRON_HELMET));
            price = (level + 1) * 10;
        }
    }

    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        if (getLevel() != 4) {
            lore.addAll(Arrays.asList(
                    "§7После улучшения,",
                    "§7броня вашей команды будет",
                    "§7улучшена до железной",
                    "",
                    "§7Стоимость: §6" + getPrice() + " золота",
                    ""
            ));
            if (player.getInventory().contains(getItem(), getPrice())) {
                lore.add("§aНажмите, чтобы улучшить");
            } else {
                lore.add("§cУ вас недостаточно ресурсов");
            }
        } else {
            lore.addAll(Arrays.asList(
                    "§7Броня вашей команды",
                    "§7улучшена до железной",
                    "",
                    "§eМаксимальный уровень"
            ));
        }
        return lore;
    }

    @Override
    public String getName(Player player) {
        return (getLevel() > 0 ? "§aАпгрейд " + StringUtil.getRomanNumber(getLevel()) : "§cАпгрейд");
    }

    @Override
    public Material getItem() {
        return Material.GOLD_INGOT;
    }

    @Override
    public boolean click(Player player) {
        if (getLevel() == 4) {
            MessageUtil.sendMessage(player, "§cУ вас максимальный уровень прокачки!");
            return false;
        }
        if (player.getInventory().contains(getItem(), getPrice())) {
            InventoryUtil.removeItemByMaterial(player.getInventory(), getItem(), getPrice());
            UpgradeTeam upgradeTeam = EWTeam.getPlayerTeam(player).getUpgradeTeam();
            setLevel(upgradeTeam);
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }
}
