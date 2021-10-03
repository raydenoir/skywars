package thelonebarkeeper.mgame;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import thelonebarkeeper.mgame.listeners.PlayerConnection;
import thelonebarkeeper.mgame.listeners.PlayerEvents;

import java.io.IOException;

//TODO: loot mechanics, HUB and ADMIN commands

public final class SkyWars extends JavaPlugin {

    private static SkyWars instance;

    public static SkyWars getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupListeners();
        try {
            setupMapConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getWorld("world").setAutoSave(false);

        Bukkit.getConsoleSender().sendMessage(getServer().getServerName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public void setupListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerConnection(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "SkyWarsConnect");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "SkyWarsPingTrue");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "SkyWarsPingFalse");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "PlayerData");
    }

    public void setupDirectories() {
        if (this.getDataFolder().mkdir())
            Bukkit.getServer().getLogger().info("Plugin directories are successfully created.");

    }

    public void setupMapConfig() throws IOException {
        FileConfiguration config = getConfig();
        if(!this.getDataFolder().exists()) {
            setupDirectories();
            config.options().copyDefaults(true);
            saveConfig();
            saveDefaultConfig();
            config.set("name", "");
            config.set("minPlayers", 0);
            config.set("maxPlayers", 0);
            String[] locations = {"0,0,0", "0,0,0"};
            config.set("locations", locations);
        }

        config.options().copyDefaults(true);
        saveConfig();

    }

    public void pingBungee(Boolean bool) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF( "GameState," + getServer().getServerName() + "," + bool);

        for (Player player : getServer().getOnlinePlayers()) {
            if (player != null) {
                player.sendPluginMessage(this, "SkyWarsConnect", out.toByteArray());
                return;
            }
        }
    }
}
