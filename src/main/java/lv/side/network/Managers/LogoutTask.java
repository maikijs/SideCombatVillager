package lv.side.network.Managers;

import com.connorlinfoot.titleapi.TitleAPI;
import lv.side.network.CombatVillager;
import lv.side.network.Commands.LogoutCMD;
import lv.side.network.Utils.ColorUtils;
import lv.side.network.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.NumberConversions;

import java.util.*;

public final class LogoutTask extends BukkitRunnable {

    private final static Map<UUID, LogoutTask> tasks = new HashMap<>();

    private final CombatVillager plugin;

    private final UUID playerId;

    private final Location loc;

    private final long logoutTime;

    private int remainingSeconds = Integer.MAX_VALUE;

    private boolean finished;

    LogoutTask(CombatVillager plugin, Player player, long logoutTime) {
        this.plugin = plugin;
        this.playerId = player.getUniqueId();
        this.loc = player.getLocation();
        this.logoutTime = logoutTime;
    }

    private int getRemainingSeconds() {
        long currentTime = System.currentTimeMillis();
        return logoutTime > currentTime ? NumberConversions.ceil((logoutTime - currentTime) / 1000D) : 0;
    }

    @Override
    public void run() {
        // Cancel the task if player is no longer online
        Player player = LogoutCMD.logoutplayers.get(playerId);;
        if (player == null) {
            cancel();
            return;
        }

        // Cancel the task if player has moved
        if (hasMoved(player)) {
            player.sendMessage(Messages.get("chat-logout-canceled"));
            TitleAPI.sendTitle(player, "", Messages.get("title-logout-canceled"), 0, 25, 0);
            cancel();
            return;
        }

        // Safely logout the player once timer is up
        int remainingSeconds = getRemainingSeconds();
        if (remainingSeconds <= 0) {
            finished = true;

            player.kickPlayer(Messages.get("kick-message"));


            if(isFinished(player)){
                purgeFinished();
            }
            cancel();
            return;
        }

        // Inform player
        if (remainingSeconds < this.remainingSeconds) {
            for (String number : plugin.getConfig().getStringList("logout-command.broadcast-times")){
                if(remainingSeconds == Integer.parseInt(number)){
                    String message = Messages.get("remaining-time").replace("{remaining}", String.valueOf(remainingSeconds));
                    TitleAPI.sendTitle(player, message, ColorUtils.color("&4&l" + remainingSeconds), 0, 25, 0);
                }
            }


            this.remainingSeconds = remainingSeconds;
        }
    }

    private boolean hasMoved(Player player) {
        Location l = player.getLocation();
        return loc.getWorld() != l.getWorld() || loc.getBlockX() != l.getBlockX() ||
                loc.getBlockY() != l.getBlockY() || loc.getBlockZ() != l.getBlockZ();
    }

    public static void run(CombatVillager plugin, Player player) {
        // Do nothing if player already has a task
        if (hasTask(player)) return;

        // Calculate logout time
        long logoutTime = System.currentTimeMillis() + (plugin.getConfig().getInt("logout-command.time") * 1000);

        // Run the task every few ticks for accuracy
        LogoutTask task = new LogoutTask(plugin, player, logoutTime);
        task.runTaskTimer(plugin, 0, 5);

        // Cache the task
        tasks.put(player.getUniqueId(), task);
    }

    public static boolean hasTask(Player player) {
        LogoutTask task = tasks.get(player.getUniqueId());
        if (task == null) return false;

        BukkitScheduler s = Bukkit.getScheduler();
        if (s.isQueued(task.getTaskId()) || s.isCurrentlyRunning(task.getTaskId())) {
            return true;
        }

        tasks.remove(player.getUniqueId());
        return false;
    }

    public static boolean isFinished(Player player) {
        return hasTask(player) && tasks.get(player.getUniqueId()).finished;
    }

    public static boolean cancel(Player player) {
        // Do nothing if player has no logout task
        if (!hasTask(player)) return false;

        // Cancel logout task
        Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()).getTaskId());

        // Remove task early to prevent exploits
        tasks.remove(player.getUniqueId());

        return true;
    }

    public static void purgeFinished() {
        Iterator<LogoutTask> iterator = tasks.values().iterator();
        BukkitScheduler s = Bukkit.getScheduler();

        // Loop over each task
        while (iterator.hasNext()) {
            int taskId = iterator.next().getTaskId();

            // Remove entry if task isn't running anymore
            if (!s.isQueued(taskId) && !s.isCurrentlyRunning(taskId)) {
                iterator.remove();
            }
        }
    }

}

