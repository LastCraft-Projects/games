package net.lastcraft.eggwars.managers;

import com.google.common.collect.Lists;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.base.sql.ConnectionConstants;
import net.lastcraft.base.sql.api.MySqlDatabase;

import java.util.List;

public class ShopLoader
{

    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();
    private final MySqlDatabase mySqlDatabase = MySqlDatabase.newBuilder()
                                                    .host("s2" + ConnectionConstants.DOMAIN.getValue())
                                                    .password(ConnectionConstants.PASSWORD.getValue())
                                                    .data("EggWarsShop")
                                                    .user("root")
                                                    .create();

    public ShopLoader() {
        connect();
    }
    
    private void connect() {
        mySqlDatabase.execute("CREATE TABLE IF NOT EXISTS `Shop` (`id` INT(11) AUTO_INCREMENT PRIMARY KEY, `playerId` INT(11), `itemId` INT)");
        mySqlDatabase.execute("CREATE TABLE IF NOT EXISTS `Choose` (`id` INT(11) AUTO_INCREMENT PRIMARY KEY,`playerId` INT(11), `itemId` INT)");
    }
    
    public void close() {
        mySqlDatabase.close();
    }
    
    public List<Integer> loadData(String player, String table) {
        int idPlayer = GAMER_MANAGER.getGamer(player).getPlayerID();

        return mySqlDatabase.executeQuery("SELECT * FROM `" + table + "` WHERE `playerId` = ?;", rs -> {
            List<Integer> data = Lists.newArrayList();

            while (rs.next())
                data.add(rs.getInt("itemId"));

            return data;
        }, idPlayer);
    }
    
    public void createData(String player, String table, int id) {
        int playerID = GAMER_MANAGER.getGamer(player).getPlayerID();

        mySqlDatabase.execute("INSERT INTO `" + table + "` (`playerId`, `itemId`) VALUES (?, ?)", playerID, id);
    }
    
    public void updateData(String player, int id) {
        int playerID = GAMER_MANAGER.getGamer(player).getPlayerID();

        mySqlDatabase.execute("UPDATE `Choose` SET `itemId` = ? WHERE `playerId` = ? LIMIT 1;", id, playerID);
    }
}