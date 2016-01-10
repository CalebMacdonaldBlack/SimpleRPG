package commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.skills.Level;
import com.gigabytedx.rpgleveling.skills.Skill;

public class PrintSkills implements CommandExecutor {
	Main plugin;
	
	public PrintSkills(Main plugin) {
		super();
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			List<Skill> skills = plugin.skills.getSkills();
			
			for(Skill skill: skills){
				player.sendMessage(ChatColor.GREEN + "Skill Name: " + ChatColor.GOLD + skill.getName());
				for(Level level: skill.getLevels()){
					player.sendMessage(ChatColor.GREEN + "Level Name: " + ChatColor.GOLD + level.getLevelName());
					player.sendMessage(ChatColor.GREEN + "Level number: " + ChatColor.GOLD + level.getLvlNumber());
					for(String itemName: level.getNamesOfItemsUnlocked()){
						player.sendMessage(ChatColor.GREEN + "Item Unlock: " + ChatColor.GOLD + itemName);
					}
				}
			}
			player.sendMessage("");
			
		}
		return false;
	}

}
