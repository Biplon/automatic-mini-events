package ame.java.command;

import ame.java.ameevent.AMEEventManager;
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
                if (!AMEEventManager.getInstance().eventactive)
                {
                    AMEEventManager.getInstance().initEvents();
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
