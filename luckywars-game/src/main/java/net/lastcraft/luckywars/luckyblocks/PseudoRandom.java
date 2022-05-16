package net.lastcraft.luckywars.luckyblocks;

import net.lastcraft.luckywars.LuckyWars;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PseudoRandom {

    private static Map<String, Integer> players = new ConcurrentHashMap<>();

    public static void reset(){
        players.clear();
    }

    public static int getChance(Player player){
        String playerName = player.getName().toLowerCase();
        players.putIfAbsent(playerName, 0);
        players.put(playerName, players.get(playerName)+1);
        int number = LuckyWars.getInstance().getRandom().nextInt(players.get(playerName));
        if (number >= 3){
            players.put(playerName, 1);
        }
        return number;
    }

}
