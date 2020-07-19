package ame.java.event;

import ame.java.ameevent.AMEEventManager;
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
            if (event.getEntity().getKiller() != null)
            {
                AMEEventManager.getInstance().addCount(event.getEntity().getKiller(),event.getEntityType(),1);
            }
        }
    }
}
