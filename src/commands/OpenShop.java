package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.shop.Shop;

public class OpenShop implements CommandExecutor {
	Main plugin;

	public OpenShop(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 1 && plugin.locations.getLocationNames().contains(args[0].toLowerCase())) {
				Player player = (Player) sender;
				Shop.openShop(plugin, player, args[0]);
			} else {
				sender.sendMessage(ChatColor.RED
						+ "Syntax: /openshop <locationName>");
				sender.sendMessage(ChatColor.GOLD + "List of Locations:");
				for (String locationName : plugin.locations.getLocationNames()) {
					sender.sendMessage(ChatColor.DARK_GREEN + locationName);
				}
			}
		}else{
			Shop.openShop(plugin, Bukkit.getPlayer(args[0]), args[1]);
		}
		return false;
	}
	
	
}