package net.lastcraft.eggwars.upgrade;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.inventory.type.DInventory;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.upgrade.type.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpgradeMenu {
    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();
    private static SoundAPI soundAPI = LastCraft.getSoundAPI();

    private DInventory dInventory;
    private Player player;
    private UpgradeTeam upgradeTeam;
    private int lang;

    private static Map<String, UpgradeMenu> menus = new ConcurrentHashMap<>();

    public static Map<String, UpgradeMenu> getMenus() {
        return menus;
    }

    public static UpgradeMenu getOrCreate(Player player) {
        if (!menus.containsKey(player.getName())) {
            menus.put(player.getName(), new UpgradeMenu(player));
        }
        return menus.get(player.getName());
    }

    private UpgradeMenu(Player player) {
        dInventory = LastCraft.getInventoryAPI().createInventory(player, "Улучшения", 4);
        this.player = player;
        upgradeTeam = EWTeam.getPlayerTeam(player).getUpgradeTeam();

        lang = GAMER_MANAGER.getGamer(player).getLanguage().getId();
        updateInventory();
    }

    private void updateInventory() {
        EmeraldArmy emeraldArmy = upgradeTeam.getEmeraldArmy();
        SharpenSword sword = upgradeTeam.getSword();
        ArmorProtect armorProtect = upgradeTeam.getArmorProtect();
        HealthAura health = upgradeTeam.getAura();
        ToolDig toolDig = upgradeTeam.getToolDig();
        FatiqAura fatiqAura = upgradeTeam.getFatiqAura();
        MaxHealth maxHealth = upgradeTeam.getMaxHealth();
        UpgradeArmor armor = upgradeTeam.getUpgradeArmor();
        FastDigging fastDigging = upgradeTeam.getFastDigging();
        TeamWork teamWork = upgradeTeam.getTeamWork();

        dInventory.clearInventory();

        dInventory.setItem(10, new DItem(ItemUtil.createItemStack(
                emeraldArmy.getItem(), emeraldArmy.getName(player), (emeraldArmy.getLevel() == 0 ? 1 : emeraldArmy.getLevel()), emeraldArmy.getLore(player)), (player, clickType, slot) -> {
            if (emeraldArmy.click(player)) {
                playYes(player);
                upgradeTeam.setUpgrade();
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(11, new DItem(ItemUtil.createItemStack(
                Material.WOOD_SWORD, sword.getName(player), sword.getLore(player)), (player, clickType, slot) -> {
            if (sword.click(player)) {
                playYes(player);
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(12, new DItem(ItemUtil.createItemStack(
                Material.DIAMOND_CHESTPLATE, armorProtect.getName(player), armorProtect.getLore(player)), (player, clickType, slot) -> {
            if (armorProtect.click(player)) {
                playYes(player);
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(13, new DItem(ItemUtil.createItemStack(
                Material.BEACON, health.getName(player), health.getLore(player)), (player, clickType, slot) -> {
            if (health.click(player)) {
                playYes(player);
                updateInventoryAll();
                upgradeTeam.setUpgrade();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(14, new DItem(ItemUtil.createItemStack(
                Material.IRON_PICKAXE, toolDig.getName(player), toolDig.getLore(player)), (player, clickType, slot) -> {
            if (toolDig.click(player)) {
                playYes(player);
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(15, new DItem(ItemUtil.createItemStack(
                Material.GLASS_BOTTLE, fatiqAura.getName(player), fatiqAura.getLore(player)), (player, clickType, slot) -> {
            if (fatiqAura.click(player)) {
                playYes(player);
                updateInventoryAll();
                upgradeTeam.setUpgrade();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(16, new DItem(ItemUtil.createItemStack(
                Material.GOLDEN_APPLE, maxHealth.getName(player), maxHealth.getLore(player)), (player, clickType, slot) -> {
            if (maxHealth.click(player)) {
                playYes(player);
                updateInventoryAll();
                upgradeTeam.setUpgrade();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(19, new DItem(ItemUtil.setItemMeta(
                ItemUtil.getColorLeatherArmor(Material.LEATHER_CHESTPLATE, upgradeTeam.getColor()),
                armor.getName(player), armor.getLore(player), (armor.getLevel() == 0 ? 1 : armor.getLevel())), (player, clickType, slot) -> {
            if (armor.click(player)) {
                playYes(player);
                upgradeTeam.setUpgrade();
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(20, new DItem(ItemUtil.createItemStack(
                Material.GOLD_PICKAXE, fastDigging.getName(player), fastDigging.getLore(player)), (player, clickType, slot) -> {
            if (fastDigging.click(player)) {
                playYes(player);
                updateInventoryAll();
                upgradeTeam.setUpgrade();
            } else {
                playNo(player);
            }
        }));
        dInventory.setItem(21, new DItem(ItemUtil.createItemStack(
                Material.POTION, (short) 8201, teamWork.getName(player), teamWork.getLore(player)), (player, clickType, slot) -> {
            if (teamWork.click(player)) {
                playYes(player);
                updateInventoryAll();
            } else {
                playNo(player);
            }
        }));
    }

    private void playYes(Player player) {
        soundAPI.play(player, SoundType.SELECTED);
    }

    private void playNo(Player player) {
        soundAPI.play(player, SoundType.NO);
    }

    private static void updateInventoryAll() {
        for (UpgradeMenu upgradeMenu : menus.values()) {
            upgradeMenu.updateInventory();
        }
    }

    public void openInv() {
        updateInventory();
        dInventory.openInventory(player);
    }
}
