package lv.side.network.Listeners;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.event.PlayerPunishEvent;
import com.SirBlobman.combatlogx.api.utility.ICombatManager;
import lv.side.network.CombatVillager;
import lv.side.network.Managers.VillagerTask;
import lv.side.network.Managers.VillagerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

    @EventHandler(ignoreCancelled=true)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (e.getPlayer().hasPermission("SideAdmin.logout")) {
            return;
        }
        if(!logger && isInCombat(e.getPlayer())){
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
//        fillList(items, e.getPlayer().getInventory().getArmorContents());
        VillagerManager.addCombatVillager(u, e.getPlayer().getDisplayName(), e.getPlayer().getLocation(), items, e.getPlayer().getExp(), e.getPlayer().getHealth());
        name.put(u, e.getPlayer().getName());
        Bukkit.getScheduler().runTaskLater(CombatVillager.getInstance(), new VillagerTask(u), delay * 20);
    }

    public boolean isInCombat(Player player) {
        ICombatLogX plugin = (ICombatLogX) Bukkit.getPluginManager().getPlugin("CombatLogX");
        ICombatManager combatManager = plugin.getCombatManager();
        return combatManager.isInCombat(player);
    }

}
