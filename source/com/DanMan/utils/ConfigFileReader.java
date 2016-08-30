package com.DanMan.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class ConfigFileReader {
	int activationTime = 0;
	Material DHDMaterial = Material.OBSIDIAN;
	Material GateMaterial = Material.OBSIDIAN;
	Material ChevronMaterial = Material.REDSTONE_BLOCK;
	Material ShieldMaterial = Material.STONE;
	int DHD_Distance = 9;
	int KawooshSoundRadius = 20;
	int KawooshSoundVolume = 10;
	int IrisDamage = 18;
	int IrisDestroyInventory = 1;
	String IrisNoTeleport = "false";
	String Language = "en";
	Material RingMaterial = Material.getMaterial(44);
	byte RingMaterial_data = 0;
	Material RingGroundMaterial = Material.SMOOTH_BRICK.getNewData((byte) 3).getItemType();
	byte RingGroundMaterial_data = 3;
	int RingDistance = 2;
	Sound KawooshSound = Sound.ENDERDRAGON_GROWL;

	String InterWorldConnectionCosts = "none";

	int networkTeleportUnallowedPlayers;

	Map<String, String> VALUES = new HashMap();

	public boolean getConfig() {
		try {
			Object localObject1 = null;
			Object localObject4 = null;
			Object localObject3;
			try {
				BufferedReader br = new BufferedReader(new FileReader("plugins/MPStargate/mpgatesConfig.txt"));
				try {
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						String sCurrentLine;
						if (!sCurrentLine.startsWith("#")) {
							String[] a = sCurrentLine.split("#")[0].split(":");
							String key = a[0].trim();
							String value = a[1].trim();

							this.VALUES.put(key, value);
						}
					}
					return true;
				} finally {
					if (br != null)
						br.close();
				}
			} finally {
				if (localObject2 == null)
					localObject3 = localThrowable;
				else if (localObject3 != localThrowable) {
					((Throwable) localObject3).addSuppressed(localThrowable);
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ConfigFileReader() {
		if (getConfig()) {
			if ((this.VALUES.get("activationTime") != null)
					&& (Integer.parseInt((String) this.VALUES.get("activationTime")) >= 0)) {
				this.activationTime = Integer.parseInt((String) this.VALUES.get("activationTime"));
			}

			if (Material.getMaterial((String) this.VALUES.get("DHDMaterial")) != null) {
				if (Material.getMaterial((String) this.VALUES.get("DHDMaterial")).isSolid()) {
					if (!Material.getMaterial((String) this.VALUES.get("DHDMaterial")).hasGravity()) {
						this.DHDMaterial = Material.getMaterial((String) this.VALUES.get("DHDMaterial"));
					}
				}
			} else {
				System.out.println("[WARNING] Wrong Material for DHD in configfile!");
			}

			if (Material.getMaterial((String) this.VALUES.get("GateMaterial")) != null) {
				if (Material.getMaterial((String) this.VALUES.get("GateMaterial")).isSolid()) {
					if (!Material.getMaterial((String) this.VALUES.get("GateMaterial")).hasGravity()) {
						this.GateMaterial = Material.getMaterial((String) this.VALUES.get("GateMaterial"));
					}
				}
			} else {
				System.out.println("[WARNING] Wrong Material for Stargate in configfile!");
			}

			if (Material.getMaterial((String) this.VALUES.get("ChevronMaterial")) != null) {
				if (Material.getMaterial((String) this.VALUES.get("ChevronMaterial")).isSolid()) {
					if (!Material.getMaterial((String) this.VALUES.get("ChevronMaterial")).hasGravity()) {
						this.ChevronMaterial = Material.getMaterial((String) this.VALUES.get("ChevronMaterial"));
					}
				}
			} else {
				System.out.println("[WARNING] Wrong Material for Chevrons in configfile!");
			}

			if ((Material.getMaterial((String) this.VALUES.get(this.ShieldMaterial)) != null)
					&& (Material.getMaterial((String) this.VALUES.get(this.ShieldMaterial)).isSolid())) {
				this.ShieldMaterial = Material.getMaterial((String) this.VALUES.get(this.ShieldMaterial));
			}

			if ((this.VALUES.get("DHDDistance") != null)
					&& (Integer.parseInt((String) this.VALUES.get("DHDDistance")) >= 8)) {
				this.DHD_Distance = Integer.parseInt((String) this.VALUES.get("DHDDistance"));
			}

			if ((this.VALUES.get("KawooshSoundRadius") != null)
					&& (Integer.parseInt((String) this.VALUES.get("KawooshSoundRadius")) >= 0)) {
				this.KawooshSoundRadius = Integer.parseInt((String) this.VALUES.get("KawooshSoundRadius"));
			}

			if ((this.VALUES.get("KawooshSoundVolume") != null)
					&& (Integer.parseInt((String) this.VALUES.get("KawooshSoundVolume")) >= 0)) {
				this.KawooshSoundVolume = Integer.parseInt((String) this.VALUES.get("KawooshSoundVolume"));
			}

			if ((this.VALUES.get("IrisDamage") != null)
					&& (Integer.parseInt((String) this.VALUES.get("IrisDamage")) >= 0)) {
				this.IrisDamage = Integer.parseInt((String) this.VALUES.get("IrisDamage"));
			}

			if ((this.VALUES.get("IrisDestroyInventory") != null)
					&& ((Integer.parseInt((String) this.VALUES.get("IrisDestroyInventory")) == 0)
							|| (Integer.parseInt((String) this.VALUES.get("IrisDestroyInventory")) == 1))) {
				this.IrisDestroyInventory = Integer.parseInt((String) this.VALUES.get("IrisDamage"));
			}

			if ((this.VALUES.get("Language") != null) && (((String) this.VALUES.get("Language")).length() > 0)) {
				this.Language = ((String) this.VALUES.get("Language"));
			}

			if ((this.VALUES.get("IrisNoTeleport") != null)
					&& (((String) this.VALUES.get("IrisNoTeleport")).length() > 0)) {
				this.IrisNoTeleport = ((String) this.VALUES.get("IrisNoTeleport"));
			}

			if ((this.VALUES.get("NetworkTeleportUnallowedPlayers") != null)
					&& (Integer.parseInt((String) this.VALUES.get("NetworkTeleportUnallowedPlayers")) >= 0)) {
				this.networkTeleportUnallowedPlayers = Integer
						.parseInt((String) this.VALUES.get("NetworkTeleportUnallowedPlayers"));
			}

			if (this.VALUES.get("RingMaterial") != null) {
				if (((String) this.VALUES.get("RingMaterial")).length() > 0) {
					String[] s = ((String) this.VALUES.get("RingMaterial")).split(",");
					if (s.length > 0) {
						this.RingMaterial = Material.getMaterial(Integer.parseInt(s[0]));
						this.RingMaterial_data = Byte.parseByte(s[1]);
					}
				}
			}

			if ((this.VALUES.get("RingGroundMaterial") != null)
					&& (((String) this.VALUES.get("RingGroundMaterial")).length() > 0)) {
				String[] value = ((String) this.VALUES.get("RingGroundMaterial")).split(",");
				if (value.length > 0) {
					this.RingGroundMaterial = Material.getMaterial(Integer.parseInt(value[0]));
					this.RingGroundMaterial_data = Byte.parseByte(value[1]);
				}
			}

			if (this.VALUES.get("RingsDistance") != null) {
				if ((Integer.parseInt((String) this.VALUES.get("RingDistance")) > -2)
						|| (Integer.parseInt((String) this.VALUES.get("RingDistance")) <= -5)) {
					this.RingDistance = Integer.parseInt((String) this.VALUES.get("RingDistance"));
				}
			}

			if (this.VALUES.get("InterWorldConnectionCosts") != null) {
				String s = (String) this.VALUES.get("InterWorldConnectionCosts");
				String[] s2 = s.split(",");
				if (s2.length == 2) {
					if ((Integer.parseInt(s2[0]) >= 0) && (Integer.parseInt(s2[1]) >= 0)) {
						this.InterWorldConnectionCosts = ((String) this.VALUES.get("InterWorldConnectionCosts"));
					}
				} else {
					this.InterWorldConnectionCosts = "none";
				}
			}

			if ((this.VALUES.get("KawooshSound") != null)
					&& (Sound.valueOf((String) this.VALUES.get("KawooshSound")) != null)) {
				this.KawooshSound = Sound.valueOf((String) this.VALUES.get("KawooshSound"));
			}
		}
	}
}