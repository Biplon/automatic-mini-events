package ame.java.config;

import ame.java.AME;
import ame.java.ameevent.AMEEvent;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventConfigLoader
{
    public static AMEEvent[] loadEvents()
    {
        List<AMEEvent> e = new ArrayList<>();

        File folder = new File(AME.getInstance().getDataFolder() + "/events");

        List<String> result = new ArrayList<>();

        search(".*\\.yml", folder, result);

        for (String s : result)
        {
            AMEEvent event = new AMEEvent(s);
            e.add(event);
        }

        return e.toArray(new AMEEvent[0]);
    }

    public static void search(final String pattern, final File folder, List<String> result)
    {
        try
        {
            for (final File f : Objects.requireNonNull(folder.listFiles()))
            {
                if (f.isDirectory())
                {
                    search(pattern, f, result);
                }

                if (f.isFile())
                {
                    if (f.getName().matches(pattern))
                    {
                        result.add(f.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}
