package com.DanMan.MCStargates.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;

public class ConfigFileReader {
	private int activationTime = 0;
	private Material DHDMaterial = Material.OBSIDIAN;
	private Material GateMaterial = Material.OBSIDIAN;
	private Material ChevronMaterial = Material.REDSTONE_BLOCK;
	private Material ShieldMaterial = Material.STONE;
	private int DHD_Distance = 9;
	private int KawooshSoundRadius = 20;
	private int KawooshSoundVolume = 10;
	private int IrisDamage = 18;
	private int IrisDestroyInventory = 1;
	private String IrisNoTeleport = "false";
	private String Language = "en";
	private Material RingMaterial = Material.getMaterial(44);
	private byte RingMaterial_data = 0;
	private Material RingGroundMaterial = Material.SMOOTH_BRICK.getNewData((byte) 3).getItemType();
	private byte RingGroundMaterial_data = 3;
	private int RingDistance = 2;
	private Sound KawooshSound = Sound.ENTITY_ENDERDRAGON_GROWL;

	private String InterWorldConnectionCosts = "none";

	private int networkTeleportUnallowedPlayers;

	private Map<String, String> VALUES = new HashMap<String, String>();

	public boolean getConfig() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("plugins/MPStargate/mpgatesConfig.txt"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.startsWith("#")) {
					String[] a = sCurrentLine.split("#")[0].split(":");
					String key = a[0].trim();
					String value = a[1].trim();
					this.VALUES.put(key, value);
				}
			}
			br.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(int activationTime) {
		this.activationTime = activationTime;
	}

	public Material getDHDMaterial() {
		return DHDMaterial;
	}

	public void setDHDMaterial(Material dHDMaterial) {
		DHDMaterial = dHDMaterial;
	}

	public Material getGateMaterial() {
		return GateMaterial;
	}

	public void setGateMaterial(Material gateMaterial) {
		GateMaterial = gateMaterial;
	}

	public Material getChevronMaterial() {
		return ChevronMaterial;
	}

	public void setChevronMaterial(Material chevronMaterial) {
		ChevronMaterial = chevronMaterial;
	}

	public Material getShieldMaterial() {
		return ShieldMaterial;
	}

	public void setShieldMaterial(Material shieldMaterial) {
		ShieldMaterial = shieldMaterial;
	}

	public int getDHD_Distance() {
		return DHD_Distance;
	}

	public void setDHD_Distance(int dHD_Distance) {
		DHD_Distance = dHD_Distance;
	}

	public int getKawooshSoundRadius() {
		return KawooshSoundRadius;
	}

	public void setKawooshSoundRadius(int kawooshSoundRadius) {
		KawooshSoundRadius = kawooshSoundRadius;
	}

	public int getKawooshSoundVolume() {
		return KawooshSoundVolume;
	}

	public void setKawooshSoundVolume(int kawooshSoundVolume) {
		KawooshSoundVolume = kawooshSoundVolume;
	}

	public int getIrisDamage() {
		return IrisDamage;
	}

	public void setIrisDamage(int irisDamage) {
		IrisDamage = irisDamage;
	}

	public int getIrisDestroyInventory() {
		return IrisDestroyInventory;
	}

	public void setIrisDestroyInventory(int irisDestroyInventory) {
		IrisDestroyInventory = irisDestroyInventory;
	}

	public String getIrisNoTeleport() {
		return IrisNoTeleport;
	}

	public void setIrisNoTeleport(String irisNoTeleport) {
		IrisNoTeleport = irisNoTeleport;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public Material getRingMaterial() {
		return RingMaterial;
	}

	public void setRingMaterial(Material ringMaterial) {
		RingMaterial = ringMaterial;
	}

	public byte getRingMaterial_data() {
		return RingMaterial_data;
	}

	public void setRingMaterial_data(byte ringMaterial_data) {
		RingMaterial_data = ringMaterial_data;
	}

	public Material getRingGroundMaterial() {
		return RingGroundMaterial;
	}

	public void setRingGroundMaterial(Material ringGroundMaterial) {
		RingGroundMaterial = ringGroundMaterial;
	}

	public byte getRingGroundMaterial_data() {
		return RingGroundMaterial_data;
	}

	public void setRingGroundMaterial_data(byte ringGroundMaterial_data) {
		RingGroundMaterial_data = ringGroundMaterial_data;
	}

	public int getRingDistance() {
		return RingDistance;
	}

	public void setRingDistance(int ringDistance) {
		RingDistance = ringDistance;
	}

	public Sound getKawooshSound() {
		return KawooshSound;
	}

	public void setKawooshSound(Sound kawooshSound) {
		KawooshSound = kawooshSound;
	}

	public String getInterWorldConnectionCosts() {
		return InterWorldConnectionCosts;
	}

	public void setInterWorldConnectionCosts(String interWorldConnectionCosts) {
		InterWorldConnectionCosts = interWorldConnectionCosts;
	}

	public int getNetworkTeleportUnallowedPlayers() {
		return networkTeleportUnallowedPlayers;
	}

	public void setNetworkTeleportUnallowedPlayers(int networkTeleportUnallowedPlayers) {
		this.networkTeleportUnallowedPlayers = networkTeleportUnallowedPlayers;
	}

	public Map<String, String> getVALUES() {
		return VALUES;
	}

	public void setVALUES(Map<String, String> vALUES) {
		VALUES = vALUES;
	}

	public ConfigFileReader() {
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