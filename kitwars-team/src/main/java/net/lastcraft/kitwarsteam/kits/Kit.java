package net.lastcraft.kitwarsteam.kits;

import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Kit {
    private boolean endKit;
    private String name;
    private ItemStack icon;
    private HashMap<PotionEffectType, Integer> effects = new HashMap<>();
    private HashSet<ItemStack> items = new HashSet<>();

    public Kit(String name, HashSet<ItemStack> items, HashMap<PotionEffectType, Integer> effects, boolean endKit, ItemStack icon){
        this.effects = effects;
        this.name = name;
        this.items = items;
        this.endKit = endKit;
        this.icon = icon;
    }

    public ItemStack getIcon(){
        return icon;
    }

    public boolean getEndKit(){
        return endKit;
    }

    public void giveKit(Player player){
        BukkitUtil.runTask(() -> {
            for (ItemStack item : items){
                if (ItemUtil.isHelment(item.getType())){
                    player.getInventory().setHelmet(item);
                    continue;
                }
                if (ItemUtil.isChestplate(item.getType())){
                    player.getInventory().setChestplate(item);
                    continue;
                }
                if (ItemUtil.isLeggings(item.getType())){
                    player.getInventory().setLeggings(item);
                    continue;
                }
                if (ItemUtil.isBoots(item.getType())){
                    player.getInventory().setBoots(item);
                    continue;
                }
                player.getInventory().addItem(item);
            }
        });


        if (effects != null) {
            for (Map.Entry<PotionEffectType, Integer> effect : effects.entrySet()) {
                PlayerUtil.addPotionEffect(player, effect.getKey(), effect.getValue());
            }
        }

        MessageUtil.sendMessage(player, "Вам выдан набор §a" + name);
    }

    public String getName(){
        return name;
    }
}
