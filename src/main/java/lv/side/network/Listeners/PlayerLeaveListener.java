package lv.side.network.Listeners;

import lv.side.network.CombatVillager;
import lv.side.network.Managers.VillagerTask;
import lv.side.network.Managers.VillagerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static lv.side.network.Utils.ColorUtils.fillList;

public class PlayerLeaveListener implements Listener {

    public static HashMap<UUID, String> name = new HashMap();
    private final CombatVillager plugin;
    private static List<String> worlds;
    private boolean worldcheck;
    private int delay;

    public PlayerLeaveListener(CombatVillager plugin) {
        this.plugin = plugin;
        worlds = plugin.getConfig().getStringList("logger.worlds.list");
        worldcheck = plugin.getConfig().getBoolean("logger.worlds.enabled");
        delay = plugin.getConfig().getInt("logger.time-till-disappear");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (e.getPlayer().hasPermission("SideAdmin.logout")) {
            return;
        }
        if (e.getPlayer().isDead()) {
            return;
        }
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (worldcheck) {
            if (!(worlds.contains(e.getPlayer().getWorld().getName()))) {
                return;
            }
        }
        UUID u = e.getPlayer().getUniqueId();
        final List<ItemStack> items = new ArrayList<>();
        fillList(items, e.getPlayer().getInventory().getContents());
        fillList(items, e.getPlayer().getInventory().getArmorContents());
        VillagerManager.addCombatVillager(u, e.getPlayer().getDisplayName(), e.getPlayer().getLocation(), items, e.getPlayer().getExp());
        name.put(u, e.getPlayer().getName());
        Bukkit.getScheduler().runTaskLater(CombatVillager.getInstance(), new VillagerTask(u), delay * 20);
    }


}
