package thelonebarkeeper.mgame.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.manager.InventoryManager;
import thelonebarkeeper.mgame.objects.GamePlayer;
import thelonebarkeeper.mgame.objects.GameState;

public class GameStartTask {

    int time;
    int timeSaver;
    BukkitTask task;

    public GameStartTask(int time) {
        this.time = time;
        timeSaver = time;
    }

    public BukkitTask startTimer() {
        time = timeSaver;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        GameManager.getGame().setState(GameState.COUNTDOWN);
        task = scheduler.runTaskTimer(SkyWars.getInstance(), () -> {
            if(time == 0) {
                stopTimer();

                Bukkit.broadcastMessage(ChatColor.BOLD + "Игра началась!");

                SkyWars.getInstance().pingBungee(false);

                GameManager.getGame().setState(GameState.RUN);

                for (GamePlayer player : GameManager.getGame().getGamePlayers()) {
                    Player actualPlayer = player.getPlayer();
                    actualPlayer.playSound(actualPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 1.5f);
                    actualPlayer.setGameMode(GameMode.SURVIVAL);

                    InventoryManager.setupInventory(player);
                }
                return;
            }

            if (time <= 5) {
                for (Player player : GameManager.getGame().getPlayers()) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.7f, 2f);
                }
            }

            time--;
        }, 0L, 20L);

        return task;
    }

    public void stopTimer() {
        task.cancel();
    }


}
