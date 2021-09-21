package thelonebarkeeper.mgame.objects;

import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.manager.GameManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private ArrayList<GamePlayer> gamePlayers = new ArrayList<>();
    private ArrayList<GamePlayer> alivePlayers = new ArrayList<>();
    private static GameState state;
    private static HashMap<String, Boolean> freeSpawn = new HashMap<>();


    public Game() {
        for (String location : GameManager.getMap().getSpawnLocations())
            freeSpawn.put(location, true);
    }

    public ArrayList<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            players.add(gamePlayer.getPlayer());
        }
        return players;
    }

    public ArrayList<GamePlayer> getAlivePlayers() {
        return alivePlayers;
    }

    public void removePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        alivePlayers.remove(gamePlayer);
    }

    public GamePlayer findGamePlayer(Player player) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getPlayer() == player) {
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

    public void addPlayer(GamePlayer player) {
        gamePlayers.add(player);
        alivePlayers.add(player);
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
}


