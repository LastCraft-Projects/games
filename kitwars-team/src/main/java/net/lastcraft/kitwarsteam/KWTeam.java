package net.lastcraft.kitwarsteam;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class KWTeam {

    private String team;
    private String name;
    private String shortName;
    private ChatColor chatColor;
    private Location spawn;
    private int kills = 0;

    private Set<Player> players;
    private Set<Player> playersname;

    private static Map<String, KWTeam> teams = new HashMap<>();

    public static Map<String, KWTeam> getTeams() {
        return teams;
    }

    public KWTeam(String team, String name, ChatColor chatColor, String shortName, Location spawn) {
        this.shortName = shortName;
        this.team = team;
        this.name = name;
        this.chatColor = chatColor;
        this.spawn = spawn;

        players = new HashSet<>();
        playersname = new HashSet<>();

        teams.put(team, this);
    }

    public Collection<Player> getPlayersInTeam() {
        return this.players;
    }

    public boolean playerInTeam(Player player){
        return players.contains(player);
    }

    public void addKills() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public void addPlayer(Player player) {
        players.add(player);
        playersname.add(player);
        new KWPlayer(player, this);
    }

    public Location getSpawn() {
        return spawn;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public Collection<Player> getPlayers(){
        return players;
    }

    public String getShortName() {
        return shortName;
    }

    public void setTags() {
        PlayerTag friendTags = LastCraft.getScoreBoardAPI().createTag("§a§l" + shortName + " §a");
        friendTags.addPlayersToTeam(players);
        friendTags.setPrefix("§a§l" + shortName + " §a");
        friendTags.sendTo(players);

        for (KWTeam team : teams.values()) {
            if (!team.equals(this)) {
                PlayerTag enemyTags = LastCraft.getScoreBoardAPI().createTag(1 + "§c§l" + team.getShortName() + " §c");
                enemyTags.addPlayersToTeam(team.getPlayers());
                enemyTags.setPrefix("§c§l" + team.getShortName() + " §c");
                enemyTags.sendTo(players);
            }
        }
    }

    public String getDisplayName(){
        return chatColor + name;
    }

    public int size() {
        int size = 0;
        for (Player player : players) {
            if (PlayerUtil.isAlive(player)) {
                size++;
            }
        }
        return size;
    }

    public Set<Player> getPlayersName() {
        return playersname;
    }
}
