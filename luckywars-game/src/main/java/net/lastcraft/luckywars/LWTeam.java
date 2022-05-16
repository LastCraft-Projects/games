package net.lastcraft.luckywars;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.api.util.Head;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LWTeam {

    private static final ItemStack LUCKY_ITEM = ItemUtil.getBuilder(Head.LUCKY.getHead())
                                                            .setAmount(3)
                                                            .build();

    private String name;
    private ChatColor chatColor;
    private Color color;
    private short subID;
    private Location spawn;
    private String shortName;
    private Set<String> players = new HashSet<>();

    private static Map<String, LWTeam> teams = new ConcurrentHashMap<>();

    public static Map<String, LWTeam> getTeams() {
        return teams;
    }

    public static LWTeam getPlayerTeam(Player player){
        for (LWTeam lwTeam : teams.values()){
            if (lwTeam.playerInTeam(player)){
                return lwTeam;
            }
        }
        return null;
    }

    public List<Player> getPlayersInTeam() {
        return players.stream()
                .filter(s -> Bukkit.getPlayer(s) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    public LWTeam(Player player, Location spawn) {
        this.players.add(player.getName().toLowerCase());
        this.spawn = spawn;

        addPlayer(player);
        teams.put(player.getName().toLowerCase(), this);
    }

    public LWTeam(String team, String name, ChatColor chatColor, Color color, short subID, Location spawn, String shortName) {
        this.name = name;
        this.chatColor = chatColor;
        this.color = color;
        this.subID = subID;
        this.spawn = spawn;
        this.shortName = shortName;
        this.players = new HashSet<>();

        teams.put(team, this);
    }

    public Location getSpawn() {
        return spawn;
    }

    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return color;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public short getSubID() {
        return subID;
    }

    public String getShortName() {
        return shortName;
    }

    public void addPlayer(Player player) {
        if (player == null) {
            return;
        }
        players.add(player.getName().toLowerCase());
        spawn(player);
    }

    public void spawn(Player player) {
        if (player == null || spawn == null) {
            return;
        }

        if (chatColor != null) {
            player.setDisplayName(chatColor.toString() + player.getName());
        }

        player.teleport(spawn.clone().add(0, 0.3, 0));

        BukkitUtil.runTask(() -> {
            player.getInventory().addItem(new ItemStack(Material.WOOD, 16));
            player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
            player.getInventory().addItem(LUCKY_ITEM);
        });
    }

    public void removePlayer(Player player) {
        players.remove(player.getName().toLowerCase());
    }

    public boolean isAlive(){
        return size() > 0;
    }

    public String getDisplayName(){
        return chatColor + name;
    }

    public int size() {
        return getPlayers().size();
    }

    public boolean playerInTeam(Player player){
        return getPlayersInTeam().contains(player);
    }

    public void setTags() {
        PlayerTag friendTags = LastCraft.getScoreBoardAPI().createTag(((this.shortName == null) ? "" : ("§a§l" + this.shortName + " ")) + "§a");
        friendTags.addPlayersToTeam(getPlayersInTeam());
        friendTags.setPrefix(((this.shortName == null) ? "" : ("§a§l" + this.shortName + " ")) + "§a");
        friendTags.sendTo(getPlayersInTeam());

        for (LWTeam team : LWTeam.teams.values()) {
            if (!team.equals(this)) {
                PlayerTag enemyTags = LastCraft.getScoreBoardAPI().createTag(((team.getShortName() == null) ? "" : ("§c§l" + team.getShortName() + " ")) + "§c");
                enemyTags.addPlayersToTeam(team.getPlayersInTeam());
                enemyTags.setPrefix(((team.getShortName() == null) ? "" : ("§c§l" + team.getShortName() + " ")) + "§c");
                enemyTags.sendTo(getPlayersInTeam());
            }
        }
    }

}
