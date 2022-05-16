package net.lastcraft.luckywars.executor;


import lombok.Getter;
import lombok.NonNull;
import net.lastcraft.luckywars.util.RandomizedQueue;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ActionExecutorQueue {

    @Getter
    private final Map<ActionExecutorCategory, RandomizedQueue<ActionExecutor>> executors;
    private final ThreadLocalRandom random;

    public ActionExecutorQueue(ThreadLocalRandom random) {
        this.executors = new EnumMap<>(ActionExecutorCategory.class);
        this.random = random;
    }

    public RandomizedQueue<ActionExecutor> queueOf(@NonNull ActionExecutorCategory category) {
        return executors.get(category);
    }

    public ActionExecutor nextExecutor() {
        return nextExecutor(randomCategory());
    }

    public ActionExecutor nextExecutor(@NonNull ActionExecutorCategory category) {
        RandomizedQueue<ActionExecutor> executors = this.executors.get(category);

        if (executors == null) {
            throw new IllegalStateException("There is no executors for category: " + category);
        }

        return executors.sample();
    }

    public ActionExecutorCategory randomCategory() {
        int chanceSum = executors.keySet().stream().mapToInt(ActionExecutorCategory::getChance).sum();
        int num = random.nextInt(chanceSum);
        int from = 0;

        for (ActionExecutorCategory category : executors.keySet()) {
            if (from <= num && num < from + category.getChance() && category.getChance() != -1) {
                return category;
            }
            from += category.getChance();
        }

        throw new IllegalStateException("Cannot retrieve random category");
    }

    public ExecutorContext createContext(@NonNull Player player) {
        return new ExecutorContext(random, player);
    }
}
