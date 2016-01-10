package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;

public class GetXP implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;

	public GetXP(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
		}
		return false;
	}

}
