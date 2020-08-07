package ame.java.event;

import ame.java.ameevent.AMEEventManager;
import ame.java.ameevent.EventTyp;
import org.bukkit.CropState;;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.material.Crops;

public class OnBlockDrop implements Listener
{
    @EventHandler
    public void onBlockDrop(final BlockDropItemEvent event)
    {
        if (AMEEventManager.getInstance().eventactive && AMEEventManager.getInstance().getEventType() == EventTyp.harvest)
        {
            if (event.getBlockState().getData() instanceof Crops)
            {
                Crops state = (Crops) event.getBlockState().getData();
                if (state.getState() == CropState.RIPE)
                {
                    AMEEventManager.getInstance().addCountHarvestEvent(event.getPlayer(), event.getBlockState().getType(), 1);
                }
            }
            else
            {
                AMEEventManager.getInstance().addCountHarvestEvent(event.getPlayer(), event.getBlockState().getType(), 1);
            }

        }
    }
}
