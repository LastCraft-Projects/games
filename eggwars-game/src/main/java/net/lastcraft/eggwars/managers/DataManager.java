package net.lastcraft.eggwars.managers;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.eggwars.perks.types.immovable.Immovable;
import net.lastcraft.eggwars.perks.types.maniac.Maniac;
import net.lastcraft.eggwars.perks.types.timemanager.TimeManager;

/**
 * Created by Kambet on 30.04.2018
 */
public class DataManager {

    public static void loadPerks() {
        new Immovable(Rarity.LEGENDARY, 4000);
        new Maniac(Rarity.EPIC, 3000);
        //new Necromancer(Rarity.Rare, 2000);
        new TimeManager(Rarity.RARE, 2500);
        //new Scrooge(Rarity.Epic, 3100);
    }

}
