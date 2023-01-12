package lv.side.network.Commands;

import lv.side.network.CombatVillager;
import lv.side.network.Managers.LogoutTask;
import lv.side.network.Utils.ColorUtils;
import lv.side.network.Utils.Messages;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LogoutCMD implements CommandExecutor {

    private final CombatVillager plugin;
    public static Map<UUID, Player> logoutplayers = new HashMap<>();
    private static List<String> worlds;
    private final boolean worldcheck;

    public LogoutCMD(CombatVillager plugin) {
        this.plugin = plugin;
        worlds = plugin.getConfig().getStringList("logger.worlds.list");
        worldcheck = plugin.getConfig().getBoolean("logger.worlds.enabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ColorUtils.sendConsoleMessage(Messages.get("must-be-player"));
            return true;
        }

        Player player = (Player) sender;

        if(!plugin.getConfig().getBoolean("logout-command.enabled")) {
            player.sendMessage(Messages.get("logout-cmd-disabled"));
            return false;
        }

        if (worldcheck) {
            if (!(worlds.contains(player.getWorld().getName()))) {
                player.sendMessage(Messages.get("not-in-this-world"));
                return false;
            }
        }

        Player p = (Player) sender;
        logoutplayers.put(p.getUniqueId(), p);
        LogoutTask.run(plugin, p);
        ColorUtils.sendTitle("", Messages.get("title-logout-started"), 0, 25, 0, p);
        ColorUtils.playSound(Sound.BLAZE_DEATH, 1, 1, p);
        p.sendMessage(Messages.get("chat-logout-started"));
        return true;
    }
}
