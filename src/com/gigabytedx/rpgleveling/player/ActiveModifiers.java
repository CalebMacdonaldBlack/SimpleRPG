package com.gigabytedx.rpgleveling.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

public class ActiveModifiers {
	private List<PotionEffectType> activePotionEffects = new ArrayList<>();
	private List<ActiveModifier> ActiveModifier = new ArrayList<>();
	
	public void addEffect(PotionEffectType activePotionEffect){
		if(!activePotionEffects.contains(activePotionEffect))
			activePotionEffects.add(activePotionEffect);
	}
	
	public boolean removeEffect(PotionEffectType activePotionEffect){
		if(activePotionEffects.contains(activePotionEffect)){
			activePotionEffects.remove(activePotionEffect);
			return true;
		}
		return false;
	}

	public List<PotionEffectType> getActivePotionEffects() {
		return activePotionEffects;
	}

	public List<ActiveModifier> getActiveModifier() {
		return ActiveModifier;
	}
	
}
