package net.lastcraft.gameeffects.api.manager;

import gnu.trove.map.TIntObjectMap;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;

import java.util.List;

public interface CosmeticManager {

    /**
     * получить косметик по айди
     * @param id - ид
     * @return - гаджет
     */
    Cosmetic getCosmetic(int id);

    /**
     * добавить косметику в память
     * @param cosmetic - косметика
     */
    void addCosmetic(Cosmetic cosmetic);

    TIntObjectMap<Cosmetic> getAllCosmetics();

    List<Cosmetic> getCosmetics(GameCosmeticType gameCosmeticType);
}
