package net.lastcraft.eggwars;

import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.util.ConfigManager;
import net.lastcraft.connector.Core;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.eggwars.game.CreateGame;
import net.lastcraft.eggwars.game.GameFactory;
import net.lastcraft.eggwars.managers.DataManager;
import net.lastcraft.eggwars.managers.ShopLoader;
import net.lastcraft.eggwars.perks.listeners.PerkListener;
import net.lastcraft.eggwars.shop.OtherMenuData;
import net.lastcraft.eggwars.shop.ShopMenu;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EggWars extends JavaPlugin {

    private static EggWars instance;

    private ShopLoader shopLoader;

    private List<Location> villagerShopSpawns = new ArrayList<>();
    private List<Location> villagerUpgradeSpawns = new ArrayList<>();

    public static EggWars getInstance(){
        return instance;
    }

    public List<Location> getVillagerShopSpawns(){
        return villagerShopSpawns;
    }

    public List<Location> getVillagerUpgradeSpawns(){
        return villagerUpgradeSpawns;
    }

    public ShopLoader getShopLoader() {
        return shopLoader;
    }

    @Override
    public void onLoad(){
        instance = this;
    }

    @Override
    public void onEnable(){
        shopLoader = new ShopLoader();
        DataManager.loadPerks();

        new PerkListener();

        new CreateGame();
        new GameFactory();

        loadConfig();

        CoreUtil.registerGame(CoreUtil.getGameWorld() + " " + GameSettings.numberOfTeams + "x" + GameSettings.playersInTeam);
    }

    @Override
    public void onDisable() {
        shopLoader.close();
    }

    private void loadConfig(){
        ConfigManager cmShop = new ConfigManager(new File("/home/lastcraft/create/" + Core.getUsername().substring(0, 3) + "/config/"), "event.yml");
        FileConfiguration configShop = cmShop.getConfig();

        for (String menu : configShop.getConfigurationSection("Shop").getKeys(false)){

            String menuPath = "Shop." + menu + ".";

            String menuName = configShop.getString(menuPath + "Name");
            String menuIcon = configShop.getString(menuPath + "Icon");
            int menuSlot = configShop.getInt(menuPath + "Slot");

            OtherMenuData otherMenu = new OtherMenuData(menuName, menuIcon, menuSlot);

            for (String item : configShop.getConfigurationSection(menuPath + "Items").getKeys(false)){

                String itemPath = menuPath + ".Items." + item + ".";

                String itemName = configShop.getString(itemPath + "Name");
                String itemIcon = configShop.getString(itemPath + "Icon");
                int itemSlot = configShop.getInt(itemPath + "Slot");
                List<String> itemLore = configShop.getStringList(itemPath + "Lore");
                String itemCost = configShop.getString(itemPath + "Cost");
                List<String> giveItems = configShop.getStringList(itemPath + "Give");

                otherMenu.addShopItem(itemName, itemIcon, itemSlot, itemLore, itemCost, giveItems);
            }
        }

        new ShopMenu();
    }
}
