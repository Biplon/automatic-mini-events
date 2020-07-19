package ame.java.config;

import ame.java.AME;

import java.io.File;

public class ConfigManager
{

    static ConfigManager instance;

    public ConfigManager()
    {
        instance = this;
    }

    public static ConfigManager getInstance()
    {
        return instance;
    }

    public static void loadConfig()
    {
        File configFile = new File("plugins" + File.separator + AME.getInstance().getName() + File.separator + "config.yml");
        if (!configFile.exists())
        {
            AME.getInstance().getLogger().info("Creating config ...");
            AME.getInstance().saveDefaultConfig();
        }
        try
        {
            AME.getInstance().getLogger().info("Loading the config ...");
            AME.getInstance().getConfig().load(configFile);
        }
        catch (Exception e)
        {
            AME.getInstance().getLogger().severe("Could not load the config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
