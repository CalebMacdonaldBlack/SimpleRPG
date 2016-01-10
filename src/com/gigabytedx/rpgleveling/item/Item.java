package com.gigabytedx.rpgleveling.item;

import java.util.List;

import org.bukkit.Material;

import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class Item {
	private String name;
	private String lore;
	private double cost;
	private Material type;
	private boolean enchanted;
	private List<Modifier> buffs;
	private List<Modifier> debuffs;
	private String locationName;
	private int damage;
	private int protection;
	
	public Item(String name, String lore, double cost, Material type, boolean enchanted, List<Modifier> buffs,
			List<Modifier> debuffs, String locationName, int damage, int protection) {
		super();
		this.name = name;
		this.lore = lore;
		this.cost = cost;
		this.type = type;
		this.enchanted = enchanted;
		this.buffs = buffs;
		this.debuffs = debuffs;
		this.locationName = locationName;
		this.damage = damage;
		this.protection = protection;
	}

	public String getName() {
		return name;
	}

	public String getLore() {
		return lore;
	}

	public double getCost() {
		return cost;
	}

	public Material getType() {
		return type;
	}

	public boolean isEnchanted() {
		return enchanted;
	}

	public List<Modifier> getBuffs() {
		return buffs;
	}

	public List<Modifier> getDebuffs() {
		return debuffs;
	}

	public String getLocationName() {
		return locationName;
	}

	public int getDamage() {
		return damage;
	}

	public int getProtection() {
		return protection;
	}
	
	
}
