package net.lastcraft.eggwars.upgrade;

import net.lastcraft.api.event.game.PlayerKillEvent;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.upgrade.type.EmeraldArmy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class UpgradeListener extends DListener {

    public UpgradeListener() {
        new UpgradeTask();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(PlayerKillEvent e) {
        Player death = e.getPlayer();

        if (!(e.getKiller() instanceof Player))
            return;

        Player killer = (Player) e.getKiller();
        EWTeam killerTeam = EWTeam.getPlayerTeam(killer);

        assert killerTeam != null;
        EmeraldArmy emeraldArmyKiller = killerTeam.getUpgradeTeam().getEmeraldArmy();
        if (emeraldArmyKiller.getLevel() > 0) {
            killer.getInventory().addItem(ItemUtil.createItemStack(Material.EMERALD, "§2Изумруд", emeraldArmyKiller.getLevel()));
        }
    }
}
