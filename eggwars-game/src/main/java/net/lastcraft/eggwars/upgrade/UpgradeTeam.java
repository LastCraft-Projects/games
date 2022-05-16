package net.lastcraft.eggwars.upgrade;

import net.lastcraft.dartaapi.utils.core.StringUtil;
import net.lastcraft.dartaapi.utils.inventory.ItemUtil;
import net.lastcraft.eggwars.upgrade.type.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UpgradeTeam {

    private Color color;
    private int upgrade = 0;

    private EmeraldArmy emeraldArmy = new EmeraldArmy();
    private SharpenSword sword = new SharpenSword();
    private ArmorProtect armorProtect = new ArmorProtect();
    private HealthAura aura = new HealthAura();
    private ToolDig toolDig = new ToolDig();
    private UpgradeArmor upgradeArmor = new UpgradeArmor();
    private FatiqAura fatiqAura = new FatiqAura();
    private MaxHealth maxHealth = new MaxHealth();
    private FastDigging fastDigging = new FastDigging();
    private TeamWork teamWork = new TeamWork();

    private ItemStack boots;
    private ItemStack leggings;
    private ItemStack chestPlate;
    private ItemStack helmet;

    public UpgradeTeam(Color color) {
        this.color = color;
        helmet = ItemUtil.getColorLeatherArmor(Material.LEATHER_HELMET, color);
        chestPlate = ItemUtil.getColorLeatherArmor(Material.LEATHER_CHESTPLATE, color);
        leggings = ItemUtil.getColorLeatherArmor(Material.LEATHER_LEGGINGS, color);
        boots = ItemUtil.getColorLeatherArmor(Material.LEATHER_BOOTS, color);
    }

    public void addItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestPlate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
        inventory.addItem(new ItemStack(Material.WOOD_PICKAXE));
        if (fastDigging.isEnable()) player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 60, 0));
    }

    public FastDigging getFastDigging() {
        return fastDigging;
    }

    public Color getColor() {
        return color;
    }

    public ToolDig getToolDig() {
        return toolDig;
    }

    public void setUpgrade() {
        upgrade++;
    }

    public Integer getPercent() {
        return StringUtil.onPercent(upgrade, 11);
    }

    public TeamWork getTeamWork() {
        return teamWork;
    }

    public ArmorProtect getArmorProtect() {
        return armorProtect;
    }

    public EmeraldArmy getEmeraldArmy() {
        return emeraldArmy;
    }

    public HealthAura getAura() {
        return aura;
    }

    public SharpenSword getSword() {
        return sword;
    }

    public UpgradeArmor getUpgradeArmor() {
        return upgradeArmor;
    }

    public FatiqAura getFatiqAura() {
        return fatiqAura;
    }

    public MaxHealth getMaxHealth() {
        return maxHealth;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }
}
