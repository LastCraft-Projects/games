package net.lastcraft.gameeffects.api.player;

import net.lastcraft.api.depend.BaseUser;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;

import java.util.List;
import java.util.Map;

public interface CosmeticPlayer extends BaseUser {

    boolean hasCosmetic(Cosmetic cosmetic);
    boolean hasCosmetic(int cosmeticId);

    Map<GameCosmeticType, Cosmetic> getActiveCosmetics();
    <T extends Cosmetic> T getActiveCosmetic(GameCosmeticType type);

    /**
     * купить косметику (добавить в БД просто, деньги не забираем)
     * @param cosmetic - косметика
     */
    void buyCosmetic(Cosmetic cosmetic);

    /**
     * получить все косметики, что доступны игроку
     * @return - гаджеты
     */
    List<Cosmetic> getCosmetics();
    List<Cosmetic> getCosmetics(GameCosmeticType type);

    /**
     * активация/деактивация косметики
     */
    void enableCosmetic(Cosmetic cosmetic);
    void disableCosmetic(Cosmetic cosmetic);
}
