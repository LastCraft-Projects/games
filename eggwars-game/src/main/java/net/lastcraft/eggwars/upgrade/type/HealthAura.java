package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthAura implements TypeUpgrade{

    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable() {
        this.enable = true;
    }

    @Override
    public int getPrice() {
        return 25;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7Создает ауру регенрации I",
                "§7в радиусе 15 блоков",
                "§7от вашего яйца",
                ""
        ));
        if (enable) {
            lore.add("§eУлучшено!");
        } else {
            lore.addAll(Arrays.asList(
                    "§7Стоимость: §2" + getPrice() + " изумрудов",
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
            return "§aАура регенерации";
        }
        return "§cАура регенерации";
    }

    @Override
    public Material getItem() {
        return Material.EMERALD;
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
            ewTeam.getUpgradeTeam().getAura().setEnable();
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }
}
