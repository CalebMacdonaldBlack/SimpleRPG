package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;

public class SetWalk implements CommandExecutor {
	
	Main plugin;
	public SetWalk(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			player.setWalkSpeed(Float.parseFloat(args[0]));
		}
		return false;
	}

}
