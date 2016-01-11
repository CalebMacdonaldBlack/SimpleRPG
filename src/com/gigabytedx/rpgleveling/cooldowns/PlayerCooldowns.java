package com.gigabytedx.rpgleveling.cooldowns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class PlayerCooldowns {
	Map<Player, List<Cooldown>> cooldownMap = new HashMap<>();

	public Map<Player, List<Cooldown>> getCooldownMap() {
		return cooldownMap;
	}

	public boolean addCooldown(Player player, Cooldown cooldown) {
		try {
			List<Cooldown> cooldownList = cooldownMap.get(player);
			for (Cooldown cd : cooldownList) {
				if (cd.getItem().getName().equals(cooldown.getItem().getName())) {
					return false;
				}
			}
			cooldownList.add(cooldown);
			cooldownMap.put(player, cooldownList);
			return true;
		} catch (NullPointerException e) {
			List<Cooldown> cooldownList = new ArrayList<>();
			cooldownList.add(cooldown);
			cooldownMap.put(player, cooldownList);
			return true;
		}
	}
	
	public void removeCooldown(Player player, Cooldown cooldown){
		cooldownMap.get(player).remove(cooldown);
	}
}
