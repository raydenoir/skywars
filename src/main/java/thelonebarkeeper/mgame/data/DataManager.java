package thelonebarkeeper.mgame.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import thelonebarkeeper.mgame.SkyWars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DataManager {

    public static HashMap<String, Data> dataHashMap = new HashMap<>();

    public static void sendStats() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("hub");
        out.writeUTF("BungeeCord");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        for (Data data : dataHashMap.values()) {
            try {
                msgout.writeUTF(data.toString());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());

            SkyWars.getInstance().getServer().sendPluginMessage(SkyWars.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

}
