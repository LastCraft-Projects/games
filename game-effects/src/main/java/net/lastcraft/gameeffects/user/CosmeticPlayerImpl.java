package net.lastcraft.gameeffects.user;

import gnu.trove.map.TIntObjectMap;
import lombok.AllArgsConstructor;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.gamer.IBaseGamer;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import net.lastcraft.gameeffects.manager.SqlManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CosmeticPlayerImpl implements CosmeticPlayer {

    private final BukkitGamer gamer;
    private final Map<GameCosmeticType, Cosmetic> activeCosmetic;
    private final TIntObjectMap<Cosmetic> all; //все доступные
    private final TIntObjectMap<Cosmetic> buyCosmetics; //купленные только

    @Override
    public boolean hasCosmetic(Cosmetic cosmetic) {
        return cosmetic != null && hasCosmetic(cosmetic.getId());
    }

    @Override
    public boolean hasCosmetic(int cosmeticId) {
        return all.containsKey(cosmeticId);
    }

    @Override
    public Map<GameCosmeticType, Cosmetic> getActiveCosmetics() {
        return new HashMap<>(activeCosmetic);
    }

    @Override
    public <T extends Cosmetic> T getActiveCosmetic(GameCosmeticType gameCosmeticType) {
        return (T) activeCosmetic.get(gameCosmeticType);
    }

    @Override
    public void buyCosmetic(Cosmetic cosmetic) {
        if (all.containsKey(cosmetic.getId()) || buyCosmetics.containsKey(cosmetic.getId())) {
            return;
        }

        all.put(cosmetic.getId(), cosmetic);
        buyCosmetics.put(cosmetic.getId(), cosmetic);
        SqlManager.buyCosmetic(getPlayerID(), cosmetic);
    }

    @Override
    public List<Cosmetic> getCosmetics() {
        return new ArrayList<>(all.valueCollection());
    }

    @Override
    public List<Cosmetic> getCosmetics(GameCosmeticType type) {
        return all.valueCollection().stream()
                .filter(cosmetic -> cosmetic.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void enableCosmetic(Cosmetic cosmetic) {
        Cosmetic old = getActiveCosmetic(cosmetic.getType());
        if (old != null) {
            if (old.getId() == cosmetic.getId()) {
                return;
            }

            disableCosmetic(old);
        }

        activeCosmetic.put(cosmetic.getType(), cosmetic);
        SqlManager.activeCosmetic(getPlayerID(), cosmetic, !buyCosmetics.containsKey(cosmetic.getId()));
    }

    @Override
    public void disableCosmetic(Cosmetic cosmetic) {
        Cosmetic old = getActiveCosmetic(cosmetic.getType());
        if (old == null) {
            return;
        }

        if (old.getId() != cosmetic.getId()) {
            return;
        }

        activeCosmetic.remove(cosmetic.getType());
        SqlManager.deactiveCosmetic(getPlayerID(), cosmetic);
    }

    @Override
    public int getPlayerID() {
        return gamer == null ? -1 : gamer.getPlayerID();
    }

    @Override
    public Player getPlayer() {
        return gamer.getPlayer();
    }

    @Override
    public String getName() {
        return gamer.getName();
    }

    @Override
    public IBaseGamer getGamer() {
        return gamer;
    }
}
