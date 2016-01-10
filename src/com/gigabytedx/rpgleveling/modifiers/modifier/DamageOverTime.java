package com.gigabytedx.rpgleveling.modifiers.modifier;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class DamageOverTime extends Modifier {

	public DamageOverTime(Main plugin, String name, double rate, Long duration, Long interval, double intensity,
			String type, String target, String trigger, String modifierType) {
		super(plugin, name, rate, duration, interval, intensity, type, target, trigger, modifierType);
	}

	@Override
	public void applyBuff(Player damager, Entity entity) {
		if (!(getCurrentBuffs().contains(entity))) {
			getCurrentBuffs().add(entity);
			final Long timeUntilBuffIsGone = System.currentTimeMillis() + getDuration() - 1000;
			new RunSchedular(getCurrentBuffs(), timeUntilBuffIsGone, getInterval(), damager, entity, getIntensity(),
					getTarget());
		} else {
		}
	}

	class RunSchedular {
		private int task;
		private Entity entity;

		public RunSchedular(final List<Entity> currentBuffs, final Long timeUntilBuffIsGone, final Long interval,
				final Player damager, final Entity entity, final double intensity, final String target) {
			this.entity = entity;
			this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable() {

				@Override
				public void run() {
					if (System.currentTimeMillis() > timeUntilBuffIsGone) {
						stopSchedular();
					}
					if (target.equals("player")) {
						damager.damage(intensity);
					} else {
						if (entity instanceof Damageable) {
							Damageable d = (Damageable) entity;
							d.damage(intensity);
						}
					}

				}
			}, interval, interval);
		}

		public void stopSchedular() {
			Bukkit.getScheduler().cancelTask(task);
			getCurrentBuffs().remove(entity);
		}
	}
}
