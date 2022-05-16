package net.lastcraft.luckywars.executor.trap;

import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ExecutorContext;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.Location;

public class TrapLightingAround implements ActionExecutor {

    @Override
    public void execute(LuckyWarsGame game, ExecutorContext context) {
        Location center = context.getInitiator().getLocation();

        game
                .getAlivePlayers()
                .stream()
                .filter(each -> each.getLocation().distanceSquared(center) <= 15)
                .min((o1, o2) -> context.getRandom().nextInt(-1, 1))
                .ifPresent(player -> player.getLocation().getWorld().strikeLightning(player.getLocation()));
    }
}
