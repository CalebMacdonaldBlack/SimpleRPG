package com.gigabytedx.rpgleveling.modifiers.modifier;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class Speed extends Modifier {

	public Speed(Main plugin, String name, double rate, int duration, Long interval, double intensity, String type,
			String target, String trigger, String modifierType) {
		super(plugin, name, rate, duration, interval, intensity, type, target, trigger, modifierType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void applyBuff(Player player, Entity entity) {
		if (getTarget().equals("player")) {
			if (getTrigger().equals("hold") || getTrigger().equals("have"))
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (int) getIntensity()));
			else {
				System.out.println("Trying to add pot effect");
				player.sendMessage("ADDING POT EFECT");
				System.out.println("DATA: " + (getDuration() / 1000) + " " + getIntensity());
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3000, 3));
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.SPEED, getDuration() * 20, (int) getIntensity()));
			}
		} else {
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).addPotionEffect(
						new PotionEffect(PotionEffectType.SPEED, getDuration() * 20, (int) getIntensity()), true);
				System.out.println(((LivingEntity) entity).getActivePotionEffects().toString());
			}
		}

	}

	public void removeBuff(Player player) {
		if (getTarget().equals("player")) {
			player.removePotionEffect(PotionEffectType.SPEED);
		}
	}
}
