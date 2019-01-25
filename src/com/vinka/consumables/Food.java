package com.vinka.consumables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class Food implements Listener {
	@EventHandler
	private void consumeEvent(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		
		if (e.getItem().getType() == Material.DRIED_KELP) {
			// 20 ticks is 1 second, 200 ticks is 10 seconds, 600 ticks is 30 seconds
			if (!e.getPlayer().hasPotionEffect(PotionEffectType.HUNGER)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 1)); //60s
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1)); //10s
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1)); //1s
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You just ate something disgusting! You start to feel sick.. (You can continue to eat this without anymore negative effects until the hunger effect goes away)"));
			}
		}
	}
}
