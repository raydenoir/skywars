package thelonebarkeeper.mgame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.objects.Game;
import thelonebarkeeper.mgame.objects.GamePlayer;
import thelonebarkeeper.mgame.objects.GameState;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");

        Bukkit.getScheduler().runTaskLater(SkyWars.getInstance(), () -> {
            Player player = event.getPlayer();

            //In case if game has started while the player was trying to connect.
            if (GameManager.getGame().getState() == GameState.RUN) {
                player.sendMessage(ChatColor.BOLD + "Похоже, игра уже началась :/");
                GameManager.playerToLobby(player);
                return;
            }

            Location spawnLocation = getFreeLocation(player);
            player.teleport(spawnLocation);
            GameManager.addPlayer(new GamePlayer(player,spawnLocation));
            DataManager.setupPlayerData(player);

            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                onlinePlayer.sendMessage(ChatColor.BOLD + event.getPlayer().getName() + " присоединился (-ась) к игре. ("
                        + GameManager.getGame().getPlayers().size()
                        + "/" + GameManager.getMap().getMaxPlayers() + ")");
            }


        }, 20L);

        if (Bukkit.getServer().getOnlinePlayers().size() == GameManager.getMap().getMinPlayers()) {
            GameManager.startGame();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();
        GamePlayer gamePlayer = GameManager.getGamePlayer(playerName);
        Game game = GameManager.getGame();

        // If player is spectating, just return.
        if (!game.getAliveGamePlayers().contains(gamePlayer))
            return;

        GameManager.removePlayer(gamePlayer, true);

        //If game is going rn, change Event#eventMessage to the appropriate
        if (game.getState() == GameState.RUN || game.getState() == GameState.END) {
            int alivePlayers = GameManager.getGame().getAliveGamePlayers().size();
            event.setQuitMessage(ChatColor.BOLD + getMessageByPlayers(alivePlayers, playerName));
            return;
        }

        Location spawnLocation = gamePlayer.getSpawnLocation().add(-0.5, 1, -0.5);
        game.openLocation(spawnLocation);

        //If countdown is running, stop it.
        if (game.getState() == GameState.COUNTDOWN && game.getAlivePlayers().size() < GameManager.getMap().getMinPlayers()) {
            GameManager.stopTimer();
        }

        event.setQuitMessage(ChatColor.BOLD + playerName + " вышел (-ла) из игры. ("
                + (GameManager.getGame().getPlayers().size())
                + "/" + GameManager.getMap().getMaxPlayers() + ")");
    }

    private Location getFreeLocation(Player player) {
        String[] vectors = GameManager.getGame().getFreeLocation().split(",");
        int x = Integer.parseInt(vectors[0]);
        int y = Integer.parseInt(vectors[1]);
        int z = Integer.parseInt(vectors[2]);
        return player.getWorld().getBlockAt(x, y, z).getLocation().add(0.5, 0, 0.5);
    }

    private String getMessageByPlayers(int amount, String name) {
        if (amount % 100 > 10 && amount % 100 < 20) {
            return String.format("%s вышел (-ла). В живых осталось %d игроков.", name, amount);
        }

        if (amount % 10 == 1) {
            if (amount == 1)
                return "";
            else
                return String.format("%s вышел (-ла). В живых остался %d игрок.", name, amount);
        }

        if (amount % 10 == 0) {
            return String.format("%s вышел (-ла). В живых осталось %d игроков.", name, amount);
        }

        if (amount % 10 > 1 && amount % 10 < 5) {
            return String.format("%s вышел (-ла). В живых осталось %d игрока.", name, amount);
        }

        return String.format("%s вышел (-ла). В живых осталось %d игроков.", name, amount);
    }


}
