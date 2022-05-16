package net.lastcraft.gameeffects.lobby;

import gnu.trove.TCollections;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.gamer.constans.KeyType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.base.util.StringUtil;
import net.lastcraft.box.api.BoxAPI;
import net.lastcraft.box.api.ItemBox;
import net.lastcraft.box.api.ItemBoxManager;
import net.lastcraft.box.type.KeysBox;
import net.lastcraft.box.util.BoxUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;

public final class CosmeticBox extends ItemBox {

    private static final ItemBoxManager BOX_MANAGER = BoxAPI.getItemBoxManager();
    private static final CosmeticPlayerManager COSMETIC_PLAYER_MANAGER = GameEffectsAPI.getCosmeticPlayerManager();

    public static final TIntList MONEY_PLAYERS = TCollections.synchronizedList(new TIntArrayList());

    private final Cosmetic cosmetic;

    public CosmeticBox(Cosmetic cosmetic) {
        super(cosmetic.getItemStack(), cosmetic.getRarity());
        this.cosmetic = cosmetic;

        BOX_MANAGER.addItemBox(KeyType.GAME_COSMETIC_KEY, this);
    }

    @Override
    public String getName(Language lang) {
        return cosmetic.getRarity().getColor() + cosmetic.getName(lang)
                + " §f(" + lang.getMessage("GAME_COSMETIC_GUI_" + cosmetic.getType() + "_NAME") + "§f)";
    }

    @Override
    public void onApply(BukkitGamer gamer) {
        BukkitUtil.runTaskAsync(() -> {
            CosmeticPlayer loadCosmeticPlayer = COSMETIC_PLAYER_MANAGER.getOrCreateCosmeticPlayer(gamer);
            if (loadCosmeticPlayer == null) {
                return;
            }

            if (loadCosmeticPlayer.hasCosmetic(cosmetic)) {
                int money = BoxUtil.getMoney(KeyType.GAME_COSMETIC_KEY, cosmetic.getRarity());
                gamer.changeMoney(PurchaseType.MYSTERY_DUST, money);

                MONEY_PLAYERS.add(gamer.getPlayerID()); //добавляем в мапу

                return;
            }

            loadCosmeticPlayer.buyCosmetic(cosmetic);
        });
    }

    @Override
    public void onMessage(BukkitGamer gamer) {
        if (!MONEY_PLAYERS.remove(gamer.getPlayerID())) {
            return;
        }

        int money = BoxUtil.getMoney(KeyType.GAME_COSMETIC_KEY, cosmetic.getRarity());

        Language lang = gamer.getLanguage();
        String moneyString = StringUtil.getCorrectWord(money, "MONEY_1", lang);
        gamer.sendMessage("§b" + lang.getMessage("BOX_NAME")
                + " §8| " + lang.getMessage("GAME_COSMETIC_ALREADY_HAVE",
                StringUtil.getNumberFormat(money), moneyString));

        gamer.sendActionBar("§6+" + money + " " + moneyString);
    }

    public static void initKeys() {
        //мб еще что-то добавить
        BOX_MANAGER.addItemBox(KeyType.GAME_COSMETIC_KEY, new KeysBox(2, Rarity.RARE, KeyType.GAME_COSMETIC_KEY));
        BOX_MANAGER.addItemBox(KeyType.GAME_COSMETIC_KEY, new KeysBox(3, Rarity.EPIC, KeyType.GAME_COSMETIC_KEY));
        BOX_MANAGER.addItemBox(KeyType.GAME_COSMETIC_KEY, new KeysBox(4, Rarity.LEGENDARY, KeyType.GAME_COSMETIC_KEY));
        BOX_MANAGER.addItemBox(KeyType.GAME_COSMETIC_KEY, new KeysBox(5, Rarity.LEGENDARY, KeyType.GAME_COSMETIC_KEY));
    }
}
