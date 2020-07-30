package ame.java.ameevent;

import ame.java.AME;
import ame.java.config.EventConfigLoader;
import ame.java.lang.LanguageManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.DoubleBlockFinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class AMEEventManager
{
    public AMEEvent[] aMEEvents;

    private AMEEvent activeEvent;

    static AMEEventManager instance;

    public AMEEventManager()
    {
        instance = this;
    }

    public boolean eventactive = false;

    public static AMEEventManager getInstance()
    {
        return instance;
    }

    private int timertask;
    private int timerrepeatleft = 3;

    private int autoeventtask;
    private int mintime = 40;
    private int maxtime = 80;

    private int eventsstart = 0;
    private int eventends = 0;

    private boolean eventsperi;

    private final DecimalFormat df = new DecimalFormat("#.##");

    public void initEvents()
    {
        aMEEvents = EventConfigLoader.loadEvents();
        mintime = AME.getInstance().getConfig().getInt("general.mintime");
        maxtime = AME.getInstance().getConfig().getInt("general.maxtime");
        eventsstart = AME.getInstance().getConfig().getInt("general.eventsstart");
        eventends = AME.getInstance().getConfig().getInt("general.eventsend");
        eventsperi = AME.getInstance().getConfig().getBoolean("general.period");
    }

    public static ItemStack createItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public void startEvent()
    {
        if (!eventactive && aMEEvents.length > 0)
        {
            EventStartTyp time = isDay();
            boolean eventfind = false;
            Random generator = new Random();
            int randomNumber = 0;
            if (eventsperi)
            {
                Date date = new Date();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                if (calendar.get(Calendar.HOUR_OF_DAY) >= eventsstart && calendar.get(Calendar.HOUR_OF_DAY)<=eventends)
                {

                    while (!eventfind)
                    {
                        randomNumber = generator.nextInt(aMEEvents.length);

                        if (aMEEvents[randomNumber].startTyp == time || aMEEvents[randomNumber].startTyp == EventStartTyp.all)
                        {
                            start(aMEEvents[randomNumber]);
                            eventfind = true;
                        }
                    }

                }
                else
                {
                    Bukkit.getScheduler().cancelTask(autoeventtask);
                    startTimer();
                }
            }
            else
            {
                while (!eventfind)
                {
                    randomNumber = generator.nextInt(aMEEvents.length);

                    if (aMEEvents[randomNumber].startTyp == time || aMEEvents[randomNumber].startTyp == EventStartTyp.all)
                    {
                        start(aMEEvents[randomNumber]);
                        eventfind = true;
                    }
                }
            }
        }
    }

    public EventStartTyp isDay()
    {

        long time = Bukkit.getWorlds().get(0).getTime();
        if (time < 12300)
        {
            return EventStartTyp.day;
        }
        else
        {
            return EventStartTyp.night;
        }
    }

    public void startEvent(String name)
    {
        if (!eventactive)
        {
            for (AMEEvent e : aMEEvents)
            {
                if (e.name.equals(name))
                {
                    start(e);
                    return;
                }
            }
        }
    }

    private void start(AMEEvent e)
    {
        eventactive = true;
        activeEvent = e;
        Bukkit.getScheduler().cancelTask(autoeventtask);
        Bukkit.broadcastMessage("§6"+e.name + " " + LanguageManager.getInstance().starttext);
        for (String msg : e.desc)
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.isOnline())
                {
                    p.sendMessage(msg);
                }
            }
        }
        Bukkit.broadcastMessage(LanguageManager.getInstance().eventduration + " §6" + e.time);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), () -> stopEvent(), (long) ((e.time * 60) * 20));
        timertask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AME.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                String formatted = df.format(((e.time) / 3) * timerrepeatleft);
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if (p.isOnline())
                    {
                        p.sendMessage(LanguageManager.getInstance().eventminleft + " §6" + formatted + " min!");
                    }
                }
                timerrepeatleft--;
            }
        }, 0L, (long) ((e.time * 60) * 20) / 3);
    }

    public void stopEvent()
    {
        Bukkit.getScheduler().cancelTask(timertask);
        timerrepeatleft = 3;
        activeEvent.getPlayerRewards(sortByValues(activeEvent.count));
        Bukkit.broadcastMessage("§6"+activeEvent.name + " " + LanguageManager.getInstance().eventendtext);
        activeEvent.count.clear();
        eventactive = false;
        activeEvent = null;
        startTimer();
    }

    private static HashMap sortByValues(HashMap map)
    {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public void addCountKillEvent(Player p, EntityType typ, int value)
    {
        if (activeEvent.countentity.contains(typ))
        {
            if (activeEvent.count.containsKey(p))
            {
                activeEvent.count.put(p, activeEvent.count.get(p) + value);
            }
            else
            {
                activeEvent.count.put(p, value);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(LanguageManager.getInstance().progresstext + " §6" + activeEvent.count.get(p)));
        }
    }

    public void addCountHarvestEvent(Player p, Material typ, int value)
    {
        if (activeEvent.countblock.contains(typ))
        {
            if (activeEvent.count.containsKey(p))
            {
                activeEvent.count.put(p, activeEvent.count.get(p) + value);
            }
            else
            {
                activeEvent.count.put(p, value);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(LanguageManager.getInstance().progresstext + " §6" + activeEvent.count.get(p)));
        }
    }

    public void addCountFishEvent(Player p, int value)
    {
        if (activeEvent.count.containsKey(p))
        {
            activeEvent.count.put(p, activeEvent.count.get(p) + value);
        }
        else
        {
            activeEvent.count.put(p, value);
        }
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(LanguageManager.getInstance().progresstext + " §6" + activeEvent.count.get(p)));
    }

    public EventTyp getEventType()
    {
        return activeEvent.type;
    }

    public void startTimer()
    {
        Random r = new Random();
        int randomNumber = r.nextInt(maxtime - mintime) + mintime;
        autoeventtask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), () -> startEvent(), (long) ((randomNumber * 60) * 20));
    }
}
