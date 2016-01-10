package com.gigabytedx.rpgleveling.Mobs;

import java.util.List;

import com.gigabytedx.rpgleveling.item.Drop;

public class MobData {
	private int health, attack, level;
	private List<String> items;
	private List<Drop> drops;
	private String type;
	private int spawnRate;
	private String mobName;
	private boolean naturalAllowed;
	private int xp;

	public MobData(String mobName, int spawnRate, String type, int health, int attack, int level, List<Drop> drops,
			List<String> items, boolean naturalAllowed, int xp) {
		this.health = health;
		this.attack = attack;
		this.level = level;
		this.drops = drops;
		this.items = items;
		this.type = type;
		this.spawnRate = spawnRate;
		this.mobName = mobName;
		this.naturalAllowed = naturalAllowed;
		this.xp = xp;
	}

	public int getXp() {
		return xp;
	}

	public boolean isNaturalAllowed() {
		return naturalAllowed;
	}

	public String getMobName() {
		return mobName;
	}

	public int getSpawnRate() {
		return spawnRate;
	}

	public String getType() {
		return type;
	}

	public int getHealth() {
		return health;
	}

	public int getAttack() {
		return attack;
	}

	public int getLevel() {
		return level;
	}

	public List<String> getItems() {
		return items;
	}

	public List<Drop> getDrops() {
		return drops;
	}

}
