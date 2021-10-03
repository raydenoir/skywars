package thelonebarkeeper.mgame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.manager.GameManager;

import java.util.HashMap;

public class PlayerEvents implements Listener {

    private static HashMap<Player, Player> lastDamage = new HashMap<>();

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
        GameManager.removePlayer(GameManager.getGamePlayer(player), false);
        DataManager.dataHashMap.get(lastDamage.get(player).getName()).addKill();

        for(Player inGamePlayer : Bukkit.getServer().getOnlinePlayers()) {
            inGamePlayer.sendMessage(ChatColor.BOLD + String.format("%s убил %s", lastDamage.get(player).getName(), player.getName()));
        }



    }

}





