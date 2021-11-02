package lv.side.network.Managers;

import lv.side.network.Utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerManager {
    static FileConfiguration event;
    static File efile;

    public static void setup(Plugin p) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        if (!(efile = new File(p.getDataFolder(), "players.yml")).exists()) {
            try {
                efile.createNewFile();
            }
            catch (IOException e) {
                ColorUtils.sendConsoleMessage("&4[SVC] Could not create players.yml!");
            }
        }
        event = YamlConfiguration.loadConfiguration(efile);
    }

    public static FileConfiguration getData() {
        return event;
    }

    public static void saveData() {
        try {
            event.save(efile);
        }
        catch (IOException e) {
            ColorUtils.sendConsoleMessage("&4[SVC] Could not save players.yml!");
        }
    }

    public static void removeKilled(UUID player) {
        getData().set("combat.killed." + player.toString(), null);
        saveData();
    }

    public static void addKilled(UUID player) {
        getData().set("combat.killed." + player.toString(), "true");
        saveData();
    }

    public static boolean wasKilled(UUID player) {
        return getData().contains("combat.killed." + player.toString());
    }
}

