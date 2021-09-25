package thelonebarkeeper.mgame.cmds;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.manager.GameManager;

public class HubCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !command.getName().equalsIgnoreCase("hub"))
            return false;
        Player player = (Player) sender;

        GameManager.removePlayer(GameManager.getGamePlayer(player), true);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("hub");
        player.sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());

        return false;
    }
}
