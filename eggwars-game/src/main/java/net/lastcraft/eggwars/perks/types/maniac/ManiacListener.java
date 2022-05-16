package net.lastcraft.eggwars.perks.types.maniac;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.eggwars.event.EggBrokenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Kambet on 30.04.2018
 */
public class ManiacListener extends DListener {

    private final Player player;

    ManiacListener(Player player) {
        GameManager.getInstance().addGameListener(this);

        this.player = player;
    }

    @EventHandler
    public void onEgg(EggBrokenEvent event) {
        if (player != event.getBroken())
            return;

        player.sendMessage(GameSettings.prefix + "Вы сломали яйцо вражеской команды и получили §6Силу I");
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*15, 0), true);
    }
}
