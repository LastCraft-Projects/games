package net.lastcraft.gameeffects.lobby;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.gamer.constans.KeyType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.box.util.BoxUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.manager.CosmeticPlayerManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import net.lastcraft.lobby.api.LobbyAPI;
import net.lastcraft.lobby.api.leveling.LevelReward;

public final class CosmeticLevelReward extends LevelReward {

    private static final CosmeticPlayerManager COSMETIC_PLAYER_MANAGER = GameEffectsAPI.getCosmeticPlayerManager();

    private final Cosmetic cosmetic;

    public CosmeticLevelReward(int level, Cosmetic cosmetic) {
        this.cosmetic = cosmetic;

        LobbyAPI.getLeveling().addReward(level, this);
    }

    @Override
    public void giveReward(BukkitGamer gamer) {
        BukkitUtil.runTaskAsync(() -> {
            CosmeticPlayer loadCosmeticPlayer = COSMETIC_PLAYER_MANAGER.getOrCreateCosmeticPlayer(gamer);
            if (loadCosmeticPlayer == null) {
                return;
            }

            if (loadCosmeticPlayer.hasCosmetic(cosmetic)) {
                int money = BoxUtil.getMoney(KeyType.GAME_COSMETIC_KEY, cosmetic.getRarity());
                gamer.changeMoney(PurchaseType.MYSTERY_DUST, money);
                return;
            }

            loadCosmeticPlayer.buyCosmetic(cosmetic);
        });
    }

    @Override
    public String getLore(Language lang) {
        return lang.getMessage("LEVEL_REWARD_COSMETIC",
                cosmetic.getRarity().getColor() + cosmetic.getName(lang)
                        + " §8(§7" + lang.getMessage("GAME_COSMETIC_GUI_" + cosmetic.getType() + "_NAME") + "§8)");
    }

    @Override
    public int getPriority() {
        return 600 + cosmetic.getId();
    }
}
