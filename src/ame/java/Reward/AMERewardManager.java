package ame.java.Reward;

import ame.java.AME;
import ame.java.config.EventConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AMERewardManager
{
    public List<AMEReward> rewards;

    static AMERewardManager instance;

    public AMERewardManager()
    {
        instance = this;
    }

    public static AMERewardManager getInstance()
    {
        return instance;
    }

    public void loadRewards()
    {
        List<AMEReward> e = new ArrayList<>();

        File folder = new File(AME.getInstance().getDataFolder() + "/rewards");

        if (!folder.exists())
        {
            folder.mkdir();
        }

        List<String> result = new ArrayList<>();

        EventConfigLoader.search(".*\\.yml", folder, result);
        for (String s : result)
        {

            e.add(new AMEReward(s));
        }

        rewards = e;
    }

    public boolean getPlayerReward(Player p, ItemStack i)
    {
        String name = Objects.requireNonNull(Objects.requireNonNull(i.getItemMeta()).getLore()).get(1);
        for (AMEReward re: rewards)
        {
            if (re.name.equals(name))
            {
                name = Objects.requireNonNull(i.getItemMeta().getLore()).get(2).replace("#","");
                switch (name)
                {
                    case "1":
                        for (String co : re.rewardcommands1)
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
                        }
                        return true;
                    case "2":
                        for (String co : re.rewardcommands2)
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
                        }
                        return true;
                    case "3":
                        for (String co : re.rewardcommands3)
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
                        }
                        return true;
                    case "4-10":
                        for (String co : re.rewardcommands410)
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
                        }
                        return true;
                    case ">10":
                        for (String co : re.rewardcommands11)
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
                        }
                        return true;
                }
            }
        }
        return false;
    }
}
