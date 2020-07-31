package ame.java.ameevent;

import ame.java.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class AMEEvent
{
    public String name;

    public String[] desc;

    public EventTyp type;

    public double time;

    public EventStartTyp startTyp;

    public  List<Material> countBlocks = new ArrayList<>();

    public List<EntityType> countEntities = new ArrayList<>();

    public HashMap<Player, Integer> count = new HashMap<>();

    public ItemStack[] rewards = new ItemStack[5];

    public AMEEvent(String path)
    {
        try
        {
            File f = new File(path);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            name = cfg.getString("general.name");
            desc = Objects.requireNonNull(cfg.getString("general.desc")).split("/n");
            type = EventTyp.valueOf(cfg.getString("general.typ"));
            time = cfg.getDouble("general.time");
            startTyp = EventStartTyp.valueOf(cfg.getString("general.starttyp"));
            if (type == EventTyp.killenemy)
            {
                boolean isnext = true;
                int counter = 0;
                while (isnext)
                {
                    if (cfg.getString("counter." + counter + ".name") != null)
                    {
                        countEntities.add(EntityType.valueOf(cfg.getString("counter." + counter + ".name")));
                        counter++;
                    }
                    else
                    {
                        isnext = false;
                    }
                }
            }
            else if (type == EventTyp.harvest)
            {
                boolean isnext = true;
                int counter = 0;
                while (isnext)
                {
                    if (cfg.getString("counter." + counter + ".name") != null)
                    {
                        countBlocks.add(Material.valueOf(cfg.getString("counter." + counter + ".name")));
                        counter++;
                    }
                    else
                    {
                        isnext = false;
                    }
                }
            }
            /*
            else if (type == EventTyp.fishing)
            {

            }
             */
            createRewards();
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().warning(ex.getMessage());
        }
    }

    public void createRewards()
    {
        String place;
        for (int i = 0; i < 5; i++)
        {
            if (i < 3)
            {
                place = "#" + (i + 1);
            }
            else if (i == 3)
            {
                place = "#4-10";
            }
            else
            {
                place = "#>10";
            }
            rewards[i] = AMEEventManager.createItem(Material.PAPER, LanguageManager.getInstance().rewardBagName, name, place);
        }
    }

    public void getPlayerRewards(HashMap<Player, Integer> player)
    {
        Map<Integer, ItemStack> map;
        Object[] p = player.keySet().toArray();
        for (int i = 0; i < p.length; i++)
        {
            if (i == 0)
            {
                map = ((Player) p[i]).getInventory().addItem(rewards[i]);
                Bukkit.broadcastMessage(LanguageManager.getInstance().eventwinneris+ " §6" +  ((Player) p[i]).getName());
            }
            else if (i == 1)
            {
                map = ((Player) p[i]).getInventory().addItem(rewards[i]);
                Bukkit.broadcastMessage(LanguageManager.getInstance().eventwinner2is+ " §6" +  ((Player) p[i]).getName());
            }
            else if (i == 2)
            {
                map = ((Player) p[i]).getInventory().addItem(rewards[i]);
                Bukkit.broadcastMessage(LanguageManager.getInstance().eventwinner3is+ " §6" +  ((Player) p[i]).getName());
            }
            else if (i < 10)
            {
                map = ((Player) p[i]).getInventory().addItem(rewards[3]);
            }
            else
            {
                map = ((Player) p[i]).getInventory().addItem(rewards[4]);
            }
            ((Player) p[i]).sendMessage(LanguageManager.getInstance().eventgetplacetext+ " §6"+ (i +1));
            ((Player) p[i]).sendMessage(LanguageManager.getInstance().eventgetrewardtext+ " §6"+ LanguageManager.getInstance().rewardBagName);
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    ((Player) p[i]).getWorld().dropItemNaturally(((Player) p[i]).getLocation(), item);
                }
                map.clear();
            }
        }
    }
}
