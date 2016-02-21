package com.gigabytedx.rpgleveling.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.cooldowns.Cooldown;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.item.PotionItem;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.shop.Shop;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

class RandItem {
	
	private double weight;
	private String name;

	RandItem(double weight, String name){
		this.weight = weight;
		this.name = name;
	}

	public double getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}
	
	
    
}

public class Interact implements Listener {

	Main plugin;

	public Interact(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void itemInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		if(event.getClickedBlock().getType().equals(Material.CHEST)){
			chestClickEvent(event);
		}
		try{
		if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Material.STAINED_GLASS_PANE)){
			event.setCancelled(true);
			return;
		}
		}catch(NullPointerException e){
			return;
		}
		try {
			Item itemUsed = Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
			if (plugin.itemClassValue.getBaseClassValues(event.getPlayer()).get(itemUsed.getBaseClass()) < Main.itemMap
					.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()
						+ ChatColor.DARK_RED
						+ " is not being used effectively because you do not have enough " + Main.itemMap
								.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getBaseClass()
						+ " items equipped!");
				event.getPlayer().updateInventory();
				return;
			}

		} catch (NullPointerException e) {
		}

		try {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if (Main.itemMap
						.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()) instanceof PotionItem) {
					try {
						for (Cooldown coolDown : plugin.playerCooldowns.getCooldownMap().get(event.getPlayer())) {
							if ((ChatColor.BLUE + coolDown.getItem().getName())
									.equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()))
								return;
						}
					} catch (NullPointerException e) {
					}
					if (event.getPlayer().getItemInHand().getAmount() == 1) {
						Item item = Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
						plugin.playerCooldowns.addCooldown(event.getPlayer(),
								new Cooldown(((PotionItem) item).getCooldown(), item,
										event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer(), plugin));
					}
				}
		} catch (NullPointerException e) {
		}

	}

	private void chestClickEvent(PlayerInteractEvent event) {
		event.setCancelled(true);
		
		Inventory finalInv = Bukkit.createInventory(event.getPlayer(), 27,ChatColor.GOLD + "TREASURE");
		Chest chest = ((Chest) event.getClickedBlock().getState());
		Inventory chestInv = chest.getBlockInventory();
		Set<String> materialNames = null;
		try{
		materialNames = plugin.chestRollConfig.getConfigurationSection("loot").getKeys(false);
		}catch(NullPointerException e){
		event.getPlayer().sendMessage(ChatColor.RED + "Error with the 'chestRollFile' config for plugin: " + plugin.getName() + ". Could not find the 'loot' path.");
		plugin.logError("Error with the 'chestRollFile' config for plugin: " + plugin.getName() + ". Could not find the 'loot' path.");
		return;
		}
		List<RandItem> lootPool = new ArrayList<>();
		
		for(ItemStack itemInChest: chestInv.getContents()){
			if(itemInChest == null){continue;}
			if(materialNames.contains(itemInChest.getType().toString())){
				Set<String> lootNames = plugin.chestRollConfig.getConfigurationSection("loot." + itemInChest.getType().toString()).getKeys(false);
				for(String lootName: lootNames){
					lootPool.add(new RandItem(plugin.chestRollConfig.getDouble("loot." + itemInChest.getType().toString() + "." + lootName + ".weight" ), lootName));
				}
				RandItem itemSelected= chooseOnWeight(lootPool);
				String itemName = itemSelected.getName();
				Item item = Main.itemMap.get(ChatColor.BLUE + itemName);
				if(item == null){
					event.getPlayer().sendMessage(ChatColor.RED + "Item with the name '" + itemName + "' could not be found in the list of items. Ensure the spelling is correct");
					plugin.logError("Item with the name '" + itemName + "' could not be found in the list of items. Ensure the spelling is correct");
				}else
				finalInv.addItem(AddItemToInventory.getItemStack(item, plugin));
			}
		}
		event.getPlayer().openInventory(finalInv);
	}
	
	private RandItem chooseOnWeight(List<RandItem> randItems) {
		// Compute the total weight of all items together
		double totalWeight = 0.0d;
		for (RandItem i : randItems)
		{
			if(i.getWeight() <= 0){
				plugin.logError("Item with the name '" + i.getName() + "' has an invalid weight. Please check to see if this is an error and fix this in the chestRollFile config");
				continue;
			}
		    totalWeight += i.getWeight();
		}
		// Now choose a random item
		int randomIndex = -1;
		double random = Math.random() * totalWeight;
		for (int i = 0; i < randItems.size(); ++i)
		{
		    random -= randItems.get(i).getWeight();
		    if (random <= 0.0d)
		    {
		        randomIndex = i;
		        break;
		    }
		}
		RandItem myRandomItem = randItems.get(randomIndex);
		return myRandomItem;
    }
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		event.setDamage(0);
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			try {
				Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());

				if (plugin.itemClassValue.getBaseClassValues(damager).get(itemUsed.getBaseClass()) >= Main.itemMap
						.get(damager.getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {

					for (Modifier buff : itemUsed.getBuffs()) {
						buff.applyBuff(damager, event.getEntity());
					}
					applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage(), damager);
				} else {
					(damager)
							.sendMessage(damager.getItemInHand().getItemMeta().getDisplayName() + ChatColor.DARK_RED
									+ " is not being used effectively because you do not have enough " + Main.itemMap
											.get(damager.getItemInHand().getItemMeta().getDisplayName()).getBaseClass()
									+ " items equipped");
					event.setDamage(1);
					return;
				}
			} catch (NullPointerException e) {
			}

		} else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player damager = (Player) arrow.getShooter();
				if (damager.getItemInHand().getType().equals(Material.BOW)) {
					try {
						Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());
						for (Modifier buff : itemUsed.getBuffs()) {
							buff.applyBuff(damager, (LivingEntity) event.getEntity());
						}
						if (arrow.isCritical()) {
							applyDamage((LivingEntity) event.getEntity(), Math.floor(itemUsed.getDamage() * 1.5),
									(Player) arrow.getShooter());
							damager.sendMessage(ChatColor.GOLD + "1.5x Damage for critical hit!");
						} else {
							applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage(),
									(Player) arrow.getShooter());
						}
					} catch (NullPointerException e) {
					}
				}
			}
		} else if (event.getDamager() instanceof LivingEntity) {
			Set<ProtectedRegion> protectedRegions = WorldGuardPlugin.inst()
					.getRegionManager(event.getDamager().getLocation().getWorld())
					.getApplicableRegions(event.getDamager().getLocation()).getRegions();
			if (protectedRegions.size() > 0) {
				ProtectedRegion[] prArray = new ProtectedRegion[protectedRegions.size()];
				prArray = protectedRegions.toArray(prArray);
				String name = event.getDamager().getCustomName();
				name = name.substring(name.indexOf(ChatColor.GOLD + ""), name.length());
				name = name.substring(2);

				if (event.getEntity() instanceof Player) {
					applyDamage((Player) event.getEntity(),
							plugin.MobSpawningData
									.getConfigurationSection("Regions." + prArray[0].getId() + ".SpawnableMobs." + name)
									.getInt("Attack"),
							null);
				} else {
					((LivingEntity) event.getEntity()).damage(plugin.MobSpawningData
							.getConfigurationSection("Regions." + prArray[0].getId() + ".SpawnableMobs." + name)
							.getInt("Attack"));
				}
			}
		}

	}

	private void applyDamage(LivingEntity entity, double damage, Player player) {
		double armorProtectionValue = 0;

		for (ItemStack itemStack : entity.getEquipment().getArmorContents()) {
			try {
				try {
					if (plugin.itemClassValue.getBaseClassValues((Player) entity)
							.get(Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
									.getBaseClass()) >= Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
											.getClassLevelRequirement()) {
						armorProtectionValue += Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
								.getProtection();
					} else {
						((Player) entity).sendMessage(itemStack.getItemMeta().getDisplayName() + ChatColor.DARK_RED
								+ "Is not being used effectively because you do not have enough "
								+ Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getBaseClass()
								+ " items equipped");
					}
				} catch (ClassCastException e) {
					armorProtectionValue += Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getProtection();
				}
			} catch (NullPointerException e) {

			}
		}
		armorProtectionValue = armorProtectionValue * 4;
		double dmgToSubtract = armorProtectionValue / 100 * damage;
		damage -= dmgToSubtract;
		try {
			for (PotionEffect effect : player.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
					damage = damage + ((1 + effect.getAmplifier()) * 1.3);
				}
			}
		} catch (NullPointerException e) {

		}
		entity.damage(damage);

	}

	/////////////////DISABLED///////////////////
	public void onInteract(PlayerInteractAtEntityEvent event) {
//		if (!plugin.getConfig().getString("world name").equals(event.getPlayer().getLocation().getWorld().getName()))
//			return;
//		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
//			return;
//		}
		if (event.getRightClicked() instanceof Villager) {
			Villager getPersonClicked = (Villager) event.getRightClicked();
			if (plugin.locations.getLocationNames().contains(getPersonClicked.getName().toLowerCase())) {
				Shop.openShop(plugin, event.getPlayer(), getPersonClicked.getName());
			}

		}
	}
}
