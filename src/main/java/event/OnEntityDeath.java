package main.java.event;

import main.java.ameevent.AMEEventManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityDeath implements Listener
{
    @EventHandler
    public void onDeath(final EntityDeathEvent event)
    {
        if (AMEEventManager.getInstance().eventactive)
        {

        }
    }
}
