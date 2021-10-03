package thelonebarkeeper.mgame.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.SkyWars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DataManager {

    public static HashMap<String, Data> dataHashMap = new HashMap<>();

    public static void sendStats() {

        for (Data data : dataHashMap.values()) {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("lobby");
            out.writeUTF("PlayerData");

            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);

            try {
                msgout.writeUTF(data.toString());
                msgout.writeShort(123);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());

            Bukkit.getServer().sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());


        }
    }

    public static void setupPlayerData(Player player) {
        dataHashMap.put(player.getName().toLowerCase(), new Data(player.getName().toLowerCase()));
    }

}
