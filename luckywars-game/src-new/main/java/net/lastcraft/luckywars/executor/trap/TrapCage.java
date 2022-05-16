package net.lastcraft.luckywars.executor.trap;

import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ExecutorContext;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.Location;
import org.bukkit.Material;

public class TrapCage implements ActionExecutor {

    private final Material outerMaterial;
    private final Material floorMaterial;
    private final Material apexMaterial;

    public TrapCage(Material outerMaterial, Material innerMaterial, Material apexMaterial) {
        this.outerMaterial = outerMaterial;
        this.floorMaterial = innerMaterial;
        this.apexMaterial = apexMaterial;
    }

    @Override
    public void execute(LuckyWarsGame game, ExecutorContext context) {
        Location location = context.getInitiator().getLocation();

        for (int y = -1; y <= 3; y++) {
            location.clone().add(1.0, y, -1.0).getBlock().setType(outerMaterial);
            location.clone().add(1.0, y, 0.0).getBlock().setType(outerMaterial);
            location.clone().add(1.0, y, 1.0).getBlock().setType(outerMaterial);
            location.clone().add(-1.0, y, -1.0).getBlock().setType(outerMaterial);
            location.clone().add(-1.0, y, 0.0).getBlock().setType(outerMaterial);
            location.clone().add(-1.0, y, 1.0).getBlock().setType(outerMaterial);
            location.clone().add(-1.0, y, 1.0).getBlock().setType(outerMaterial);
            location.clone().add(0.0, y, 1.0).getBlock().setType(outerMaterial);
            location.clone().add(1.0, y, 1.0).getBlock().setType(outerMaterial);
            location.clone().add(-1.0, y, -1.0).getBlock().setType(outerMaterial);
            location.clone().add(0.0, y, -1.0).getBlock().setType(outerMaterial);
            location.clone().add(1.0, y, -1.0).getBlock().setType(outerMaterial);
        }
        Location floorCenter = location.clone().add(0, -1, 0);
        Location ceilCenter = location.clone().add(0, 3, 0);

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {

                floorCenter.clone().add(x, 0, z).getBlock().setType(floorMaterial);
                ceilCenter.clone().add(x, 0, z).getBlock().setType(apexMaterial);
            }
        }
    }
}
