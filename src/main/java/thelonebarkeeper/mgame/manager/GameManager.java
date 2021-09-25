package thelonebarkeeper.mgame.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.objects.*;
import thelonebarkeeper.mgame.tasks.GameStartTask;

public class GameManager {

    private static GameMap gameMap = new GameMap();
    private static final Game game = new Game();

    public static GameStartTask startTask = new GameStartTask(10);


    public static GamePlayer getGamePlayer(Player player) {
        return game.findGamePlayer(player);
    }

    public static GameMap getMap() {
        return gameMap;
    }

    public static void setMap(GameMap gameMap) {
        GameManager.gameMap = gameMap;
    }

    public static Game getGame() {
        return game;
    }

    public static void addPlayer(GamePlayer player) {
        game.addPlayer(player);
        player.getPlayer().setGameMode(GameMode.ADVENTURE);

        if (game.getAlivePlayers().size() == GameManager.getMap().getMinPlayers()) {
            startTask.startTimer();
        }
    }

    public static void removePlayer(GamePlayer player, boolean isLeft) {
        game.removePlayer(player);
        player.setSpectator();
        if (isLeft) {
            player.setState(GamePlayerState.LEFT);
            game.getGamePlayers().remove(player);
        }

        if (game.getAlivePlayers().size() == 1) {
            endGame();
        }

        player.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation().add(0, 30, 0));
    }

    public static void endGame() {
        Game game = GameManager.getGame();
        GamePlayer winner = game.getAlivePlayers().get(0);
        for (Player player : game.getPlayers()) {
            player.sendMessage(ChatColor.BOLD + String.format("%s победил!", winner.getPlayer().getName()));
            DataManager.dataHashMap.get(player.getName()).setWinner();
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 0.2f, 1.2f);
            DataManager.sendStats();
        }
        Bukkit.getScheduler().runTaskLater(SkyWars.getInstance(), () -> {
            for (Player player : game.getPlayers()) {
                playerToLobby(player);
            }
            Bukkit.getServer().unloadWorld("world", true);
            Bukkit.getServer().shutdown();
        }, 200);


    }

    public void startGame() {
        startTask.startTimer();
    }

    public static void playerToLobby(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("lobby");
        player.sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());
    }
}
