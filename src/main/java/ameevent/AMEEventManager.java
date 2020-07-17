package main.java.ameevent;

import main.java.config.EventConfigLoader;

public class AMEEventManager
{
   private AMEEvent[] AMEEvents;

    static AMEEventManager instance;

    public AMEEventManager()
    {
        instance = this;
    }

    public boolean eventactive;

    public static AMEEventManager getInstance()
    {
        return instance;
    }

    public void initEvents()
   {
       AMEEvents = EventConfigLoader.loadEvents();
   }
}
