package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamWork implements TypeUpgrade {

    @Override
    public int getPrice() {
        return 5;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7Наложить эффект сила II",
                "§7длительностью в 30 секунд",
                "§7всем игрокам из вашей команды.",
                "",
                "§7Стоимость: §2" + getPrice() + " изумрудов",
                ""
        ));
        if (player.getInventory().contains(getItem(), getPrice())) {
            lore.add("§aНажмите, чтобы улучшить");
        } else {
            lore.add("§cУ вас недостаточно ресурсов");
        }
        return lore;
    }

    @Override
    public String getName(Player player) {
        return (player.getInventory().contains(getItem(), getPrice()) ? "§a" : "§c") + "Командная работа";
    }

    @Override
    public Material getItem() {
        return Material.EMERALD;
    }

    @Override
    public boolean click(Player player) {
        if (player.getInventory().contains(getItem(), getPrice())) {
            EWTeam ewTeam = EWTeam.getPlayerTeam(player);
            InventoryUtil.removeItemByMaterial(player.getInventory(), getItem(), getPrice());
            BukkitUtil.runTask(() -> {
                for (Player pl : ewTeam.getPlayersInTeam()) {
                    if (!PlayerUtil.isAlive(pl)) continue;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 1));
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
