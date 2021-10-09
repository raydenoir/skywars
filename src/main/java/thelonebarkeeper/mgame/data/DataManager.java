package thelonebarkeeper.mgame.data;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import thelonebarkeeper.mgame.SkyWars;

public class DataManager {
    public static void sendStat(DataType type, String playerName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(String.format("PlayerData,%s,%s", playerName, type.name()));

        SkyWars.getInstance().getServer().sendPluginMessage(SkyWars.getInstance(), "SkyWarsData", out.toByteArray());
    }
}
