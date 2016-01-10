package com.gigabytedx.rpgleveling.modifiers.modifier;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class Slowness extends Modifier{
	
	public Slowness(Main plugin, String name, double rate, Long duration, Long interval, double intensity, String type,
			String target, String trigger, String modifierType) {
		super(plugin, name, rate, duration, interval, intensity, type, target, trigger, modifierType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyBuff(Player player, Entity entity) {
		if(getTarget().equals("player")){
			if(getTrigger().equals("hold"))
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, (int) getIntensity()));
			else
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (getDuration()/1000), (int) getIntensity()));
		}else{
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (getDuration()/1000*20), (int) getIntensity()),true);
				System.out.println(((LivingEntity) entity).getActivePotionEffects().toString());
			}
		}
		
	}
}
