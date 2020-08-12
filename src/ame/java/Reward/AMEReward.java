package ame.java.Reward;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AMEReward
{
    public String name;

    public final List<String> rewardCommands1 = new ArrayList<>();
    public final List<String> rewardCommands2 = new ArrayList<>();
    public final List<String> rewardCommands3 = new ArrayList<>();
    public final List<String> rewardCommands410 = new ArrayList<>();
    public final List<String> rewardCommands11 = new ArrayList<>();

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
                rewardCommands1.add(cfg.getString("rewards.1."+count));
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
                rewardCommands2.add(cfg.getString("rewards.2."+count));
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
                rewardCommands3.add(cfg.getString("rewards.3."+count));
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
                rewardCommands410.add(cfg.getString("rewards.410."+count));
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
                rewardCommands11.add(cfg.getString("rewards.11."+count));
            }
            else
            {
                isnext = false;
            }
            count++;
        }
    }
}
