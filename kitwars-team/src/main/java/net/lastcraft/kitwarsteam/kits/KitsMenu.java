package net.lastcraft.kitwarsteam.kits;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.kitwarsteam.KitWars;
import net.lastcraft.kitwarsteam.game.GameTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class KitsMenu {
    private static Inventory inventory;
    private static HashMap<Integer, Kit> kits = new HashMap<>();

    public static HashMap<Integer, Kit> getKits(){
        return kits;
    }

    public KitsMenu() {
        inventory = Bukkit.createInventory(null, 54, "Выбор набора");
        loadKit();
        loadDefaultKit();
    }

    public static void openMenu(Player player) {
        player.openInventory(inventory);
    }

    private void loadKit() {
        File folder = new File("/home/lastcraft/create/" + GameSettings.channel + "/config/kits/");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".yml")){
                    ConfigManager cm = new ConfigManager(folder, file.getName());

                    HashSet<ItemStack> items = cm.getConfig().getStringList("Kit.Items").stream().map(ItemUtil::stringToItem).collect(Collectors.toCollection(HashSet::new));

                    int slot = InventoryUtil.getSlotByXY(Integer.parseInt(cm.getConfig().getString("Slot").split("-")[0]), Integer.parseInt(cm.getConfig().getString("Slot").split("-")[1]));

                    HashMap<PotionEffectType, Integer> effects = null;
                    if (cm.getConfig().contains("Kit.Effect")){
                        effects = new HashMap<>();
                        for (String effect : cm.getConfig().getStringList("Kit.Effect")){
                            if(effect.contains(":")){
                                effects.put(PotionEffectType.getByName(effect.split(":")[0]), Integer.parseInt(effect.split(":")[1]));
                            } else {
                                effects.put(PotionEffectType.getByName(effect), 1);
                            }
                        }
                    }

                    kits.put(slot, new Kit(cm.getConfig().getString("Name"), items, effects, cm.getConfig().getBoolean("EndKit"), ItemUtil.setItemMeta(ItemUtil.stringToItem(cm.getConfig().getString("Icon")), cm.getConfig().getString("Name"), cm.getConfig().getStringList("Kit.Lore"))));
                    updateIcon(slot);
                }
            }
        }
    }

    private void loadDefaultKit(){
        HashSet<ItemStack> items = KitWars.getInstance().getConfig().getStringList("DefaultKit.Items").stream().map(ItemUtil::stringToItem).collect(Collectors.toCollection(HashSet::new));

        HashMap<PotionEffectType, Integer> effects = null;
        if (KitWars.getInstance().getConfig().contains("DefaultKit.Effect")){
            effects = new HashMap<>();
            for (String effect : KitWars.getInstance().getConfig().getStringList("DefaultKit.Effect")){
                if(effect.contains(":")){
                    effects.put(PotionEffectType.getByName(effect.split(":")[0]), Integer.parseInt(effect.split(":")[1]));
                } else {
                    effects.put(PotionEffectType.getByName(effect), 1);
                }
            }
        }

        int slot = InventoryUtil.getSlotByXY(Integer.parseInt(KitWars.getInstance().getConfig().getString("DefaultKit.Slot").split("-")[0]), Integer.parseInt(KitWars.getInstance().getConfig().getString("DefaultKit.Slot").split("-")[1]));

        kits.put(slot, new Kit(KitWars.getInstance().getConfig().getString("DefaultKit.Name"), items, effects, false, ItemUtil.setItemMeta(ItemUtil.stringToItem(KitWars.getInstance().getConfig().getString("DefaultKit.Icon")), KitWars.getInstance().getConfig().getString("DefaultKit.Name"), KitWars.getInstance().getConfig().getStringList("DefaultKit.Lore"))));
        updateIcon(slot);
    }

    private static void updateIcon(int slot){
        ItemStack itemStack = new ItemStack(kits.get(slot).getIcon());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (kits.get(slot).getEndKit()){
            if (GameTime.getGameTime() <= 180){
                itemMeta.setDisplayName("§a" + itemMeta.getDisplayName());
            } else {
                itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§c" + kits.get(slot).getIcon().getItemMeta().getDisplayName());
                itemMeta.setLore(kits.get(slot).getIcon().getItemMeta().getLore());
            }
        } else {
            itemMeta.setDisplayName("§a" + itemMeta.getDisplayName());
        }
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slot, itemStack);
    }

    public static void updateIcons(){
        kits.keySet().forEach(KitsMenu::updateIcon);
    }
}
