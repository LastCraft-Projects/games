package net.lastcraft.luckywars.executor;

import lombok.Getter;

public enum ActionExecutorCategory {

    TRAP(100),
    LOOT_LOW(-1),
    LOOT_HIGH(-1),

    MID();

    @Getter
    private final int chance;

    ActionExecutorCategory(int chance) {
        this.chance = chance;
    }

    ActionExecutorCategory() {
        this(-1);
    }
}
