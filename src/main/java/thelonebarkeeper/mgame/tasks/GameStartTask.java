package thelonebarkeeper.mgame.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import thelonebarkeeper.mgame.SkyWars;
import thelonebarkeeper.mgame.manager.GameManager;
import thelonebarkeeper.mgame.objects.GameState;

public class GameStartTask {

    int time;
    int taskID;

    public GameStartTask(int time) {
        this.time = time;
    }

    public void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        GameManager.getGame().setState(GameState.COUNTDOWN);
        taskID = scheduler.scheduleSyncRepeatingTask(SkyWars.getInstance(), () -> {

            if(time == 0) {
                Bukkit.broadcastMessage(ChatColor.BOLD + "Игра началась!");
                GameManager.getGame().setState(GameState.RUN);
                for (Player player : GameManager.getGame().getPlayers()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.5f);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                }
                stopTimer();
                return;
            }

            if (time <= 5) {
                for (Player player : GameManager.getGame().getPlayers()) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.7f, 2f);
                }
            }

            time--;
        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }


}
