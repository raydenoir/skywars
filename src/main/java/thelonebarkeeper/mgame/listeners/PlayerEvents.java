package thelonebarkeeper.mgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.objects.GameState;

import java.util.HashMap;
import java.util.Objects;

public class PlayerEvents implements Listener {

    private static HashMap<Player, Player> lastDamage = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!(GameManager.getGame().getState() == GameState.RUN || GameManager.getGame().getState() == GameState.END)) {
            if (!Objects.equals(player.getVelocity(), new Vector(0, 0, 0)))
                player.teleport(player.getLocation().add(player.getVelocity().multiply(-1)));
        }


        if (player.getLocation().getBlockY() < 0) {
            GameManager.removePlayer(GameManager.getGamePlayer(player), false);
        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getEntity() instanceof Player) {
            //DamageTaker is a Player
            Player taker = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                //Damage Causer is also a player
                Player damagerPlayer = (Player) event.getDamager();

                lastDamage.put(taker, damagerPlayer);
            }
        }

        Player player = (Player) event.getEntity();
        if (event.getDamage() > player.getHealth()) {
            event.setCancelled(true);
            for(Player inGamePlayer : GameManager.getGame().getPlayers()) {
                inGamePlayer.sendMessage(String.format("%s убил %s", lastDamage.get(player).getName(), player.getName()));
                DataManager.dataHashMap.get(lastDamage.get(player).getName()).addKill();
            }
            GameManager.removePlayer(GameManager.getGamePlayer(player), false);
        }
    }

}





