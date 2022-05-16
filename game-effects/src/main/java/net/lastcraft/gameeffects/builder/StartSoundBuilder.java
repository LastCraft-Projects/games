package net.lastcraft.gameeffects.builder;

import net.lastcraft.base.SoundType;
import net.lastcraft.gameeffects.cosmetics.sounds.start.StartSoundCosmetic;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StartSoundBuilder extends BaseBuilder<StartSoundCosmetic> {

    private SoundType soundType;

    public StartSoundBuilder(int id, ItemStack itemStack) {
        super(id, itemStack);
    }

    public StartSoundBuilder(int id, Material material) {
        this(id, new ItemStack(material));
    }

    public StartSoundBuilder setSoundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    @Override
    protected StartSoundCosmetic init() {
        return new StartSoundCosmetic(id, item, rarity, minLevel, rewardLevel, purchaseType, price, shulkerFree, soundType);
    }
}
