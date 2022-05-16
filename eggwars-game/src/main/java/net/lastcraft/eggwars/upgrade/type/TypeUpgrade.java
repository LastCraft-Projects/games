package net.lastcraft.eggwars.upgrade.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public interface TypeUpgrade  {

    int getPrice();

    List<String> getLore(Player player);

    String getName(Player player);

    Material getItem();

    boolean click(Player player);

}
