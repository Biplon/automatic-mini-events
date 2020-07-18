package main.java.ameevent;

import main.java.config.EventConfigLoader;
import main.java.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AMEEventManager
{
    private AMEEvent[] aMEEvents;

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

    public void initEvents()
    {
        aMEEvents = EventConfigLoader.loadEvents();
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
        if (!eventactive)
        {
            Random generator = new Random();
            int randomNumber = generator.nextInt(aMEEvents.length - 1);
            start(aMEEvents[randomNumber]);
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
        Bukkit.broadcastMessage(e.name + LanguageManager.getInstance().starttext);
        for (String msg : e.desc)
        {
            Bukkit.broadcastMessage(msg);
        }
        Bukkit.broadcastMessage(LanguageManager.getInstance().eventduration + " " + e.time);
    }

    public void stopEvent()
    {
        int count = 0;
        HashMap map = sortByValues(activeEvent.count);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            Map.Entry me = (Map.Entry) iterator.next();
            if (me.getKey() != null)
            {
                ((Player)me.getKey()).getInventory().addItem();
            }
            me.getValue();
        }
        eventactive = false;
        activeEvent = null;
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

    public void addCount(Player p, int value)
    {
        if (activeEvent.count.containsKey(p.getUniqueId()))
        {
            activeEvent.count.put(p, value + activeEvent.count.get(p.getUniqueId()));
        }
        else
        {
            activeEvent.count.put(p, value);
        }
    }
}
