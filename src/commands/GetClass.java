package commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;

public class GetClass implements CommandExecutor {

	Main plugin;

	public GetClass(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				List<String> itemNames = (List<String>) plugin.getConfig().getList("skills." + args[0].toLowerCase() + ".items");
				for (String itemName : itemNames) {
					Item item = Main.itemMap.get(ChatColor.BLUE + itemName);
					
					try{
						item.getName();
						player.getInventory().addItem(AddItemToInventory.getItemStack(item, plugin));
					} catch(NullPointerException e) {
						plugin.logError("Could not find " + itemName + " in the list of items. Please check the name is correct");
					}
				}

			} else {
				player.sendMessage(ChatColor.RED + "Syntax: /getClass <class name>");
			}
		}
		return false;
	}

}
