package main.java.event;

import main.java.ameevent.AMEEventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Objects;

public class OnEntityDeath implements Listener
{
    @EventHandler
    public void onDeath(final EntityDeathEvent event)
    {
        if (AMEEventManager.getInstance().eventactive)
        {
            if (event.getEntity().getKiller() != null)
            {
                AMEEventManager.getInstance().addCount(event.getEntity().getKiller(),1);
            }
        }
    }
}
