package thelonebarkeeper.mgame.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import thelonebarkeeper.mgame.objects.GameClass;
import thelonebarkeeper.mgame.objects.GamePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class InventoryManager {

    private static HashMap<Player, GameClass> playerClasses = new HashMap<>();
    private static HashMap<Player, ArrayList<ItemStack>> playerChest = new HashMap<>();
    private static ArrayList<Chest> openedChests = new ArrayList<>();


    public static void setupInventory(GamePlayer player) {
        Player actualPlayer = player.getPlayer();
        actualPlayer.getInventory().clear();

        if (player.getGameClass() != null)
            actualPlayer.getInventory().setContents(player.getGameClass().getInventory());
    }

    public static void setupStartInventory(GamePlayer player) {
        Player actualPlayer = player.getPlayer();
        actualPlayer.getInventory().clear();

        ItemMeta meta;

        ItemStack classChoose = new ItemStack(Material.BOOK);
        meta = classChoose.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "Выбор класса");
        classChoose.setItemMeta(meta);
        actualPlayer.getInventory().setItem(0, classChoose);

        ItemStack leave = new ItemStack(Material.MAGMA_CREAM);
        meta = leave.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "Выйти в лобби");
        leave.setItemMeta(meta);
        actualPlayer.getInventory().setItem(8, leave);
    }

    public static void createChest(Player player, Chest chest, boolean isRich) {

        if ((!openedChests.isEmpty() && openedChests.contains(chest)) || (!chest.getBlock().getMetadata("isPlaced").isEmpty() && chest.getBlock().getMetadata("isPlaced").get(0).asBoolean()))
            return;

        openedChests.add(chest);

        if (playerChest.get(player) != null) {
            addItemsRandomly(chest, playerChest.get(player));
            playerChest.put(player, null);
            return;
        }

        ArrayList<ItemStack> items = new ArrayList<>();

        items.addAll(getArmor(isRich));
        items.addAll(getTools(isRich));
        items.addAll(getConsumables(isRich));
        items.addAll(getWeapons(isRich));
        items.addAll(getOther(isRich));
        Collections.shuffle(items);

        if (!isRich) {
            Random random = new Random();
            ArrayList<ItemStack> halfItems = new ArrayList<>();
            for (int i = 0; i < items.size() / 2; i++) {
                int slot = random.nextInt(items.size());
                ItemStack itemStack = items.get(slot);
                halfItems.add(itemStack);
                items.remove(itemStack);
            }
            addItemsRandomly(chest, halfItems);
            playerChest.put(player, items);
            return;
        }

        addItemsRandomly(chest, items);

    }

    private static void addItemsRandomly (Chest chest, ArrayList<ItemStack> items) {

        Inventory inventory = chest.getInventory();
        Random random = new Random();
        for (ItemStack item : items) {
            int slot;
            boolean isPlaced = false;
            do {
                slot = random.nextInt(27);
                if (inventory.getItem(slot) == null) {
                    inventory.setItem(slot, item);
                    isPlaced = true;
                }
            } while (!isPlaced);
        }
    }

    private static ArrayList<ItemStack> getArmor (boolean isRich) {
        ArrayList<ItemStack> armor = new ArrayList<>();
        Random random = new Random();

        if (!isRich) {
            armor.add(ItemPreset.LEATHER_HELMET.itemStack);
            armor.add(ItemPreset.LEATHER_CHESTPLATE.itemStack);
            armor.add(ItemPreset.LEATHER_LEGGINGS.itemStack);
            armor.add(ItemPreset.LEATHER_BOOTS.itemStack);

            if (random.nextInt(100) < 5) {
                armor.set(0, ItemPreset.DIAMOND_HELMET.itemStack);
                return armor;
            }
            if (random.nextInt(100) < 5) {
                armor.set(3, ItemPreset.DIAMOND_BOOTS.itemStack);
                return armor;
            }
            if (random.nextInt(100) < 30) {
                armor.set(2, ItemPreset.IRON_LEGGINGS.itemStack);
                return armor;
            }
            if (random.nextInt(100) < 30) {
                armor.set(1, ItemPreset.IRON_CHESTPLATE.itemStack);
                return armor;
            }
            if (random.nextInt(100) < 50) {
                armor.set(0, ItemPreset.IRON_HELMET.itemStack);
                return armor;
            }
            if (random.nextInt(100) < 50) {
                armor.set(3, ItemPreset.IRON_BOOTS.itemStack);
                return armor;
            }

            return armor;
        }

        if (random.nextInt(100) < 20) {
            armor.add(ItemPreset.DIAMOND_CHESTPLATE.itemStack);
        }
        if (random.nextInt(100) < 30) {
            armor.add(ItemPreset.DIAMOND_LEGGINGS.itemStack);
        }
        if (random.nextInt(100) < 50) {
            armor.add(ItemPreset.DIAMOND_HELMET.itemStack);
        }
        if (random.nextInt(100) < 20) {
            armor.add(ItemPreset.DIAMOND_BOOTS.itemStack);
        }

        return armor;
    }

    private static ArrayList<ItemStack> getTools (boolean isRich) {
        if (isRich)
            return new ArrayList<>();

        ArrayList<ItemStack> tools = new ArrayList<>();
        Random random = new Random();

        tools.add(ItemPreset.WOOD_PICKAXE.itemStack);
        tools.add(ItemPreset.WOOD_AXE.itemStack);

        if (random.nextInt(100) < 50)
            tools.set(0, ItemPreset.STONE_PICKAXE.itemStack);
        if (random.nextInt(100) < 50)
            tools.set(1, ItemPreset.STONE_AXE.itemStack);

        if (random.nextInt(100) < 30)
            tools.set(0, ItemPreset.IRON_PICKAXE.itemStack);
        if (random.nextInt(100) < 50)
            tools.set(1, ItemPreset.IRON_AXE.itemStack);


        return tools;
    }

    private static ArrayList<ItemStack> getConsumables (boolean isRich) {

        ArrayList<ItemStack> consumables = new ArrayList<>();
        Random random = new Random();

        if (!isRich) {
            consumables.add(ItemPreset.SALO.itemStack);
            consumables.add(ItemPreset.BREAD.itemStack);

            ItemStack exp = null;
            if (random.nextInt(100) < 70)
                exp = ItemPreset.EXP8.itemStack;
            if (random.nextInt(100) < 50)
                exp = ItemPreset.EXP10.itemStack;
            if (random.nextInt(100) < 40)
                exp = ItemPreset.EXP14.itemStack;
            if (random.nextInt(100) < 20)
                exp = ItemPreset.EXP16.itemStack;
            if (exp != null)
                consumables.add(exp);

            if (random.nextInt(100) < 30)
                consumables.add(ItemPreset.GAPPLE.itemStack);

            ItemStack arrows = null;
            if (random.nextInt(100) < 70)
                arrows = ItemPreset.ARROW4.itemStack;
            if (random.nextInt(100) < 50)
                arrows = ItemPreset.ARROW6.itemStack;
            if (random.nextInt(100) < 30)
                arrows = ItemPreset.ARROW8.itemStack;
            if (arrows != null)
                consumables.add(arrows);


            return consumables;
        }

        if (random.nextInt(100) < 50)
            consumables.add(ItemPreset.GAPPLE_MORE.itemStack);
        if (random.nextInt(100) < 50)
            consumables.add(ItemPreset.ENDER_PEARL.itemStack);
        if (random.nextInt(100) < 50)
            consumables.add(ItemPreset.SALO_MORE.itemStack);
        if (random.nextInt(100) < 50)
            consumables.add(ItemPreset.EXP16.itemStack);

        return consumables;
    }

    private static ArrayList<ItemStack> getWeapons (boolean isRich) {
        ArrayList<ItemStack> weapons = new ArrayList<>();
        Random random = new Random();

        if (!isRich) {

            ItemStack sword = ItemPreset.WOOD_SWORD.itemStack;
            if (random.nextInt(100) < 50)
                sword = ItemPreset.STONE_SWORD.itemStack;
            if (random.nextInt(100) < 25)
                sword = ItemPreset.IRON_SWORD.itemStack;
            weapons.add(sword);

            ItemStack bow = null;
            if (random.nextInt(100) < 20)
                bow = ItemPreset.BOW.itemStack;
            if (random.nextInt(100) < 5) {
                bow = ItemPreset.BOW.itemStack;
                bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 0);
                ItemMeta meta = bow.getItemMeta();
                meta.setDisplayName("А ты везучий!");
                bow.setItemMeta(meta);
            }
            if (bow != null)
                weapons.add(bow);

            return weapons;
        }

        if (random.nextInt(100) < 50)
            weapons.add(ItemPreset.DIAMOND_SWORD.itemStack);

        return weapons;
    }

    private static ArrayList<ItemStack> getOther (boolean isRich) {
        if (isRich)
            return new ArrayList<>();

        ArrayList<ItemStack> other = new ArrayList<>();
        Random random = new Random();

        if (random.nextInt(100) < 50)
            other.add(ItemPreset.PLANKS.itemStack);
        else
            other.add(ItemPreset.COBBLESTONE.itemStack);

        if (random.nextInt(100) < 60)
            other.add(ItemPreset.WATER.itemStack);

        if (random.nextInt(100) < 40) {
            other.add(ItemPreset.FEATHERS.itemStack);
            other.add(ItemPreset.FLINT.itemStack);
        }

        return other;
    }
}

enum ItemPreset {

    /**
     * Armor
     */
    LEATHER_HELMET (new ItemStack(Material.LEATHER_HELMET)),
    LEATHER_CHESTPLATE (new ItemStack(Material.LEATHER_CHESTPLATE)),
    LEATHER_LEGGINGS (new ItemStack(Material.LEATHER_LEGGINGS)),
    LEATHER_BOOTS (new ItemStack(Material.LEATHER_BOOTS)),

    IRON_HELMET (new ItemStack(Material.IRON_HELMET)),
    IRON_CHESTPLATE (new ItemStack(Material.IRON_CHESTPLATE)),
    IRON_LEGGINGS (new ItemStack(Material.IRON_LEGGINGS)),
    IRON_BOOTS (new ItemStack(Material.IRON_BOOTS)),

    DIAMOND_HELMET (new ItemStack(Material.DIAMOND_HELMET)),
    DIAMOND_CHESTPLATE (new ItemStack(Material.DIAMOND_CHESTPLATE)),
    DIAMOND_LEGGINGS (new ItemStack(Material.DIAMOND_LEGGINGS)),
    DIAMOND_BOOTS (new ItemStack(Material.DIAMOND_BOOTS)),


    /**
     * Tools
     */
    WOOD_PICKAXE (new ItemStack(Material.WOOD_PICKAXE)),
    WOOD_AXE (new ItemStack(Material.WOOD_AXE)),
    STONE_PICKAXE (new ItemStack(Material.STONE_PICKAXE)),
    STONE_AXE (new ItemStack(Material.STONE_AXE)),
    IRON_PICKAXE (new ItemStack(Material.IRON_PICKAXE)),
    IRON_AXE (new ItemStack(Material.IRON_AXE)),


    /**
     * Weapons
     */
    WOOD_SWORD (new ItemStack(Material.WOOD_SWORD)),
    STONE_SWORD (new ItemStack(Material.STONE_SWORD)),
    IRON_SWORD (new ItemStack(Material.IRON_SWORD)),
    DIAMOND_SWORD (new ItemStack(Material.DIAMOND_SWORD)),

    BOW (new ItemStack(Material.BOW)),


    /**
     * Consumables
     */
    BREAD (new ItemStack(Material.BREAD, 3)),
    SALO (new ItemStack(Material.GRILLED_PORK, 2)),
    SALO_MORE (new ItemStack(Material.GRILLED_PORK, 10)),

    EXP8 (new ItemStack(Material.EXP_BOTTLE, 8)),
    EXP10 (new ItemStack(Material.EXP_BOTTLE, 10)),
    EXP14 (new ItemStack(Material.EXP_BOTTLE, 14)),
    EXP16 (new ItemStack(Material.EXP_BOTTLE, 16)),

    GAPPLE (new ItemStack(Material.GOLDEN_APPLE)),
    GAPPLE_MORE (new ItemStack(Material.GOLDEN_APPLE, 4)),

    ENDER_PEARL(new ItemStack(Material.ENDER_PEARL)),

    ARROW4 (new ItemStack(Material.ARROW, 4)),
    ARROW6 (new ItemStack(Material.ARROW, 6)),
    ARROW8 (new ItemStack(Material.ARROW, 8)),


    /**
     * Others
     */
    PLANKS (new ItemStack(Material.WOOD, 19)),
    COBBLESTONE (new ItemStack(Material.COBBLESTONE, 19)),
    WATER (new ItemStack(Material.WATER_BUCKET)),

    FEATHERS (new ItemStack(Material.FEATHER, 2)),
    FLINT (new ItemStack(Material.FLINT, 2))

    ;


    public final ItemStack itemStack;

    ItemPreset(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
