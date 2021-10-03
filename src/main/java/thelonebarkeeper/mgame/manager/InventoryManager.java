package thelonebarkeeper.mgame.manager;

import org.bukkit.entity.Player;
import thelonebarkeeper.mgame.objects.GamePlayer;

public class InventoryManager {



    public void setupPlayerInventory(GamePlayer player) {
        Player actualPlayer = player.getPlayer();
        actualPlayer.getInventory().clear();
    }
}
