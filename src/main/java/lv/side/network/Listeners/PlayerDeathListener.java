package lv.side.network.Listeners;

import lv.side.network.Managers.PlayerManager;
import lv.side.network.Managers.VillagerManager;
import lv.side.network.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler(ignoreCancelled=true)
    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) {
            return;
        }
        Villager v = (Villager)e.getEntity();
        if (!VillagerManager.isVillager(v)) {
            return;
        }
        if(!(e.getDrops().isEmpty())){
            e.getDrops().clear();
        }
        if(e.getEntity().getKiller() != null) {
            UUID kUUID = VillagerManager.getVillagerPlayer(v);
            PlayerManager.addKilled(kUUID);
            VillagerManager.dropInventory(v, v.getLocation());
            VillagerManager.removeVillager(v);
            if (!PlayerLeaveListener.name.containsKey(kUUID)) {
                return;
            }
            Bukkit.broadcastMessage(Messages.get("death-message").replace("%killer%", e.getEntity().getKiller().getName()).replace("%player%", Bukkit.getOfflinePlayer(kUUID).getName()));
            PlayerLeaveListener.name.remove(kUUID);

        } else{
            UUID kUUID = VillagerManager.getVillagerPlayer(v);
            PlayerManager.addKilled(kUUID);
            VillagerManager.dropInventory(v, v.getLocation());
            VillagerManager.removeVillager(v);
            if (!PlayerLeaveListener.name.containsKey(kUUID)) {
                return;
            }
            Bukkit.broadcastMessage(Messages.get("death-message-null").replace("%player%", Bukkit.getOfflinePlayer(kUUID).getName()));
            PlayerLeaveListener.name.remove(kUUID);
        }
    }

}
