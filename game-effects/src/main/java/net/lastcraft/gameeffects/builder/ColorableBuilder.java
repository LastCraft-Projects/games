package net.lastcraft.gameeffects.builder;

import net.lastcraft.gameeffects.cosmetics.colorable.ColorableCosmetic;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorableBuilder extends BaseBuilder<ColorableCosmetic> {

    private DyeColor color;
    private final List<Pattern> patterns = new ArrayList<>();

    public ColorableBuilder(int id) {
        super(id, null);
    }

    public ColorableBuilder setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    public ColorableBuilder addPattern(Pattern... pattern) {
        this.patterns.addAll(Arrays.asList(pattern));
        return this;
    }

    @Override
    protected ColorableCosmetic init() {
        ItemStack itemStack = new ItemStack(Material.SHIELD);
        ItemMeta meta = itemStack.getItemMeta();
        BlockStateMeta blockStateMeta = (BlockStateMeta) meta;

        Banner banner = (Banner) blockStateMeta.getBlockState();
        banner.setBaseColor(color);
        banner.setPatterns(patterns);
        banner.update();

        blockStateMeta.setBlockState(banner);
        itemStack.setItemMeta(meta);

        return new ColorableCosmetic(id, itemStack, rarity, color, patterns, minLevel, rewardLevel, purchaseType, price, shulkerFree);
    }
}
