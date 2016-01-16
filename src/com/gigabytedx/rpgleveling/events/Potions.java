package com.gigabytedx.rpgleveling.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gigabytedx.rpgleveling.Main;

public class Potions implements Listener{

	private Main plugin;

	public Potions(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSplash(PotionSplashEvent event){
		if (!plugin.getConfig().getString("world name").equals(event.getEntity().getLocation().getWorld().getName()))
			return;
		for(PotionEffect effect: event.getPotion().getEffects()){
			if(effect.getType().equals(PotionEffectType.HARM)){
				for(LivingEntity entity : event.getAffectedEntities()){
					
					entity.damage(6 * (effect.getAmplifier() + 1));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void throwPot(ProjectileLaunchEvent event){
		if (!plugin.getConfig().getString("world name").equals(event.getEntity().getLocation().getWorld().getName()))
			return;
		if(event.getEntityType().equals(EntityType.SPLASH_POTION)){
			event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2));
		}
	}
}
