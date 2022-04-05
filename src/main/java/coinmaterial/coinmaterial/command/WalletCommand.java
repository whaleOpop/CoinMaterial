package coinmaterial.coinmaterial.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;

/**
 * Implements CoinMaterial wallet command
 * Usage:        /wallet [amount]
 * Requirements: none
 */
public class WalletCommand extends AbstractCommand {
	public WalletCommand() {
		// Simple constructor with super support
		super("wallet");
	}

	public String pluralize(String pluralizable, Integer amount) {
		// pluralize method - return pluralized pluralizable string according to amount
		// in Russian
		Integer mod10 = amount % 10;
		Integer mod100 = amount % 100;

		if ((mod10 == 1) && (mod100 != 11)) {
			return pluralizable + readConfig("coin", "pluralizeOne");
		} else if ((mod10 >= 2) && (mod10 <= 4) && ((mod100 < 10) || (mod100 >= 20))) {
			return pluralizable + readConfig("coin", "pluralizeTwoFour");
		}
		return pluralizable + readConfig("coin", "pluralizeMany");
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements wallet command
		if (!(sender instanceof Player)) {
			sender.sendMessage(readConfig("error", "issuerNotPLayer"));
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.BOLD + readConfig("msg", "promptInputMoney") + readConfig("msgWallet", "promptAdd") + readConfig("msgWallet", "allDepositAdd") + ChatColor.RESET);
			return;
		}

		if (!(isNumber(args[0]) || args[0].equalsIgnoreCase("all"))) {
			sender.sendMessage(ChatColor.RED + readConfig("error", "incorrectNumber") + readConfig("msgWallet", "allDepositAdd") + ChatColor.RESET);
			return;
		}
		
		Player player = (Player)sender;
		String playerName = player.getName();
		
		Double depositAmount = 0.0;
		Double playerCoins = Hashmapper.getPlayerCoin(playerName);
		
		if (args[0].equalsIgnoreCase("all")) {
			// Deposit all coins
			depositAmount = playerCoins;
		} else {
			// Deposit value provided as number
			depositAmount = Double.parseDouble(args[0]);
			if (depositAmount < 1.0) {
				// Deposit value is 0 or less
				sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + readConfig("msgWallet", "nullDeposit") + ChatColor.RESET);
				return;
			}
			
			if (playerCoins < depositAmount) {
				// Player doesn`t have enough coins to deposit
				sender.sendMessage(ChatColor.RED + readConfig("error", "notEnoughMoney") + ChatColor.RESET);
				return;
			}
		}
		
		// Put in inventory, calculate what does not fit
		Integer didntFit = 0;
		HashMap<Integer, ItemStack> didntFitHashmap = player.getInventory().addItem(new ItemStack(Material.getMaterial(readConfig("coin", "COIN_MATERIAL")), depositAmount.intValue()));

		for (ItemStack stack : didntFitHashmap.values()) {
			didntFit += stack.getAmount();
		}
		
		Integer canDeposit = depositAmount.intValue() - didntFit;

		if (canDeposit == 0) {
			// All player slots are filled
			sender.sendMessage(ChatColor.RED + readConfig("msgWallet", "playerInventoryFull") + ChatColor.RESET);
			return;
			
		} else if (canDeposit != depositAmount.intValue()) {
			// Deposited amount is not the same as inputed amount
			String msg = ChatColor.BOLD + readConfig("msgWallet", "notEnoughSlots") + ChatColor.RESET;
			sender.sendMessage(msg);
		}

		// Remove coin from wallet, save
		BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
		Hashmapper.performCoinOperation(playerName, Double.valueOf(canDeposit), bFuncSub);
		Hashmapper.SaveCoin();

		// Message the player
		String msgPlayer = readConfig("msgWallet", "correctDeposit");
		msgPlayer = msgPlayer.replace("{amount}", ChatColor.GREEN + canDeposit.toString() + ChatColor.RESET);
		msgPlayer = msgPlayer.replace("{coinName}", ChatColor.AQUA + pluralize(readConfig("coin", "coinName"), canDeposit) + ChatColor.RESET);
		msgPlayer = msgPlayer.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
		sender.sendMessage(msgPlayer);
	}
	
	@Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command
		if (args.length == 1)
			return Lists.newArrayList("<amount>", "all");
		return Lists.newArrayList();
	}
}
