package net.lastcraft.luckywars.executor;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.entity.stand.CustomStand;
import net.lastcraft.api.util.Head;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class SpinningBlockTask extends BukkitRunnable {

    private final Location blockLocation;

    private final Set<Runnable> preListeners = new HashSet<>();
    private final Set<Consumer<Location>> postListeners = new HashSet<>();
    private final long ticks;

    private long time = -1;
    private CustomStand stand;

    public SpinningBlockTask(Location location, long ticks) {
        this.blockLocation = location;
        this.ticks = ticks;
    }

    public void prepareEntities(GameProfile profile) {
        stand = LastCraft.getEntityAPI().createStand(blockLocation);

        stand.setInvisible(true);
        stand.setPublic(true);
        stand.setSmall(true);

        Property textures = profile.getProperties().get("textures").iterator().next();

        stand.getEntityEquip().setHelmet(textures == null ? new ItemStack(Material.SKULL_ITEM) : Head.getHeadByValue(textures.getValue()));

        time = ticks;
    }


    @Override
    public void run() {
        if (time == 0) {
            stand.remove();
            postListeners.forEach(locationConsumer -> locationConsumer.accept(stand.getLocation().clone().add(0, 0.5, 0)));
            cancel();
            return;
        }

        Location location = stand.getLocation().clone();

        if (ticks - time >= 0 && ticks - time <= 5) {
            location = location.add(0, 0.1, 0);
        }

        stand.setHeadPose(stand.getHeadPose().subtract(0, Math.toRadians(4), 0));

        stand.onTeleport(location);

        location.getBlock().setType(Material.AIR);

        time--;
    }
}
