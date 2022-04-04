package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;

/**
 * Implements CoinMaterial pay command Usage: /CoinMaterial pay Requirements:
 * none
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
            sender.sendMessage("Only players are able to use this command!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD
                    + "Введите количество Арабских лигатур Джаллаялалоуху, которое хотите перевести игроку.");
            return;
        }

        if (isNumber(args[0]) == true) {
            // Payment value provided is a number

            if (Double.parseDouble(args[0]) > 0.0) {
                // Payment value is greater than zero

                if (enoughCoins(sender, Double.parseDouble(args[0]))) {
                    // Player has no less than a payment value coins in wallet

                    if (args[1] != null) {
                        // Player provided payment receiver player who is online

                        if (!args[1].equals(sender.getName())) {
                            // Payment Sender is not Receiver

                            if (Hashmapper.playerExists(args[1])) {
                                // Payment Receiceiver is a Player and has a wallet

                                // Temporary player, receiver variables
                                Player player = (Player) sender;
                                Player receiver = Bukkit.getPlayerExact(args[1]);

                                // Messages sender and receiver about payment
                                sender.sendMessage("Вы перевели игроку " + ChatColor.BOLD + args[1] + ChatColor.RESET
                                        + " " + args[0] + ChatColor.GOLD + "ﷻ");
                                receiver.sendMessage("Вы получили " + args[0] + ChatColor.GOLD + "ﷻ" + ChatColor.RESET
                                        + " от " + ChatColor.BOLD + sender.getName());

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
                                sender.sendMessage(ChatColor.RED + "У данного пользователя нет кошелька.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Вы " + "أنت شخص غبي"
                                    + ", зачем самому себе переводить лигатуры?");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Введите имя игрока на сервере!");
                    }
                } else {
                    sender.sendMessage(ChatColor.BOLD + "У вас не достаточно лигатур!"+ "الوغد");
                }
            } else {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Партия осуждать попытки обман плагин! "
                        + "السجن والإعدام" + "!");
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + "Введите корректную сумму перевода числом!");
        }
    }
}
