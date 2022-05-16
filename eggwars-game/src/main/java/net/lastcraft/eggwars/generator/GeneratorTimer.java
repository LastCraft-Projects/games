package net.lastcraft.eggwars.generator;

import lombok.AllArgsConstructor;
import net.lastcraft.dartaapi.utils.bukkit.BukkitUtil;
import net.lastcraft.dartaapi.utils.core.StringUtil;

import java.util.function.Supplier;

@AllArgsConstructor
public class GeneratorTimer implements Supplier<String> {

    private final Generator generator;

    @Override
    public String get() {
        if (generator.getLevel() == 0) return "§cСломан!";
        int respawnTime = generator.getRespawnTime();
        if (respawnTime > 0) {
            generator.setRespawnTime();
            return "§fСпавн через §c" + respawnTime + " §f" + StringUtil.getCorrectWord(respawnTime, "секунд", "у", "ы", "");
        }
        BukkitUtil.runTask(generator::spawnItems);
        generator.setRespawnTime(generator.getGeneratorData().getLevelData(generator.getLevel()).getPeriod());
        return "§fЗаспавнено!";
    }
}
