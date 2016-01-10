package com.gigabytedx.rpgleveling.skills;

import java.util.List;

public class Level {
	private List<String> namesOfItemsUnlocked;
	private List<String> buffs;
	private List<String> debuffs;
	private int lvlNumber;
	private String skillName;
	private String levelName;

	public Level(List<String> namesOfItemsUnlocked, List<String> buffs, List<String> debuffs, int lvlNumber,
			String skillName, String levelName) {
		super();
		this.namesOfItemsUnlocked = namesOfItemsUnlocked;
		this.buffs = buffs;
		this.debuffs = debuffs;
		this.lvlNumber = lvlNumber;
		this.skillName = skillName;
		this.levelName = levelName;
	}

	public List<String> getNamesOfItemsUnlocked() {
		return namesOfItemsUnlocked;
	}

	public void setNamesOfItemsUnlocked(List<String> namesOfItemsUnlocked) {
		this.namesOfItemsUnlocked = namesOfItemsUnlocked;
	}

	public List<String> getBuffs() {
		return buffs;
	}

	public void setBuffs(List<String> buffs) {
		this.buffs = buffs;
	}

	public List<String> getDebuffs() {
		return debuffs;
	}

	public void setDebuffs(List<String> debuffs) {
		this.debuffs = debuffs;
	}

	public int getLvlNumber() {
		return lvlNumber;
	}

	public void setLvlNumber(int lvlNumber) {
		this.lvlNumber = lvlNumber;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

}
