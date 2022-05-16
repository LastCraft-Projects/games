package net.lastcraft.luckywars.executor;

import net.lastcraft.luckywars.game.LuckyWarsGame;

public interface ActionExecutor {

    void execute(LuckyWarsGame game, ExecutorContext context);
}
