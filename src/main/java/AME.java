package main.java;

import main.java.config.ConfigManager;
import main.java.ameevent.AMEEventManager;
import main.java.event.OnEntityDeath;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AME extends JavaPlugin
{
    static AME instance;

    public void onEnable()
    {
        instance = this;
        new ConfigManager();
        try
        {
            ConfigManager.loadConfig();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Bukkit.getLogger().info("[AME] register commands!");
        regCommands();
        Bukkit.getLogger().info("[AME] commands registered!");
        Bukkit.getLogger().info("[AME] load events!");
        new AMEEventManager();
        AMEEventManager.getInstance().initEvents();
        Bukkit.getLogger().info("[AME] events loaded!");
        regEvents();
        Bukkit.getLogger().info("[AME] has been enabled!");
    }

    private void regCommands()
    {
     //   this.getCommand("command").setExecutor(new Command());

    }

    private void regEvents()
    {
        PluginManager pm = getServer().getPluginManager();
       pm.registerEvents(new OnEntityDeath(), this);
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        Bukkit.getLogger().info("[AME] has been disabled!");
    }

    public static AME getInstance()
    {
        return instance;
    }
}
