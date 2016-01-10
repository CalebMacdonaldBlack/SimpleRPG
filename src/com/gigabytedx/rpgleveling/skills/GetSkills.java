package com.gigabytedx.rpgleveling.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gigabytedx.rpgleveling.Main;

public class GetSkills {
	private List<Skill> skills;
	private List<Level> levels = new ArrayList<>();
	Main main;

	public GetSkills(Main main) {
		this.main = main;
		skills = new ArrayList<>();
		getSkillsFromConfig(main);
	}

	@SuppressWarnings("unchecked")
	private void getSkillsFromConfig(Main main) {
		// this gets all the skill configuration section key names and adds them
		// to a list
		Set<String> skillConfigSectionNames = main.getConfig().getConfigurationSection("skills").getKeys(false);

		// iterate through skill names
		for (String skillName : skillConfigSectionNames) {
			List<String> experienceGainedThrough = (List<String>) main.getConfig().getConfigurationSection("skills")
					.getConfigurationSection(skillName).getList("experienceGainedThough");
			Set<String> levelNames = main.getConfig().getConfigurationSection("skills")
					.getConfigurationSection(skillName).getConfigurationSection("levels").getKeys(false);

			// iterate through level names
			for (String levelName : levelNames) {
				List<String> namesOfItemsUnlocked = (List<String>) main.getConfig().getConfigurationSection("skills")
						.getConfigurationSection(skillName).getConfigurationSection("levels")
						.getConfigurationSection(levelName).getList("itemUnlock");
				List<String> buffs = (List<String>) main.getConfig().getConfigurationSection("skills")
						.getConfigurationSection(skillName).getConfigurationSection("levels")
						.getConfigurationSection(levelName).getList("buffs");
				List<String> debuffs = (List<String>) main.getConfig().getConfigurationSection("skills")
						.getConfigurationSection(skillName).getConfigurationSection("levels")
						.getConfigurationSection(levelName).getList("debuffs");
				int lvlNumber = main.getConfig().getConfigurationSection("skills").getConfigurationSection(skillName)
						.getConfigurationSection("levels").getConfigurationSection(levelName).getInt("levelNumber");

				// add new level to list
				levels.add(new Level(namesOfItemsUnlocked, buffs, debuffs, lvlNumber, skillName, levelName));
			}

			// add new skill to list
			skills.add(new Skill(skillName, experienceGainedThrough, levels));
		}

	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Level> getLevels() {
		return levels;
	}
	
	
}
