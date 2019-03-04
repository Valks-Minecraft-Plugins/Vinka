package com.vinka.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.valkcore.modules.TextModule;
import com.vinka.events.CustomPlayerRespawnEvent;
import com.vinka.utils.Utils;

public class CustomPlayerRespawnEventListener implements Listener {
	@EventHandler
	private void onPlayerRespawn(CustomPlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Utils.updateHealth(p);
		p.sendMessage(TextModule.color("Type &q/back &wto get back to where you died."));
	}
}
