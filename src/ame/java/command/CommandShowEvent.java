package ame.java.command;

import ame.java.ameevent.AMEEventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShowEvent implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            AMEEventManager.getInstance().showPlayerEvent(player);
            return true;
        }
        return false;
    }
}