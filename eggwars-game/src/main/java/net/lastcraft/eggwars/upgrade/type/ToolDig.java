package net.lastcraft.eggwars.upgrade.type;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolDig implements TypeUpgrade {

    @Override
    public int getPrice() {
        return 15;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7Все кирки в инвентарях",
                "§7ваших союзников получат",
                "§7эффективность I",
                "§7(Максимум Эффективность III)",
                "",
                "§7Стоимость: §f" + getPrice() + " железа",
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
        return (player.getInventory().contains(getItem(), getPrice()) ? "§a" : "§c") + "Наточить кирки";
    }

    @Override
    public Material getItem() {
        return Material.IRON_INGOT;
    }

    @Override
    public boolean click(Player player) {
        if (player.getInventory().contains(getItem(), getPrice())) {
            EWTeam ewTeam = EWTeam.getPlayerTeam(player);
            InventoryUtil.removeItemByMaterial(player.getInventory(), getItem(), getPrice());
            for (Player pl : ewTeam.getPlayersInTeam()) {
                if (!PlayerUtil.isAlive(pl)) continue;
                BukkitUtil.runTask(() -> {
                    PlayerInventory inventory = pl.getInventory();
                    for (ItemStack itemStack : inventory.getContents()) {
                        if (itemStack == null) continue;
                        if (itemStack.getType() == Material.IRON_PICKAXE
                                || itemStack.getType() == Material.GOLD_PICKAXE
                                || itemStack.getType() == Material.DIAMOND_PICKAXE
                                || itemStack.getType() == Material.STONE_PICKAXE
                                || itemStack.getType() == Material.WOOD_PICKAXE) {
                            setEnchantment(itemStack);
                        }
                    }
                });
            }
            MessageUtil.sendMessage(player, "§aУлучшено!");
            return true;
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            return false;
        }
    }

    private ItemStack setEnchantment(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getEnchants().containsKey(Enchantment.DIG_SPEED)) {
            if (meta.getEnchants().get(Enchantment.DIG_SPEED) != 3) {
                meta.addEnchant(Enchantment.DIG_SPEED, meta.getEnchants().get(Enchantment.DIG_SPEED) + 1, true);
            }
        } else {
            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
