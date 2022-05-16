package net.lastcraft.gameeffects.api.event;

import lombok.Getter;
import lombok.Setter;
import net.lastcraft.api.event.player.PlayerEvent;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class PlayerBuyCosmeticEvent extends PlayerEvent implements Cancellable {

    private Cosmetic cosmetic;

    private boolean cancelled;

    public PlayerBuyCosmeticEvent(Player player, Cosmetic cosmetic) {
        super(player);
        this.cosmetic = cosmetic;
    }
}
