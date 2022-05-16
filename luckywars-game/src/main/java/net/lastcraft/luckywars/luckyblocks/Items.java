package net.lastcraft.luckywars.luckyblocks;

import net.lastcraft.luckywars.LuckyWars;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Items {

    public static void send(Block block, boolean type){
        List<ItemStack> items;
        int itemCount;

        if (type){
            items = LuckyWars.getInstance().getLowItems();
            itemCount = (int) (Math.random() * 3) + 3;
        } else {
            items = LuckyWars.getInstance().getHighItems();
            itemCount = (int) (Math.random() * 3) + 1;
        }

        for (int i = 0; i < itemCount; i++) {
            block.getWorld().dropItem(block.getLocation(), items.get(LuckyWars.getInstance().getRandom().nextInt(items.size())));
        }
    }

}
