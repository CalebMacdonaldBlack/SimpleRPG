package com.gigabytedx.rpgleveling.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.Mobs.MobData;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EntitySpawn implements Listener {

	private Main plugin;

	public EntitySpawn(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public boolean onEntitySpawn(CreatureSpawnEvent event) {
		Set<ProtectedRegion> protectedRegions = WorldGuardPlugin.inst().getRegionManager(event.getLocation().getWorld())
				.getApplicableRegions(event.getLocation()).getRegions();
		try {
			if (plugin.regions.hasRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId())) {
				List<MobData> mobDataRandomPool = new ArrayList<>();
				if (event.getSpawnReason().equals(SpawnReason.NATURAL))
					for (MobData mobData : plugin.regions
							.getRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId()).getSpawnableMobs()) {

						if (mobData.isNaturalAllowed()) {
							int countOfMobsInRegion = 0;
							for (Entity entity : event.getLocation().getWorld().getEntities()) {
								if (entity instanceof Monster || entity instanceof Animals) {
									System.out.println("IS INSTANCE OF");
									if (WorldGuardPlugin.inst().getRegionManager(entity.getLocation().getWorld())
											.getApplicableRegions(entity.getLocation()).getRegions()
											.contains((ProtectedRegion) protectedRegions.toArray()[0])) {
										System.out.println("IS IN REGION");
										countOfMobsInRegion++;
									}

								}
							}
							if (countOfMobsInRegion < plugin.regions
									.getRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId())
									.getMaxAllowedToSpawnInRegion())
								for (int x = 0; x < mobData.getSpawnRate(); x++) {
									mobDataRandomPool.add(mobData);
								}
							else
								System.out.println("Fuckk off we're full");
						} else {
							event.setCancelled(true);
							return false;
						}
					}
				else {
					for (MobData mobData : plugin.regions
							.getRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId()).getSpawnableMobs()) {
						if (mobData.getType().equals(event.getEntityType().toString())) {
							for (int x = 0; x < mobData.getSpawnRate(); x++) {
								mobDataRandomPool.add(mobData);
							}
						}

					}
				}
				Random random = new Random();
				int randomIndex = random.nextInt(mobDataRandomPool.size());

				EntityType type = EntityType.valueOf(mobDataRandomPool.get(randomIndex).getType());
				LivingEntity mob = event.getEntity();
				if (!mob.getType().equals(type)) {
					event.setCancelled(true);
					return false;
				}
				mob.setCustomName(
						ChatColor.RED + "LVL: " + ChatColor.GREEN + mobDataRandomPool.get(randomIndex).getLevel() + " "
								+ ChatColor.GOLD + mobDataRandomPool.get(randomIndex).getMobName());
				mob.setMaxHealth(mobDataRandomPool.get(randomIndex).getHealth());
				mob.setHealth(mobDataRandomPool.get(randomIndex).getHealth());
				for (String itemType : mobDataRandomPool.get(randomIndex).getItems()) {
					if (itemType.toLowerCase().contains("helmet")) {
						mob.getEquipment().setHelmet(new ItemStack(Material.valueOf(itemType)));
					} else if (itemType.toLowerCase().contains("chestplate")) {
						mob.getEquipment().setChestplate(new ItemStack(Material.valueOf(itemType)));
					} else if (itemType.toLowerCase().contains("leggings")) {
						mob.getEquipment().setLeggings(new ItemStack(Material.valueOf(itemType)));
					} else if (itemType.toLowerCase().contains("boots")) {
						mob.getEquipment().setBoots(new ItemStack(Material.valueOf(itemType)));
					} else
						mob.getEquipment().setItemInHand(new ItemStack(Material.valueOf(itemType)));
				}
				System.out.println("Spawn successful");
				return false;
			} else {
				event.setCancelled(true);
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
			if (!event.getSpawnReason().equals(SpawnReason.NATURAL)
					&& !event.getSpawnReason().equals(SpawnReason.JOCKEY)
					&& !event.getSpawnReason().equals(SpawnReason.MOUNT)) {

				System.out.println("Spawn successful 1: " + event.getSpawnReason().toString());
				return false;
			} else
				event.setCancelled(true);
			return false;
		}
	}
}
