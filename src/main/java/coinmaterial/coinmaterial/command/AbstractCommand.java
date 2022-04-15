package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.CoinMaterial;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract command class for plugin
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
	/**
	 * CoinMaterial plugin instance.
	 */
	protected CoinMaterial plugin;

	/**
	 * Constructor, sets executor of command argument passed.
	 * 
	 * @param command Command to bind current *Command class to
	 */
	public AbstractCommand(String command) {
		PluginCommand pluginCommand = CoinMaterial.getInstance().getCommand(command);
		if (pluginCommand != null) {
			pluginCommand.setExecutor(this);
		} else {
			CoinMaterial.getInstance().getLogger()
					.severe("Command executor for " + this.getClass().toString() + " could not be set!");
		}
		plugin = CoinMaterial.getInstance();
	}

	/**
	 * Abstract "virtual" execute method, implements command execution.
	 * 
	 * @param sender Command issuer
	 * @param label  Command name alias
	 * @param args   String array or arguments
	 */
	public abstract void execute(CommandSender sender, String label, String[] args);

	/**
	 * Fixed override of onCommand handler. Calls execute method.
	 * 
	 * @return true
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// onCommand listener override - executes command
		execute(sender, label, args);
		return true;
	}

	/**
	 * Completes command based upon given arguments. Must override to implement.
	 * 
	 * @param sender Command issuer
	 * @param args   String array of command arguments
	 * @return null
	 */
	public List<String> complete(CommandSender sender, String args[]) {
		return null;
	}

	/**
	 * Completes commands based upon given arguments on tab hit. Calls class
	 * complete() method.
	 * 
	 * @return String List after filter() on complete() results
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return filter(complete(sender, args), args);
	}

	/**
	 * Filters and completes last command argument passed using list available.
	 * 
	 * @param list List of String of available commands
	 * @param args String array of command arguments
	 * @return resulting List of Strings with completed argument or null if there
	 *         are none available commands
	 */
	private List<String> filter(List<String> list, String[] args) {
		if (list == null)
			return null;

		String last = args[args.length - 1];
		List<String> result = new ArrayList<>();

		for (String arg : list) {
			if (arg.toLowerCase().startsWith(last.toLowerCase()))
				result.add(arg);
		}

		return result;
	}

	/**
	 * Parses a string to check if it contains only positive integer or zero.
	 * 
	 * @param str String to parse
	 * @return true if string is parsed, false otherwise
	 */
	public boolean isNumber(String str) {
		if (str != null && !str.isEmpty()) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i)))
					return false;
			}
			return true;
		}
		return true;
	}

	/**
	 * Reads config.yml file for strings under namespace.
	 * 
	 * @param ns  Namespace to read under
	 * @param key String to read
	 * @return String from config.yml
	 */
	private String readConfig(String ns, String key) {
		return CoinMaterial.getInstance().getConfig().getString(ns + "." + key);
	}

	/**
	 * Reads config.yml file for strings under localization.namespace.
	 * 
	 * @param commandNS Command to get l10n string to
	 * @param localize  String to read
	 * @return String localized command string from config.yml
	 */
	public String getLocal(String commandNS, String localize) {
		return readConfig("localization", commandNS + "." + localize);
	}

	/**
	 * Reads config.yml file for strings under settings.namespace.
	 * 
	 * @param settingsType Settings type under namespace settings.
	 * @param settingName  String to read
	 * @return String setting from config.yml
	 */
	public String getSettings(String settingsType, String settingName) {
		return readConfig("settings", settingsType + "." + settingName);
	}

	/**
	 * Applies a color of ChatColor enum to string.
	 * 
	 * @param color ChatColor color
	 * @param msg   String to apply color to
	 * @return string with applied color
	 */
	public String colorize(ChatColor color, String msg) {
		return color + msg + ChatColor.RESET;
	}
}
