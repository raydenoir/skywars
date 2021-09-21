package thelonebarkeeper.mgame.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.data.Data;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.objects.Game;
import thelonebarkeeper.mgame.objects.GamePlayer;
import thelonebarkeeper.mgame.objects.GameState;

public class PlayerConnection implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(SkyWars.getInstance(), () -> {
            Player player = event.getPlayer();

            if (GameManager.getGame().getState() == GameState.RUN || GameManager.getGame().getState() == GameState.END) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServer");
                player.sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());
                event.setJoinMessage("");
                return;
            }


            DataManager.dataHashMap.put(player.getName(), new Data(player.getName()));


            String[] vectors = GameManager.getGame().getFreeLocation().split(",");
            int x = Integer.parseInt(vectors[0]);
            int y = Integer.parseInt(vectors[1]);
            int z = Integer.parseInt(vectors[2]);
            GameManager.addPlayer(new GamePlayer(player, player.getWorld().getBlockAt(x, y, z).getLocation().add(0.5, 0, 0.5) ));
            player.teleport(player.getWorld().getBlockAt(x, y, z).getLocation().add(0.5, 0, 0.5));
            player.getLocation().add(0, -1, 0).getBlock().setType(Material.BARRIER);
            player.getInventory().clear();



            event.setJoinMessage(ChatColor.BOLD + event.getPlayer().getName() + " присоединился (-ась) к игре. ("
                    + GameManager.getGame().getPlayers().size()
                    + "/" + GameManager.getMap().getMaxPlayers() + ")");
        }, 20L);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = GameManager.getGame();

        if (game.getState() == GameState.RUN || game.getState() == GameState.END) {
            if (!GameManager.getGame().getPlayers().contains(player)) {
                event.setQuitMessage("");
                return;
            }
        }
        GameManager.removePlayer(GameManager.getGamePlayer(player), true);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("hub");
        player.sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());

        if (game.getState() == GameState.COUNTDOWN && game.getGamePlayers().size() < GameManager.getMap().getMinPlayers()) {
            GameManager.startTask.stopTimer();
        }


        event.setQuitMessage(ChatColor.BOLD + event.getPlayer().getName() + " вышел (-ла) из игры. ("
                + GameManager.getGame().getPlayers().size()
                + "/" + GameManager.getMap().getMaxPlayers() + ")");


    }
}
