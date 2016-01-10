package com.gigabytedx.rpgleveling.modifiers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;

public class Modifier {
	double rate;
	Long duration;
	Long interval;
	double intensity;
	String type;
	String target;
	String trigger;
	String name;
	Main plugin;
	String modifierType;
	List<Entity> currentBuffs = new ArrayList<>();
	

	public List<Entity> getCurrentBuffs() {
		return currentBuffs;
	}

	public double getRate() {
		return rate;
	}

	public Long getDuration() {
		return duration;
	}

	public Long getInterval() {
		return interval;
	}

	public double getIntensity() {
		return intensity;
	}

	public String getType() {
		return type;
	}

	public String getTarget() {
		return target;
	}

	public String getTrigger() {
		return trigger;
	}

	public String getName() {
		return name;
	}

	public Main getPlugin() {
		return plugin;
	}

	public String getModifierType() {
		return modifierType;
	}

	public Modifier(Main plugin, String name, double rate, Long duration, Long interval, double intensity, String type,
			String target, String trigger, String modifierType) {
		super();
		this.plugin = plugin;
		this.name = name;
		this.rate = rate;
		this.duration = duration;
		this.interval = interval;
		this.intensity = intensity;
		this.type = type;
		this.target = target;
		this.trigger = trigger;
		this.modifierType = modifierType;
	}

	public void applyBuff(Player damager, Entity entity) {
	}

}
