package net.lastcraft.gameeffects.api.cosmetic;

import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Cosmetic {

    int getId();

    /**
     * узнать тип косметика
     * @return - тип
     */
    GameCosmeticType getType();

    /**
     * узнать какой айтем
     * @return - айтем
     */
    ItemStack getItemStack();

    Rarity getRarity();

    /**
     * Имя предмета
     * @param lang - язык
     * @return - имя предмета
     */
    String getName(Language lang);
    List<String> getLore(Language lang);

    /**
     * как купить его и сколько стоит
     * @return - за что покупаются головы
     */
    PurchaseType getPurchaseType();
    int getPrice();

    /**
     * давать бесплатно шулкерам или нет
     * @return - да или нет
     */
    boolean isShulkerFree();

    /**
     * мин лвл, который нужно, чтобы использовать его в игре!
     * @return - левел
     */
    int getMinLevel();

    /**
     * уровень с которого дают эту награду
     * @return - уровень
     */
    int getRewardLevel();

    /**
     * купить косметик (добавит сразу в БД игроку и снять деньги)
     * @param player - кто покупает
     * @return купить
     */
    boolean onPurchase(CosmeticPlayer player);
}
