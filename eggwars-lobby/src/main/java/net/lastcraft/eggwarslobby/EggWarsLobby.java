package net.lastcraft.eggwarslobby;

import lombok.Getter;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.KeyType;
import net.lastcraft.box.api.BoxAPI;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.eggwars.managers.DataManager;
import net.lastcraft.eggwars.managers.ShopLoader;
import net.lastcraft.eggwars.perks.listeners.PerkListener;
import net.lastcraft.eggwarslobby.box.PerkBox;
import net.lastcraft.eggwarslobby.listeners.EWBoardListener;
import net.lastcraft.eggwarslobby.listeners.ShopListener;
import net.lastcraft.eggwarslobby.shop.perks.PerkItem;
import net.lastcraft.lobby.api.LobbyAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Kambet on 30.04.2018
 */
@Getter
public class EggWarsLobby extends JavaPlugin implements Listener
{
    @Getter
    private static EggWarsLobby instance;

    private ShopLoader shopLoader;

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        //Perks
        DataManager.loadPerks();

        //Boxes and items
        initAll();

        //Listeners
        new PerkListener();
        LobbyAPI.setBoardLobby(new EWBoardListener());
        new ShopListener();

        //Shop loaders
        shopLoader = new ShopLoader();
    }

    private void initAll() {
        for (Perk perk : Perk.perks) {
            new PerkItem(perk);

            if (perk.getRarity() != Rarity.NONE) {
                BoxAPI.getItemBoxManager().addItemBox(KeyType.GAME_KEY, new PerkBox(perk));
            }
        }
    }
}