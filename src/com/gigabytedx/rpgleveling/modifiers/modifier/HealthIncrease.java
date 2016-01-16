package com.gigabytedx.rpgleveling.modifiers.modifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class HealthIncrease extends Modifier {

	public HealthIncrease(Main plugin, String name, double rate, int duration, Long interval, double intensity,
			String type, String target, String trigger, String modifierType) {
		super(plugin, name, rate, duration, interval, intensity, type, target, trigger, modifierType);
	}

	@Override
	public void applyBuff(Player player, Entity entity) {
		if(getTarget().equals("player")){
			if(getTrigger().equals("hold") || getTrigger().equals("have")){
			player.setMaxHealth(20 + getIntensity());
			}else
				addHealthForTime(player, entity);
			}else{
			if (entity instanceof LivingEntity) {
				((Damageable) entity).setMaxHealth(20 + getIntensity());
			}
		}
		
	}

	private void addHealthForTime(final Player player, final Entity entity) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if(getTarget().equals("player")){
					((LivingEntity) entity).setMaxHealth(20);
				}else{
					if (entity instanceof LivingEntity) {
						((LivingEntity) entity).setMaxHealth(20);
					}
				}
			}
		}, getDuration() * 20);
		
	}
	
	public void removeBuff(Player player){
		if(getTarget().equals("player")){
			player.setMaxHealth(20);
		}
	}
}
