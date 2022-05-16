package net.lastcraft.eggwars.perks;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.eggwars.managers.data.EWData;
import org.bukkit.entity.Player;

public abstract class EWPerk extends Perk {

    private boolean isTeam;
    
    public EWPerk() {
        this.isTeam = false;
    }
    
    public EWPerk(Rarity rarity, int cost) {
        super(rarity, cost);
        this.isTeam = false;
    }
    
    public boolean isTeam() {
        return this.isTeam;
    }
    
    public boolean has(final Player player) {
        return EWData.getDataPlayer(player).hasPerk(this);
    }
}
