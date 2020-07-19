package ame.java.command;

import ame.java.ameevent.AMEEventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStartEvent implements CommandExecutor
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
                if (args.length == 1)
                {
                    AMEEventManager.getInstance().startEvent(args[0]);
                }
                else
                {
                    AMEEventManager.getInstance().startEvent();
                }
                return true;
            }
        }
        return false;
    }
}
