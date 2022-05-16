package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastDigging implements TypeUpgrade {

    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    @Override
    public int getPrice() {
        return 15;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7После смерти выдает",
                "§7эффект скорость копания I",
                "§7длительностью в 1 минуту",
                ""
        ));
        if (enable) {
            lore.add("§eУлучшено!");
        } else {
            lore.addAll(Arrays.asList(
                    "§7Стоимость: §6" + getPrice() + " золота",
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

    private void setEnable() {
        enable = true;
    }

    @Override
    public String getName(Player player) {
        if (isEnable()) {
            return "§aУскорение";
        }
        return "§cУскорение";
    }

    @Override
    public Material getItem() {
        return Material.GOLD_INGOT;
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
            ewTeam.getUpgradeTeam().getFastDigging().setEnable();
            BukkitUtil.runTask(() -> {
                for (Player teamPlayer : ewTeam.getPlayers()) {
                    teamPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 60, 0), true);
                }
            });
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }
}
