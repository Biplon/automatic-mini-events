package ame.java;

import ame.java.Reward.AMERewardManager;
import ame.java.command.CommandShowEvent;
import ame.java.event.OnBlockDrop;
import ame.java.event.OnEntityDeath;
import ame.java.command.CommandReloadEvent;
import ame.java.command.CommandStartEvent;
import ame.java.config.ConfigManager;
import ame.java.ameevent.AMEEventManager;
import ame.java.event.OnFishEvent;
import ame.java.event.OnPlayerClicks;
import ame.java.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class AME extends JavaPlugin
{
    static AME instance;

    public void onEnable()
    {
        instance = this;
        new LanguageManager();

        ConfigManager.loadConfig();
        LanguageManager.getInstance().loadLang();

        Bukkit.getLogger().info("[AME] register commands!");
        regCommands();
        Bukkit.getLogger().info("[AME] commands registered!");

        Bukkit.getLogger().info("[AME] load events!");
        new AMEEventManager();
        AMEEventManager.getInstance().initEvents();
        AMERewardManager.loadRewards();
        Bukkit.getLogger().info("[AME] events loaded!");
        regListener();
        Bukkit.getLogger().info("[AME] has been enabled!");
        if (getConfig().getBoolean("general.active"))
        {
            Bukkit.getLogger().info("[AME] Event timer started !");
            AMEEventManager.getInstance().startTimer();
        }
    }

    private void regCommands()
    {
        Objects.requireNonNull(this.getCommand("amereload")).setExecutor(new CommandReloadEvent());
        Objects.requireNonNull(this.getCommand("amestartevent")).setExecutor(new CommandStartEvent());
        Objects.requireNonNull(this.getCommand("ameshow")).setExecutor(new CommandShowEvent());
    }

    private void regListener()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnEntityDeath(), this);
        pm.registerEvents(new OnPlayerClicks(), this);
        pm.registerEvents(new OnBlockDrop(), this);
        pm.registerEvents(new OnFishEvent(), this);
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
