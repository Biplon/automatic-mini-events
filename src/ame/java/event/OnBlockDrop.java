package ame.java.event;

import ame.java.ameevent.AMEEventManager;
import ame.java.ameevent.EventTyp;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;

public class OnBlockDrop implements Listener
{
    @EventHandler
    public void onBlockDrop(final BlockDropItemEvent event)
    {


        if (AMEEventManager.getInstance().eventActive && AMEEventManager.getInstance().getEventType() == EventTyp.harvest)
        {
            if (!event.getBlock().hasMetadata("fkbsplayer"))
            {
                if (event.getBlockState().getData() instanceof Crops)
                {
                    Crops state = (Crops) event.getBlockState().getData();
                    if (state.getState() == CropState.RIPE)
                    {
                        AMEEventManager.getInstance().addCountHarvestEvent(event.getPlayer(), event.getBlockState().getType(), 1);
                    }
                }
                else if (event.getBlockState().getData() instanceof NetherWarts)
                {
                    NetherWarts state = (NetherWarts) event.getBlockState().getData();
                    if (state.getState() == NetherWartsState.RIPE)
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
}
