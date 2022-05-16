package net.lastcraft.luckywars.converter;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MaterialToDropConverter {

    private final Predicate<BlockBreakEvent> predicate;
    private final Function<Block, ItemStack> supplier;

    public MaterialToDropConverter(Predicate<BlockBreakEvent> predicate, Function<Block, ItemStack> supplier) {
        this.predicate = predicate;
        this.supplier = supplier;
    }

    public Predicate<BlockBreakEvent> predicate() {
        return predicate;
    }

    public Function<Block, ItemStack> supplier() {
        return supplier;
    }

    public static MaterialToDropConverter alwaysDrop(Supplier<ItemStack> supplier) {
        return new MaterialToDropConverter(block -> true, (block) -> supplier.get());
    }
}
