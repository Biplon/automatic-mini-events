package main.java.ameevent;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AMEEvent
{
    public String name;

    public String[] desc;

    public EventTyp type;

    private Block[] countblock;

    private EntityType[] countentity;

    private HashMap<UUID, Integer> count = new HashMap<>();

    public AMEEvent(String path)
    {
        try
        {
            File f = new File(path);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            name = cfg.getString("general.name");
            desc = cfg.getString("general.desc").split("/n");
            type = EventTyp.valueOf(cfg.getString("general.typ"));
            List<EntityType> types = new ArrayList<>();
            if (type == EventTyp.killenemy)
            {
                boolean isnext = true;
                int counter = 0;
                while (isnext)
                {
                    if (cfg.getString("counter." + counter) != null)
                    {
                       types.add(EntityType.valueOf(cfg.getString("counter.name")));
                    }
                    else
                    {
                        isnext = false;
                    }
                }
                countentity = (EntityType[]) types.toArray();
            }

        }
        catch (Exception ex)
        {
            Bukkit.getLogger().warning(ex.getMessage());
        }
    }
}
