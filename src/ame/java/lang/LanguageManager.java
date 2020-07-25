package ame.java.lang;

import ame.java.AME;
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

    public String progresstext;

    public String eventendtext;

    public String eventminleft;

    public String eventgetrewardtext;

    public String eventgetplacetext;

    public String eventwinneris;

    public void loadLang()
    {
        File configFile = new File("plugins" + File.separator + AME.getInstance().getName() + File.separator + AME.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        rewardBagName = cfg.getString("rewardBagName") != null ? cfg.getString("rewardBagName") : "Reward bag";
        starttext  = cfg.getString("starttext") != null ? cfg.getString("starttext") : "started!";
        eventduration = cfg.getString("eventduration") != null ? cfg.getString("eventduration") : "Event duration:";
        progresstext = cfg.getString("progresstext") != null ? cfg.getString("progresstext") : "Score:";
        eventendtext = cfg.getString("eventendtext") != null ? cfg.getString("eventendtext") : "is finished!";
        eventminleft = cfg.getString("eventminleft") != null ? cfg.getString("eventminleft") : "Event ends in:";
        eventgetrewardtext = cfg.getString("eventgetrewardtext") != null ? cfg.getString("eventgetrewardtext") : "Your reward is:";
        eventgetplacetext = cfg.getString("eventgetplacetext") != null ? cfg.getString("eventgetplacetext") : "Your rank in the event was:";
        eventwinneris = cfg.getString("eventwinneris") != null ? cfg.getString("eventwinneris") : "Winner is:";
    }
}
