package lv.side.network.Commands;

import com.connorlinfoot.titleapi.TitleAPI;
import lv.side.network.CombatVillager;
import lv.side.network.Managers.LogoutTask;
import lv.side.network.Managers.VillagerTask;
import lv.side.network.Utils.ColorUtils;
import lv.side.network.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogoutCMD implements CommandExecutor {

    private final CombatVillager plugin;
    public static Map<UUID, Player> logoutplayers = new HashMap<>();

    public LogoutCMD(CombatVillager plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ColorUtils.sendConsoleMessage(Messages.get("must-be-player"));
            return true;
        }
        Player p = (Player) sender;
        logoutplayers.put(p.getUniqueId(), p);
        LogoutTask.run(plugin, p);
        TitleAPI.sendTitle(p, "", Messages.get("title-logout-started"), 0, 25, 0);
        p.sendMessage(Messages.get("chat-logout-started"));
        return true;
    }
}
