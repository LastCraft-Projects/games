package net.lastcraft.eggwars.upgrade;

import net.lastcraft.api.game.GameState;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.eggwars.EWTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UpgradeTask extends Thread {

    public UpgradeTask() {
        start();
    }

    @Override
    public void run() {
        while (GameState.getCurrent() == GameState.GAME) {
            try {
                Thread.sleep(1000);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (PlayerUtil.isSpectator(player)) continue;
                    EWTeam ewTeam = EWTeam.getPlayerTeam(player);
                    if (ewTeam == null) continue;

                    for (EWTeam team : EWTeam.getTeams().values()) {
                        if (team == ewTeam) continue;
                        if (team.getUpgradeTeam().getFatiqAura().isEnable() && team.isCanRespawn()) {
                            Location egg = team.getEgg().clone();
                            egg.setWorld(Bukkit.getWorld(ewTeam.getEgg().getWorld().getName()));
                            double distance = LocationUtil.distance(player.getLocation(), egg);
                            if (distance < 15 && distance != -1) {
                                BukkitUtil.runTask(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 5, 0), true));
                            }
                        }
                    }

                    if (ewTeam.getUpgradeTeam().getAura().isEnable() && ewTeam.isCanRespawn()) {
                        Location egg = ewTeam.getEgg().clone();
                        egg.setWorld(Bukkit.getWorld(ewTeam.getEgg().getWorld().getName()));
                        double distance = LocationUtil.distance(player.getLocation(), egg);
                        if (distance < 15 && distance != -1) {
                            BukkitUtil.runTask(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 0), true));
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
