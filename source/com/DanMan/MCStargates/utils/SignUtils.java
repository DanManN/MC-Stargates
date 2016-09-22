package com.DanMan.MCStargates.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class SignUtils {
	public static BlockFace signFacing(org.bukkit.block.Sign sign) {
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		return signData.getFacing();
	}

	public static org.bukkit.block.Sign signFromBlock(Block block) {
		return ((org.bukkit.block.Sign) block.getState());
	}
}