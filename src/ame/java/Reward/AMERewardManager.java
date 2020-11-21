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
    public static List<AMEReward> rewards;

    public static void loadRewards()
    {
        List<AMEReward> e = new ArrayList<>();

        File folder = new File(AME.getInstance().getDataFolder() + "/rewards");

        List<String> result = new ArrayList<>();

        EventConfigLoader.search(".*\\.yml", folder, result);
        for (String s : result)
        {

            e.add(new AMEReward(s));
        }

        rewards = e;
    }

    public static boolean getPlayerReward(Player p, ItemStack i)
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
                        for (String co : re.rewardCommands1)
                        {
                            if (co.contains("nachricht"))
                            {
                                p.sendMessage(co.replace("nachricht",""));
                            }
                            else
                            {
                                executeCommand(co,p);
                            }
                        }
                        return true;
                    case "2":
                        for (String co : re.rewardCommands2)
                        {
                            if (co.contains("nachricht"))
                            {
                                p.sendMessage(co.replace("nachricht",""));
                            }
                            else
                            {
                                executeCommand(co,p);
                            }
                        }
                        return true;
                    case "3":
                        for (String co : re.rewardCommands3)
                        {
                            if (co.contains("nachricht"))
                            {
                                p.sendMessage(co.replace("nachricht",""));
                            }
                            else
                            {
                                executeCommand(co,p);
                            }
                        }
                        return true;
                    case "4-10":
                        for (String co : re.rewardCommands410)
                        {
                            if (co.contains("nachricht"))
                            {
                                p.sendMessage(co.replace("nachricht",""));
                            }
                            else
                            {
                                executeCommand(co,p);
                            }
                        }
                        return true;
                    case ">10":
                        for (String co : re.rewardCommands11)
                        {
                            if (co.contains("nachricht"))
                            {
                                p.sendMessage(co.replace("nachricht",""));
                            }
                            else
                            {
                                executeCommand(co,p);
                            }
                        }
                        return true;
                }
            }
        }
        return false;
    }

    private static void executeCommand(String co, Player p)
    {
        try
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), co.replace("%player%", p.getName()));
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}
