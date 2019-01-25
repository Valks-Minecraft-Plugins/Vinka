package com.vinka.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.vinka.Vinka;

public class Schematic {
	Clipboard clipboard;
	
	public Schematic(String filename) {
		File file = new File(Vinka.vinka.getDataFolder().toString() + "\\schematics\\" + filename);
		
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
		    clipboard = reader.read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pasteSchematicV2(Location loc) {
		List<SchemBlock> blocks = getSchematicData(loc);
		
		new BukkitRunnable() {
			int counter = 0;
			
			@Override
			public void run() {
				SchemBlock schemBlock = blocks.get(counter);
				schemBlock.getBlock().setType(schemBlock.getMaterial());
				
				counter++;
				
				if (counter >= blocks.size()) {
					cancel();
				}
			}
		}.runTaskTimer(Vinka.vinka, 5, 5);
	}
	
	private List<SchemBlock> getSchematicData(Location loc) {
		List<SchemBlock> blocks = new ArrayList<SchemBlock>();
		
		BlockVector3 origin = clipboard.getOrigin();
		
		for (BlockVector3 v : clipboard.getRegion()) {
			Material material = BukkitAdapter.adapt(clipboard.getBlock(v).getBlockType());
			
			int schemX = v.getX() - origin.getX();
			int schemY = v.getY() - origin.getY();
			int schemZ = v.getZ() - origin.getZ();
			
			Block block = loc.getWorld().getBlockAt((int) loc.getX() + schemX, (int) loc.getY() + schemY, (int) loc.getZ() + schemZ);
			
			if (material != Material.AIR) {
				blocks.add(new SchemBlock(block, material));
			}
		}
		
		return blocks;
	}
	
	public void pasteSchematic() {
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(Bukkit.getWorld("world")), -1)) {
			Operation operation = new ClipboardHolder(clipboard)
			        .createPaste(editSession)
			        .to(BlockVector3.at(0, 100, 0))
			        .ignoreAirBlocks(false)
			        .build();
		    try {
				Operations.complete(operation);
			} catch (WorldEditException e) {
				e.printStackTrace();
			}
		}
	}
}
