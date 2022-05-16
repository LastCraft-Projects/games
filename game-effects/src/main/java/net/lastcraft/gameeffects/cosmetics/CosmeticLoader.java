package net.lastcraft.gameeffects.cosmetics;

import lombok.experimental.UtilityClass;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.effect.ParticleAPI;
import net.lastcraft.api.effect.ParticleEffect;
import net.lastcraft.api.entity.EntityAPI;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.api.util.Rarity;
import net.lastcraft.base.SoundType;
import net.lastcraft.base.gamer.constans.PurchaseType;
import net.lastcraft.dartaapi.utils.core.MathUtil;
import net.lastcraft.gameeffects.GameEffects;
import net.lastcraft.gameeffects.builder.ArrowEffectBuilder;
import net.lastcraft.gameeffects.builder.ColorableBuilder;
import net.lastcraft.gameeffects.builder.KillEffectBuilder;
import net.lastcraft.gameeffects.builder.StartSoundBuilder;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.potion.PotionType;

import java.util.List;

@UtilityClass
public class CosmeticLoader {

    private final EntityAPI ENTITY_API = LastCraft.getEntityAPI();
    private final ParticleAPI PARTICLE_API = LastCraft.getParticleAPI();

    public void initAll(GameEffects plugin) {
        new ArrowEffectBuilder(0, Material.NOTE_BLOCK)
                .setEffect(ParticleEffect.NOTE)
                .setAmount(1)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 12)
                .setRarity(Rarity.EPIC)
                .build();
        new ArrowEffectBuilder(1, Material.FURNACE)
                .setEffect(ParticleEffect.SMOKE_NORMAL)
                .setPeriod(1)
                .setPurchaseType(PurchaseType.GOLD, 10)
                .setRarity(Rarity.LEGENDARY)
                .setRewardLevel(95)
                .build();
        new ArrowEffectBuilder(2, Material.BLAZE_POWDER)
                .setEffect(ParticleEffect.FLAME)
                .setPurchaseType(PurchaseType.GOLD, 6)
                .setMinLevel(10)
                .build();
        new ArrowEffectBuilder(3, ItemUtil.getBuilder(Material.POTION)
                    .setPotionData(PotionType.INSTANT_HEAL)
                    .build())
                .setEffect(ParticleEffect.HEART)
                .setAmount(1)
                .setPurchaseType(PurchaseType.GOLD, 13)
                .setRarity(Rarity.LEGENDARY)
                .setRewardLevel(150)
                .build();
        new ArrowEffectBuilder(4, Material.EMERALD)
                .setEffect(ParticleEffect.VILLAGER_HAPPY)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 8)
                .setMinLevel(15)
                .build();
        new ArrowEffectBuilder(5, Material.FIREWORK)
                .setEffect(ParticleEffect.FIREWORKS_SPARK)
                .setAmount(3)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 8)
                .setRarity(Rarity.LEGENDARY)
                .setMinLevel(15)
                .setRewardLevel(80)
                .build();
        new ArrowEffectBuilder(6, Material.REDSTONE)
                .setEffect(ParticleEffect.REDSTONE)
                .setPeriod(1)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 12)
                .build();
        new ArrowEffectBuilder(7, ItemUtil.getBuilder(Material.TIPPED_ARROW)
                    .setPotionData(PotionType.INSTANT_DAMAGE)
                    .build())
                .setEffect(ParticleEffect.SPELL_WITCH)
                .setPeriod(1)
                .setPurchaseType(PurchaseType.GOLD, 14)
                .setRarity(Rarity.LEGENDARY)
                .setMinLevel(25)
                .build();
        new ArrowEffectBuilder(8, Material.FIREBALL)
                .setEffect(ParticleEffect.VILLAGER_ANGRY)
                .setPeriod(2)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 15000)
                .setRarity(Rarity.EPIC)
                .build();
        new ArrowEffectBuilder(9, Material.WEB)
                .setEffect(ParticleEffect.CLOUD)
                .setAmount(1)
                .setPurchaseType(PurchaseType.GOLD, 10)
                .setRarity(Rarity.LEGENDARY)
                .setMinLevel(15)
                .setRewardLevel(85)
                .build();
        new ArrowEffectBuilder(10, Material.TOTEM)
                .setEffect(ParticleEffect.TOTEM)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 15000)
                .setRewardLevel(55)
                .setRarity(Rarity.EPIC)
                .build();
        new ArrowEffectBuilder(11, Material.DRAGONS_BREATH)
                .setEffect(ParticleEffect.SPELL_MOB)
                .setPeriod(1)
                .setAmount(1)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 12)
                .setMinLevel(25)
                .build();

        new StartSoundBuilder(100, Material.FIREWORK)
                .setSoundType(SoundType.ENTITY_FIREWORK_TWINKLE)
                .setShulkerFree(true)
                .setRarity(Rarity.COMMON)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 2000)
                .build();
        new StartSoundBuilder(101, Material.ANVIL)
                .setSoundType(SoundType.SELECTED)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 1)
                .build();
        new StartSoundBuilder(102, Material.ENDER_PEARL)
                .setSoundType(SoundType.TELEPORT)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 1800)
                .build();
        new StartSoundBuilder(103, Material.EGG)
                .setSoundType(SoundType.ENTITY_CHICKEN_HURT)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setPurchaseType(PurchaseType.GOLD, 1)
                .build();

        new ColorableBuilder(110)
                .setColor(DyeColor.RED)
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER))
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(31)
                .build();
        new ColorableBuilder(111)
                .setColor(DyeColor.WHITE)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 2500)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setRewardLevel(36)
                .build();
        new ColorableBuilder(112)
                .setColor(DyeColor.ORANGE)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(113)
                .build();
        new ColorableBuilder(113)
                .setColor(DyeColor.MAGENTA)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(93)
                .build();
        new ColorableBuilder(114)
                .setColor(DyeColor.LIGHT_BLUE)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(108)
                .build();
        new ColorableBuilder(115)
                .setColor(DyeColor.YELLOW)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(26)
                .build();
        new ColorableBuilder(116)
                .setColor(DyeColor.LIME)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .build();
        new ColorableBuilder(117)
                .setColor(DyeColor.PINK)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(78)
                .build();
        new ColorableBuilder(118)
                .setColor(DyeColor.GRAY)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setRewardLevel(118)
                .build();
        new ColorableBuilder(119)
                .setColor(DyeColor.SILVER)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .setRewardLevel(103)
                .build();
        new ColorableBuilder(120)
                .setColor(DyeColor.CYAN)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 2500)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .build();
        new ColorableBuilder(121)
                .setColor(DyeColor.PURPLE)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(83)
                .build();
        new ColorableBuilder(122)
                .setColor(DyeColor.BLUE)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(88)
                .build();
        new ColorableBuilder(123)
                .setColor(DyeColor.BROWN)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 2500)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .build();
        new ColorableBuilder(124)
                .setColor(DyeColor.GREEN)
                .setPurchaseType(PurchaseType.GOLD, 2)
                .setRarity(Rarity.COMMON)
                .setShulkerFree(true)
                .build();
        new ColorableBuilder(125)
                .setColor(DyeColor.BLACK)
                .setPurchaseType(PurchaseType.MYSTERY_DUST, 3000)
                .setRarity(Rarity.RARE)
                .setShulkerFree(true)
                .setRewardLevel(98)
                .build();
        new ColorableBuilder(126) //россия
                .setColor(DyeColor.BLUE)
                .addPattern(
                        new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
                        new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM))
                .setPurchaseType(PurchaseType.GOLD, 4)
                .setRarity(Rarity.EPIC)
                .build();
        new ColorableBuilder(127) //украина
                .setColor(DyeColor.YELLOW)
                .addPattern(new Pattern(DyeColor.BLUE, PatternType.HALF_HORIZONTAL))
                .setPurchaseType(PurchaseType.GOLD, 4)
                .setRarity(Rarity.EPIC)
                .build();
        new ColorableBuilder(128) //дракончик
                .setColor(DyeColor.WHITE)
                .addPattern(
                        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
                        new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL_MIRROR),
                        new Pattern(DyeColor.PURPLE, PatternType.FLOWER),
                        new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE),
                        new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP),
                        new Pattern(DyeColor.BLACK, PatternType.MOJANG))
                .setPurchaseType(PurchaseType.GOLD, 5)
                .setRarity(Rarity.EPIC)
                .setRewardLevel(123)
                .build();
        new ColorableBuilder(129) //скелет
                .setColor(DyeColor.BLACK)
                .addPattern(
                        new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER),
                        new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER),
                        new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM),
                        new Pattern(DyeColor.WHITE, PatternType.CREEPER),
                        new Pattern(DyeColor.YELLOW, PatternType.STRIPE_TOP),
                        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP))
                .setMinLevel(25)
                .setPurchaseType(PurchaseType.GOLD, 6)
                .setRarity(Rarity.LEGENDARY)
                .build();
        new ColorableBuilder(130) //дед пул
                .setColor(DyeColor.WHITE)
                .addPattern(
                        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL),
                        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
                        new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
                        new Pattern(DyeColor.RED, PatternType.STRIPE_CENTER),
                        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
                        new Pattern(DyeColor.RED, PatternType.TRIANGLE_TOP),
                        new Pattern(DyeColor.RED, PatternType.TRIANGLE_BOTTOM))
                .setPurchaseType(PurchaseType.GOLD, 5)
                .setMinLevel(20)
                .setRarity(Rarity.EPIC)
                .build();
        new ColorableBuilder(131) //Гитара
                .setColor(DyeColor.BLACK)
                .addPattern(
                        new Pattern(DyeColor.PURPLE, PatternType.STRIPE_TOP),
                        new Pattern(DyeColor.PURPLE, PatternType.CROSS),
                        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
                        new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS),
                        new Pattern(DyeColor.PURPLE, PatternType.BORDER),
                        new Pattern(DyeColor.PURPLE, PatternType.CURLY_BORDER))
                .setPurchaseType(PurchaseType.GOLD, 5)
                .setRarity(Rarity.EPIC)
                .setRewardLevel(63)
                .build();
        new ColorableBuilder(132) //сердце
                .setColor(DyeColor.WHITE)
                .addPattern(
                        new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE),
                        new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE),
                        new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP),
                        new Pattern(DyeColor.RED, PatternType.GRADIENT_UP),
                        new Pattern(DyeColor.RED, PatternType.GRADIENT))
                .setMinLevel(20)
                .setPurchaseType(PurchaseType.GOLD, 6)
                .setRarity(Rarity.LEGENDARY)
                .setRewardLevel(105)
                .build();
        new ColorableBuilder(133) //олень
                .setColor(DyeColor.BROWN)
                .addPattern(
                        new Pattern(DyeColor.RED, PatternType.TRIANGLES_BOTTOM),
                        new Pattern(DyeColor.BLACK, PatternType.CREEPER),
                        new Pattern(DyeColor.BLACK, PatternType.SKULL),
                        new Pattern(DyeColor.BROWN, PatternType.RHOMBUS_MIDDLE),
                        new Pattern(DyeColor.BROWN, PatternType.STRIPE_SMALL),
                        new Pattern(DyeColor.SILVER, PatternType.STRIPE_MIDDLE),
                        new Pattern(DyeColor.SILVER, PatternType.HALF_HORIZONTAL),
                        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
                        new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP),
                        new Pattern(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE),
                        new Pattern(DyeColor.BLACK, PatternType.BORDER),
                        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER))
                .setPurchaseType(PurchaseType.GOLD, 5)
                .setRarity(Rarity.EPIC)
                .setRewardLevel(53)
                .build();
        new ColorableBuilder(134) //покебол
                .setColor(DyeColor.WHITE)
                .addPattern(
                        new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL),
                        new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE),
                        new Pattern(DyeColor.RED, PatternType.STRIPE_TOP),
                        new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
                        new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE),
                        new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE))
                .setPurchaseType(PurchaseType.GOLD, 5)
                .setRarity(Rarity.EPIC)
                .setRewardLevel(56)
                .build();

        new KillEffectBuilder(135, Material.GOLD_RECORD, (spawnLocation, gamer) -> {
            List<Location> heartLocations = MathUtil.getCircleSide(spawnLocation, 1.0, 10);
            heartLocations.forEach(location -> PARTICLE_API.sendEffect(ParticleEffect.NOTE, location.clone().add(0, 0.5, 0), 0.1F, 5));
        }).setPurchaseType(PurchaseType.GOLD, 10)
                .setRarity(Rarity.EPIC)
                .setShulkerFree(true)
                .build();
        new KillEffectBuilder(136, ItemUtil.getBuilder(Material.POTION)
                .setPotionData(PotionType.INSTANT_HEAL)
                .build(), (spawnLocation, gamer) -> {
            List<Location> heartLocations = MathUtil.getCircleSide(spawnLocation, 1.0, 10);
            heartLocations.forEach(location -> PARTICLE_API.sendEffect(ParticleEffect.HEART, location.clone().add(0, 0.5, 0), 0.1F, 2));
        }).setPurchaseType(PurchaseType.GOLD, 12)
                .setShulkerFree(true)
                .setRarity(Rarity.LEGENDARY)
                .build();
        new KillEffectBuilder(137, ItemUtil.getBuilder(Material.IRON_BLOCK)
                .build(), (spawnLocation, gamer) -> spawnLocation.getWorld().strikeLightningEffect(spawnLocation))
                .setPurchaseType(PurchaseType.GOLD, 8)
                .setShulkerFree(true)
                .setRarity(Rarity.EPIC)
                .build();
        new KillEffectBuilder(138, ItemUtil.getBuilder(Material.FIREWORK)
                .build(), (spawnLocation, gamer) -> PARTICLE_API.launchInstantFirework(FireworkEffect.builder().flicker(false)
                        .withColor(Color.YELLOW, Color.WHITE, Color.ORANGE, Color.RED)
                        .with(FireworkEffect.Type.BALL)
                        .trail(false)
                        .build(), spawnLocation))
                .setPurchaseType(PurchaseType.GOLD, 10)
                .setShulkerFree(true)
                .setRarity(Rarity.LEGENDARY)
                .build();
        //todo сделать еще эффектов убийств

    }
}
