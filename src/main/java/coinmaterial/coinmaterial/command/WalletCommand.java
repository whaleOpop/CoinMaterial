package coinmaterial.coinmaterial.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import doublewhaleapi.dwapi.DataModels.CoinModel;
import doublewhaleapi.dwapi.DataModels.GuildModel;
import doublewhaleapi.dwapi.DataModels.PlayerModel;

import java.util.HashMap;
import java.util.List;

/**
 * WalletCommand class. Handles money withdraw wallet command.
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public class WalletCommand extends AbstractCommand {
	/**
	 * Simple constructor with super support
	 */
	public WalletCommand() {
		super("wallet");
	}

	/**
	 * Pluralizes given string according to config.yml data.
	 * 
	 * @param pluralizable String to pluralize
	 * @param amount       Amount to pluralize to
	 * @return pluralized string
	 */
	public String pluralize(String pluralizable, Integer amount) {
		Integer mod10 = amount % 10;
		Integer mod100 = amount % 100;

		if ((mod10 == 1) && (mod100 != 11)) {
			return pluralizable + getLocal("general", "pluralizeOne");
		} else if ((mod10 >= 2) && (mod10 <= 4) && ((mod100 < 10) || (mod100 >= 20))) {
			return pluralizable + getLocal("general", "pluralizeTwoFour");
		}
		return pluralizable + getLocal("general", "pluralizeMany");
	}

	/**
	 * Execute command for player issuer.
	 * 
	 * @param player         Player issuer
	 * @param wallet         CoinModel wallet
	 * @param withdrawAmount Withdraw amount Double
	 */
	public void executeForPlayer(Player player, CoinModel wallet, Double withdrawAmount) {
		// executeForPlayer method - executes wallet command for player
		// Part of Guilded plugin integration

		if (withdrawAmount < 1.0) {
			// Check for withdrawing 0 coins
			player.sendMessage(colorize(ChatColor.RED, getLocal("msgWallet", "nullWithdraw")));
			return;
		}

		// Put in player inventory, calculate what does not fit
		Integer didntFit = 0;
		HashMap<Integer, ItemStack> didntFitHashmap = player.getInventory().addItem(new ItemStack(
				Material.getMaterial(getSettings("currency", "COIN_MATERIAL")), withdrawAmount.intValue()));

		for (ItemStack stack : didntFitHashmap.values()) {
			didntFit += stack.getAmount();
		}

		Integer canWithdraw = withdrawAmount.intValue() - didntFit;

		if (canWithdraw == 0) {
			// All player slots are filled
			player.sendMessage(colorize(ChatColor.RED, getLocal("msgWallet", "playerInventoryFull")));
			return;

		} else if (canWithdraw != withdrawAmount.intValue()) {
			// Deposited amount is not the same as inputed amount
			player.sendMessage(colorize(ChatColor.BOLD, getLocal("msgWallet", "notEnoughSlots")));
		}

		// Remove coin from wallet, save
		wallet.setBalance(wallet.getBalance() - canWithdraw);

		// Message the player
		player.sendMessage(getLocal("msgWallet", "correctWithdraw")
				.replace("{amount}", colorize(ChatColor.GREEN, canWithdraw.toString()))
				.replace("{materialName}",
						colorize(ChatColor.AQUA, pluralize(getLocal("general", "materialName"), canWithdraw)))
				.replace("{currencySymbol}", colorize(ChatColor.GOLD, getSettings("currency", "currencySymbol"))));
	}

	/**
	 * Execute command for guild.
	 * 
	 * @param player         Player issuer
	 * @param wallet         CoinModel wallet
	 * @param withdrawAmount Withdraw amount
	 */
	public void executeForGuild(Player player, CoinModel wallet, Double withdrawAmount) {
		// executeForGuild method - executes wallet command for player guild
		// Part of Guilded plugin integration

		if (withdrawAmount < 1.0) {
			// Check for withdrawing 0 coins
			player.sendMessage(colorize(ChatColor.RED, getLocal("msgWallet", "nullWithdraw")));
			return;
		}

		// Put in player inventory, calculate what does not fit
		Integer didntFit = 0;
		HashMap<Integer, ItemStack> didntFitHashmap = player.getInventory().addItem(new ItemStack(
				Material.getMaterial(getSettings("currency", "COIN_MATERIAL")), withdrawAmount.intValue()));

		for (ItemStack stack : didntFitHashmap.values()) {
			didntFit += stack.getAmount();
		}

		Integer canWithdraw = withdrawAmount.intValue() - didntFit;

		if (canWithdraw == 0) {
			// All player slots are filled
			player.sendMessage(colorize(ChatColor.RED, getLocal("msgWallet", "playerInventoryFull")));
			return;

		} else if (canWithdraw != withdrawAmount.intValue()) {
			// Deposited amount is not the same as inputed amount
			player.sendMessage(colorize(ChatColor.BOLD, getLocal("msgWallet", "notEnoughSlots")));
		}

		// Remove coin from wallet, save
		wallet.setBalance(wallet.getBalance() - canWithdraw);

		// Message the player
		player.sendMessage(getLocal("msgWallet", "correctWithdraw")
				.replace("{amount}", colorize(ChatColor.GREEN, canWithdraw.toString()))
				.replace("{materialName}",
						colorize(ChatColor.AQUA, pluralize(getLocal("general", "materialName"), canWithdraw)))
				.replace("{currencySymbol}", colorize(ChatColor.GOLD, getSettings("currency", "currencySymbol"))));
	}

	/**
	 * Command execute method. Implements wallet command.
	 * 
	 * @param sender Command issuer
	 * @param label  Command alias
	 * @param args   Command arguments
	 */
	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements wallet command

		if (!(sender instanceof Player)) {
			sender.sendMessage(getLocal("general", "issuerNotPLayer"));
			return;
		}

		if (args.length == 0) {
			if (plugin.guildedInstalled) {
				// Suggest amount or guild keyword
				sender.sendMessage(colorize(ChatColor.BOLD, getLocal("guildedWallet", "promptInputAmount")));
			} else {
				// Suggest passing an amount to the command
				sender.sendMessage(colorize(ChatColor.BOLD,
						getLocal("general", "promptInputAmount") + getLocal("msgWallet", "promptAdd")));
			}
			return;
		}

		if (args[0].equalsIgnoreCase("guild")) {
			// Player tries accessing guild's wallet

			if (plugin.guildedInstalled) {
				// Guilded plugin integration

				// Testing player's permissions in the guild
				String playerName = sender.getName();
				GuildModel gm = plugin.core.guildStorage.getGuildByPlayer(playerName);
				if (gm != null) {
					// Found player's guild
					PlayerModel player = gm.getPlayerByName(playerName);

					if (player.testOperatorship()) {
						// Player is operator/creator level role

						if (args.length == 2) {
							// Player passed withdraw amount to the command

							if (isNumber(args[1])) {
								// Amount is number

								CoinModel wallet = plugin.core.coinStorage.getWalletByName(gm.getCreatorName(), true);
								Double withdrawAmount = Double.parseDouble(args[1]);
								Double guildCoins = wallet.getCoins();

								if (guildCoins >= withdrawAmount) {
									// Execute guild wallet command

									executeForGuild((Player) sender, wallet, withdrawAmount);
								} else
									sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "notEnoughMoney")));

							} else if (args[1].equalsIgnoreCase("all")) {
								// Player can not withdraw all from guild`s wallet

								sender.sendMessage(
										colorize(ChatColor.RED, getLocal("guildedWallet", "withdrawAmountNumberOnly")));
							} else
								sender.sendMessage(colorize(ChatColor.RED, getLocal("general", "incorrectAmount")));
						} else
							sender.sendMessage(colorize(ChatColor.BOLD,
									getLocal("general", "promptInputAmount") + getLocal("msgWallet", "promptAdd")));
					} else
						sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "playerTooLowRole")));
				} else
					sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "playerNotInGuild")));
			} else
				sender.sendMessage(colorize(ChatColor.RED, getLocal("guilded", "incorrectArgument")));

		} else if (isNumber(args[0]) || args[0].equalsIgnoreCase("all")) {
			// Simple player wallet withdrawal

			CoinModel wallet = plugin.core.coinStorage.getWalletByName(sender.getName(), false);
			Double withdrawAmount = 0.0;
			Double playerCoins = wallet.getCoins();

			if (args[0].equalsIgnoreCase("all")) {
				// Deposit all coins
				withdrawAmount = playerCoins;
			} else {
				// Deposit amount provided as number
				withdrawAmount = Double.parseDouble(args[0]);

				if (playerCoins < withdrawAmount) {
					// Player doesn`t have enough coins to deposit
					sender.sendMessage(colorize(ChatColor.RED, getLocal("general", "notEnoughMoney")));
					return;
				}
			}

			// Execute player wallet command
			executeForPlayer((Player) sender, wallet, withdrawAmount);
		} else
			sender.sendMessage(colorize(ChatColor.RED, getLocal("general", "incorrectAmount")));
	}

	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command

		if (plugin.guildedInstalled) {
			// Guilded plugin integration support
			if (args.length == 1)
				return Lists.newArrayList("<amount>", "all", "guild");

			if (args.length == 2)
				if (args[1].equalsIgnoreCase("guild"))
					return Lists.newArrayList("<amount>");

		} else if (args.length == 1)
			return Lists.newArrayList("<amount>", "all");

		return Lists.newArrayList();
	}
}