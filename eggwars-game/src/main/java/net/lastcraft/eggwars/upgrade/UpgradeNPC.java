package net.lastcraft.eggwars.upgrade;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.entity.npc.NPC;
import net.lastcraft.api.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class UpgradeNPC {
    private Hologram hologram;
    private NPC npc;

    private static List<UpgradeNPC> upgradeNPCs = new ArrayList<>();

    public UpgradeNPC(Location location) {
        hologram = LastCraft.getHologramAPI().createHologram(location.clone().add(0.0, 1.65, 0.0));
        hologram.addTextLine("§eМагазин улучшений");
        hologram.addTextLine("§fНажмите, чтобы открыть!");
        npc = LastCraft.getEntityAPI().createNPC(location.clone(), "eyJ0aW1lc3RhbXAiOjE1MTAyNjU5NTc0MDYsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc4NzJiMTk0YzhjZDdiZWVlMjNiYjdlOTU0MTZkYWRiNmZlZDAwMzViYzg5NTRkOGY0NjljODhlNDM3NiJ9fX0=", "RMzlKRp5PsDkoFh/oD7eZ+V7GoBsoqcbYCtgcEnyODbWYLg+QUblJAID5ASrXrLUlJHFK2A+sG1kQJb0IbPl8o05JhAabGiFNd6PW37y8akC8JW6Wa0vdw1WADUr+jYRjwtfaPnoLErRrGtwHEgDw7G/5qLHlQCg7dAHQAq6gS7haOScXg2uqYCKwnI7yTWW5udxsxy1rTWCT8dls1V+HmNV62arBT+3lSUydGqgrk0ch1KPocsMg8He3kNV3s3m2on4ycH33ZGbq/yHmCfrLzTTpv8oXUXA7mF5KJlBpxTp/mFzz52gMQZ3ReoJbQXWzLZPgBUTHRPzLl8GR9AZE8XxuZ6fv0CEt6Vif6Kam0LV47ApIblX+IoPBmLeu44QAxEgN5njMkSfW/6SR2F7bKzTISmLcgEvmjn4SpgL7Nao3V/lclUPivFv1VZMay9/ecmVr4oPu6YqvBAN2oto2GOyHhlzTxuhd2I0O90ZJ+TDH9PsYYtseV+iwoJ3dzornQLWOb4oPxLAIM2xj6eKpBvzfWdaH0PvIuVnSusXwE2yu7bYAVHWgOwdhULhMQbETx0yPc9CVU6b6maR7zPL2HV1TMZYo9+ebDguz53xIQ/HpBX0lqazYamfagrgHdAB9fRjOYDww31TA3EzKt+AHyEAeXkH0JiFnjuERgfze3Q=");
        upgradeNPCs.add(this);
    }

    public static List<UpgradeNPC> getUpgradeNPCs() {
        return upgradeNPCs;
    }

    public NPC getNpc() {
        return npc;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public static void clearData() {
        for (UpgradeNPC upgradeNPC : upgradeNPCs) {
            upgradeNPC.getHologram().remove();
        }
        UpgradeNPC.upgradeNPCs.clear();
    }
}
