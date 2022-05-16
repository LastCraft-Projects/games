package net.lastcraft.eggwars.perks.types.immovable;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.event.EggBrokenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Kambet on 30.04.2018
 */
public class ImmovableListener extends DListener {

    private final Player player;

    ImmovableListener(Player player) {
        GameManager.getInstance().addGameListener(this);

        this.player = player;
    }

    @EventHandler
    public void onEgg(EggBrokenEvent event) {
        EWTeam team = EWTeam.getPlayerTeam(player);

        if (team != event.getTeam())
            return;

        player.sendMessage(GameSettings.prefix + "Ваше яйцо было сломано. Вы получили эффекты");

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120*20, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120*20, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120*20, 0), true);
    }
}
