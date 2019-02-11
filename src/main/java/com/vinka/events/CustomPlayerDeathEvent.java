package com.vinka.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CustomPlayerDeathEvent extends Event {
	Player p;
	DamageCause dc;
	
	public CustomPlayerDeathEvent(Player p, DamageCause dc) {
		this.p = p;
		this.dc = dc;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public DamageCause getCause() {
		return dc;
	}
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
