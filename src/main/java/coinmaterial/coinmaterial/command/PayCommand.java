package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import doublewhaleapi.dwapi.DataModels.CoinModel;
import doublewhaleapi.dwapi.DataModels.GuildModel;
import doublewhaleapi.dwapi.DataModels.GuildModel.RoleGuild;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

/**
 * PayCommand class. Handles pay command.
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public class PayCommand extends AbstractCommand {
	/**
	 * Simple constructor with super support.
	 */
	public PayCommand() {
		super("pay");
	}

	/**
	 * Command execute method. Implements pay command.
	 * 
	 * @param sender Command issuer
	 * @param label  Command alias
	 * @param args   Command arguments
	 */
	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements pay command
		if (!(sender instanceof Player)) {
			sender.sendMessage(getLocal("general", "issuerNotPLayer"));
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(
					ChatColor.BOLD + getLocal("general", "promptInputAmount") + getLocal("msgPay", "promptAdd"));
			return;
		}

		Player player = (Player) sender;
		CoinModel playerWallet = plugin.core.coinStorage.getWalletByName(player.getName(), true);

		if (playerWallet == null) {
			// Player had not opened a wallet
			sender.sendMessage("Вы не открыли кошелька, воспользуйтесь командой /money!");
			return;
		}

		if (args[0].equalsIgnoreCase("guild")) {
			// Guilded plugin integration support

			if (plugin.guildedInstalled) {
				// Transfer to guild wallet

				if (args.length == 2) {
					// Player provided a payment amount

					if (isNumber(args[1])) {
						// Payment amount is positive integer

						Double transferAmount = Double.parseDouble(args[1]);

						if (transferAmount > 0.0) {
							// Payment is not 0 coins

							if (transferAmount <= playerWallet.getCoins()) {
								// Player has enough money to make payment

								if (args.length == 3) {
									// Player specified nickname of player (preferably) in a guild

									GuildModel gm = plugin.core.guildStorage.getGuildByPlayerRole(player.getName(),
											RoleGuild.Member);
									if (gm != null) {
										// Player is in a guild
										CoinModel receiverWallet = plugin.core.coinStorage
												.getWalletByName(gm.getCreatorName(), true);

										String msg = getLocal("guildedPay", "senderMessage");
										msg = msg.replace("{receiver}", ChatColor.BOLD + gm.getGuildName());
										msg = msg.replace("{amount}", transferAmount.toString());
										msg = msg.replace("{currencySymbol}", ChatColor.GOLD
												+ getSettings("currency", "currencySymbol") + ChatColor.RESET);
										player.sendMessage(msg);
										player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);

										playerWallet.setCoins(playerWallet.getCoins() - transferAmount);
										receiverWallet.setCoins(receiverWallet.getCoins() + transferAmount);
									} else
										sender.sendMessage(ChatColor.RED + getLocal("guilded", "playerNotInGuild")
												+ ChatColor.RESET);
								} else
									sender.sendMessage(ChatColor.BOLD + getLocal("guildedPay", "nullPlayerName")
											+ ChatColor.RESET);
							} else
								sender.sendMessage(
										ChatColor.RED + getLocal("general", "notEnoughMoney") + ChatColor.RESET);
						} else
							sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + getLocal("msgPay", "nullTransfer")
									+ ChatColor.RESET);
					} else
						sender.sendMessage(ChatColor.BOLD + getLocal("general", "incorrectAmount") + ChatColor.RESET);
				} else
					sender.sendMessage(ChatColor.BOLD + getLocal("general", "promptInputAmount")
							+ getLocal("msgWallet", "promptAdd") + ChatColor.RESET);
			} else
				sender.sendMessage(ChatColor.RED + getLocal("general", "guildedNotIncluded") + ChatColor.RESET);

		} else if (isNumber(args[0])) {
			// Payment value provided is a number
			Double transferAmount = Double.parseDouble(args[0]);

			if (transferAmount > 0.0) {
				// Payment value is greater than zero

				if (transferAmount <= playerWallet.getCoins()) {
					// Player has no less than a payment value coins in wallet

					if (args.length == 2) {
						// Player provided payment receiver nickname

						if (!args[1].equals(player.getName())) {
							// Payment Sender is not Receiver

							CoinModel receiverWallet = plugin.core.coinStorage.getWalletByName(args[1], false);

							if (receiverWallet != null) {
								// Payment Receiver is a Player and has a wallet

								Player receiver = Bukkit.getPlayerExact(args[1]);

								// Messages sender and receiver about payment, play villager_trade sound to both
								String msg = getLocal("msgPay", "senderMessage");
								msg = msg.replace("{receiver}",
										ChatColor.BOLD + receiverWallet.getWalletName() + ChatColor.RESET);
								msg = msg.replace("{amount}", transferAmount.toString());
								msg = msg.replace("{currencySymbol}",
										ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
								player.sendMessage(msg);
								player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);

								if (receiver != null) {
									// Payment receiver might be offline, if so - don`t message him
									msg = getLocal("msgPay", "receiverMessage");
									msg = msg.replace("{sender}",
											ChatColor.BOLD + playerWallet.getWalletName() + ChatColor.RESET);
									msg = msg.replace("{amount}", transferAmount.toString());
									msg = msg.replace("{currencySymbol}", ChatColor.GOLD
											+ getSettings("currency", "currencySymbol") + ChatColor.RESET);
									receiver.sendMessage(msg);
									receiver.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
								}
								playerWallet.setCoins(playerWallet.getCoins() - transferAmount);
								receiverWallet.setCoins(receiverWallet.getCoins() + transferAmount);

							} else
								sender.sendMessage(
										ChatColor.RED + getLocal("msgPay", "incorrectPlayer") + ChatColor.RESET);
						} else
							sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED
									+ getLocal("msgPay", "transferToSelf") + ChatColor.RESET);
					} else
						sender.sendMessage(ChatColor.BOLD + getLocal("msgPay", "nullPlayerName") + ChatColor.RESET);
				} else
					sender.sendMessage(ChatColor.RED + getLocal("general", "notEnoughMoney") + ChatColor.RESET);
			} else
				sender.sendMessage(
						ChatColor.BOLD + "" + ChatColor.RED + getLocal("msgPay", "nullTransfer") + ChatColor.RESET);
		} else
			sender.sendMessage(ChatColor.BOLD + getLocal("general", "incorrectAmount") + ChatColor.RESET);
	}

	/**
	 * Complete method for command arguments.
	 * 
	 * @return ArrayList of available arguments.
	 */
	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command

		if (plugin.guildedInstalled) {
			// Guilded plugin integration support

			if (args.length == 1)
				return Lists.newArrayList("<amount>", "guild");

			if (args.length == 2 && args[0].equalsIgnoreCase("guild"))
				return plugin.core.coinStorage.getAllWallets(true);

			return Lists.newArrayList();
		} else {
			if (args.length == 1)
				return Lists.newArrayList("<amount>");

			if (args.length == 2 && !args[0].equalsIgnoreCase("guild"))
				return plugin.core.coinStorage.getAllWallets(false);

			return Lists.newArrayList();
		}
	}
}
