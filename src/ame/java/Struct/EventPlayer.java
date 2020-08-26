package ame.java.Struct;

public class EventPlayer
{
    private String p;

    private int count;

    public EventPlayer(String p, int count)
    {
        this.p = p;
        this.count = count;
    }

    public void addCount(int count)
    {
        this.count += count;
    }

    public String getPlayer()
    {
        return p;
    }

    public int getCount()
    {
        return count;
    }
}
