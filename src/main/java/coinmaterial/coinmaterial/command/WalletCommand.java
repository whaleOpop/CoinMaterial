package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.BiFunction;


import coinmaterial.coinmaterial.Hash.Hashmapper;


/**
 * Implements CoinMaterial wallet command
 * Usage:        /CoinMaterial wallet
 * Requirements: none
 */
public class WalletCommand extends AbstractCommand {
    public WalletCommand() {
        // Simple constructor with super support
        super("wallet");
    }

    public boolean enoughCoins(CommandSender sender, Double paymentValue) {
        // enoughCoins method - tests if sender user has enough coins in their wallet
        return paymentValue <= Hashmapper.getPlayerCoin(Bukkit.getPlayer(sender.getName()).getName());
    }

    public String pluralize(String pluralizable, Double amount) {
        // pluralize method - return pluralized pluralizable string according to amount in Russian
        Integer mod10 = amount.intValue() % 10;
        Integer mod100 = amount.intValue() % 100;

        if ((mod10 == 1) && (mod100 != 11)) {
            return pluralizable + "ь";
        } else if ((mod10 >= 2) && (mod10 <= 4) && ((mod100 < 10) || (mod100 >= 20))) {
            return pluralizable + "я";
        } else {
            return pluralizable + "ей";
        }
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - implements wallet command
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players are able to use this command!");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + "Введите колличество Арабских лигатур Джаллаялалоуху, которое хотите вывести.");
            return;
        }

        if (isNumber(args[0]) == true) {
            // Deposit value provided as number
            if (enoughCoins(sender, Double.parseDouble(args[0]))) {
                // Player has enough coins to deposit
                if (Bukkit.getPlayer(sender.getName()).getInventory().firstEmpty() != -1) {
                    // Player has any empty slots

                    // Put in inventory, calculate what does not fit
                    Integer didntFit = 0;
                    HashMap<Integer, ItemStack> didntFitHashmap = Bukkit.getPlayer(sender.getName()).getInventory().addItem(new ItemStack(Material.EMERALD, Integer.valueOf(args[0])));
                    for (ItemStack stack : didntFitHashmap.values())
                        didntFit += stack.getAmount();

                    // Send emeralds to inventory, remove from wallet, save wallet
                    BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
                    Hashmapper.playerCoin.put(Bukkit.getPlayer(sender.getName()).getName(), Hashmapper.playerCoin.merge(Bukkit.getPlayer(sender.getName()).getName(), Double.valueOf(args[0]) - Double.valueOf(didntFit), bFuncSub));

                    Hashmapper.SaveCoin();

                    // Message the player
                    sender.sendMessage("Вы получили " + ChatColor.GREEN + (Double.parseDouble(args[0]) - Double.valueOf(didntFit)) + pluralize(" смарагд", Double.parseDouble(args[0]) - Double.valueOf(didntFit)) + ChatColor.RESET + " по курсу 1:1 с " + ChatColor.GOLD + "ﷻ");
                } else {
                    sender.sendMessage(ChatColor.RED + "Освободите ваш инвентарь");
                }
            } else {
                sender.sendMessage(ChatColor.BOLD + "У вас не достаточно лигатур, " + "الوغد" + "!");
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + "Введите корректную сумму вывода числом!");

        }
    }
}
