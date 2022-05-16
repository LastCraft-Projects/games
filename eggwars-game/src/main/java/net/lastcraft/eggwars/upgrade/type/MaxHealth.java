package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaxHealth implements TypeUpgrade {

    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable() {
        this.enable = true;
    }

    @Override
    public int getPrice() {
        return 35;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7Увеличивает максимальное",
                "§7здоровье всех игроков в",
                "§7команде на 2 сердца",
                ""
        ));
        if (enable) {
            lore.add("§eУлучшено!");
        } else {
            lore.addAll(Arrays.asList(
                    "§7Стоимость: §b" + getPrice() + " алмазов",
                    ""
            ));
            if (player.getInventory().contains(getItem(), getPrice())) {
                lore.add("§aНажмите, чтобы улучшить");
            } else {
                lore.add("§cУ вас недостаточно ресурсов");
            }
        }

        return lore;
    }

    @Override
    public String getName(Player player) {
        if (isEnable()) {
            return "§aМаксимальное здоровье";
        }
        return "§cМаксимальное здоровье";
    }

    @Override
    public Material getItem() {
        return Material.DIAMOND;
    }

    @Override
    public boolean click(Player player) {
        if (enable) {
            MessageUtil.sendMessage(player, "§cУ вас максимальный уровень прокачки!");
            return false;
        }
        if (player.getInventory().contains(getItem(), getPrice())) {
            EWTeam ewTeam = EWTeam.getPlayerTeam(player);
            InventoryUtil.removeItemByMaterial(player.getInventory(), getItem(), getPrice());
            ewTeam.getUpgradeTeam().getMaxHealth().setEnable();
            ewTeam.getPlayers().forEach(teamPlayer -> teamPlayer.setMaxHealth(teamPlayer.getMaxHealth() + 4.0));
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }
}
