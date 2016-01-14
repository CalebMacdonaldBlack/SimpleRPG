package com.gigabytedx.rpgleveling.events;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.cooldowns.Cooldown;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.item.PotionItem;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.modifiers.modifier.HealthIncrease;

public class InventoryInteract implements Listener {
	Main plugin;

	public InventoryInteract(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		System.out.println("fired");
		if (event.getView().getTopInventory().getTitle().contains("vender")) {
			System.out.println("vender found");
			if (event.getClick().equals(ClickType.LEFT)
					&& event.getRawSlot() < event.getView().getTopInventory().getSize()) {
				System.out.println("corrent timem");
				shopOpenEvent(event);
				return;
			}
		}
		try {
			if (event.getCursor().getType().equals(Material.STAINED_GLASS_PANE)) {
				event.setCancelled(true);
			} else if (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
				event.setCancelled(true);
			}
		} catch (NullPointerException e) {

		}
		// System.out.println("SLOT IS THIS: " + event.getRawSlot());
		// System.out.println("OTHER SLOT IS THIS: " + event.getSlot());
		// System.out.println("TOP SIZE: " +
		// event.getView().getTopInventory().getSize());
		try {
			if (event.getSlot() < 8) {
				if (!(plugin.playerCooldowns.getCooldownMap().get((Player) event.getWhoClicked()).isEmpty())) {
					event.setCancelled(true);
					event.getWhoClicked().sendMessage(
							ChatColor.RED + "Please wait for your item cooldowns to finish before altering the hotbar");
				}
			} else if (event.isShiftClick()) {
				if (!(plugin.playerCooldowns.getCooldownMap().get((Player) event.getWhoClicked()).isEmpty())) {
					event.setCancelled(true);
					event.getWhoClicked().sendMessage(
							ChatColor.RED + "Please wait for your item cooldowns to finish before altering the hotbar");
				}
			}
		} catch (NullPointerException e) {

		}
		try {
			if (event.isShiftClick()) {
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
					Item item = Main.itemMap.get(event.getCurrentItem().getItemMeta().getDisplayName());
					for (Modifier buff : item.getBuffs()) {
						if (buff instanceof HealthIncrease) {
							event.getWhoClicked().setMaxHealth(20);
						}
					}
				}
			} else if (event.getCursor().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
				Item item = Main.itemMap.get(event.getCursor().getItemMeta().getDisplayName());
				if (event.getSlot() > 8) {
					for (Modifier buff : item.getBuffs()) {
						if (buff instanceof HealthIncrease) {
							event.getWhoClicked().setMaxHealth(20);
						}
					}
				}
			}
		} catch (Exception e1) {

		}

		try {
			if (event.getCursor().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")
					&& event.getRawSlot() < event.getView().getTopInventory().getSize()) {
				event.setCancelled(true);
			}
		} catch (NullPointerException e) {
		}
		try {
			if (event.isRightClick()) {
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
					event.setCancelled(true);
				}
			} else if (event.isShiftClick()) {
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")
						&& (!(event.getInventory().getType() == InventoryType.CRAFTING)
								&& event.getRawSlot() > event.getView().getTopInventory().getSize())) {
					event.setCancelled(true);
				}
			}
		} catch (NullPointerException e) {
			try {
				if (event.getAction().equals(InventoryAction.PLACE_ONE))
					if (event.getCursor().getItemMeta().getDisplayName().contains(ChatColor.BLUE + ""))
						event.setCancelled(true);
			} catch (Exception e1) {

			}
		}
	}

	private void shopOpenEvent(InventoryClickEvent event) {
		System.out.println("NAME " + event.getCurrentItem().getItemMeta().getDisplayName());
		System.out.println("LIST: " + Main.itemMap.keySet().toString());
		Item item = Main.itemMap.get(event.getCurrentItem().getItemMeta().getDisplayName());
		int currentGold = 0;
		PlayerInventory playerInv = event.getWhoClicked().getInventory();

		for (int itemSlot = 0; itemSlot < 40; itemSlot++) {
			try {
				System.out.println("SLOT: " + itemSlot + "MAXSIZE" + playerInv.getMaxStackSize());
				if (playerInv.getItem(itemSlot).getType().equals(Material.GOLD_NUGGET)) {
					currentGold += Main.goldNuggetWorth * playerInv.getItem(itemSlot).getAmount();
				} else if (playerInv.getItem(itemSlot).getType().equals(Material.GOLD_INGOT)) {
					currentGold += Main.goldBarWorth * playerInv.getItem(itemSlot).getAmount();
				}
			} catch (NullPointerException e) {

			}
		}

		if (item.getCost() <= currentGold) {
			playerInv.addItem(AddItemToInventory.getItemStack(item, plugin));
			removeGoldFromInventory(playerInv, item.getCost());
		} else {
			event.getWhoClicked().sendMessage(ChatColor.RED + "Sorry but you need " + (item.getCost() - currentGold)
					+ " more gold to purchase this item");

		}
		event.setCancelled(true);
	}

	private void removeGoldFromInventory(PlayerInventory playerInv, double cost) {
		int amountLeftToRemove = (int) cost;
		for (int itemSlot = 0; itemSlot < 40; itemSlot++) {
			try {
				if (playerInv.getItem(itemSlot).getType().equals(Material.GOLD_NUGGET)) {
					amountLeftToRemove = amountLeftToRemove
							- playerInv.getItem(itemSlot).getAmount() * Main.goldNuggetWorth;
					playerInv.setItem(itemSlot, null);
				} else if (playerInv.getItem(itemSlot).getType().equals(Material.GOLD_INGOT)) {
					amountLeftToRemove = amountLeftToRemove
							- playerInv.getItem(itemSlot).getAmount() * Main.goldBarWorth;
					playerInv.setItem(itemSlot, null);
				}
			} catch (NullPointerException e) {

			}
			if (amountLeftToRemove == 0) {
				break;
			} else if (amountLeftToRemove < 0) {
				int change = Math.abs(amountLeftToRemove);
				if (change > Main.goldBarWorth) {
					ItemStack bars = new ItemStack(Material.GOLD_INGOT, change / Main.goldBarWorth);
					playerInv.addItem(bars);
				}
				if (change > 0) {
					if (change % Main.goldBarWorth != 0)
						playerInv.addItem(new ItemStack(Material.GOLD_NUGGET, change % Main.goldBarWorth));
					break;
				}
			}
		}

	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		try {
			if (event.getNewItems().get(0).getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
				if (Main.itemMap.get(event.getNewItems().get(0).getItemMeta().getDisplayName()) instanceof PotionItem) {
					PotionItem item = (PotionItem) Main.itemMap
							.get(event.getNewItems().get(0).getItemMeta().getDisplayName());
					if (event.getNewItems().size() != item.getQty())
						event.setCancelled(true);
				}
			}
		} catch (NullPointerException e) {
			event.setCancelled(true);
		}
		try {
			for (Cooldown coolDown : plugin.playerCooldowns.getCooldownMap().get(event.getWhoClicked())) {
				if ((ChatColor.BLUE + coolDown.getItem().getName())
						.equals(event.getNewItems().get(0).getItemMeta().getDisplayName()))
					event.setCancelled(true);
			}
		} catch (NullPointerException e) {

		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
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
						System.out.println("no case");
					}
					if (event.getPlayer().getItemInHand().getAmount()==1) {
						PotionItem item = (PotionItem) Main.itemMap
								.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
						plugin.playerCooldowns.addCooldown(event.getPlayer(), new Cooldown(item.getCooldown(), item,
								event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer(), plugin));
					}
				}
		} catch (NullPointerException e) {
			System.out.println("null here bruh");
		}
	}

	@EventHandler
	public void dropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onHoldItemInHand(PlayerItemHeldEvent event) {

		if (event.getNewSlot() > 2 && event.getNewSlot() < 9) {
			System.out.println("canlesded mate");
			event.setCancelled(true);
		}

		checkArmor(event.getPlayer());
		for (int itemSlot = 0; itemSlot < 8; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				System.out.println(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());

				Map<String, Integer> baseClassValue = plugin.itemClassValue.getBaseClassValues(event.getPlayer());
				System.out.println(baseClassValue.toString());
				String baseClassForItem = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getBaseClass();
				int itemBaseClassLevelRequirement = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getClassLevelRequirement();

				if (baseClassValue.get(baseClassForItem) >= itemBaseClassLevelRequirement) {
					System.out.println("made it this far");
					if (itemUsed != null)
						for (Modifier buff : itemUsed.getBuffs()) {
							System.out.println("APPLYING BUFF: " + buff.getName());
							if (buff.getTrigger().equals("have"))
								buff.applyBuff(event.getPlayer(), null);
							if (buff.getTrigger().equals("hold") && itemSlot == event.getNewSlot()) {
								buff.applyBuff(event.getPlayer(), null);
							}
						}

				} else {
					(event.getPlayer())
							.sendMessage(
									event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName()
											+ ChatColor.DARK_RED
											+ " is not being used effectively because you do not have enough "
											+ Main.itemMap.get(event.getPlayer().getInventory().getItem(itemSlot)
													.getItemMeta().getDisplayName()).getBaseClass()
									+ " items equipped");
					event.getPlayer().setMaxHealth(20);
				}

				if (itemUsed != null) {
					for (Modifier buff : itemUsed.getDebuffs()) {
						if (buff.getTrigger().equals("have"))
							buff.applyBuff(event.getPlayer(), null);
						if (buff.getTrigger().equals("hold") && itemSlot == event.getNewSlot()) {
							buff.applyBuff(event.getPlayer(), null);
						}
					}
				}
			} catch (NullPointerException e) {
			}
		}

	}

	@EventHandler
	public void onInventory(InventoryCloseEvent event) {
		checkArmor((Player) event.getPlayer());
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				if (itemUsed != null)
					for (Modifier modifier : itemUsed.getBuffs()) {
						if (modifier.getTrigger().equals("have"))
							modifier.applyBuff((Player) event.getPlayer(), null);
						else if (modifier.getTrigger().equals("hold")
								&& itemSlot == event.getPlayer().getInventory().getHeldItemSlot()) {
							modifier.applyBuff((Player) event.getPlayer(), null);
						}
					}
			} catch (NullPointerException e) {

			}
		}

		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				if (itemUsed != null)
					for (Modifier modifier : itemUsed.getDebuffs()) {
						if (modifier.getTrigger().equals("have"))
							modifier.applyBuff((Player) event.getPlayer(), null);
						else if (modifier.getTrigger().equals("hold")
								&& itemSlot == event.getPlayer().getInventory().getHeldItemSlot()) {
							modifier.applyBuff((Player) event.getPlayer(), null);
						}
					}
			} catch (NullPointerException e) {

			}
		}
		boolean hasHealthIncreaseItem = false;
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				for (Modifier modifier : Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getBuffs()) {
					if (modifier instanceof HealthIncrease) {
						hasHealthIncreaseItem = true;
					}
				}
			} catch (NullPointerException e) {

			}
		}
		if (!hasHealthIncreaseItem)
			event.getPlayer().setMaxHealth(20);
	}

	private void checkArmor(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
			if (effect.getDuration() > 200000) {
				player.removePotionEffect(effect.getType());
			}
		}
		for (ItemStack itemStack : player.getEquipment().getArmorContents()) {
			try {
				for (Modifier modifier : Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getBuffs()) {
					modifier.applyBuff((Player) player, null);
				}
				for (Modifier modifier : Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getDebuffs()) {
					modifier.applyBuff((Player) player, null);
				}
			} catch (NullPointerException e) {

			}
		}
	}

}
