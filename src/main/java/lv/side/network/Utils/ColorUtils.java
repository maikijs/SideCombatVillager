package lv.side.network.Utils;

import lv.side.network.CombatVillager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ColorUtils {
    private CombatVillager plugin;

    public ColorUtils(CombatVillager plugin) {
        this.plugin = plugin;
    }

    public static void playSound(Sound sound, float volume, float pitch, Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player player) {
        Title t = new Title(title, subtitle, 0, 30, 0);
        t.setTimingsToTicks();
        t.send(player);
    }

    public static List<String> color(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, color(input.get(i)));
        }

        return input;
    }

    public static String[] color(String[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = color(input[i]);
        }

        return input;
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(color(msg));
    }

    public static void sendMessage(Player p, String msg) {
        if (msg.isEmpty())
            return;
        p.sendMessage(color(msg));
    }

    public void sendMessageWithoutPrefix(Player p, String msg) {
        if (msg.isEmpty())
            return;
        p.sendMessage(color(msg));
    }

    public void sendList(Player p, List<String> list) {
        if (list.isEmpty())
            return;
        for (String msg : list)
            p.sendMessage(color(msg));
    }

    public void sendMessageToStaff(String msg) {
        if (msg.isEmpty())
            return;
        String prefix = this.plugin.getConfig().getString("prefix");
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("SideStaff.staff"))
                staff.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
        }
    }

    public void sendAnnouncement(String message) {
        if (message.isEmpty())
            return;
        String prefix = plugin.getConfig().getString("prefix");
        Bukkit.broadcastMessage(color(prefix + message));
    }

    public static void fillList(final List<ItemStack> items, final ItemStack[] array) {
        for (final ItemStack item : array) {
            if (item != null && item.getType() != Material.AIR) {
                items.add(item);
            }
        }
    }
}
