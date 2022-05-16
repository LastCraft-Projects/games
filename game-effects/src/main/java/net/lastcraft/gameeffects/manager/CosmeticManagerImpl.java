package net.lastcraft.gameeffects.manager;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticManager;

import java.util.List;
import java.util.stream.Collectors;

public class CosmeticManagerImpl implements CosmeticManager {

    private final TIntObjectMap<Cosmetic> cosmetics = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    @Override
    public Cosmetic getCosmetic(int id) {
        return cosmetics.get(id);
    }

    @Override
    public void addCosmetic(Cosmetic cosmetic) {
        if (cosmetic == null) {
            return;
        }

        cosmetics.putIfAbsent(cosmetic.getId(), cosmetic);
    }

    @Override
    public TIntObjectMap<Cosmetic> getAllCosmetics() {
        return new TIntObjectHashMap<>(cosmetics);
    }

    @Override
    public List<Cosmetic> getCosmetics(GameCosmeticType gameCosmeticType) {
        return getAllCosmetics().valueCollection().stream()
                .filter(cosmetic -> cosmetic.getType() == gameCosmeticType)
                .collect(Collectors.toList());
    }
}
