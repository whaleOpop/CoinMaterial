package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;

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

    public boolean enoughCoins(CommandSender sender, Double paymentValue) {
        // enoughCoins method - tests if sender user has enough coins in their wallet
        return paymentValue <= Hashmapper.getPlayerCoin(Bukkit.getPlayer(sender.getName()).getName());

    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - implements pay command
        if (!(sender instanceof Player)) {
            sender.sendMessage(readConfig("error", "issuerNotPLayer"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + readConfig("msg", "promptInputMoney") + readConfig("msgPay", "promptAdd"));
            return;
        }
        
        if (isNumber(args[0]) == true) {
            // Payment value provided is a number

            if (Double.parseDouble(args[0]) > 0.0) {
                // Payment value is greater than zero

                if (enoughCoins(sender, Double.valueOf(args[0]))) {
                    // Player has no less than a payment value coins in wallet

                    if (args.length != 2) {
                        // Player provided payment receiver player who is online

                        if (!args[1].equals(sender.getName())) {
                            // Payment Sender is not Receiver

                            if (Hashmapper.playerExists(args[1])) {
                                // Payment Receiver is a Player and has a wallet

                                // Temporary player, receiver variables
                                Player player = (Player) sender;
                                Player receiver = Bukkit.getPlayerExact(args[1]);

                                // Messages sender and receiver about payment
                                String msg = readConfig("msgPay", "senderMessage");
                                msg = msg.replace("{receiver}", ChatColor.BOLD + args[1] + ChatColor.RESET);
                                msg = msg.replace("{amount}", args[0]);
                                msg = msg.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
                                sender.sendMessage(msg);
                                
                                msg = readConfig("msgPay", "receiverMessage");
                                msg = msg.replace("{sender}", ChatColor.BOLD + sender.getName() + ChatColor.RESET);
                                msg = msg.replace("{amount}", args[0]);
                                msg = msg.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
                                receiver.sendMessage(msg);

                                // Plays villager_trade sound to both players
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                                receiver.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);

                                // Transfers the money
                                BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue
                                        - newValue;
                                BiFunction<Double, Double, Double> bFuncAdd = (oldValue, newValue) -> oldValue
                                        + newValue;

                                Hashmapper.playerCoin.merge(player.getName(), Double.valueOf(args[0]), bFuncSub);
                                Hashmapper.playerCoin.merge(args[1], Double.valueOf(args[0]), bFuncAdd);

                                // Saves state
                                Hashmapper.SaveCoin();

                            } else {
                                sender.sendMessage(ChatColor.RED + readConfig("msgPay", "incorrectPlayer") + ChatColor.RESET);
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + readConfig("msgPay", "transferToSelf") + ChatColor.RESET);
                        }
                    } else {
                        sender.sendMessage(ChatColor.BOLD + readConfig("msgPay", "nullPlayerName") + ChatColor.RESET);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + readConfig("error", "notEnoughMoney") + ChatColor.RESET);
                }
            } else {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + readConfig("msgPay", "nullTransfer") + ChatColor.RESET);
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + readConfig("error", "incorrectNumber") + ChatColor.RESET);
        }
    }
}
