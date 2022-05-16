package net.lastcraft.eggwarslobby.box;

import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.gamer.constans.KeyType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.base.locale.Language;
import net.lastcraft.box.api.ItemBox;
import net.lastcraft.box.util.BoxUtil;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwars.managers.data.EWData;

public class PerkBox extends ItemBox {

    private Perk perk;
    private boolean check;

    public PerkBox(final Perk perk) {
        super(perk.getItemPublic(), perk.getRarity());

        this.check = false;
        this.perk = perk;
    }

    @Override
    public String getName(Language language) {
        return "§7Умение §f(" + perk.getRarity().getName(Language.RUSSIAN).substring(0, 2) + perk.getName() + "§f)";
    }

    @Override
    public void onApply(BukkitGamer gamer) {
        this.check = false;

        if (!perk.has(gamer.getPlayer())) {
            EWData.addPerk(gamer.getName(), perk);
            check = true;
            return;
        }

        gamer.changeMoney(PurchaseType.MYSTERY_DUST, BoxUtil.getMoney(KeyType.GAME_KEY, getRarity()));
    }

    @Override
    public void onMessage(BukkitGamer gamer) {
        if (check)
            return;

        int money = BoxUtil.getMoney(KeyType.GAME_KEY, getRarity());

        gamer.sendActionBar("§6+" + money + " " + StringUtil.getCorrectWord(money, "монет", "а", "ы", ""));
        gamer.sendMessage("§b" + Language.RUSSIAN.getMessage( "BOX_NAME") + " §8| §fУ вас уже есть данное умение, вы получили §6+" + money + " " + StringUtil.getCorrectWord(money, "монет", "а", "ы", ""));
    }
}