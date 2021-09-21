package thelonebarkeeper.mgame;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import thelonebarkeeper.mgame.listeners.PlayerConnection;
import thelonebarkeeper.mgame.listeners.PlayerEvents;
import thelonebarkeeper.mgame.listeners.RedirectConnection;

import java.io.IOException;

//TODO: main game mechanics, GameManager, cmd, map cfg

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
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new RedirectConnection());

//        Bukkit.getPluginCommand("end").setExecutor(new TestCommands());

    }

    public void setupDirectories() {
        if (instance.getDataFolder().mkdir())
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
}
