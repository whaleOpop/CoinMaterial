package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.net.DatagramPacket;
import java.util.function.BiFunction;

public class PayCommand extends AbstractCommand {
    public PayCommand() {
        super("pay");
    }
    public boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        label="s";
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD +"Введите кол-во которое хотите перевести игроку");
            return;
        }
        if (isNumber(args[0]) == true) {
            if (!(Double.parseDouble(args[0]) > Double.parseDouble(String.valueOf(Hashmapper.playerCoin.get(Bukkit.getPlayer(sender.getName()).getName()))) )) {


            if(!(args[1]==null)) {
                if(!args[1].equals(sender.getName())) {
                    BiFunction<Double, Double, Double> bFunc = (oldValue, newValue) -> oldValue - newValue;
                    BiFunction<Double, Double, Double> bFuncplus = (oldValue, newValue) -> oldValue + newValue;
                    sender.sendMessage(ChatColor.BOLD + "Вы перевели игроку " + args[1] +ChatColor.GOLD+ " ﷻ ");
                    Hashmapper.playerCoin.merge(Bukkit.getPlayer(sender.getName()).getName(), Double.valueOf(args[0]), bFunc);
                    Hashmapper.playerCoin.merge(args[1], Double.valueOf(args[0]), bFuncplus);
                    Bukkit.getPlayer(args[1]).sendMessage(ChatColor.BOLD +"Вы получили " +  args[0] +" ");
                    Bukkit.getPlayer(sender.getName()).playSound(Bukkit.getPlayer(sender.getName()).getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                    Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[1]).getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                    Hashmapper.SaveCoin();

                    return;
                }else {
                    sender.sendMessage(ChatColor.BOLD+"Ты дебил зачем ты сам себе кидаешь деньги");
                    return;
                }
            }else {
                sender.sendMessage(ChatColor.BOLD+"Введите имя игрока");
                return;
            }
            }else {
                sender.sendMessage(ChatColor.BOLD+"Чел ты бомж");
                return;
            }
        }else {
            sender.sendMessage(ChatColor.BOLD+"Введите число");
            return;
        }
    }
}
