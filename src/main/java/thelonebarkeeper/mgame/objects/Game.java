package thelonebarkeeper.mgame.objects;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.manager.InventoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Game {

    private HashSet<GamePlayer> gamePlayers = new HashSet<>();
    private HashSet<GamePlayer> alivePlayers = new HashSet<>();
    private static GameState state;
    private static HashMap<String, Boolean> freeSpawn = new HashMap<>();


    public Game() {
        for (String location : GameManager.getMap().getSpawnLocations())
            freeSpawn.put(location, true);

        this.setState(GameState.PREPARATION);
    }

    public HashSet<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            players.add(gamePlayer.getPlayer());
        }
        return players;
    }

    public HashSet<GamePlayer> getAliveGamePlayers() {
        return alivePlayers;
    }

    public HashSet<Player> getAlivePlayers() {
        HashSet<Player> players = new HashSet<>();
        for (GamePlayer gamePlayer : alivePlayers) {
            players.add(gamePlayer.getPlayer());
        }
        return players;
    }

    public void killPlayer(GamePlayer gamePlayer) {
        alivePlayers.remove(gamePlayer);
        gamePlayer.setSpectator();
        gamePlayer.getPlayer().teleport(gamePlayer.getPlayer().getWorld().getSpawnLocation().add(0, 30, 0));
    }

    public GamePlayer findGamePlayer(Player player) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    public GamePlayer findGamePlayer(String playerName) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (Objects.equals(gamePlayer.getName(), playerName)) {
                return gamePlayer;
            }
        }
        return null;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        Game.state = state;
    }

    public void spawnPlayer(GamePlayer player) {
        Player actualPlayer = player.getPlayer();
        gamePlayers.add(player);
        alivePlayers.add(player);
        actualPlayer.setGameMode(GameMode.ADVENTURE);
        InventoryManager.setupStartInventory(player);
        actualPlayer.setHealth(actualPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public String getFreeLocation() {
        for (String location : freeSpawn.keySet()) {
            if (freeSpawn.get(location)) {
                freeSpawn.put(location, false);
                return location;
            }
        }
        return null;
    }

    public void openLocation(Location location) {
        String loc = location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        freeSpawn.put(loc, true);
    }
}


