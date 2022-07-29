package lv.side.network.Listeners;

import lv.side.network.CombatVillager;
import lv.side.network.Managers.PlayerManager;
import lv.side.network.Managers.VillagerManager;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {


    private CombatVillager plugin;
    private boolean xpsave;
    private String permission;

    public PlayerJoinListener(CombatVillager plugin) {
        this.plugin = plugin;
        xpsave =  plugin.getConfig().getBoolean("logger.dont-lose-xp.enabled");
        permission = plugin.getConfig().getString("logger.dont-lose-xp.permission");
    }

    @EventHandler(ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("SideAdmin.logout")) {
            return;
        }
        if (PlayerManager.wasKilled(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(null);
            e.getPlayer().setHealth(0.0);
            User user = CombatVillager.getInstance().getLuckPerms().getUserManager().getUser(e.getPlayer().getUniqueId());
            if(xpsave && hasPermission(user, permission)){
                e.getPlayer().setExp(VillagerManager.getVillagerXP(e.getPlayer().getUniqueId()));
            } else {
                e.getPlayer().setExp(0);
            }
            PlayerManager.removeKilled(e.getPlayer().getUniqueId());
        }
        if (!VillagerManager.hasVillagerByUUID(e.getPlayer().getUniqueId())) {
            return;
        }
        Villager v = VillagerManager.getVillagerByUUID(e.getPlayer().getUniqueId());
        if (v == null) {
            return;
        }
        e.getPlayer().teleport(v.getLocation());
        e.getPlayer().setHealth(v.getHealth());
        VillagerManager.removeVillager(v);
        v.remove();
        PlayerLeaveListener.name.remove(e.getPlayer().getUniqueId());
    }


    public boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
