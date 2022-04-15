package coinmaterial.coinmaterial.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import doublewhaleapi.dwapi.DataModels.GuildModel;
import doublewhaleapi.dwapi.DataModels.PlayerModel;

/**
 * MoneyCommand class. Handles money command.
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public class MoneyCommand extends AbstractCommand {
	/**
	 * Simple constructor with super support.
	 */
	public MoneyCommand() {
		super("money");
	}

	/**
	 * Execute command for player issuer.
	 * 
	 * @param sender Player issuer
	 */
	public void executeForPlayer(CommandSender sender) {
		// Get player wallet
		sender.sendMessage(colorize(ChatColor.BOLD, getLocal("msgMoney", "infoMoney"))
				.replace("{amount}", plugin.core.coinStorage.gocPlayerWallet(sender.getName()).getBalance().toString())
				.replace("{currencySymbol}", colorize(ChatColor.GOLD, getSettings("currency", "currencySymbol"))));
	}

	/**
	 * Execute command for guild.
	 * 
	 * @param sender Player issuer
	 */
	public void executeForGuild(CommandSender sender) {
		GuildModel gm = plugin.core.guildStorage.getGuildByPlayer(sender.getName());

		if (gm != null) {
			// If guild exists
			PlayerModel player = gm.getPlayerByName(sender.getName());

			if (player.testMembership()) {
				// If issuer is guild member

				sender.sendMessage(
						colorize(ChatColor.BOLD, getLocal("guildedMoney", "infoMoney"))
								.replace("{amount}",
										plugin.core.coinStorage.getWalletByName(gm.getCreatorName(), true).getBalance()
												.toString())
								.replace("{currencySymbol}",
										colorize(ChatColor.GOLD, getSettings("currency", "currencySymbol"))));

			} else
				sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "playerNotAdded")));
		} else
			sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "playerNotInGuild")));
	}

	/**
	 * Command execute method. Implements money command.
	 * 
	 * @param sender Command issuer
	 * @param label  Command alias
	 * @param args   Command arguments
	 */
	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - messages balance info to sender
		if (!(sender instanceof Player)) {
			sender.sendMessage(getLocal("general", "issuerNotPLayer"));
			return;
		}

		if (args.length == 1) {
			// Player passed an argument

			if (plugin.guildedInstalled) {
				// CoinMaterial integration

				if (args[0].equalsIgnoreCase("guild")) {
					// Argument is a guild string

					// Executing for guild
					executeForGuild(sender);
				} else {
					// Too many arguments for money command
					sender.sendMessage(colorize(ChatColor.RED, getLocal("general", "tooManyArguments")));
				}
			} else {
				// Guilded not installed
				if (args[0].equalsIgnoreCase("guild")) {
					// Yet player tried to access guild's wallet
					sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "incorrectArgument")));
				} else {
					// Too many arguments for money command
					sender.sendMessage(colorize(ChatColor.RED, getLocal("general", "tooManyArguments")));
				}
			}
		} else {
			// Player issued a simple money command
			executeForPlayer(sender);
		}
	}

	/**
	 * Complete method for command arguments.
	 * 
	 * @return ArrayList of available arguments.
	 */
	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command

		// Guilded plugin integration support
		if ((args.length == 1) && plugin.guildedInstalled)
			return Lists.newArrayList("guild");

		return Lists.newArrayList();
	}
}
