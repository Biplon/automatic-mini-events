package ame.java;

import ame.java.Reward.AMERewardManager;
import ame.java.event.OnEntityDeath;
import ame.java.command.CommandReloadEvent;
import ame.java.command.CommandStartEvent;
import ame.java.config.ConfigManager;
import ame.java.ameevent.AMEEventManager;
import ame.java.event.OnPlayerClicks;
import ame.java.lang.LanguageManager;
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
        new LanguageManager();

        ConfigManager.loadConfig();
        LanguageManager.getInstance().loadLang();

        Bukkit.getLogger().info("[AME] register commands!");
        regCommands();
        Bukkit.getLogger().info("[AME] commands registered!");
        Bukkit.getLogger().info("[AME] load events!");
        new AMEEventManager();
        new AMERewardManager();
        AMEEventManager.getInstance().initEvents();
        AMERewardManager.getInstance().loadRewards();
        Bukkit.getLogger().info("[AME] events loaded!");
        regEvents();
        Bukkit.getLogger().info("[AME] has been enabled!");
        AMEEventManager.getInstance().startTimer();
    }

    private void regCommands()
    {
        this.getCommand("amereloadevent").setExecutor(new CommandReloadEvent());
        this.getCommand("amestartevent").setExecutor(new CommandStartEvent());
    }

    private void regEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnEntityDeath(), this);
        pm.registerEvents(new OnPlayerClicks(), this);
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
