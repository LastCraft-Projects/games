package net.lastcraft.luckywars.executor;

import lombok.RequiredArgsConstructor;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class LootDropActionExecutor implements ActionExecutor {

    private final List<ItemStack> items;
    private final int maxItemCount;

    @Override
    public void execute(LuckyWarsGame game, ExecutorContext context) {
        Location dropLocation = (Location) context.getLocals().get("drop_loc");

        for (int i = 0; i <= context.getRandom().nextInt(maxItemCount); i++) {
            dropLocation.getWorld().dropItem(dropLocation, items.get(context.getRandom().nextInt(items.size())));
        }

    }
}
