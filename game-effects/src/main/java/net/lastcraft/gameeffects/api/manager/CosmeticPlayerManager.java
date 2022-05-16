package net.lastcraft.gameeffects.api.manager;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public interface CosmeticPlayerManager {

    /**
     * Получить юзера который хранит инфу
     * @param player - игрок bukkit'a
     * @param player - ник игрока
     * @return - объект юзера
     */
    CosmeticPlayer getCosmeticPlayer(Player player);
    CosmeticPlayer getCosmeticPlayer(String name);
    CosmeticPlayer getOrCreateCosmeticPlayer(BukkitGamer gamer);

    /**
     * получить список всех юзеров
     * в памяти сервера
     * @return - мапа с юзерами (ник - юзер)
     */
    Map<String, CosmeticPlayer> getCosmeticPlayers();

    /**
     * добавить юзера в память
     * @param cosmeticPlayer - юзер которого надо добавить
     */
    void addCosmeticPlayer(CosmeticPlayer cosmeticPlayer);

    /**
     * Удалить юзера из памяти
     * @param cosmeticPlayer - юзер которого удалить
     * @param name - ник юзера, которого надо удалить
     */
    void removeCosmeticPlayer(CosmeticPlayer cosmeticPlayer);
    void removeCosmeticPlayer(String name);
}
