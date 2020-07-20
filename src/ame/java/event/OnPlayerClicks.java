package ame.java.event;

import ame.java.Reward.AMERewardManager;
import ame.java.lang.LanguageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class OnPlayerClicks implements Listener
{
    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
        {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
            {
                if (Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getDisplayName().equals(LanguageManager.getInstance().rewardBagName))
                {
                    if (AMERewardManager.getInstance().getPlayerReward(player, event.getItem()))
                    {
                        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
