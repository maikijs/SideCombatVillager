package lv.side.network.Managers;


import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;

import java.util.UUID;

public class VillagerTask implements Runnable {
    private final UUID uuid;

    public VillagerTask(UUID uuid) {
        this.uuid = uuid;
    }

    public void run() {
        if (VillagerManager.hasVillagerByUUID(uuid)) {
            Villager v = VillagerManager.getVillagerByUUID(uuid);
            if (v != null) {
                v.remove();
            }
            VillagerManager.removeVillager(v);
        }
    }
}

