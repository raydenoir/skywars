package thelonebarkeeper.mgame.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DataManager {

    public static HashMap<String, Data> dataHashMap = new HashMap<>();

    public static void sendStats() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("hub");
        out.writeUTF("BungeeCord"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        for (Data data : dataHashMap.values()) {
            try {
                msgout.writeUTF(data.toString()); // You can do anything you want with msgout
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
        }
    }

}
