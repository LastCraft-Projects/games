package net.lastcraft.gameeffects.cosmetics.dead;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.depend.CraftVector;
import net.lastcraft.api.entity.EntityAPI;
import net.lastcraft.api.entity.stand.CustomStand;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.listeners.DListener;
import net.lastcraft.gameeffects.GameEffects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TombListener extends DListener<GameEffects> {

    private final EntityAPI entityAPI = LastCraft.getEntityAPI();

    private CustomStand back;

    public TombListener(GameEffects javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler
    public void onComamnd(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        BukkitGamer gamer = LastCraft.getGamerManager().getGamer(player);
        if (gamer == null || !gamer.isDeveloper()) {
            return;
        }

        if (e.getMessage().startsWith("/testing")) {
            create(player.getLocation(), gamer);
        }

        if (e.getMessage().startsWith("/kek")) {
            String[] split = e.getMessage().split(" ");
            back.setRightArmPose(new CraftVector(Float.valueOf(split[1]), Float.valueOf(split[2]), Float.valueOf(split[3])));
        }

    }

    public void create(Location location, BukkitGamer gamer) {
        CustomStand headStand = entityAPI.createStand(location.clone());
        headStand.getEntityEquip().setHelmet(gamer.getHead());
        headStand.setInvisible(true);
        headStand.showTo(gamer);

        Vector backVector = location.clone().getDirection().normalize().multiply(-0.41);
        Location backLocation = location.clone().add(backVector);
        backLocation.setY(location.getY());
        //backLocation.subtract(0,0, -0.3);
        back = entityAPI.createStand(backLocation);
        back.getEntityEquip().setItemInMainHand(new ItemStack(Material.STICK));
        back.setInvisible(true);
        back.setRightArmPose(new CraftVector(0, -90, -100));
        back.showTo(gamer);
    }
}
