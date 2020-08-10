package ame.java.ameevent;

import ame.java.AME;
import ame.java.config.EventConfigLoader;
import ame.java.lang.LanguageManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    private AMEEvent lastEvent;

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
    private int timerrepeatleft = 2;

    private int autoeventtask;
    private int mintime = 40;
    private int maxtime = 80;

    private int eventsstart = 0;
    private int eventends = 0;

    private boolean eventsperi;

    private boolean eventminplayer;

    private int eventminplayervalue;

    private double remainingtimeloop;

    private String[] toplist = new String[10];

    private final DecimalFormat df = new DecimalFormat("#.##");

    public void initEvents()
    {
        aMEEvents = EventConfigLoader.loadEvents();
        mintime = AME.getInstance().getConfig().getInt("general.mintime");
        maxtime = AME.getInstance().getConfig().getInt("general.maxtime");
        eventsstart = AME.getInstance().getConfig().getInt("general.eventsstart");
        eventends = AME.getInstance().getConfig().getInt("general.eventsend");
        eventsperi = AME.getInstance().getConfig().getBoolean("general.period");
        eventminplayervalue = AME.getInstance().getConfig().getInt("general.eventminplayervalue");
        eventminplayer = AME.getInstance().getConfig().getBoolean("general.eventminplayer");
        remainingtimeloop = AME.getInstance().getConfig().getDouble("general.remainingtimeloop");
        lastEvent = aMEEvents[0];
        Arrays.fill(toplist, "---");
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
            boolean eventCanStart = true;

            if (eventsperi)
            {
                Date date = new Date();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                if (calendar.get(Calendar.HOUR_OF_DAY) < eventsstart && calendar.get(Calendar.HOUR_OF_DAY) > eventends)
                {
                    eventCanStart = false;
                }
            }

            if (eventminplayer && eventCanStart)
            {
                if (Bukkit.getOnlinePlayers().size() < eventminplayervalue)
                {
                    eventCanStart = false;
                }
            }

            if (eventCanStart)
            {
                start(findEvent());
            }
            else
            {
                Bukkit.getScheduler().cancelTask(autoeventtask);
                startTimer();
            }
        }
    }

    private AMEEvent findEvent()
    {
        EventStartTyp time = isDay();
        Random generator = new Random();
        int randomNumber = 0;
        while (true)
        {
            randomNumber = generator.nextInt(aMEEvents.length);

            if ((aMEEvents[randomNumber].startTyp == time || aMEEvents[randomNumber].startTyp == EventStartTyp.all) && !aMEEvents[randomNumber].name.equals(lastEvent.name))
            {
                lastEvent = aMEEvents[randomNumber];
                return aMEEvents[randomNumber];
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
                if (e.name.replaceAll("\\s", "").equals(name))
                {
                    start(e);
                    return;
                }
            }
        }
    }

    private String getTimeString(double time)
    {
        double minutes = (time % 3600) / 60;
        double seconds = time % 60;
        String t;
        String sec;
        if (seconds < 10)
        {
            sec = "0" + +(int) seconds;
        }
        else
        {
            sec = (int) seconds + "";
        }
        if (minutes >= 1)
        {

            t = (int) minutes + ":" + sec + LanguageManager.getInstance().mintext;
            ;
        }
        else
        {
            t = sec + LanguageManager.getInstance().sectext;
        }
        return t;
    }

    private void start(AMEEvent e)
    {
        eventactive = true;
        activeEvent = e;
        timeleft = e.time * 60;
        Bukkit.getScheduler().cancelTask(autoeventtask);
        sendMessage(LanguageManager.getInstance().starttext, null);
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
        sendMessage(LanguageManager.getInstance().eventduration.replace("%time%", getTimeString(e.time * 60) + ""), null);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), () -> stopEvent(), (long) ((e.time * 60) * 20));
        if (e.time * 60 < remainingtimeloop)
        {
            timertask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AME.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    sendMessage(LanguageManager.getInstance().eventminleft.replace("%timeleft%", (activeEvent.time / 2) * 60 + ""), null);
                }
            }, (long) (e.time * 60 / 2) * 20, (long) (e.time * 60 / 2) * 20);
        }
        else
        {
            timertask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AME.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    timeleft -= remainingtimeloop;
                    sendMessage(LanguageManager.getInstance().eventminleft.replace("%timeleft%", getTimeString(timeleft)), null);
                }
            }, (long) remainingtimeloop * 20, (long) remainingtimeloop * 20);
        }

    }

    private double timeleft;

    public void stopEvent()
    {
        Bukkit.getScheduler().cancelTask(timertask);
        timerrepeatleft = 2;
        HashMap<Player, Integer> list = sortByValues(activeEvent.count);
        if (list.size() > 0)
        {
            Object[] p = list.keySet().toArray();
            for (int i = 0; i < 10; i++)
            {
                if (p.length > i)
                {
                    toplist[i] = ((Player) p[i]).getName();
                }
                else
                {
                    toplist[i] = "---";
                }
            }
        }
        activeEvent.getPlayerRewards(list);
        sendMessage(LanguageManager.getInstance().eventendtext,null);
        activeEvent.count.clear();
        eventactive = false;
        activeEvent = null;
        if (AME.getInstance().getConfig().getBoolean("general.active"))
        {
            startTimer();
        }
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

    String replaceEventPlaceHolder(String msg)
    {
        return msg.replace("%eventname%", activeEvent.name);
    }

    public void addCountKillEvent(Player p, EntityType typ, int value)
    {
        if (activeEvent.countEntities.contains(typ))
        {
            if (activeEvent.count.containsKey(p))
            {
                activeEvent.count.put(p, activeEvent.count.get(p) + value);
            }
            else
            {
                activeEvent.count.put(p, value);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progresstext) + "" + activeEvent.count.get(p)));
        }
    }

    public void addCountHarvestEvent(Player p, Material typ, int value)
    {
        if (activeEvent.countBlocks.contains(typ))
        {
            if (activeEvent.count.containsKey(p))
            {
                activeEvent.count.put(p, activeEvent.count.get(p) + value);
            }
            else
            {
                activeEvent.count.put(p, value);
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progresstext) + "" + activeEvent.count.get(p)));
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progresstext) + "" + activeEvent.count.get(p)));
    }

    public EventTyp getEventType()
    {
        return activeEvent.type;
    }

    public void startTimer()
    {
        Random r = new Random();
        int randomNumber = r.nextInt(maxtime - mintime) + mintime;
        autoeventtask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), this::startEvent, ((randomNumber * 60) * 20));
    }

    public void getTopList(Player player)
    {
        for (String t : LanguageManager.getInstance().toplist.replace("%eventname%", lastEvent.name).split("/n"))
        {
            player.sendMessage(t);
        }
        for (int i = 0; i < 10; i++)
        {
            player.sendMessage(LanguageManager.getInstance().toplistplaces[i].replace("%p" + (i + 1) + "%", toplist[i]));
        }
    }

    public void sendMessage(String msg, Player p)
    {
        msg = replaceEventPlaceHolder(msg);
        String[] tmp = msg.split("/n");

        if (p == null)
        {
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                for (String s : tmp)
                {
                    pl.sendMessage(s);
                }
            }
        }
        else
        {
            for (String s : tmp)
            {
                p.sendMessage(s);
            }
        }
    }
}