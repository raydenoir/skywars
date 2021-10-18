package thelonebarkeeper.mgame.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.data.DataManager;
import thelonebarkeeper.mgame.data.DataType;
import thelonebarkeeper.mgame.objects.*;
import thelonebarkeeper.mgame.tasks.GameStartTask;

public class GameManager {

    private static GameMap gameMap = new GameMap();
    private static final Game game = new Game();
    private static BukkitTask task;

    private static GameStartTask startTask = new GameStartTask(10);



    public static GamePlayer getGamePlayer(Player player) {
        return game.findGamePlayer(player);
    }

    public static GamePlayer getGamePlayer(String playerName) {
        return game.findGamePlayer(playerName);
    }

    public static GameMap getMap() {
        return gameMap;
    }

    public static Game getGame() {
        return game;
    }

    public static void addPlayer(GamePlayer player) {
        game.spawnPlayer(player);

    }

    public static void removePlayer(GamePlayer player, boolean isLeft) {
        if (isLeft) {
            game.getGamePlayers().remove(player);
            player.resetDefaults();
        }

        game.killPlayer(player);

        if (game.getState() == GameState.COUNTDOWN || game.getState() == GameState.PREPARATION)
            return;

        if (game.getAlivePlayers().size() == 1) {
            endGame();
        }
    }

    public static void endGame() {
        Game game = GameManager.getGame();
        GamePlayer winner = (GamePlayer) game.getAliveGamePlayers().toArray()[0];

        for (Player player : game.getPlayers()) {
            player.sendMessage(ChatColor.BOLD + String.format("%s победил!", winner.getPlayer().getName()));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 0.2f, 1.2f);

            DataManager.sendStat(DataType.WIN, winner.getName());
        }

        Bukkit.getScheduler().runTaskLater(SkyWars.getInstance(), () -> {
            for (Player player : game.getPlayers()) {
                playerToLobby(player);
            }
            for (World world : Bukkit.getWorlds())
                Bukkit.getServer().unloadWorld(world, false);
        }, 100);
        Bukkit.getScheduler().runTaskLater(SkyWars.getInstance(), () -> {
            Bukkit.getServer().shutdown();
        }, 200);


    }

    public static void startGame() {
        task = startTask.startTimer();
    }

    public static void stopTimer() {
        task.cancel();
    }

    public static void playerToLobby(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("SW-LOBBY");
        player.sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());
    }
}
