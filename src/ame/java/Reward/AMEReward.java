package ame.java.Reward;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AMEReward
{

    public String name;

    public final List<String> rewardcommands1 = new ArrayList<>();
    public final List<String> rewardcommands2 = new ArrayList<>();
    public final List<String> rewardcommands3 = new ArrayList<>();
    public final List<String> rewardcommands410 = new ArrayList<>();
    public final List<String> rewardcommands11 = new ArrayList<>();

    public AMEReward(String path)
    {
        File f = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        name = cfg.getString("event.name");
        boolean isnext = true;
        int count = 0;
        while (isnext)
        {
            if (cfg.getString("rewards.1."+count) != null)
            {
                rewardcommands1.add(cfg.getString("rewards.1."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("rewards.2."+count) != null)
            {
                rewardcommands2.add(cfg.getString("rewards.2."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("rewards.3."+count) != null)
            {
                rewardcommands3.add(cfg.getString("rewards.3."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("rewards.410."+count) != null)
            {
                rewardcommands410.add(cfg.getString("rewards.410."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("rewards.11."+count) != null)
            {
                rewardcommands11.add(cfg.getString("rewards.11."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
    }
}
