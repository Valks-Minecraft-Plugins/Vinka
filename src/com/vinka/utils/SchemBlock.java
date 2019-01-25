package com.vinka.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SchemBlock {
	private final Block block;
	private final Material material;
	
	public SchemBlock(Block block, Material material) {
		this.block = block;
		this.material = material;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public Material getMaterial() {
		return material;
	}
}
