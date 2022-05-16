package net.lastcraft.eggwars;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.game.GameModeType;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.scoreboard.PlayerTag;
import net.lastcraft.dartaapi.gamemodes.SpectatorMode;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.CoreUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.eggwars.event.EggBrokenEvent;
import net.lastcraft.eggwars.game.GameBoard;
import net.lastcraft.eggwars.game.GameFactory;
import net.lastcraft.eggwars.generator.Generator;
import net.lastcraft.eggwars.shop.OtherMenu;
import net.lastcraft.eggwars.shop.ShopNPC;
import net.lastcraft.eggwars.upgrade.UpgradeNPC;
import net.lastcraft.eggwars.upgrade.UpgradeTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class EWTeam {

    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();

    private String name;
    private ChatColor chatColor;
    private Color color;
    private short subID;
    private Location spawn;
    private Location egg;
    private boolean canRespawn;
    private UpgradeTeam upgradeTeam;

    private String shortName;

    private Set<String> players;

    private static Map<String, EWTeam> teams = new HashMap<>();

    public static Map<String, EWTeam> getTeams() {
        return teams;
    }

    public static EWTeam getPlayerTeam(Player player){
        for (EWTeam ewTeam : teams.values()){
            if (ewTeam.playerInTeam(player)){
                return ewTeam;
            }
        }
        return null;
    }

    public Collection<Player> getPlayersInTeam() {
        return players.stream()
                .filter(s -> Bukkit.getPlayer(s) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    public static EWTeam getEWTeamByEgg(Location location){
        for (EWTeam ewTeam : teams.values()){
            Location egg = ewTeam.getEgg();
            if (egg.getBlockX() == location.getBlockX() && egg.getBlockY() == location.getBlockY() && egg.getBlockZ() == location.getBlockZ()){
                return ewTeam;
            }
        }
        return null;
    }

    public EWTeam(String team, String name, ChatColor chatColor, Color color, short subID, Location spawn, Location egg, String shortName) {
        this.name = name;
        this.chatColor = chatColor;
        this.color = color;
        this.subID = subID;
        this.spawn = spawn;
        this.egg = egg;
        this.shortName = shortName;

        this.upgradeTeam = new UpgradeTeam(color);

        canRespawn = true;

        players = new HashSet<>();

        teams.put(team, this);
    }

    public boolean playerInTeam(Player player){
        return getPlayersInTeam().contains(player);
    }

    public void addPlayer(Player player) {
        players.add(player.getName().toLowerCase());
        new OtherMenu(player);
        player.setDisplayName(chatColor.toString() + player.getName());
        spawn(player);
    }

    private void spawn(Player player) {
        respawn(player);

        for (ShopNPC shopNPC : ShopNPC.getShopsNPC()) {
            shopNPC.getNpc().showTo(player);
            shopNPC.getHologram().showTo(player);
        }
        for (UpgradeNPC upgradeNPC : UpgradeNPC.getUpgradeNPCs()) {
            upgradeNPC.getNpc().showTo(player);
            upgradeNPC.getHologram().showTo(player);
        }
        for (Generator generator : Generator.getGenerators()) {
            generator.getHologram().showTo(player);
        }
    }

    public void rejoin(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);

        gamer.setGameMode(GameModeType.SURVIVAL);
        SpectatorMode.removeSpectatorMode(player);

        spawn(player);

        for (EWTeam team : EWTeam.getTeams().values())
            team.setTags();

        new GameBoard(player);

        new OtherMenu(player);
        player.setDisplayName(chatColor.toString() + player.getName());

        GameFactory.getStats().createPlayerStats(player);
    }

    public short getSubID(){
        return subID;
    }

    public Location getEgg(){
        return egg;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public void disableRespawn(){
        canRespawn = false;
    }

    public void brokeEgg(Player who) {
        BukkitUtil.callEvent(new EggBrokenEvent(this, who));
        disableRespawn();
    }

    public boolean isCanRespawn(){
        return canRespawn;
    }

    public void respawn(Player player){
        /* AAC BUG FIX
          player.setFallDistance(0);
          player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 4));
          player.setHealth(player.getMaxHealth());
          player.setFallDistance(0);
          player.setLastDamage(0); */

        player.teleport(spawn.clone().add(0, 0.3, 0));
        BukkitUtil.runTask(() -> upgradeTeam.addItems(player));
    }

    public UpgradeTeam getUpgradeTeam() {
        return upgradeTeam;
    }

    public Collection<Player> getPlayers(){
        return getPlayersInTeam()
                .stream()
                .filter(PlayerUtil::isAlive)
                .collect(Collectors.toSet());
    }

    public Location getSpawn(){
        spawn.setWorld(Bukkit.getWorld(CoreUtil.getGameWorld()));
        return spawn;
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

    public String getShortName() {
        return shortName;
    }

    public void setTags() {
        Collection<Player> players = getPlayersInTeam();

        PlayerTag friendTags = LastCraft.getScoreBoardAPI().createTag(0 + chatColor.toString() + "§l" + shortName + " " + chatColor.toString());
        friendTags.addPlayersToTeam(players);
        friendTags.setPrefix(chatColor.toString() + "§l" + shortName + " " + chatColor.toString());
        friendTags.sendTo(players);

        for (EWTeam team : teams.values()) {
            if (!team.equals(this)) {
                PlayerTag enemyTags = LastCraft.getScoreBoardAPI().createTag(1 + "§c§l" + team.getShortName() + " §c");
                enemyTags.addPlayersToTeam(team.getPlayers());
                enemyTags.setPrefix(team.getChatColor().toString() + "§l" + team.getShortName() + " " + team.getChatColor().toString());
                enemyTags.sendTo(players);
            }
        }
    }
}
