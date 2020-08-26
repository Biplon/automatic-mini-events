package ame.java.ameevent;

import ame.java.AME;
import ame.java.config.EventConfigLoader;
import ame.java.lang.LanguageManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public boolean eventActive = false;

    public static AMEEventManager getInstance()
    {
        return instance;
    }

    private int timerTask;

    private int autoEventTask;
    private int minTime = 40;
    private int maxTime = 80;

    private int eventsStart = 0;
    private int eventEnds = 0;
    private boolean eventsInPeriod;


    private boolean eventMinPlayer;
    private int eventMinPlayerValue;


    private double remainingTimeLoop;

    private double timeLeft;
    
    private final String[] topList = new String[10];

    public void initEvents()
    {
        aMEEvents = EventConfigLoader.loadEvents();
        minTime = AME.getInstance().getConfig().getInt("general.mintime");
        maxTime = AME.getInstance().getConfig().getInt("general.maxtime");
        eventsStart = AME.getInstance().getConfig().getInt("general.eventsstart");
        eventEnds = AME.getInstance().getConfig().getInt("general.eventsend");
        eventsInPeriod = AME.getInstance().getConfig().getBoolean("general.period");
        eventMinPlayerValue = AME.getInstance().getConfig().getInt("general.eventminplayervalue");
        eventMinPlayer = AME.getInstance().getConfig().getBoolean("general.eventminplayer");
        remainingTimeLoop = AME.getInstance().getConfig().getDouble("general.remainingtimeloop");
        lastEvent = aMEEvents[0];
        Arrays.fill(topList, "---");
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
        if (!eventActive && aMEEvents.length > 0)
        {
            boolean eventCanStart = true;

            if (eventsInPeriod)
            {
                Date date = new Date();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(date);
                if (calendar.get(Calendar.HOUR_OF_DAY) < eventsStart && calendar.get(Calendar.HOUR_OF_DAY) > eventEnds)
                {
                    eventCanStart = false;
                }
            }

            if (eventMinPlayer && eventCanStart)
            {
                if (Bukkit.getOnlinePlayers().size() < eventMinPlayerValue)
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
                Bukkit.getScheduler().cancelTask(autoEventTask);
                if (AME.getInstance().getConfig().getBoolean("general.active"))
                {
                    startTimer();
                }
            }
        }
    }

    private AMEEvent findEvent()
    {
        EventStartTyp time = isDay();
        Random generator = new Random();
        int randomNumber;
        while (true)
        {
            randomNumber = generator.nextInt(aMEEvents.length);

            if ((aMEEvents[randomNumber].startTyp == time || aMEEvents[randomNumber].startTyp == EventStartTyp.all) && !aMEEvents[randomNumber].name.equals(lastEvent.name))
            {
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
        if (!eventActive)
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
            t = (int) minutes + ":" + sec + LanguageManager.getInstance().minText;
        }
        else
        {
            t = sec + LanguageManager.getInstance().secText;
        }
        return t;
    }

    private void start(AMEEvent e)
    {
        eventActive = true;
        activeEvent = e;
        timeLeft = e.time * 60;
        Bukkit.getScheduler().cancelTask(autoEventTask);
        sendMessage(LanguageManager.getInstance().startText, null);
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
        sendMessage(LanguageManager.getInstance().eventDuration.replace("%time%", getTimeString(e.time * 60) + ""), null);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), this::stopEvent, (long) ((e.time * 60) * 20));
        if (e.time * 60 < remainingTimeLoop)
        {
            timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AME.getInstance(), () ->
            {
                for (Player u :  activeEvent.count.keySet())
                {
                    if (u.isOnline())
                    {
                        sendMessage(LanguageManager.getInstance().eventMinLeft.replace("%timeleft%", (activeEvent.time / 2) * 60 + ""), u);
                    }
                }
            }, (long) (e.time * 60 / 2) * 20, (long) (e.time * 60 / 2) * 20);
        }
        else
        {
            timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AME.getInstance(), () ->
            {
                timeLeft -= remainingTimeLoop;
                for (Player u :  activeEvent.count.keySet())
                {
                    if (u.isOnline())
                    {
                        sendMessage(LanguageManager.getInstance().eventMinLeft.replace("%timeleft%", getTimeString(timeLeft)), u);
                    }
                }
            }, (long) remainingTimeLoop * 20, (long) remainingTimeLoop * 20);

            Bukkit.getScheduler().runTaskLater(AME.getInstance(), () ->
            {
                for (Player u :  activeEvent.count.keySet())
                {
                    if (u.isOnline())
                    {
                        sendMessage(LanguageManager.getInstance().eventMinLeft.replace("%timeleft%", getTimeString(60)), u);
                    }
                }
            }, (long) (timeLeft -60) * 20);
        }
    }

    public void stopEvent()
    {
        Bukkit.getScheduler().cancelTask(timerTask);
        for (Player u :  activeEvent.count.keySet())
        {
            if (u.isOnline())
            {
                sendMessage(LanguageManager.getInstance().eventEndText, u);
            }
        }
        HashMap<Player, Integer> list = sortByValues(activeEvent.count);
        if (list.size() > 0)
        {
            Object[] p = list.keySet().toArray();
            for (int i = 0; i < 10; i++)
            {
                if (p.length > i)
                {
                    topList[i] = ((Player) p[i]).getName();
                }
                else
                {
                    topList[i] = "---";
                }
            }
        }
        else
        {
            Arrays.fill(topList, "---");
        }
        activeEvent.getPlayerRewards(list);
        activeEvent.count.clear();
        lastEvent = activeEvent;
        eventActive = false;
        activeEvent = null;
        if (AME.getInstance().getConfig().getBoolean("general.active"))
        {
            startTimer();
        }
    }

    private static HashMap sortByValues(HashMap map)
    {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue()));

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
        if (activeEvent != null)
        {
            return msg.replace("%eventname%", activeEvent.name);
        }
        else
        {
            return msg;
        }
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
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progressText) + "" + activeEvent.count.get(p)));
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
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progressText) + "" + activeEvent.count.get(p)));
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(replaceEventPlaceHolder(LanguageManager.getInstance().progressText) + "" + activeEvent.count.get(p)));
    }

    public EventTyp getEventType()
    {
        return activeEvent.type;
    }

    public void startTimer()
    {
        Random r = new Random();
        int randomNumber = r.nextInt(maxTime - minTime) + minTime;
        autoEventTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AME.getInstance(), this::startEvent, ((randomNumber * 60) * 20));
    }

    public void getTopList(Player player)
    {
        for (String t : LanguageManager.getInstance().topList.replace("%eventname%", lastEvent.name).split("/n"))
        {
            player.sendMessage(t);
        }
        for (int i = 0; i < 10; i++)
        {
            player.sendMessage(LanguageManager.getInstance().topListPlaces[i].replace("%p" + (i + 1) + "%", topList[i]));
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

    public void showPlayerEvent(Player player)
    {
        if(activeEvent != null)
        {
            sendMessage(activeEvent.name,player);
            for (String msg : activeEvent.desc)
            {
                sendMessage(msg,player);
            }
            sendMessage(LanguageManager.getInstance().eventMinLeft.replace("%timeleft%", getTimeString(timeLeft)),player);
        }
        else
        {
            sendMessage(LanguageManager.getInstance().noEventRunning,player);
        }
    }
}