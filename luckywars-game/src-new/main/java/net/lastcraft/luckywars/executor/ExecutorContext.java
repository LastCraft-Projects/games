package net.lastcraft.luckywars.executor;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import net.lastcraft.gameapi.users.GameUser;
import net.lastcraft.gameapi.users.GameUserRegistry;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ExecutorContext {

    private final Map<String, Object> locals = new HashMap<>();

    private final ThreadLocalRandom random;
    private final Player initiator;

    public ExecutorContext(ThreadLocalRandom random, Player initiator) {
        this.random = random;
        this.initiator = initiator;
    }

    public GameUser asUser() {
        return GameUserRegistry.getUser(initiator);
    }

    public Map<String, Object> getLocals() {
        return locals;
    }

    public ThreadLocalRandom getRandom() {
        return random;
    }

    public Player getInitiator() {
        return initiator;
    }

    public void addDrops(Entity entity, @NonNull ImmutableSet<ItemStack> drops, LuckyWarsGame game) {
        entity.setMetadata("drops", new FixedMetadataValue(game.getOwner(), drops));
    }

    public void addDamagePlayerHandler(Entity entity, Consumer<Player> playerConsumer, LuckyWarsGame game){
        entity.setMetadata("damage_handler", new FixedMetadataValue(game.getOwner(), playerConsumer));
    }
}
