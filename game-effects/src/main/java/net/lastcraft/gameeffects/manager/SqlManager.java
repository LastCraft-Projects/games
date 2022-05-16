package net.lastcraft.gameeffects.manager;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.experimental.UtilityClass;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.base.gamer.constans.Group;
import net.lastcraft.base.sql.PlayerInfoLoader;
import net.lastcraft.base.sql.api.MySqlDatabase;
import net.lastcraft.base.sql.api.query.MysqlQuery;
import net.lastcraft.base.sql.api.query.QuerySymbol;
import net.lastcraft.base.sql.api.table.ColumnType;
import net.lastcraft.base.sql.api.table.TableColumn;
import net.lastcraft.base.sql.api.table.TableConstructor;
import net.lastcraft.gameeffects.api.GameEffectsAPI;
import net.lastcraft.gameeffects.api.cosmetic.Cosmetic;
import net.lastcraft.gameeffects.api.cosmetic.GameCosmeticType;
import net.lastcraft.gameeffects.api.manager.CosmeticManager;
import net.lastcraft.gameeffects.api.player.CosmeticPlayer;
import net.lastcraft.gameeffects.user.CosmeticPlayerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class SqlManager {

    private final CosmeticManager COSMETIC_MANAGER = GameEffectsAPI.getCosmeticManager();

    private static final MySqlDatabase MY_SQL_DATABASE = PlayerInfoLoader.getMysqlDatabase();

    CosmeticPlayer loadCosmeticPlayer(BukkitGamer gamer) {
        if (gamer == null) {
            return null;
        }

        int playerID = gamer.getPlayerID();

        Map<GameCosmeticType, Cosmetic> activeCosmetic = new ConcurrentHashMap<>();
        TIntObjectMap<Cosmetic> all = TCollections.synchronizedMap(new TIntObjectHashMap<>());
        TIntObjectMap<Cosmetic> buyCosmetics = TCollections.synchronizedMap(new TIntObjectHashMap<>());

        MY_SQL_DATABASE.executeQuery(MysqlQuery.selectFrom("buy_cosmetics")
                .where("player_id", QuerySymbol.EQUALLY, playerID), (rs) -> {
            while (rs.next()) {
                int id = rs.getInt("cosmetic");

                Cosmetic cosmetic = COSMETIC_MANAGER.getCosmetic(id);
                if (cosmetic == null)
                    continue;

                all.put(id, cosmetic);
                buyCosmetics.put(id, cosmetic);

                if (rs.getBoolean("active")) {
                    activeCosmetic.put(cosmetic.getType(), cosmetic);
                }
            }
            return Void.TYPE;
        });

        if (gamer.getGroup() == Group.SHULKER || gamer.getGroup() == Group.YOUTUBE) {
            COSMETIC_MANAGER.getAllCosmetics().valueCollection()
                    .stream()
                    .filter(Cosmetic::isShulkerFree)
                    .forEach(gadget -> all.put(gadget.getId(), gadget));
        }

        return new CosmeticPlayerImpl(gamer, activeCosmetic, all, buyCosmetics);
    }

    public void initTable() {
        new TableConstructor("buy_cosmetics",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).autoIncrement(true),
                new TableColumn("player_id", ColumnType.INT_11),
                new TableColumn("cosmetic", ColumnType.INT_2),
                new TableColumn("active", ColumnType.BOOLEAN).setDefaultValue(0)
        ).create(MY_SQL_DATABASE);
    }

    public void close() {
        MY_SQL_DATABASE.close();
    }

    public void buyCosmetic(int playerID, Cosmetic cosmetic) {
        if (playerID == -1)
            return;

        MY_SQL_DATABASE.execute(MysqlQuery.insertTo("buy_cosmetics")
                .set("player_id", playerID)
                .set("cosmetic", cosmetic.getId()));
    }

    public void activeCosmetic(int playerID, Cosmetic cosmetic, boolean insert) {
        if (playerID == -1) {
            return;
        }

        if (insert) {
            MY_SQL_DATABASE.execute(MysqlQuery.insertTo("buy_cosmetics")
                    .set("player_id", playerID)
                    .set("cosmetic", cosmetic.getId())
                    .set("active", 1));
            return;
        }

        MY_SQL_DATABASE.execute(MysqlQuery.update("buy_cosmetics")
                .where("player_id", QuerySymbol.EQUALLY, playerID)
                .where("cosmetic", QuerySymbol.EQUALLY, cosmetic.getId())
                .set("active", 1)
                .limit());
    }

    public void deactiveCosmetic(int playerID, Cosmetic cosmetic) {
        if (playerID == -1) {
            return;
        }

        MY_SQL_DATABASE.execute(MysqlQuery.update("buy_cosmetics")
                .where("player_id", QuerySymbol.EQUALLY, playerID)
                .where("cosmetic", QuerySymbol.EQUALLY, cosmetic.getId())
                .set("active", 0)
                .limit());
    }
}
