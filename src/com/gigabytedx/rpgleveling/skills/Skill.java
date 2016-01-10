package com.gigabytedx.rpgleveling.skills;

import java.util.List;

public class Skill {
	private String name;
	private List<String> experienceGainedThrough;
	private List<Level> levels;

	public Skill(String name, List<String> experienceGainedThrough, List<Level> levels) {
		super();
		this.name = name;
		this.experienceGainedThrough = experienceGainedThrough;
		this.levels = levels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExperienceGainedThrough() {
		return experienceGainedThrough;
	}

	public void setExperienceGainedThrough(List<String> experienceGainedThrough) {
		this.experienceGainedThrough = experienceGainedThrough;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

}
