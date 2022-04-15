package coinmaterial.coinmaterial.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * CoinMaterial command class. Handles reload config command.
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public class CoinMaterialCommand extends AbstractCommand {
	/**
	 * Simple constructor with super support.
	 */
	public CoinMaterialCommand() {
		super("coinmaterial");
	}

	/**
	 * Command execute method. Implements reload config command.
	 * 
	 * @param sender Command issuer
	 * @param label  Command alias
	 * @param args   Command arguments
	 */
	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Reload plugin config: /coinmaterial reload");
			return;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("CoinMaterial.reload")) {
				sender.sendMessage(colorize(ChatColor.RED, getLocal("configReload", "noPermission")));
				return;
			}
			// Actually reload plugin and config
			plugin.reloadConfig();

			sender.sendMessage(colorize(ChatColor.GREEN, getLocal("configReload", "configReloadMsg")));
			plugin.logger.info(getLocal("configReload", "configReloadMsg"));

			return;
		}

		sender.sendMessage(colorize(ChatColor.RED, "Unknown command: " + args[0]));
	}

	/**
	 * Complete method for command arguments.
	 * 
	 * @return ArrayList of available arguments.
	 */
	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		if (sender.hasPermission("CoinMaterial.reload") && (args.length == 1))
			return Lists.newArrayList("reload");
		return Lists.newArrayList();
	}
}
