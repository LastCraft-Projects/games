package net.lastcraft.eggwars.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.game.EndGameEvent;
import net.lastcraft.api.event.game.PlayerKillEvent;
import net.lastcraft.api.event.game.RestartGameEvent;
import net.lastcraft.api.event.game.StartGameEvent;
import net.lastcraft.api.game.*;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.dartaapi.game.GameManager;
import net.lastcraft.dartaapi.game.module.EndModule;
import net.lastcraft.dartaapi.game.perk.Perk;
import net.lastcraft.dartaapi.game.team.SelectionTeam;
import net.lastcraft.dartaapi.stats.Stats;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.bukkit.LocationUtil;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.games.TitleUtil;
import net.lastcraft.eggwars.EWTeam;
import net.lastcraft.eggwars.EggWars;
import net.lastcraft.eggwars.generator.Generator;
import net.lastcraft.eggwars.managers.data.EWData;
import net.lastcraft.eggwars.perks.types.necromancer.Necromancer;
import net.lastcraft.eggwars.perks.types.scrooge.Scrooge;
import net.lastcraft.eggwars.perks.types.timemanager.TimeManager;
import net.lastcraft.eggwars.shop.OtherMenu;
import net.lastcraft.eggwars.shop.ShopNPC;
import net.lastcraft.eggwars.upgrade.UpgradeListener;
import net.lastcraft.eggwars.upgrade.UpgradeMenu;
import net.lastcraft.eggwars.upgrade.UpgradeNPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GameFactory extends DListener {

    @EventHandler
    public void onGrow(StructureGrowEvent e) {//нельзя выращить деревья
        e.setCancelled(true);
    }

    @EventHandler
    public void onStartGame(StartGameEvent e) {
        //создание НПС и генераторов
        EggWars.getInstance().getVillagerShopSpawns().forEach(ShopNPC::new);
        EggWars.getInstance().getVillagerUpgradeSpawns().forEach(UpgradeNPC::new);

        Map<Player, TeamManager> selectedTeams = SelectionTeam.getSelectedTeams();
        Map<String, EWTeam> teams = EWTeam.getTeams();

        for (EWTeam ewTeam : teams.values()){
            LocationUtil.loadChunk(ewTeam.getSpawn());
        }

        for (Map.Entry<Player, TeamManager> selected : selectedTeams.entrySet()){
            teams.get(selected.getValue().getTeam()).addPlayer(selected.getKey());
        }

        EWTeam ewTeam = teams.values().stream().findFirst().orElse(null);

        for (Player player : PlayerUtil.getAlivePlayers()){
            for (EWTeam team : teams.values()){
                if (team.size() < GameSettings.playersInTeam){
                    ewTeam = team;
                    break;
                }
            }
            if (!selectedTeams.containsKey(player)){
                assert ewTeam != null;
                ewTeam.addPlayer(player);
            }
            new GameBoard(player);
        }

        EWTeam.getTeams().values().forEach(EWTeam::setTags);

        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§6§lEggWars"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§7" + GameSettings.typeGame.getType()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eВаша задача выжить на арене, получая преимущество"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eпри помощи улучшения генераторов. Вы возрождаетесь,"));
        Bukkit.broadcastMessage(StringUtil.stringToCenter("§eпока яйцо дракона не сломано. Защитите его!"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");

        for (Player player : PlayerUtil.getAlivePlayers()) {
            TitleUtil.StartGameTitle(player, "Сломайте яйца противников", "§7" + GameSettings.typeGame.getType());
        }

        GameManager.getInstance().addGameListener(new UpgradeListener());
        GameManager.getInstance().addGameListener(new GameListener());

        LastCraft.getScoreBoardAPI().createGameObjective(true, true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerKillEvent e) {
        Player player = e.getPlayer();
        Player killer = player.getKiller();
        EWTeam deathTeam = EWTeam.getPlayerTeam(player);

        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);

        if(deathTeam == null) {
            gamer.setGameMode(GameModeType.SPECTATOR);
            check();
            return;
        }
        if (killer != null && killer != player) {
            if (!deathTeam.isCanRespawn()) {
                getStats().addPlayerStats(killer, "Kills", 1);
                BukkitGamer gamerKiller = GAMER_MANAGER.getGamer(killer);
                gamerKiller.addExpLocal(10);
                gamerKiller.addMoneyLocal(2);
                double money = 2 * gamerKiller.getMultiple();
                LastCraft.getActionBarAPI().sendBar(killer, "§6+" + money + " " + StringUtil.getCorrectWord((int) money, "монет", "а", "ы", "") + " §a+10 XP");
            }
            getStats().addPlayerStats(killer, "tKills", 1);
            LastCraft.getScoreBoardAPI().setScoreTab(killer, getStats().getPlayerStats(killer, "Kills"));
        }
        StringUtil.sendDamageCause(player);

        if(deathTeam.isCanRespawn()) {
            Perk perk = EWData.getDataPlayer(player).getLastPerk();

            if (perk != null && perk.getClass() == Scrooge.class) {
                player.closeInventory();
                player.sendMessage(GameSettings.prefix + "Ваши вещи никто не получил");
            }

            int delay = 3;
            if(gamer.isMagma()) {
                delay = 0;
            } else if(gamer.isEmerald()) {
                delay = 1;
            } else if(gamer.isDiamond()) {
                delay = 2;
            }

            if (killer != null) {
                Perk killPerk = EWData.getDataPlayer(killer).getLastPerk();

                if (killPerk != null && killPerk.getClass() == TimeManager.class) {
                    killer.sendMessage(GameSettings.prefix + "Время возрождения противника увеличено на §c1 §fсекунду");
                    player.sendMessage(GameSettings.prefix + "Время вашего возрождения увеличено на §c1 §fсекунду игроком " + killer.getDisplayName());
                    delay++;
                }
            }

            Collection<PotionEffect> effects = Collections.emptyList();

            if (perk != null && perk.getClass() == Necromancer.class) {
                effects = player.getActivePotionEffects();
                player.sendMessage(GameSettings.prefix + "Ваши активные эффекты будут сохранены");
            }

            Collection<PotionEffect> finalEffects = effects;
            //fucking java 8
            PlayerUtil.setRespawn(player, delay, () -> BukkitUtil.runTask(() -> {
                deathTeam.respawn(player);
                BukkitUtil.runTaskLater(10, () -> player.addPotionEffects(finalEffects));
            }));
        } else {
            gamer.setGameMode(GameModeType.SPECTATOR);
            LastCraft.getTitlesAPI().sendTitle(player, "§cВы погибли!", "§7Ваше яйцо уничтожено", 20L, 60L, 20L);
            LastCraft.getBorderAPI().sendRedScreen(player);
            check();
        }
    }

    @EventHandler
    public void onEndGame(EndGameEvent e) {
        e.setHoloTop(Collections.singletonList("§eТоп убийств"));
        e.setTopValue("Kills");
        e.setTopValueSuffix("убийств;о;а");

        e.setHoloInfo(Arrays.asList(
                "Убийств: §c@Kills",
                "Сломано яиц: §b@Eggs"
        ));

        EWTeam winner = null;

        for (EWTeam ewTeam : EWTeam.getTeams().values()){
            if (ewTeam.isAlive()){
                winner = ewTeam;
                break;
            }
        }

        if (winner == null) {
            return;
        }

        e.setTeamWin(winner.getDisplayName());
        e.setWinMsg("§eПобедила команда §8- " + winner.getDisplayName());

        for (Player player : winner.getPlayersInTeam()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null) continue;
            if (GameSettings.typeGame == TypeGame.SOLO) {
                gamer.addExpLocal(80);
                gamer.addMoneyLocal(10);
            }
            if (GameSettings.typeGame == TypeGame.DOUBLES) {
                gamer.addExpLocal(60);
                gamer.addMoneyLocal(15);
            }
            if (GameSettings.typeGame == TypeGame.TEAM) {
                gamer.addExpLocal(50);
                gamer.addMoneyLocal(25);
            }
            getStats().addPlayerStats(player, "Wins", 1);
            e.addWinner(player);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            String test = "";
            if (!getStats().getStatsPlayers().containsKey(player)) {
                test = test + "@Kills:" + 0;
                test = test + ";@Eggs:" + 0;
            } else {
                test = test + "@Kills:" + getStats().getPlayerStats(player, "Kills");
                test = test + ";@Eggs:" + getStats().getPlayerStats(player, "Eggs");
            }
            e.getHoloPlayerInfo().put(player, test);

            if (winner.playerInTeam(player)) {
                LastCraft.getTitlesAPI().sendTitle(player, "§6Ваша команда", "§6победила", 20, 3 * 20, 20);
            }
            else {
                LastCraft.getTitlesAPI().sendTitle(player, "§6Победила команда", winner.getDisplayName(), 20, 3 * 20, 20);
            }
        }

    }

    @EventHandler
    public void onRestartGame(RestartGameEvent e) {
        ShopNPC.clearData();
        UpgradeNPC.clearData();
        EggWars.getInstance().getVillagerShopSpawns().clear();
        EggWars.getInstance().getVillagerUpgradeSpawns().clear();

        EWTeam.getTeams().clear();
        OtherMenu.resetOtherMenus();
        UpgradeMenu.getMenus().clear();
        Generator.clearData();
    }

    static void check(){
        BukkitUtil.runTaskLater(1, ()-> {
            int aliveTeam = 0;
            for (EWTeam ewTeam : EWTeam.getTeams().values()){
                if (ewTeam.isAlive()){
                    aliveTeam++;
                }
            }
            if (aliveTeam <= 1){
                if (GameState.END == GameState.getCurrent())
                    return;
                new EndModule();
            }
        });
    }

    public static Stats getStats() {
        return GameManager.getInstance().getStats();
    }
}
