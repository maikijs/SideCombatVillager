package lv.side.network.Managers;

import lv.side.network.CombatVillager;
import lv.side.network.Utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class VillagerManager {
    public static HashMap<Villager, UUID> villager = new HashMap();
    public static HashMap<Villager, List<ItemStack>> contents = new HashMap();
    public static HashMap<Villager, Float> xps = new HashMap();

    public static void addCombatVillager(UUID playerUUID, String playerName, Location spawnLoc, List<ItemStack> invItems, Float xp, Double health) {
        Villager v = (Villager) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        v.setCustomName(Messages.get("logger-name").replace("%player%", playerName));
        v.setCustomNameVisible(true);
        if(health > 19) {
            v.setHealth(20);
        } else {
            v.setHealth(health);
        }
        Villager.Profession prof = Villager.Profession.NONE;
        v.setProfession(prof);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, CombatVillager.getInstance().getConfig().getInt("logger.time-till-disappear")*20, 1000));
        villager.put(v, playerUUID);
        contents.put(v, invItems);
        xps.put(v, xp);
    }

    public static void dropInventory(Villager v, Location loc) {
        if (xps.containsKey(v)) {
            loc.getWorld().spawn(loc, ExperienceOrb.class).setExperience(xps.get(v).intValue());
        }
        if (contents.containsKey(v)) {
            (contents.get(v)).forEach(i -> loc.getWorld().dropItemNaturally(loc, i));
        }
    }

    public static void removeVillager(Villager v) {
        contents.remove(v);
        villager.remove(v);
        xps.remove(v);
    }

    public static boolean hasVillagerByUUID(UUID u) {
        return villager.containsValue(u);
    }

    public static Villager getVillagerByUUID(UUID u) {
        if (!villager.containsValue(u)) {
            return null;
        }
        for (Map.Entry p : villager.entrySet()) {
            if (!p.getValue().equals(u)) continue;
            return (Villager) p.getKey();
        }
        return null;
    }

    public static boolean isVillager(Villager v) {
        return villager.containsKey(v);
    }

    public static UUID getVillagerPlayer(Villager v) {
        if (isVillager(v)) {
            return villager.get(v);
        }
        return null;
    }

    public static Float getVillagerXP(UUID v) {
        if (xps.containsKey(getVillagerByUUID(v))) {
            return xps.get(getVillagerByUUID(v));
        }
        return (float) 0;
    }

    public List<ItemStack> getVillagerInventory(Villager v) {
        if (contents.containsKey(v)) {
            return contents.get(v);
        }
        return new ArrayList();
    }

    public static HashMap<Villager, UUID> getVillagers() {
        return villager;
    }

    public HashMap<Villager, List<ItemStack>> getVillagersWithContents() {
        return contents;
    }
}
