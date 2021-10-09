package thelonebarkeeper.mgame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.data.DataType;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.manager.InventoryManager;
import thelonebarkeeper.mgame.objects.ClassType;
import thelonebarkeeper.mgame.objects.GameClass;
import thelonebarkeeper.mgame.objects.GamePlayer;
import thelonebarkeeper.mgame.objects.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlayerEvents implements Listener {

    private static final HashMap<Player, Player> lastDamage = new HashMap<>();

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player taker = (Player) event.getEntity();
        if (event.getDamager() instanceof Player) {
            lastDamage.put(taker, (Player) event.getDamager());
        }
    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent event) {
        Player player = event.getEntity();
        player.spigot().respawn();
        event.setDeathMessage("");
        GameManager.removePlayer(GameManager.getGamePlayer(player), false);

        if (lastDamage.get(player).getName() != null)
            DataManager.sendStat(DataType.KILL, lastDamage.get(player).getName());

        if (GameManager.getGame().getState() == GameState.RUN) {
            if (lastDamage.get(player) == null) {
                for (Player inGamePlayer : Bukkit.getServer().getOnlinePlayers()) {
                    inGamePlayer.sendMessage(ChatColor.BOLD + String.format("%s умер (-ла).", player.getName()));
                }
                return;
            }

            for (Player inGamePlayer : Bukkit.getServer().getOnlinePlayers()) {
                inGamePlayer.sendMessage(ChatColor.BOLD + String.format("%s убил %s", lastDamage.get(player).getName(), player.getName()));
            }
        }



    }

    @EventHandler
    public void onPlayerChestPlace (BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.CHEST) {
            block.setMetadata("isPlaced", new FixedMetadataValue(SkyWars.getInstance(), true));
        }
    }

    @EventHandler
    public void onPlayerChestOpen (InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player) || !(event.getInventory().getHolder() instanceof Chest))
            return;

        Player player = (Player) event.getPlayer();
        Chest chest = (Chest) event.getInventory().getHolder();
        Block center = player.getWorld().getSpawnLocation().getBlock();

        int x = chest.getX() - center.getX();
        int y = chest.getY() - center.getY();
        int z = chest.getZ() - center.getZ();

        int d = x*x + y*y + z*z;

        InventoryManager.createChest(player, chest, d <= 225);

    }

    @EventHandler
    public void onPlayerInventoryCLick (InventoryClickEvent event) {
        GameState state = GameManager.getGame().getState();

        if (state != GameState.PREPARATION && state != GameState.COUNTDOWN)
            return;
        if (event.getCurrentItem() == null)
            return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = GameManager.getGamePlayer(player.getName());

        Material item = event.getCurrentItem().getType();
        for (ClassType type : ClassType.values()) {
            if (type.icon.getType() == item) {
                gamePlayer.setGameClass(new GameClass(type));
                player.closeInventory();
                player.sendMessage(ChatColor.BOLD + "Вы выбрали класс " + type.name);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerRMB(PlayerInteractEvent event) {
        GameState state = GameManager.getGame().getState();

        if (state != GameState.PREPARATION && state != GameState.COUNTDOWN)
            return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        Player player = event.getPlayer();
        Material item = player.getInventory().getItemInMainHand().getType();

        if (item == Material.BOOK) {
            Inventory classes = Bukkit.createInventory(player, 9);
            for (ClassType type : ClassType.values()) {
                ItemStack icon = type.icon;
                ItemMeta meta = icon.getItemMeta();
                meta.setDisplayName(type.name);
                meta.setLore(Collections.singletonList(type.lore));
                icon.setItemMeta(meta);
                classes.addItem(icon);
            }
            player.openInventory(classes);
        }

        if (item == Material.MAGMA_CREAM) {
            GameManager.playerToLobby(player);
        }
    }

    @EventHandler
    public void onPlayerF (PlayerSwapHandItemsEvent event) {
        GameState state = GameManager.getGame().getState();

        if (state != GameState.COUNTDOWN && state != GameState.PREPARATION)
            return;

        event.setCancelled(true);
    }
}





