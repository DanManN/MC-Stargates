package com.DanMan.utils;

import org.bukkit.block.Block;

public final class SignUtils {
	public static org.bukkit.block.BlockFace signFacing(org.bukkit.block.Sign sign) {
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		return signData.getFacing();
	}

	public static org.bukkit.block.Sign signFromBlock(Block block) {
		return (org.bukkit.block.Sign) block.getState();
	}
}
