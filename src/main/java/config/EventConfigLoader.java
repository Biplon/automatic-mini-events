package main.java.config;

import main.java.AME;
import main.java.ameevent.AMEEvent;

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

        if (!folder.exists())
        {
            folder.mkdir();
        }

        List<String> result = new ArrayList<>();

        search(".*\\.yml", folder, result);

        for (String s : result)
        {
            e.add(new AMEEvent());
        }


        return (AMEEvent[]) e.toArray();
    }

    private static void search(final String pattern, final File folder, List<String> result)
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

        }
    }
}
