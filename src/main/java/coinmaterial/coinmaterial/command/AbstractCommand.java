package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.CoinMaterial;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    public AbstractCommand(String command) {

        PluginCommand pluginCommand = CoinMaterial.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    public List<String> complete(CommandSender sender, String args[]) {
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return filter(complete(sender,args),args);
    }

    private List<String> filter(List<String> list ,String[] args){
        if(list==null) return null;
        String last=args[args.length-1];
        List<String> result=new ArrayList<>();
        for(String arg:list){
            if(arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);

        }
            return result;
    }
}
