package net.lastcraft.kitwarsteam;

import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.kitwarsteam.kits.Kit;
import net.lastcraft.kitwarsteam.kits.KitsMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KWPlayer {

    private final Player player;
    private Kit kit;
    private KWTeam kwTeam;

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

    public KWPlayer(Player player, KWTeam kwTeam) {
        this.player = player;
        this.kwTeam = kwTeam;

        String slot = KitWars.getInstance().getConfig().getString("DefaultKit.Slot");
        this.kit = KitsMenu.getKits().get(InventoryUtil.getSlotByXY(Integer.parseInt(slot.split("-")[0]), Integer.parseInt(slot.split("-")[1])));

        kwPlayers.put(player, this);

        respawnKWPlayer();
    }


    public KWTeam getKwTeam(){
        return kwTeam;
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
            player.teleport(kwTeam.getSpawn().clone().add(0.0, 0.3, 0.0));
            if (kit != null){
                kit.giveKit(player);
            }
        });
    }
}