package com.gigabytedx.rpgleveling.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Potions implements Listener{

	@EventHandler
	public void onSplash(PotionSplashEvent event){
		System.out.println("RUNNNN");
		for(PotionEffect effect: event.getPotion().getEffects()){
			if(effect.getType().equals(PotionEffectType.HARM)){
				System.out.println("SHULD");
				for(LivingEntity entity : event.getAffectedEntities()){
					System.out.println("LOOPS:" + effect.getAmplifier());
					
					entity.damage(6 * (effect.getAmplifier() + 1));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void throwPot(ProjectileLaunchEvent event){
		if(event.getEntityType().equals(EntityType.SPLASH_POTION)){
			event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2));
		}
	}
}