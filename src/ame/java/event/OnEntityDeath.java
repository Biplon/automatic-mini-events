package ame.java.event;

import ame.java.ameevent.AMEEventManager;
import ame.java.ameevent.EventTyp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityDeath implements Listener
{
    @EventHandler
    public void onDeath(final EntityDeathEvent event)
    {
        if (AMEEventManager.getInstance().eventActive && AMEEventManager.getInstance().getEventType() == EventTyp.killenemy)
        {
            if (event.getEntity().getKiller() != null)
            {
                AMEEventManager.getInstance().addCountKillEvent(event.getEntity().getKiller(),event.getEntityType(),1);
            }
        }
    }
}
