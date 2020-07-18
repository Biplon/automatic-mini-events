package main.java.lang;

import main.java.AME;
import main.java.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager
{
    static LanguageManager instance;

    public LanguageManager()
    {
        instance = this;
    }

    public static LanguageManager getInstance()
    {
        return instance;
    }

    public String rewardBagName;

    public String starttext;

    public String eventduration;

    public void loadLang()
    {
        File configFile = new File("plugins" + File.separator + AME.getInstance().getName() + File.separator + AME.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        rewardBagName = cfg.getString("rewardBagName") != null ? cfg.getString("rewardBagName") : "Reward bag";
        starttext  = cfg.getString("starttext") != null ? cfg.getString("starttext") : "started!";
        eventduration = cfg.getString("eventduration") != null ? cfg.getString("eventduration") : "Event duration:";
    }
}
