package com.vinka.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vinka.events.CustomPlayerRespawnEvent;
import com.vinka.utils.Utils;

public class CustomPlayerRespawnEventListener implements Listener {
	@EventHandler
	private void onPlayerRespawn(CustomPlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Utils.updateHealth(p);
	}
}
