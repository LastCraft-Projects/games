package net.lastcraft.kitwarsteam.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.entity.npc.types.HumanNPC;
import net.lastcraft.api.event.game.EndGameEvent;
import net.lastcraft.api.event.game.PlayerKillEvent;
import net.lastcraft.api.event.game.RestartGameEvent;
import net.lastcraft.api.event.game.StartGameEvent;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.game.TeamManager;
import net.lastcraft.api.hologram.Hologram;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.Head;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.game.team.SelectionTeam;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.games.TitleUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.kitwarsteam.KWPlayer;
import net.lastcraft.kitwarsteam.KWTeam;
import net.lastcraft.kitwarsteam.KitWars;
import net.lastcraft.kitwarsteam.ShopNPC;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameFactory extends DListener {

    @EventHandler
    protected void onStartGame(StartGameEvent e) {

        //создание НПС
        List<Location> locations = KitWars.getInstance().getVillagerSpawns();
        for (Location location : locations) {
            new ShopNPC(location.clone());
        }

        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§6§lKit Wars"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§7Team"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eВаша задача набрать как можно больше убийств. Побеждает"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eкоманда, набравшая наибольшее количество убийств."));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eИспользуйте жителя для смены набора."));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");

        Map<Player, TeamManager> selectedTeams = SelectionTeam.getSelectedTeams();
        Map<String, KWTeam> teams = KWTeam.getTeams();

        for (Map.Entry<Player, TeamManager> selected : selectedTeams.entrySet()){
            teams.get(selected.getValue().getTeam()).addPlayer(selected.getKey());
        }

        KWTeam kwTeam = teams.values().stream().findFirst().get();

        for (Player player : PlayerUtil.getAlivePlayers()){
            for (KWTeam team : teams.values()){
                if (team.size() < GameSettings.playersInTeam){
                    kwTeam = team;
                    break;
                }
            }
            if (!selectedTeams.containsKey(player)){
                kwTeam.addPlayer(player);
            }
            TitleUtil.StartGameTitle(player, "Убейте больше всего игроков");
        }

        BukkitUtil.runTaskAsync(() -> {
            KWTeam.getTeams().values().forEach(KWTeam::setTags);
            PlayerUtil.getAlivePlayers().forEach(GameBoard::new);
        });


        GameManager.getInstance().addGameListener(new GameListener());
        GameTime.start();
        LastCraft.getScoreBoardAPI().createGameObjective(true, true);

        BukkitUtil.runTaskLater(5, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            for (ShopNPC shopNPC : ShopNPC.getShopsNPC()) {
                HumanNPC npc = shopNPC.getNpc();
                Hologram hologram = shopNPC.getHologram();
                npc.showTo(player);
                hologram.showTo(player);
            }
        }));

    }

    @EventHandler
    public void onPlayerDeath(PlayerKillEvent e) {
        Player player = e.getPlayer();
        Player killer = player.getKiller();

        KWPlayer kwDeath = KWPlayer.getKWPlayer(player);

        if (killer != null && killer != player) {
            KWPlayer.getKWPlayer(killer).getKwTeam().addKills();
            GameManager.getInstance().getStats().addPlayerStats(killer, "Kills", 1);
            BukkitGamer gamerKiller = GAMER_MANAGER.getGamer(killer);
            gamerKiller.addExpLocal(1);
            LastCraft.getActionBarAPI().sendBar(killer, "§a+1 XP");
            LastCraft.getScoreBoardAPI().setScoreTab(killer, GameManager.getInstance().getStats().getPlayerStats(killer, "Kills"));
        }

        if (Math.random() <= 0.3){
            ItemStack bonus = ItemUtil.setItemMeta(Head.COIN.getHead(), "Бонус");
            ParticleAPI particleAPI = LastCraft.getParticleAPI();
            particleAPI.launchInstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.YELLOW, Color.WHITE).build(), player);
            player.getLocation().getWorld().dropItem(player.getLocation(), bonus);
        }

        kwDeath.respawnKWPlayer();
    }

    @EventHandler
    public void onEndGame(EndGameEvent e) {
        e.setHoloTop(Collections.singletonList("§eТоп убийств"));
        e.setTopValue("Kills");
        e.setTopValueSuffix("убийств;о;а");

        e.setHoloInfo(Collections.singletonList("Убийств: §c@Kills"));

        Collection<KWTeam> kwTeams = KWTeam.getTeams().values();
        KWTeam winner = kwTeams.stream().findFirst().get();
        for (KWTeam kwTeam : kwTeams){
            if (winner.getKills() < kwTeam.getKills()){
                winner = kwTeam;
            }
        }

        e.setTeamWin(winner.getDisplayName());
        e.setWinMsg("§eПобедила команда §8- " + winner.getDisplayName());

        for (Player player : winner.getPlayersInTeam()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null) continue;
            gamer.addMoneyLocal(30);
            gamer.addExpLocal(50);
            GameManager.getInstance().getStats().addPlayerStats(player, "Wins", 1);
            e.addWinner(player);
        }

        for (Player players: Bukkit.getOnlinePlayers()) {
            String test = "";
            if (!GameManager.getInstance().getStats().getStatsPlayers().containsKey(players)) {
                test = test + "@Kills:" + 0;
            } else {
                test = test + "@Kills:" + GameManager.getInstance().getStats().getPlayerStats(players, "Kills");
            }
            e.getHoloPlayerInfo().put(players, test);

            if (winner.playerInTeam(players)) {
                LastCraft.getTitlesAPI().sendTitle(players, "§6Ваша команда", "§6победила", 20, 3 * 20, 20);
            }
            else {
                LastCraft.getTitlesAPI().sendTitle(players, "§6Победила команда", winner.getDisplayName(), 20, 3 * 20, 20);
            }
        }


    }

    @EventHandler
    public void onRestartGame(RestartGameEvent e) {
        ShopNPC.clearData();
        KitWars.getInstance().getVillagerSpawns().clear();

        KWPlayer.resetKWPlayers();
        KWTeam.getTeams().clear();
    }
}
