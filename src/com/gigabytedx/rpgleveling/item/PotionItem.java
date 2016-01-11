package com.gigabytedx.rpgleveling.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionItem extends Item {

	private int duration;
	private int potency;
	private String data;
	private int qty;
	private int cooldown;
	private boolean splash;
	private String potionType;

	public PotionItem(Item item, int duration, int potency, String data, boolean splash, int qty, int cooldown,
			String potionType) {
		super(item.getName(), item.getLore(), item.getCost(), item.getType(), false, item.getBuffs(), item.getDebuffs(),
				item.getLocationName(), 0, 0, item.getBaseClass(), item.getClassLevelRequirement());
		this.duration = duration;
		this.potency = potency;
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

	public int isExtended() {
		return duration;
	}

	public int getPotency() {
		return potency;
	}

	public String getData() {
		return data;
	}

	public boolean isSplash() {
		return splash;
	}

	public String getpPtionType() {
		return potionType;
	}

	public ItemStack getPotion() {
		Potion potion;
		ItemStack itemStack = null;
		
		potion = new Potion(PotionType.valueOf(data));
		potion.setSplash(splash);
		try {
			potion.setLevel(potency);
		} catch (IllegalArgumentException e1) {

		}
		itemStack = potion.toItemStack(qty);
		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
		meta.setMainEffect(getEmptyPotionEffectType());
		meta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(potionType),duration * 20, potency), true);
		meta.setDisplayName(ChatColor.BLUE + getName());
		itemStack.setItemMeta(meta);
		

		return itemStack;

	}
	
	public PotionEffectType getEmptyPotionEffectType(){
		return new PotionEffectType(1) {
			
			@Override
			public boolean isInstant() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public double getDurationModifier() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
	}

}
