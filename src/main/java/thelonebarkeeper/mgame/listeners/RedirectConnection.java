package thelonebarkeeper.mgame.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import thelonebarkeeper.mgame.manager.GameManager;

public class RedirectConnection implements PluginMessageListener {

    private int maxServer = 1;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("GetServer")) {
            String servername = in.readUTF();
            int num = Integer.parseInt(servername.split("skywars-")[0]) + 1;
            if (num > maxServer) {
                GameManager.playerToLobby(player);
                return;
            }
            servername = "skywars-" + num;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(servername);
        }
    }
}
