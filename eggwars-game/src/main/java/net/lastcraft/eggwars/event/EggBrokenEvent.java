package net.lastcraft.eggwars.event;

import net.lastcraft.eggwars.EWTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Kambet on 30.04.2018
 */
public class EggBrokenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public EggBrokenEvent(EWTeam team, Player broken) {
        this.team = team;
        this.broken = broken;
    }

    public EWTeam getTeam() {
        return team;
    }

    public Player getBroken() {
        return broken;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final EWTeam team;
    private final Player broken;
}
