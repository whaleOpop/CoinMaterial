package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;
import java.util.function.BiFunction;

import coinmaterial.coinmaterial.CoinMaterial;
import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;

/**
 * Implements CoinMaterial pay command
 * Usage:        /pay [amount] [receiver]
 * Requirements: none
 */
public class PayCommand extends AbstractCommand {
	public PayCommand() {
		// Simple constructor with super support
		super("pay");
	}

	private void executeForPlayer(Player player, String receiverName, Double transferAmount) {
		// executeForPlayer method - executes pay command for player
		// Part of Guilded plugin integration
		Player receiver = Bukkit.getPlayerExact(receiverName);
		String playerName = player.getName();

		// Messages sender and receiver about payment, play villager_trade sound to both
		String msg = getLocal("msgPay", "senderMessage");
		msg = msg.replace("{receiver}", ChatColor.BOLD + receiverName + ChatColor.RESET);
		msg = msg.replace("{amount}", transferAmount.toString());
		msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
		player.sendMessage(msg);
		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);

		if (receiver != null) {
			// Payment receiver might be offline, if so - don`t message him
			msg = getLocal("msgPay", "receiverMessage");
			msg = msg.replace("{sender}", ChatColor.BOLD + playerName + ChatColor.RESET);
			msg = msg.replace("{amount}", transferAmount.toString());
			msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
			receiver.sendMessage(msg);
			receiver.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
		}

		// Transfers the money
		BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
		BiFunction<Double, Double, Double> bFuncAdd = (oldValue, newValue) -> oldValue + newValue;

		CoinSerializer.performCoinOperation(playerName, transferAmount, bFuncSub);
		CoinSerializer.performCoinOperation(receiverName, transferAmount, bFuncAdd);

		// Saves state
		CoinSerializer.SaveCoin();
	}

	private void executeForGuild(Player player, String guildName, Double transferAmount) {
		// executeForGuild method - executes pay command for player guild
		// Part of Guilded plugin integration
		
		// Messages sender about payment, play villager_trade sound
		String msg = getLocal("guildedPay", "senderMessage");
		msg = msg.replace("{receiver}", ChatColor.BOLD + guildName + ChatColor.RESET);
		msg = msg.replace("{amount}", transferAmount.toString());
		msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
		player.sendMessage(msg);
		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);

		// Transfers the money
		BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
		BiFunction<Double, Double, Double> bFuncAdd = (oldValue, newValue) -> oldValue + newValue;

		CoinSerializer.performCoinOperation(player.getName(), transferAmount, bFuncSub);
		CoinSerializer.performCoinOperation(guildName, transferAmount, bFuncAdd);

		// Saves state
		CoinSerializer.SaveCoin();
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements pay command
		if (!(sender instanceof Player)) {
			sender.sendMessage(getLocal("general", "issuerNotPLayer"));
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.BOLD + getLocal("general", "promptInputAmount") + getLocal("msgPay", "promptAdd"));
			return;
		}

		String playerName = sender.getName();

		if (args[0].equalsIgnoreCase("guild")) {
			// Guilded plugin integration support

			if (CoinMaterial.guildedInstalled) {
				// Transfer to guild wallet

				if (args.length == 2) {
					// Player provided a payment amount

					if (isNumber(args[1])) {
						// Payment amount is  positive integer
						
						Double transferAmount = Double.parseDouble(args[1]);
						Double playerCoins = CoinSerializer.getCoin(playerName);
						if (transferAmount > 0.0) {
							// Payment is not 0 coins
							
							if (transferAmount <= playerCoins) {
								// Player has enough money to make payment
								
								if (args.length == 3) {
									// Player specified nickname of player (preferably) in a guild
									if (CoinSerializer.walletExists(args[2], true)) {
										// TODO: then test player role in a guild (must higher than "requested")
										
										// Execute guild pay command
										executeForGuild((Player) sender, args[2], transferAmount);
									} else
										sender.sendMessage(ChatColor.RED + getLocal("guilded", "playerNotInGuild") + ChatColor.RESET);
								} else
									sender.sendMessage(ChatColor.BOLD + getLocal("guildedPay", "nullPlayerName") + ChatColor.RESET);
							} else
								sender.sendMessage(ChatColor.RED + getLocal("general", "notEnoughMoney") + ChatColor.RESET);
						} else
							sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + getLocal("msgPay", "nullTransfer") + ChatColor.RESET);
					} else
						sender.sendMessage(ChatColor.BOLD + getLocal("general", "incorrectAmount") + ChatColor.RESET);
				} else
					sender.sendMessage(ChatColor.BOLD + getLocal("general", "promptInputAmount") + getLocal("msgWallet", "promptAdd") + ChatColor.RESET);
			} else
				sender.sendMessage(ChatColor.RED + getLocal("general", "guildedNotIncluded") + ChatColor.RESET);
			
		} else if (isNumber(args[0])) {
			// Payment value provided is a number
			Double transferAmount = Double.parseDouble(args[0]);
			Double playerCoins = CoinSerializer.getCoin(playerName);

			if (transferAmount > 0.0) {
				// Payment value is greater than zero

				if (transferAmount <= playerCoins) {
					// Player has no less than a payment value coins in wallet

					if (args.length == 2) {
						// Player provided payment receiver nickname

						if (!args[1].equals(playerName)) {
							// Payment Sender is not Receiver

							if (CoinSerializer.walletExists(args[1], false)) {
								// Payment Receiver is a Player and has a wallet
								
								// Execute player pay command
								executeForPlayer((Player) sender, args[1], transferAmount);
							} else
								sender.sendMessage(ChatColor.RED + getLocal("msgPay", "incorrectPlayer") + ChatColor.RESET);
						} else
							sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + getLocal("msgPay", "transferToSelf") + ChatColor.RESET);
					} else
						sender.sendMessage(ChatColor.BOLD + getLocal("msgPay", "nullPlayerName") + ChatColor.RESET);
				} else
					sender.sendMessage(ChatColor.RED + getLocal("general", "notEnoughMoney") + ChatColor.RESET);
			} else
				sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + getLocal("msgPay", "nullTransfer") + ChatColor.RESET);
		} else
			sender.sendMessage(ChatColor.BOLD + getLocal("general", "incorrectAmount") + ChatColor.RESET);
	}

	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command
		
		if (CoinMaterial.guildedInstalled) {
			// Guilded plugin integration support
			
			if (args.length == 1)
				return Lists.newArrayList("<amount>", "guild");
			
			if (args.length == 2 && args[0].equalsIgnoreCase("guild"))
				return CoinSerializer.getAllGuildCreators();
			
			return Lists.newArrayList();
		} else {
			if (args.length == 1)
				return Lists.newArrayList("<amount>");
				
			if (args.length == 2 && !args[0].equalsIgnoreCase("guild"))
				return CoinSerializer.getAllPlayers();
			
			return Lists.newArrayList();
		}
	}
}
