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

    public String  rewardBagNamelore1;

    public String toplist;

    public String  eventtoplistlink;

    public String[] toplistplaces = new String[10];

    public String mintext;

    public String sectext;

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
        eventwinneris = cfg.getString("eventwinneris") != null ? cfg.getString("eventwinneris") : "1st place is:";
        rewardBagNamelore1 = cfg.getString("rewardBagNamelore1") != null ? cfg.getString("rewardBagNamelore1") : "&0Item: Reward bag";
        toplist = cfg.getString("toplist") != null ? cfg.getString("toplist") : "Toplist";
        eventtoplistlink = cfg.getString("eventtoplistlink") != null ? cfg.getString("eventtoplistlink") : "Show toplist";
        mintext = cfg.getString("mintext") != null ? cfg.getString("mintext") : "min";
        sectext = cfg.getString("sectext") != null ? cfg.getString("sectext") : "sec";
        for (int i = 0; i < toplistplaces.length; i++)
        {
            toplistplaces[i] = cfg.getString("toplistplace"+(i+1)) != null ? cfg.getString("toplistplace"+(i+1)) : "#1";
        }
    }
}
