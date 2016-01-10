package com.gigabytedx.rpgleveling.item;

public class Drop {
	private String type;
	private int qty;
	private int spawnRate;
	private String name;
	private boolean custom;

	public Drop(String type, int qty, int spawnRate, String name, boolean custom) {
		this.type = type;
		this.qty = qty;
		this.spawnRate = spawnRate;
		this.name = name;
		this.custom = custom;
	}
	
	public boolean isCustom() {
		return custom;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getQty() {
		return qty;
	}

	public int getSpawnRate() {
		return spawnRate;
	}

}
