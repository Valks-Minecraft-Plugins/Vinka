package com.vinka.dungeons;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepTeleport implements Listener {
	@EventHandler
	private void bedEnterEvent(PlayerBedLeaveEvent e) {
		Location dungeonLocation = new Location(e.getPlayer().getWorld(), -263, 64, -150);
		e.getPlayer().teleport(dungeonLocation);
	}
}
