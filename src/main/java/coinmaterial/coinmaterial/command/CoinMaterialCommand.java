package coinmaterial.coinmaterial.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import com.google.common.collect.Lists;

import coinmaterial.coinmaterial.CoinMaterial;

/**
 * Implements handler for CoinMaterial plugin config reload
 * Usage:        /coinmaterial reload
 * Requirements: CoinMaterial.reload permission
 */
public class CoinMaterialCommand extends AbstractCommand {

	public CoinMaterialCommand() {
		// Simple constructor with super support
		super("coinmaterial");
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - handles plugin reload command
		if (args.length == 0) {
			sender.sendMessage("Reload plugin config: /coinmaterial reload");
			return;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("CoinMaterial.reload")) {
				sender.sendMessage(ChatColor.RED + "You don't have the permission required to reload plugin config!" + ChatColor.RESET);
				return;
			}

			CoinMaterial.getInstance().reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "CoinMaterial config reloaded" + ChatColor.RESET);
			return;
		}

		sender.sendMessage(ChatColor.RED + "Unknown command: " + args[0] + ChatColor.RESET);
	}

	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command
		if (args.length == 1)
			return Lists.newArrayList("reload");
		return Lists.newArrayList();
	}
}
