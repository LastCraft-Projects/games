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

public class ArmorProtect implements TypeUpgrade {

    @Override
    public int getPrice() {
        return 15;
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                "§7Все нагрудники в инвентарях",
                "§7ваших союзников получат",
                "§7защиту I (Максимум Защита II)",
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
        return (player.getInventory().contains(getItem(), getPrice()) ? "§a" : "§c") + "На изготовку!";
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
                        if (itemStack.getType() == Material.DIAMOND_CHESTPLATE
                                || itemStack.getType() == Material.LEATHER_CHESTPLATE
                                || itemStack.getType() == Material.IRON_CHESTPLATE
                                || itemStack.getType() == Material.GOLD_CHESTPLATE
                                || itemStack.getType() == Material.CHAINMAIL_CHESTPLATE
                                ) {
                            setEnchantment(itemStack);
                        }
                    }
                    for (ItemStack itemStack : inventory.getArmorContents()) {
                        if (itemStack == null) continue;
                        if (itemStack.getType() == Material.DIAMOND_CHESTPLATE
                                || itemStack.getType() == Material.LEATHER_CHESTPLATE
                                || itemStack.getType() == Material.IRON_CHESTPLATE
                                || itemStack.getType() == Material.GOLD_CHESTPLATE
                                || itemStack.getType() == Material.CHAINMAIL_CHESTPLATE
                                ) {
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
        if (meta.getEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            if (meta.getEnchants().get(Enchantment.PROTECTION_ENVIRONMENTAL) != 2) {
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, meta.getEnchants().get(Enchantment.PROTECTION_ENVIRONMENTAL) + 1, true);
            }
        } else {
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
