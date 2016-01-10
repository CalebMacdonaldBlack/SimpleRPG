package com.gigabytedx.rpgleveling.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class PotionItem extends Item {

	private boolean extended;
	private int Potency;
	private int data;
	private int qty;
	private int cooldown;
	private boolean splash;
	private String potionType;

	public PotionItem(Item item, boolean extended, int potency, int data, boolean splash, int qty, int cooldown,
			String potionType) {
		super(item.getName(), item.getLore(), item.getCost(), item.getType(), false, item.getBuffs(), item.getDebuffs(),
				item.getLocationName(), 0, 0);
		this.extended = extended;
		this.Potency = potency;
		this.data = data;
		this.splash = splash;
		this.qty = qty;
		this.cooldown = cooldown;
		this.potionType = potionType;
	}

	public int getQty() {
		return qty;
	}

	public int getCooldown() {
		return cooldown;
	}

	public boolean isExtended() {
		return extended;
	}

	public int getPotency() {
		return Potency;
	}

	public int getData() {
		return data;
	}

	public boolean isSplash() {
		return splash;
	}

	public String getpPtionType() {
		return potionType;
	}

	public ItemStack getPotion() {
		Potion potion = new Potion(PotionType.valueOf(potionType));
		potion.setSplash(splash);
		try {
			potion.setLevel(Potency);
			potion.setHasExtendedDuration(extended);
		} catch (IllegalArgumentException e) {

		}
		ItemStack itemStack = potion.toItemStack(qty);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + getName());
		itemStack.setItemMeta(meta);
		return itemStack;

	}

}
