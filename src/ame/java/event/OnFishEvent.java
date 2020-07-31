package ame.java.event;

import ame.java.ameevent.AMEEventManager;
import ame.java.ameevent.EventTyp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class OnFishEvent implements Listener
{
    @EventHandler
    public void onFishEvent(final PlayerFishEvent event)
    {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH)
        {
            if (AMEEventManager.getInstance().eventactive && AMEEventManager.getInstance().getEventType() == EventTyp.fishing)
            {

                AMEEventManager.getInstance().addCountFishEvent(event.getPlayer(),1);
            }
        }
    }
}