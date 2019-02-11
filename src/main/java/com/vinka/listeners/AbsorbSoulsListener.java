package com.vinka.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.valkutils.modules.TextModule;
import com.vinka.events.AbsorbSoulsEvent;
import com.vinka.utils.Utils;

public class AbsorbSoulsListener implements Listener {
	@EventHandler
	private void absorbSoulsEvent(AbsorbSoulsEvent e) {
		Player p = e.getPlayer();
		int souls = e.getSouls();
		
		Utils.updateHealth(p);
		
		p.giveExp(souls);
		p.sendMessage(TextModule.color("&7You absorbed &f" + souls + " &7souls."));
		
		e.getSoulItem().setAmount(0);
	}
}
