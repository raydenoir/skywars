package thelonebarkeeper.mgame.objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class GameClass {

    ClassType type;
    ArrayList<ItemStack> inventory = new ArrayList<>();

    public GameClass(ClassType type) {
        this.type = type;

        setupClassInventory();
    }

    public ClassType getType() {
        return type;
    }

    public ItemStack[] getInventory() {
        return inventory.toArray(new ItemStack[inventory.size()]);
    }


    private void setupClassInventory() {
        switch (type) {
            case FLASH:
                ItemStack potion = new ItemStack(Material.POTION);
                PotionMeta meta = (PotionMeta) potion.getItemMeta();
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1), true);
                potion.setItemMeta(meta);

                inventory.add(potion);
                break;

            case MINER:
                ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE);
                pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);

                inventory.add(pickaxe);
                break;

            case ARCHER:
                ItemStack bow = new ItemStack(Material.BOW);
                ItemStack arrows = new ItemStack(Material.ARROW, 4);

                inventory.add(bow);
                inventory.add(arrows);
                break;

            case FARMER:
                ItemStack hoe = new ItemStack(Material.STONE_HOE);
                ItemStack potatoes = new ItemStack(Material.POTATO_ITEM, 9);
                ItemStack carrots = new ItemStack(Material.CARROT_ITEM, 9);
                ItemStack wheat = new ItemStack(Material.WHEAT, 9);

                inventory.add(hoe);
                inventory.add(potatoes);
                inventory.add(carrots);
                inventory.add(wheat);
                break;

            case KNIGHT:
                ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);

                inventory.add(chestplate);
                inventory.add(leggings);
                break;

            case BUILDER:
                ItemStack cobblestone = new ItemStack(Material.STONE, 16);
                ItemStack bricks = new ItemStack(Material.BRICK, 16);
                ItemStack planks = new ItemStack(Material.WOOD, 16);

                inventory.add(cobblestone);
                inventory.add(bricks);
                inventory.add(planks);
                break;

            case SNOWMAN:
                ItemStack snowballs = new ItemStack(Material.SNOW_BALL, 12);

                inventory.add(snowballs);
                break;

            case ALCHEMIST:
                ItemStack potion1 = new ItemStack(Material.POTION);
                PotionMeta meta1 = (PotionMeta) potion1.getItemMeta();
                meta1.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1), true);
                potion1.setItemMeta(meta1);

                ItemStack potion2 = new ItemStack(Material.POTION);
                PotionMeta meta2 = (PotionMeta) potion2.getItemMeta();
                meta2.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 8, 1), true);
                potion2.setItemMeta(meta2);

                inventory.add(potion1);
                inventory.add(potion2);
                break;
        }

    }

}
