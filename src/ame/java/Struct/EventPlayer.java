package ame.java.Struct;

import org.bukkit.entity.Player;

public class EventPlayer
{
    private Player p;

    private int count;

    public EventPlayer(Player p, int count)
    {
        this.p = p;
        this.count = count;
    }

    public void addCount(int count)
    {
        this.count += count;
    }

    public Player getPlayer()
    {
        return p;
    }

    public int getCount()
    {
        return count;
    }
}
