package net.lastcraft.luckywars.executor.trap;

import com.google.common.collect.ImmutableSet;
import net.lastcraft.api.LastCraft;
import net.lastcraft.api.player.BukkitGamer;
import net.lastcraft.api.util.ItemUtil;
import net.lastcraft.luckywars.executor.ActionExecutor;
import net.lastcraft.luckywars.executor.ExecutorContext;
import net.lastcraft.luckywars.game.LuckyWarsGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class MobSpawners<T extends Entity> implements ActionExecutor {

    public static final ImmutableSet<ItemStack> COW_DROPS = ImmutableSet.of(
            new ItemStack(Material.MILK_BUCKET, 2)
    );

    public static final ImmutableSet<ItemStack> ZOMBIE_DROPS = ImmutableSet.of(new ItemStack(Material.GOLDEN_APPLE));

    public static final MobSpawners<Creeper> ANGRY_CREEPER = new MobSpawners<>((creeper, game, context) -> {
        creeper.setPowered(true);
        creeper.setTarget(context.getInitiator());
    }, Creeper.class);
    public static final MobSpawners<Cow> MILK_COW = new MobSpawners<>(((entity, game, context) -> context.addDrops(entity, COW_DROPS, game)), Cow.class);
    public static final MobSpawners<Ghast> ANGRY_GHAST = new MobSpawners<>((entity, game, context) -> {
    }, Ghast.class);
    public static final MobSpawners<Blaze> ANGRY_BLAZE = new MobSpawners<>((entity, game, context) -> {
    }, Blaze.class);
    public static final MobSpawners<Witch> ANGRY_WITCH = new MobSpawners<>((entity, game, context) -> {
    }, Witch.class);
    public static final MobSpawners<IronGolem> ANGRY_GOLEM = new MobSpawners<>((entity, game, context) -> {
    }, IronGolem.class);
    public static final MobSpawners<Zombie> ZOMBIE_WARRIOR = new MobSpawners<>((entity, game, context) -> {
        EntityEquipment entityEquipment = entity.getEquipment();

        entityEquipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        entityEquipment.setHelmet(new ItemStack(Material.IRON_HELMET));
        entityEquipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        entityEquipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        entityEquipment.setItemInMainHand(new ItemStack(Material.STONE_SWORD));

        entityEquipment.setChestplateDropChance(1);
        entityEquipment.setLeggingsDropChance(1);
        entityEquipment.setBootsDropChance(1);
        entityEquipment.setHelmetDropChance(1);
        entityEquipment.setItemInMainHandDropChance(1);

    }, Zombie.class);
    public static final MobSpawners<Zombie> SMALL_ZOMBIE = new MobSpawners<>((entity, game, context) -> {
        entity.setBaby(true);

        entity.getEquipment().setItemInMainHand(ItemUtil.getBuilder(Material.STICK)
                .addEnchantment(Enchantment.KNOCKBACK, 2)
                .build());
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);

        entity.getEquipment().setItemInMainHandDropChance(0);
    }, Zombie.class);
    public static final MobSpawners<Skeleton> SKELETON = new MobSpawners<>((entity, game, context) -> {
        EntityEquipment entityEquipment = entity.getEquipment();

        entityEquipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        entityEquipment.setHelmet(new ItemStack(Material.IRON_HELMET));
        entityEquipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        entityEquipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));

        entityEquipment.setItemInMainHand(ItemUtil.getBuilder(Material.BOW)
                .addEnchantment(Enchantment.KNOCKBACK, context.getRandom().nextInt(1, 2))
                .addEnchantment(Enchantment.ARROW_DAMAGE, 2)
                .build());

        entityEquipment.setChestplateDropChance(1);
        entityEquipment.setLeggingsDropChance(1);
        entityEquipment.setBootsDropChance(1);
        entityEquipment.setHelmetDropChance(1);
        entityEquipment.setItemInMainHandDropChance(1);


    }, Skeleton.class);
    public static final MobSpawners<Zombie> ZOMBIE_WITH_PLAYER_HEAD = new MobSpawners<>((entity, game, context) -> {
        EntityEquipment entityEquipment = entity.getEquipment();

        BukkitGamer gamer = LastCraft.getGamerManager().getGamer(context.getInitiator());

        ItemStack itemStack = gamer.getHead();

        entityEquipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        entityEquipment.setHelmet(itemStack);
        entityEquipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
        entityEquipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        entityEquipment.setItemInMainHand(new ItemStack(Material.STONE_SWORD));

        entityEquipment.setChestplateDropChance(1);
        entityEquipment.setLeggingsDropChance(1);
        entityEquipment.setBootsDropChance(1);
        entityEquipment.setHelmetDropChance(0);
        entityEquipment.setItemInMainHandDropChance(1);

        entity.setCustomName(gamer.getDisplayName());
        entity.setCustomNameVisible(true);

        context.addDrops(entity, ZOMBIE_DROPS, game);

    }, Zombie.class);


    private final MobHandler<T> mobHandler;
    private final Class<T> entityClass;

    public MobSpawners(MobHandler<T> mobHandler, Class<T> entityClass) {
        this.mobHandler = mobHandler;
        this.entityClass = entityClass;
    }

    @Override
    public void execute(LuckyWarsGame game, ExecutorContext context) {
        Location location = (Location) context.getLocals().get("drop_loc");

        T entity = location.getWorld().spawn(location, entityClass);

        //Бакит апи кал
        if (entity instanceof Creature) {
            ((Creature) entity).setTarget(context.getInitiator());
        }

        mobHandler.handleMob(entity, game, context);
    }

    public interface MobHandler<T extends Entity> {

        void handleMob(T entity, LuckyWarsGame game, ExecutorContext context);
    }
}
