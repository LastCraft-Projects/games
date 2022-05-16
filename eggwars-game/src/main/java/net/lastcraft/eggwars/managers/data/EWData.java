package net.lastcraft.eggwars.managers.data;

import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.eggwars.EggWars;
import net.lastcraft.eggwars.managers.ShopLoader;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EWData
{
    private static final Map<String, EWData> DATA_MAP = new ConcurrentHashMap<>();

    public static Map<String, EWData> getDatas() {
        return DATA_MAP;
    }


    private List<Perk> boughtPerks = new ArrayList<>();
    private Perk lastPerk;
    
    public EWData(String player) {
        List<Integer> dataEW;

        ShopLoader shopLoader = EggWars.getInstance().getShopLoader();

        dataEW = shopLoader.loadData(player, "Shop");
        for (Integer value : dataEW) {
            if (value >= Perk.perks.size())
                continue;

            this.boughtPerks.add(Perk.perks.get(value));
        }

        dataEW = shopLoader.loadData(player, "Choose");

        for (Integer value : dataEW) {
            if (value == -1)
                continue;
            if (value >= Perk.perks.size())
                continue;

            this.lastPerk = Perk.perks.get(value);
        }

        if (dataEW.size() == 0)
            shopLoader.createData(player, "Choose", -1);

        DATA_MAP.put(player, this);
    }
    
    public static EWData getDataPlayer(String playerName) {
        EWData data = DATA_MAP.get(playerName);

        if (data == null) {
            return new EWData(playerName);
        }

        return data;
    }
    
    public static void removeDataPlayer(Player player) {
        DATA_MAP.remove(player.getName());
    }

    public List<Perk> getBoughtPerks() {
        return this.boughtPerks;
    }

    private void setLastPerk(Perk lastPerk) {
        this.lastPerk = lastPerk;
    }
    
    public Perk getLastPerk() {
        return this.lastPerk;
    }
    
    public static EWData getDataPlayer(Player player) {
        return getDataPlayer(player.getName());
    }
    
    public boolean hasPerk(Perk perk) {
        for (Perk bought : this.boughtPerks) {
            if (bought.getId() == perk.getId())
                return true;
        }

        return false;
    }
    
    public static void addPerk(String player, Perk perk) {
        getDataPlayer(player).getBoughtPerks().add(perk);
        EggWars.getInstance().getShopLoader().createData(player, "Shop", perk.getId());
    }
    
    public static void choosePerk(String player, Perk perk) {
        if (perk == null) {
            EggWars.getInstance().getShopLoader().updateData(player, -1);
        }
        else {
            EggWars.getInstance().getShopLoader().updateData(player, perk.getId());
        }

        getDataPlayer(player).setLastPerk(perk);
    }
}
