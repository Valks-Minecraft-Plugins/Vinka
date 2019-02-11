package com.vinka.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadPlayerFiles implements Listener {
	@EventHandler
	public void loadFiles(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		PlayerFiles cm = PlayerFiles.getConfig(p.getUniqueId());
		if (!p.hasPlayedBefore()) {
			FileConfiguration f = cm.getConfig();
			f.set("name", p.getName());
			f.set("uuid", p.getUniqueId().toString());
			f.set("achievement_1", false);
			cm.saveConfig();
		} else {
			FileConfiguration f = cm.getConfig();
			f.set("name", p.getName());
			if (!f.isSet("achievement_1")) f.set("achievement_1", false);
			cm.saveConfig();
		}
	}
}