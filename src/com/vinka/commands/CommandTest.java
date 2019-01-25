package com.vinka.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vinka.utils.Schematic;

public class CommandTest implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("test")) {
			if (!sender.isOp()) return true;
			sender.sendMessage("Pong.");
			
			Schematic schem = new Schematic("house_1.schem");
			Player p = (Player) sender;
			schem.pasteSchematicV2(p.getLocation());
			return true;
		}
		return false;
	}
}
