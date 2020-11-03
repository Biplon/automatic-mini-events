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

    public String startText;

    public String eventDuration;

    public String progressText;

    public String eventEndText;

    public String eventMinLeft;

    public String eventGetRewardText;

    public String eventGetPlaceText;

    public String eventWinnerIs;

    public String rewardBagNameLore1;

    public String minText;

    public String secText;

    public String noEventRunning;

    public void loadLang()
    {
        File configFile = new File("plugins" + File.separator + AME.getInstance().getName() + File.separator + AME.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        rewardBagName = cfg.getString("rewardBagName") != null ? cfg.getString("rewardBagName") : "Reward bag";
        startText = cfg.getString("starttext") != null ? cfg.getString("starttext") : "started!";
        eventDuration = cfg.getString("eventduration") != null ? cfg.getString("eventduration") : "Event duration:";
        progressText = cfg.getString("progresstext") != null ? cfg.getString("progresstext") : "Score:";
        eventEndText = cfg.getString("eventendtext") != null ? cfg.getString("eventendtext") : "is finished!";
        eventMinLeft = cfg.getString("eventminleft") != null ? cfg.getString("eventminleft") : "Event ends in:";
        eventGetRewardText = cfg.getString("eventgetrewardtext") != null ? cfg.getString("eventgetrewardtext") : "Your reward is:";
        eventGetPlaceText = cfg.getString("eventgetplacetext") != null ? cfg.getString("eventgetplacetext") : "Your rank in the event was:";
        eventWinnerIs = cfg.getString("eventwinneris") != null ? cfg.getString("eventwinneris") : "1st place is:";
        rewardBagNameLore1 = cfg.getString("rewardBagNamelore1") != null ? cfg.getString("rewardBagNamelore1") : "&0Item: Reward bag";
        minText = cfg.getString("mintext") != null ? cfg.getString("mintext") : "min";
        secText = cfg.getString("sectext") != null ? cfg.getString("sectext") : "sec";
        noEventRunning = cfg.getString("noeventrunning") != null ? cfg.getString("noeventrunning") : "no event active";
    }
}
