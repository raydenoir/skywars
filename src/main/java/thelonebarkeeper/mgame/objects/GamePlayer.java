package thelonebarkeeper.mgame.objects;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.manager.GameManager;

public class GamePlayer {

    private final Player player;
    private String name;
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
        this.name = player.getName();
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
        Player player = getPlayer();
        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        setState(GamePlayerState.SPECTATOR);
    }

    public void resetDefaults() {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getName() {
        return name;
    }
}

