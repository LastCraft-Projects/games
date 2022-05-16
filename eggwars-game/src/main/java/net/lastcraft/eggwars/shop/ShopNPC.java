package net.lastcraft.eggwars.shop;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.entity.npc.NPC;
import net.lastcraft.api.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ShopNPC {

    private Hologram hologram;
    private NPC npc;

    private static List<ShopNPC> shopsNPC = new ArrayList<>();

    public ShopNPC(Location location) {
        hologram = LastCraft.getHologramAPI().createHologram(location.clone().add(0.0, 1.65, 0.0));
        hologram.addTextLine("§eМагазин");
        hologram.addTextLine("§fНажмите, чтобы открыть!");
        npc = LastCraft.getEntityAPI().createNPC(location.clone(), "eyJ0aW1lc3RhbXAiOjE1MDc0ODMyMDU5OTAsInByb2ZpbGVJZCI6ImMxZWQ5N2Q0ZDE2NzQyYzI5OGI1ODFiZmRiODhhMjFmIiwicHJvZmlsZU5hbWUiOiJ5b2xvX21hdGlzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lMmRkMjM3M2FiYjE1YjQzNDJlOGUxNDA4ODk0ZDNjMjZkMmI0ODZiMGE3Mzk1M2I3YzBhZjk5NTM4OWJkNjMifX19", "bZokupLMIRLLo1Nx7JvnTwZskBz1ONVYCOS76X4rQPj+VLl4ZXYzPCKmK23L2rZ5LDArWzJxS0yLTh/zXkC4WF4BjlvNgr05JJ6bQ/LST+3V0JET676vz2FfDc0mv2tnHtatjqahi7U7Eq8exBPnPenDKrbAOvvKoJ/aS8WZqiWqjhCgum1kvrlclUU/KtZo5IlnkWQLK9at4n46GgqFYYfMNOE6v1oMAwrj1XpZv+7aotl+oNgKoZ3G7AVXgBgf1BS4X7PQY1nJTYMdPrQN2ZfHqx5bwKkNwdxYCXtERrb43zARPuAkR6x1Bj40Pn0YJ8TUhp9DlYzcOxNIiCdhvxk9UMmE8A0EjbUFFevsKkKovJ+aSzEhGzTSNKbV2zGsplm79qgz0+FbhUHAsr2L7C8Lca7rkR1CJzMAY/rCxjkzourCiA8Cwu04c78gM7azxu8YI+Ab2sjM7Y4lCKt1/ObkFF3cYCg8OCWvnJe6zaD9iWmxSQKpaGcy5QKbt/F8bXqEvbldeKpdnWsGRYEmH3Rh9aKy01ogcC3cB/Z4wezB1797DgcrVhz/Kbl3Z2PB/QgEou6Rcq3DQFUJSaU1SoMW+vkD9YicbZ29mGECPmIMw0KrvCIvhSu9Tdm5uLpmHwxQyOsWk4Ks7IavwpVOcwCvDHtjUxGlgReJic7KYkU=");
        shopsNPC.add(this);
    }

    public static List<ShopNPC> getShopsNPC() {
        return shopsNPC;
    }

    public NPC getNpc() {
        return npc;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public static void clearData() {
        for (ShopNPC shopNPC : shopsNPC) {
            shopNPC.getHologram().remove();
        }
        ShopNPC.shopsNPC.clear();
    }
}
