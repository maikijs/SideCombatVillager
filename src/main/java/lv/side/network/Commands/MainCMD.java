package lv.side.network.Commands;

import lv.side.network.CombatVillager;
import lv.side.network.Utils.ColorUtils;
import lv.side.network.Utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCMD implements CommandExecutor {

    private final CombatVillager plugin;

    public MainCMD(CombatVillager plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ColorUtils.sendConsoleMessage(Messages.get("must-be-player"));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("SideAdmin.scv")) {
            ColorUtils.sendMessage(p, Messages.get("no-permission"));
            return true;
        }
        if (args.length == 1 && args[0].equals("reload")) {
            this.plugin.saveDefaultConfig();
            this.plugin.reloadConfig();
            new Messages();
            if (plugin.getConfig().isConfigurationSection("messages"))
                Messages.load(plugin.getConfig().getConfigurationSection("messages"));
            ColorUtils.sendMessage(p, Messages.get("reloaded"));
            return true;

        }
        if(args.length == 0){
            ColorUtils.sendMessage(p, "&4&lSCV Help.");
            ColorUtils.sendMessage(p, "&r");
            ColorUtils.sendMessage(p, "&f/scv reload");
            ColorUtils.sendMessage(p, "&f/logout");
            ColorUtils.sendMessage(p, "&r");
            ColorUtils.sendMessage(p, "&7&oVersion 1.0 (side.lv edition)");
        }
        return true;
    }
}
