package lv.side.network.Listeners;

import lv.side.network.CombatVillager;
import lv.side.network.Managers.VillagerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class VillagerListener implements Listener {

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Villager)) {
            return;
        }
        if (!VillagerManager.isVillager((Villager) e.getTarget())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerSpawn(CreatureSpawnEvent e) {
        if (!(e.getEntity() instanceof Villager)) {
            return;
        }
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            return;
        }
        e.setCancelled(false);
    }

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent e) {
        if(!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        for (UUID asd : VillagerManager.getVillagers().values()) {
            if (e.getRightClicked().equals(VillagerManager.getVillagerByUUID(asd))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Villager) {
            final Entity m = e.getEntity();
            CombatVillager.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(CombatVillager.getInstance(), new Runnable() {
                public void run() {
                    m.setVelocity(new Vector(0, 0, 0));
                }
            }, 1L);
        }
    }

}
