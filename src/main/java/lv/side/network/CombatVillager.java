package lv.side.network;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import lv.side.network.Commands.LogoutCMD;
import lv.side.network.Commands.MainCMD;
import lv.side.network.Listeners.PlayerDeathListener;
import lv.side.network.Listeners.PlayerJoinListener;
import lv.side.network.Listeners.PlayerLeaveListener;
import lv.side.network.Listeners.VillagerListener;
import lv.side.network.Managers.PlayerManager;
import lv.side.network.Managers.VillagerManager;
import lv.side.network.Utils.ColorUtils;
import lv.side.network.Utils.Messages;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CombatVillager extends JavaPlugin {

    private static CombatVillager instance;
    private LuckPerms luckPermsAPI;

    public void onEnable() {
        instance = this;
        ColorUtils.sendConsoleMessage("&a[SVC] Starting to load.");
        this.saveDefaultConfig();
        PlayerManager.setup(this);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null)
            this.luckPermsAPI = provider.getProvider();
        new Messages();
        if (getConfig().isConfigurationSection("messages"))
            Messages.load(getConfig().getConfigurationSection("messages"));
        registerCommands();
        registerListeners();
        ColorUtils.sendConsoleMessage("&a[SVC] Plugin enabled.");
    }

    public void onDisable() {
        for (UUID uuid : VillagerManager.villager.values()) {
            VillagerManager.getVillagerByUUID(uuid).remove();
        }
        instance = null;
        ColorUtils.sendConsoleMessage("&a[SVC] Plugin disabled.");
    }


    private void registerCommands() {
        getCommand("scv").setExecutor(new MainCMD(this));
        getCommand("logout").setExecutor(new LogoutCMD(this));
        ColorUtils.sendConsoleMessage("&a[SVC] Successfully loaded command(s).");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerListener(), this);
        ColorUtils.sendConsoleMessage("&a[SVC] Successfully loaded plugin listeners.");
    }

    public static CombatVillager getInstance() {
        return instance;
    }

    public LuckPerms getLuckPerms() {
        return this.luckPermsAPI;
    }
}
