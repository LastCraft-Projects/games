package net.lastcraft.luckywars;

import net.lastcraft.api.LastCraft;
import net.lastcraft.gameapi.items.registry.GameItemRegistry;
import net.lastcraft.luckywars.cage.LuckyWarsCageList;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyWars extends JavaPlugin {

    @Override
    public void onEnable() {
        if (LastCraft.isGame()) {
            new LuckyWarsGame(this);
        } else if(LastCraft.isLobby()){
            //todo lobby

            loadGameItems();
        }
    }

    public static void loadGameItems() {
        //Cages
        GameItemRegistry.addItem(LuckyWarsCageList.NETHER_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.WOOD_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.SPRUCE_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.BIRCH_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.JUNGLE_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.ACACIA_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.DARK_OAK_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.IRON_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.LEAVES_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.SLIME_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.ICE_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.GLASS_CAGE);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_1);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_2);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_3);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_4);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_5);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_6);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_7);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_8);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_9);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_10);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_11);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_12);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_13);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_14);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_15);
        GameItemRegistry.addItem(LuckyWarsCageList.STAINED_GLASS_CAGE_16);
    }
}
