package net.lastcraft.gameeffects.api.cosmetic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lastcraft.gameeffects.gui.guis.CosmeticGui;
import net.lastcraft.gameeffects.gui.guis.type.ArrowGui;
import net.lastcraft.gameeffects.gui.guis.type.ColorableGui;
import net.lastcraft.gameeffects.gui.guis.type.KillEffectGui;
import net.lastcraft.gameeffects.gui.guis.type.StartSoundGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum GameCosmeticType {
    ARROW(19, new ItemStack(Material.TIPPED_ARROW), ArrowGui.class),
    START_SOUND(21, new ItemStack(Material.TOTEM), StartSoundGui.class),
    COLORABLE(23, new ItemStack(Material.SHIELD), ColorableGui.class), //цветные щиты и коженки
    KILL_EFFECT(25, new ItemStack(Material.REDSTONE), KillEffectGui.class),
    //DEATH_RATTLE //todo
    //в конце игры индивидуальные стойки победителей
    //трупики после убийства
    //следы сделать
    ;

    private final int slot;
    private final ItemStack itemStack;
    private final Class<? extends CosmeticGui> clazz;

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
