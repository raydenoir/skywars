package thelonebarkeeper.mgame.objects;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.manager.GameManager;

public class GamePlayer {

    private final Player player;
    private GameTeam team;
    private GamePlayerState gamePlayerState;
    private Location spawnLocation;

//    public GamePlayer(Player player, GameTeam gameTeam) {
//        this.player = player;
//        this.team = gameTeam;
//        this.setState(GamePlayerState.ALIVE);
//        GameManager.getGame().addPlayer(this);
//    }

    public GamePlayer(Player player, Location spawnLocation) {
        this.player = player;
        this.setState(GamePlayerState.ALIVE);
        this.spawnLocation = spawnLocation;
        GameManager.addPlayer(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public GamePlayerState getState() {
        return gamePlayerState;
    }

    public void setState(GamePlayerState gamePlayerState) {
        this.gamePlayerState = gamePlayerState;
    }

    public void setSpectator() {
        getPlayer().setHealth(20);
        getPlayer().setGameMode(GameMode.SPECTATOR);
        setState(GamePlayerState.SPECTATOR);
    }
}

