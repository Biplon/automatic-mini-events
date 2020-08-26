package ame.java.ameevent;

import ame.java.Struct.EventPlayer;
import ame.java.lang.LanguageManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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

    public List<EventPlayer> count = new ArrayList<>();

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
            rewards[i] = AMEEventManager.createItem(Material.PAPER, LanguageManager.getInstance().rewardBagName,LanguageManager.getInstance().rewardBagNameLore1, name, place);
        }
    }

    public void getPlayerRewards(Player[] player)
    {
        Map<Integer, ItemStack> map;
        for (int i = 0; i < player.length; i++)
        {
            if (i == 0)
            {
                map = player[i].getInventory().addItem(rewards[i]);
                TextComponent message = new TextComponent(LanguageManager.getInstance().eventTopListLink);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ametl"));
                ComponentBuilder tmp = new ComponentBuilder();
                tmp.append( AMEEventManager.getInstance().replaceEventPlaceHolder(LanguageManager.getInstance().eventWinnerIs).replace("%p1%",player[i].getName()).replace("%toplist%",""));
                tmp.append(message);
                for (Object pl: player)
                {
                    if (pl != null)
                    {
                        ((Player)pl).spigot().sendMessage(tmp.create());
                    }
                }
            }
            else if (i == 1)
            {
                map = player[i].getInventory().addItem(rewards[i]);
            }
            else if (i == 2)
            {
                map = player[i].getInventory().addItem(rewards[i]);
            }
            else if (i < 10)
            {
                map = player[i].getInventory().addItem(rewards[3]);
            }
            else
            {
                map = player[i].getInventory().addItem(rewards[4]);
            }
            AMEEventManager.getInstance().sendMessage(LanguageManager.getInstance().eventGetPlaceText + " "+ (i +1), player[i]);
            AMEEventManager.getInstance().sendMessage(LanguageManager.getInstance().eventGetRewardText + " "+ LanguageManager.getInstance().rewardBagName ,player[i]);
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    player[i].getWorld().dropItemNaturally(player[i].getLocation(), item);
                }
                map.clear();
            }
        }
    }
}
