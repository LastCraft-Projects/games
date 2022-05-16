package net.lastcraft.kitwarssolo.game;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.event.gamer.GamerInteractNPCEvent;
import net.lastcraft.api.game.GameSettings;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.DListener;
import net.lastcraft.dartaapi.utils.core.PlayerUtil;
import net.lastcraft.kitwarssolo.KWPlayer;
import net.lastcraft.kitwarssolo.bonuses.Bonus;
import net.lastcraft.kitwarssolo.kits.Kit;
import net.lastcraft.kitwarssolo.kits.KitsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;

public class GameListener extends DListener {

    private static SoundAPI soundAPI;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent e){
        e.setCancelled(true);

        Player player = e.getPlayer();
        Item item = e.getItem();

        if (!PlayerUtil.isAlive(player)) return;

        if (item.getItemStack().getType() == Material.SKULL_ITEM){
            if (item.getItemStack().getItemMeta() != null){
                if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("Бонус")){
                    item.remove();
                    Bonus bonus = Bonus.getBonus();
                    bonus.giveBonus(player);
                    Bukkit.broadcastMessage(GameSettings.prefix + player.getDisplayName() + " §fподобрал бонус " + bonus.getName());
                }
            }
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        switch (block.getType()) {
            case HOPPER:
            case ANVIL:
                e.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(GamerInteractNPCEvent e) {
        Player player = e.getGamer().getPlayer();
        if (PlayerUtil.isSpectator(player)) return;
        KitsMenu.openMenu(player);
    }

    @EventHandler
    public void onInventoryClickSelect(InventoryClickEvent e) {
        if (e.getInventory().getName().equals("Выбор набора")) {
            e.setCancelled(true);

            int slot = e.getRawSlot();
            Player player = (Player) e.getWhoClicked();
            HashMap<Integer, Kit> kits = KitsMenu.getKits();
            if (kits.containsKey(slot)) {
                if (kits.get(slot) == KWPlayer.getKWPlayer(player).getKit()) {
                    player.sendMessage(GameSettings.prefix + "§cНабор " + kits.get(slot).getName() + " §cуже выбран!");
                    soundAPI.play(player, SoundType.NO);
                } else {
                    if (kits.get(slot).getEndKit()) {
                        if (GameTime.getGameTime() <= 180) {
                            KWPlayer.getKWPlayer(player).setKit(kits.get(slot));
                            soundAPI.play(player, SoundType.SELECTED);
                        } else {
                            player.sendMessage(GameSettings.prefix + "§cНабор " + kits.get(slot).getName() + " §cбудет доступен после 4 минуты игры");
                            soundAPI.play(player, SoundType.NO);
                        }
                    } else {
                        KWPlayer.getKWPlayer(player).setKit(kits.get(slot));
                        soundAPI.play(player, SoundType.SELECTED);
                    }
                }

                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClickPlayer(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == player.getInventory()) {
            int slot = e.getRawSlot();
            if (slot == 8 || slot == 7 || slot == 6 || slot == 5) {
                e.setCancelled(true);
            }
        }
    }

    static {
        soundAPI = LastCraft.getSoundAPI();
    }
}
