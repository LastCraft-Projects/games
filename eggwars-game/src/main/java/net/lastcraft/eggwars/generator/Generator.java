package net.lastcraft.eggwars.generator;

import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.hologram.Hologram;
import net.lastcraft.api.hologram.lines.ItemFloatingLine;
import net.lastcraft.api.inventory.DItem;
import net.lastcraft.api.inventory.InventoryAPI;
import net.lastcraft.api.inventory.type.DInventory;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.player.GamerManager;
import net.lastcraft.api.sound.SoundAPI;
import net.lastcraft.api.util.InventoryUtil;
import net.lastcraft.base.SoundType;
import net.lastcraft.dartaapi.utils.core.MessageUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.eggwars.game.GameFactory;
import net.lastcraft.packetlib.libraries.hologram.CraftHologram;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

public class Generator {
    private static final GamerManager GAMER_MANAGER = LastCraft.getGamerManager();
    private static final InventoryAPI inventoryAPI = LastCraft.getInventoryAPI();
    private static final SoundAPI soundAPI = LastCraft.getSoundAPI();

    private Location location;
    private Hologram hologram;
    private DInventory dInventory;
    private GeneratorData generatorData;
    private int level;
    private int respawnTime = 0;

    public static Collection<Generator> getGenerators() {
        return generators.values();
    }

    private static HashMap<Block, Generator> generators = new HashMap<>();

    public Generator(Material material, Location location) {
        this.location = location.subtract(0, 0.8, 0);

        level = (material == Material.GOLD_BLOCK ? 0 : 1);
        generatorData = GeneratorData.getGeneratorData(material);

        hologram = LastCraft.getHologramAPI().createHologram(location.clone().add(0, 2.3, 0));
        hologram.addTextLine(generatorData.getColor() + "Генератор " + generatorData.getName());
        hologram.addAnimationLine(20, new GeneratorTimer(this));
        hologram.addTextLine("§fНажмите, чтобы открыть!");
        hologram.addBigItemLine((level != 0), new ItemStack(material));

        generators.put(location.clone().add(0, 1.2, 0).getBlock(), this);

        dInventory = inventoryAPI.createInventory("Генератор " + generatorData.getName(), 3);

        dInventory.setItem(11, new DItem(generatorData.getLevelData(level).getCurrentItem()));
        dInventory.setItem(15, new DItem(generatorData.getLevelData(level + 1).getUpdateItem(),
                (clicker, clickType, slot) -> click(clicker)));
    }

    int getRespawnTime() {
        return respawnTime;
    }

    void setRespawnTime() {
        respawnTime--;
    }

    void setRespawnTime(int i) {
        this.respawnTime = i;
    }

    GeneratorData getGeneratorData() {
        return generatorData;
    }

    private void click(Player player) {
        if (level == 4) {
            MessageUtil.sendMessage(player, "У этого генератора максимальный уровень прокачки");
            soundAPI.play(player, SoundType.TELEPORT);
            player.closeInventory();
            return;
        }
        Material costMaterial = Material.getMaterial(getCost().split(":")[0]);
        int costAmount = Integer.parseInt(getCost().split(":")[1]);
        if (player.getInventory().contains(costMaterial, costAmount)){
            InventoryUtil.removeItemByMaterial(player.getInventory(), costMaterial, costAmount);
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            gamer.addMoneyLocal(1);
            gamer.addExpLocal(5);
            double money = 1 * gamer.getMultiple();
            soundAPI.play(player, SoundType.SELECTED);
            LastCraft.getActionBarAPI().sendBar(player, "§6+" + money + " " + StringUtil.getCorrectWord((int) money, "монет", "а", "ы", "") + " §a+5 XP");
            GameFactory.getStats().addPlayerStats(player, "Generator", 1);
            addLevel();
        } else {
            MessageUtil.sendMessage(player, "§cУ вас недостаточно ресурсов");
            soundAPI.play(player, SoundType.NO);
            player.closeInventory();
        }
    }

    private void addLevel(){
        level++;
        if (level != 0) setRotate();

        getHologram().removeLine(0);
        getHologram().insertTextLine(0, generatorData.getColor() + "Генератор " + generatorData.getName() + " " + StringUtil.getRomanNumber(level));
        dInventory.setItem(11, new DItem(generatorData.getLevelData(level).getCurrentItem()));
        dInventory.setItem(15, new DItem(generatorData.getLevelData(level + 1).getUpdateItem(),
                (clicker, clickType, slot) -> click(clicker)));

        ParticleAPI particleAPI = LastCraft.getParticleAPI();
        particleAPI.launchInstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.YELLOW, Color.WHITE, Color.AQUA, Color.GREEN).build(), location.clone().add(0, 1.7, 0));
    }

    int getLevel() {
        return level;
    }

    void spawnItems() {
        location.getWorld().dropItem(location.clone().add(0.0, 0.5, 0.0), generatorData.getItemSpawn());
    }

    private String getCost() {
        return generatorData.getLevelData(level + 1).getCost();
    }

    private void setRotate() {
        ItemFloatingLine floatingLine = (ItemFloatingLine) ((CraftHologram)hologram).getLines().get(3);
        floatingLine.setRotate(true);
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void openInventory(Player player) {
        dInventory.openInventory(player);
    }

    public static void clearData() {
        for (Generator generator : getGenerators()) {
            generator.hologram.remove();
        }
        generators.clear();
    }
}
