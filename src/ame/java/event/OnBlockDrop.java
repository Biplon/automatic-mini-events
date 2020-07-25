package ame.java.event;

import ame.java.ameevent.AMEEventManager;
import ame.java.ameevent.EventTyp;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;

public class OnBlockDrop implements Listener
{
    @EventHandler
    public void OnBlockDrop(final BlockDropItemEvent event)
    {
        if (AMEEventManager.getInstance().eventactive && AMEEventManager.getInstance().getEventType() == EventTyp.harvest)
        {
            AMEEventManager.getInstance().addCountHarvestEvent(event.getPlayer(),event.getBlockState().getType(),1);
        }
    }
}
