package net.lastcraft.luckywars.executor.trap;

import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ExecutorContext;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.Location;

public class TrapLighting implements ActionExecutor {

    @Override
    public void execute(LuckyWarsGame game, ExecutorContext context) {
        Location location = context.getInitiator().getLocation();

        location.getWorld().strikeLightning(location);
    }
}
