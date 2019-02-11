package com.vinka.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class AbsorbSoulsEvent extends Event {
	Player p;
	int souls;
	ItemStack item;
	
	public AbsorbSoulsEvent(Player p, int souls, ItemStack item) {
		this.p = p;
		this.souls = souls;
		this.item = item;
	}
	
	public ItemStack getSoulItem() {
		return item;
	}
	
	public int getSouls() {
		return souls;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
