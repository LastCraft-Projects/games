package net.lastcraft.kitwarssolo;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.kitwarssolo.kits.Kit;
import net.lastcraft.kitwarssolo.kits.KitsMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KWPlayer {
    private final Player player;
    private Kit kit;
    private Location respawn;

    private static HashMap<Player, KWPlayer> kwPlayers = new HashMap<>();

    public static HashMap<Player, KWPlayer> getKwPlayers(){
        return kwPlayers;
    }

    public static KWPlayer getKWPlayer(Player player){
        return kwPlayers.get(player);
    }

    public static void resetKWPlayers(){
        kwPlayers.clear();
    }

    public KWPlayer(Player player, Location location) {
        this.player = player;
        this.respawn = location;

        String slot = KitWars.getInstance().getConfig().getString("DefaultKit.Slot");
        this.kit = KitsMenu.getKits().get(InventoryUtil.getSlotByXY(Integer.parseInt(slot.split("-")[0]), Integer.parseInt(slot.split("-")[1])));

        BukkitUtil.runTaskAsync(() -> {
            PlayerTag friendTags = LastCraft.getScoreBoardAPI().createTag("0" + player.getName());
            PlayerTag enemyTags = LastCraft.getScoreBoardAPI().createTag("1" + player.getName());
            friendTags.setPrefix("§a");
            enemyTags.setPrefix("§c");
            enemyTags.addPlayersToTeam(PlayerUtil.getAlivePlayers());
            enemyTags.removePlayerFromTeam(player);
            friendTags.addPlayerToTeam(player);
            friendTags.sendTo(player);
            enemyTags.sendTo(player);
        });

        kwPlayers.put(player, this);

        respawnKWPlayer();
    }

    public Player getPlayer(){
        return player;
    }

    public Kit getKit(){
        return kit;
    }

    public void setKit(Kit kit){
        PlayerUtil.reset(player);
        this.kit = kit;
        if (kit != null){
            kit.giveKit(player);
        }
    }

    public void respawnKWPlayer(){
        BukkitUtil.runTask(() -> {
            PlayerUtil.reset(player);
            player.teleport(respawn.clone().add(0.0, 0.3, 0.0));
            if (kit != null){
                kit.giveKit(player);
            }
        });
    }
}