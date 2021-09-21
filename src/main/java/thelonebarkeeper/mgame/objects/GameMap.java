package thelonebarkeeper.mgame.objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;
import thelonebarkeeper.mgame.SkyWars;

import java.util.Arrays;
import java.util.List;

public class GameMap {

    public GameMap(String name, int minPlayers, int maxPlayers, List<String> spawnLocations) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        GameMap.spawnLocations = spawnLocations;
    }

    public GameMap() {
        FileConfiguration config = SkyWars.getInstance().getConfig();
        this.name = config.getString("name");
        this.minPlayers = config.getInt("minPlayers");
        this.maxPlayers = config.getInt("maxPlayers");
        GameMap.spawnLocations = config.getStringList("locations");
    }

    private String name;
    private int minPlayers;
    private int maxPlayers;
    private static List<String> spawnLocations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<String> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(List<String> spawnLocations) {
        GameMap.spawnLocations = spawnLocations;
    }

    @Override
    public String toString() {
        return "\nName: " + name + "\nMinPlayers: " + minPlayers + "\nMaxPlayers: " + maxPlayers + "\nSpawnLocations: " + spawnLocations + "\n";
    }

}


