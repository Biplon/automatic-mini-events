package ame.java.command;

import ame.java.Reward.AMERewardManager;
import ame.java.ameevent.AMEEventManager;
import ame.java.lang.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReloadEvent implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (!player.hasPermission("ame.admin"))
            {
                return false;
            }
            else
            {
                if (!AMEEventManager.getInstance().eventActive)
                {
                    AMEEventManager.getInstance().initEvents();
                    AMERewardManager.loadRewards();
                    LanguageManager.getInstance().loadLang();
                    player.sendMessage("Reloaded AME Events!");
                }
                else
                {
                    player.sendMessage("Cannot be reloaded when an event is running!");
                }
                return true;
            }
        }
        return false;
    }
}
