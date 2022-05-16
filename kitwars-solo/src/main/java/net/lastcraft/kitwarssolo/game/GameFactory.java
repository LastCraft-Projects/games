package net.lastcraft.kitwarssolo.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.entity.npc.types.HumanNPC;
import net.lastcraft.api.event.game.EndGameEvent;
import net.lastcraft.api.event.game.PlayerKillEvent;
import net.lastcraft.api.event.game.RestartGameEvent;
import net.lastcraft.api.event.game.StartGameEvent;
import net.lastcraft.api.hologram.Hologram;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.Head;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.games.TitleUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.kitwarssolo.KWPlayer;
import net.lastcraft.kitwarssolo.KitWars;
import net.lastcraft.kitwarssolo.ShopNPC;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GameFactory extends DListener {
    @EventHandler
    public void onStartGame(StartGameEvent e) {
        //создание НПС
        KitWars.getInstance().getVillagerSpawns().forEach(ShopNPC::new);

        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§6§lKit Wars"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§7Solo"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eВаша задача набрать как можно больше убийств. Побеждает"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eигрок, набравший наибольшее количество убийств."));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eИспользуйте жителя для смены набора."));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");

        List<Location> respawn = KitWars.getInstance().getPlayerSpawns();
        Iterator<Location> iterator = respawn.iterator();

        for (Player player : PlayerUtil.getAlivePlayers()){
            new KWPlayer(player, iterator.next());
            new GameBoard(player);

            TitleUtil.StartGameTitle(player, "Убейте больше всего игроков");
        }

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
            GameManager.getInstance().getStats().addPlayerStats(killer, "Kills", 1);
            BukkitGamer gamerKiller = GAMER_MANAGER.getGamer(killer);
            gamerKiller.addExpLocal(1);
            LastCraft.getActionBarAPI().sendBar(killer, "§a+1 XP");
            LastCraft.getScoreBoardAPI().setScoreTab(killer, GameManager.getInstance().getStats().getPlayerStats(killer, "Kills"));
        }

        if (Math.random() <= 0.3){
            ItemStack bonus = ItemUtil.setItemMeta(Head.COIN.getHead(), "Бонус");

            ParticleAPI particleAPI = LastCraft.getParticleAPI();
            particleAPI.launchInstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.YELLOW, Color.WHITE).build(), player.getLocation());
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

        Player[] winners = new Player[3];

        int i = 0;
        for (Player winner : GameManager.getInstance().getStats().getPlayersTop("Kills").keySet()) {
            if (i == 3) break;
            winners[i] = winner;
            i++;
        }
        e.addWinner(winners[0]);
        if (winners[0] != null) {
            e.setWinMsg("§eПобедил игрок §8- " + winners[0].getDisplayName());
            BukkitGamer winner = GAMER_MANAGER.getGamer(winners[0]);

            if (winner != null) {
                winner.addMoneyLocal(30);
                GameManager.getInstance().getStats().addPlayerStats(winners[0], "Wins", 1);
                LastCraft.getTitlesAPI().sendTitleAll("§eПобедил игрок", winners[0].getDisplayName(), 20, 3 * 20, 20);
            }
        }
        if (winners[1] != null) {
            BukkitGamer winner = GAMER_MANAGER.getGamer(winners[1]);

            if (winner != null) {
                winner.addMoneyLocal(20);
            }
        }
        if (winners[2] != null) {
            BukkitGamer winner = GAMER_MANAGER.getGamer(winners[2]);

            if (winner != null) {
                winner.addMoneyLocal(10);
            }
        }

        for (Player players: Bukkit.getOnlinePlayers()) {

            String test = "";
            if (!GameManager.getInstance().getStats().getStatsPlayers().containsKey(players)) {
                test = test + "@Kills:" + 0;
            } else {
                test = test + "@Kills:" + GameManager.getInstance().getStats().getPlayerStats(players, "Kills");
            }
            e.getHoloPlayerInfo().put(players, test);
        }
    }

    @EventHandler
    public void onRestartGame(RestartGameEvent e) {
        ShopNPC.clearData();
        KitWars.getInstance().getVillagerSpawns().clear();

        KWPlayer.resetKWPlayers();
    }
}
