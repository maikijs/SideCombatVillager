package lv.side.network.Listeners;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.event.PlayerPunishEvent;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.object.UntagReason;
import lv.side.network.CombatVillager;
import lv.side.network.Managers.VillagerManager;
import lv.side.network.Managers.VillagerTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static lv.side.network.Utils.ColorUtils.fillList;

public class PlayerLeaveListener implements Listener {

    public static HashMap<UUID, String> name = new HashMap();
    private final CombatVillager plugin;
    private static List<String> worlds;
    private final boolean worldcheck;
    private final int delay;
    private final boolean logger;

    public PlayerLeaveListener(CombatVillager plugin) {
        this.plugin = plugin;
        worlds = plugin.getConfig().getStringList("logger.worlds.list");
        worldcheck = plugin.getConfig().getBoolean("logger.worlds.enabled");
        delay = plugin.getConfig().getInt("logger.time-till-disappear");
        logger = plugin.getConfig().getBoolean("logger.while-in-combat");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPunish(PlayerPunishEvent event) {

        if (event.getPunishReason() == UntagReason.QUIT || event.getPunishReason() == UntagReason.KICK) {

            if (!logger && isInCombat(event.getPlayer()))
                return;

            if (event.getPlayer().hasPermission("SideAdmin.logout"))
                return;

            if ((worldcheck && !worlds.contains(event.getPlayer().getWorld().getName())))
                return;

            event.setCancelled(true);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (e.getPlayer().hasPermission("SideAdmin.logout"))
            return;

        if (!logger && isInCombat(e.getPlayer()))
            return;

        if (e.getPlayer().isDead())
            return;

        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;

        if (worldcheck && !(worlds.contains(e.getPlayer().getWorld().getName())))
            return;

        UUID u = e.getPlayer().getUniqueId();
        final List<ItemStack> items = new ArrayList<>();
        fillList(items, e.getPlayer().getInventory().getContents());
//        fillList(items, e.getPlayer().getInventory().getArmorContents());
        VillagerManager.addCombatVillager(u, e.getPlayer().getDisplayName(), e.getPlayer().getLocation(), items, e.getPlayer().getExp(), e.getPlayer().getHealth());
        name.put(u, e.getPlayer().getName());
        Bukkit.getScheduler().runTaskLater(CombatVillager.getInstance(), new VillagerTask(u), delay * 20L);
    }

    public boolean isInCombat(Player player) {
        ICombatLogX plugin = getAPI();
        ICombatManager combatManager = plugin.getCombatManager();
        return combatManager.isInCombat(player);
    }

    public static ICombatLogX getAPI() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin plugin = pluginManager.getPlugin("CombatLogX");
        return (ICombatLogX) plugin;
    }
}
