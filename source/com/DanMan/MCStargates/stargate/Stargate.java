package com.DanMan.MCStargates.stargate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.DanMan.MCStargates.main.MCStargates;
import com.DanMan.MCStargates.utils.StargateFileReader;

public class Stargate implements Listener {
	public static int DHD_DISTANCE = MCStargates.configValues.getDHD_Distance();
	public static Material GATE_MATERIAL = MCStargates.configValues.getGateMaterial();
	public static Material DHD_MATERIAL = MCStargates.configValues.getDHDMaterial();
	public static Material CHEVRON_MATERIAL = MCStargates.configValues.getChevronMaterial();
	public static Material SHIELD_MATERIAL = MCStargates.configValues.getShieldMaterial();

	private String name;
	private Location loc;
	private int worldID = 0;
	private boolean shieldStatus = false;
	private boolean activationStatus = false;
	private String target = null;
	private String direction = null;

	public Vector getPosition() {
		return new Vector(this.loc.getX(), this.loc.getZ(), this.loc.getY());
	}

	public boolean setLocation(Location loc) {
		this.loc = loc;
		this.worldID = Bukkit.getWorlds().indexOf(loc.getWorld());
		return true;
	}

	public boolean activate() {
		if (checkGateShape()) {
			activateChevrons();
			if (!this.shieldStatus) {
				Kawoosh k = new Kawoosh(this);
				k.makeKawoosh();
				playActivationSound();
			}

			this.activationStatus = true;
			StargateFileReader sfr = new StargateFileReader();
			sfr.updateStargate(this);
			return true;
		}
		return false;
	}

	public boolean deactivate() {
		if (!this.shieldStatus) {
			fillGate(Material.AIR);
		}
		deactivateChevrons();
		this.activationStatus = false;
		this.target = "null";
		StargateFileReader sfr = new StargateFileReader();
		sfr.updateStargate(this);
		return true;
	}

	public boolean connectToTarget() {
		if (this.target != null) {
			if (!this.target.equals(this.name)) {
				StargateFileReader sfr = new StargateFileReader();
				Stargate s = sfr.getStargate(this.target);
				if (s != null) {
					if ((!this.activationStatus) && (!s.activationStatus)) {
						if ((checkGateShape()) && (s.checkGateShape())) {
							if (compareNetworkName(s.getNetworkName())) {

								activate();
								updateSign();
								StargateThread sT = new StargateThread(this);
								sT.teleportEntitysThread();
								sT.countForShutdown();

								s.activate();

								return true;
							}

							return false;
						}

						return false;
					}

					return false;
				}

				return false;
			}

			return false;
		}

		return false;
	}

	public String getNetworkName() {
		if (getNetwork() == null) {
			return null;
		}
		return getNetwork().name;
	}

	public boolean compareNetworkName(String str2) {
		String str1 = getNetworkName();
		return str1 == null ? false : str2 == null ? true : str1.equals(str2);
	}

	public boolean stopConnection() {
		if (this.target != null) {
			StargateFileReader sfr = new StargateFileReader();
			Stargate s = sfr.getStargate(this.target);
			if (s != null) {
				s.deactivate();
				deactivate();
				updateSign();
			}
		}
		return false;
	}

	public boolean stopConnectionFromBothSides() {
		StargateFileReader sfr = new StargateFileReader();

		if (this.target == null) {
			for (Stargate s : sfr.getStargateList()) {
				if (s.target.equals(this)) {
					s.stopConnection();
					return true;
				}
			}
		} else {
			stopConnection();
			return true;
		}
		return false;
	}

	public Vector getNormalVector() {
		Vector vector = null;
		if (this.direction.equals("NORTH")) {
			vector = new Vector(0, -1, 0);
		}
		if (this.direction.equals("SOUTH")) {
			vector = new Vector(0, 1, 0);
		}
		if (this.direction.equals("EAST")) {
			vector = new Vector(1, 0, 0);
		}
		if (this.direction.equals("WEST")) {
			vector = new Vector(-1, 0, 0);
		}

		return vector;
	}

	public void makeGateShape(Boolean unbreakable) {
		Location aloc = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ());

		Vector DHDPos = new Vector(aloc.getX(), aloc.getZ(), aloc.getY());
		DHDPos.add(getNormalVector());

		aloc.setX(DHDPos.getX());
		aloc.setY(DHDPos.getZ());
		aloc.setZ(DHDPos.getY());
		Block DHDblock = aloc.getBlock();
		DHDblock.setType(DHD_MATERIAL);
		if (unbreakable.booleanValue()) {
			DHDblock.setMetadata("StargateBlock", new FixedMetadataValue(MCStargates.getInstance(), "true"));

		} else if (DHDblock.hasMetadata("StargateBlock")) {
			DHDblock.removeMetadata("StargateBlock", MCStargates.getInstance());
		}

		ArrayList<Vector> coordinates = getRingCoordinates();
		for (Vector v : coordinates) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ(), v.getY());
			Block block = location.getBlock();

			block.setType(GATE_MATERIAL);
			if (unbreakable.booleanValue()) {
				block.setMetadata("StargateBlock", new FixedMetadataValue(MCStargates.getInstance(), "true"));

			} else if (block.hasMetadata("StargateBlock")) {
				block.removeMetadata("StargateBlock", MCStargates.getInstance());
			}
		}
	}

	public void updateSign() {
		Block b = this.loc.getBlock();
		b.setType(Material.WALL_SIGN);
		Sign sign = (Sign) b.getState();

		if ((this.target != null) && (!this.target.equals("null"))) {
			sign.setLine(2, "->" + this.target);
		} else {
			sign.setLine(2, "");
		}

		if (getNetwork() != null) {
			sign.setLine(3, getNetwork().name);
		} else {
			sign.setLine(3, "");
		}
		sign.update();
	}

	public boolean checkSign() {
		Block b = this.loc.getBlock();
		b.setType(Material.WALL_SIGN);
		Sign sign = (Sign) b.getState();

		if (!sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
			return false;
		}
		if (!sign.getLine(1).equalsIgnoreCase(this.name)) {
			return false;
		}

		return true;
	}

	public boolean checkGateShape() {
		Location loc = this.loc.clone();

		Vector DHDPos = new Vector(loc.getX(), loc.getZ(), loc.getY());
		DHDPos.add(getNormalVector());

		loc.setX(DHDPos.getX());
		loc.setY(DHDPos.getZ());
		loc.setZ(DHDPos.getY());
		Block DHDblock = loc.getBlock();

		if (!DHDblock.getType().equals(DHD_MATERIAL)) {
			return false;
		}

		ArrayList<Vector> gateShapePositions = getRingCoordinates();
		for (Vector v : gateShapePositions) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ(), v.getY());
			Block b = location.getBlock();

			if ((!b.getType().equals(GATE_MATERIAL)) && (!b.getType().equals(CHEVRON_MATERIAL))) {

				return false;
			}
		}
		return true;
	}

	public void activateShield() {
		if (!this.shieldStatus) {
			this.shieldStatus = true;
			StargateFileReader sfr = new StargateFileReader();
			sfr.updateStargate(this);
			fillGate(SHIELD_MATERIAL);
		}
	}

	public void deactivateShield() {
		if (this.shieldStatus) {
			this.shieldStatus = false;
			StargateFileReader sfr = new StargateFileReader();
			sfr.updateStargate(this);
			if (this.activationStatus) {
				fillGate(Material.WATER);
			} else {
				fillGate(Material.AIR);
			}
		}
	}

	public void switchShield() {
		if (!this.shieldStatus) {
			activateShield();
		} else {
			deactivateShield();
		}
	}

	public void fillGate(Material m) {
		ArrayList<Vector> array = getInsideCoordinates();
		for (int i = 0; i < array.size(); i++) {
			Vector v = (Vector) array.get(i);
			Location location = new Location((World) Bukkit.getWorlds().get(this.worldID), v.getX(), v.getZ(),
					v.getY());
			Block block = location.getBlock();
			block.setType(m);
			if (m.equals(Material.WATER)) {
				block.setMetadata("PortalWater", new FixedMetadataValue(MCStargates.getInstance(), "true"));

			} else if (block.hasMetadata("PortalWater")) {
				block.removeMetadata("PortalWater", MCStargates.getInstance());
			}

			if (m.equals(SHIELD_MATERIAL)) {
				block.setMetadata("StargateBlock", new FixedMetadataValue(MCStargates.getInstance(), "true"));

			} else if (block.hasMetadata("StargateBlock")) {
				block.removeMetadata("StargateBlock", MCStargates.getInstance());
			}
		}
	}

	public void makeKawoosh(boolean b) {
		Kawoosh k = new Kawoosh(this);
		k.makeKawoosh();
	}

	public void activateChevrons() {
		ArrayList<Vector> array = getRingCoordinates();
		for (int i = 0; i < array.size(); i++) {
			if ((i == 0) || (i == 3) || (i == 4) || (i == 6) || (i == 9) || (i == 11) || (i == 12) || (i == 13)) {
				Vector v = (Vector) array.get(i);
				Location location = new Location((World) Bukkit.getWorlds().get(this.worldID), v.getX(), v.getZ(),
						v.getY());
				Block block = location.getBlock();
				block.setType(CHEVRON_MATERIAL);
				block.setMetadata("StargateBlock", new FixedMetadataValue(MCStargates.getInstance(), "true"));
			}
		}
	}

	public void deactivateChevrons() {
		ArrayList<Vector> array = getRingCoordinates();
		for (Vector v : array) {
			Location location = new Location((World) Bukkit.getWorlds().get(this.worldID), v.getX(), v.getZ(),
					v.getY());
			Block block = location.getBlock();
			block.setType(GATE_MATERIAL);
			block.setMetadata("StargateBlock", new FixedMetadataValue(MCStargates.getInstance(), "true"));
		}
	}

	public ArrayList<Vector> getRingCoordinates() {
		Vector direction = getNormalVector();
		Vector h = new Vector(0, 0, 1);
		Vector sideDirection = direction.clone().crossProduct(h);

		Vector start = getPosition().add(direction.clone().multiply(DHD_DISTANCE));
		start.setZ(start.clone().getZ() - 1.0D);

		ArrayList<Vector> gateShapePositions = new ArrayList<Vector>();

		gateShapePositions.add(start.clone());
		gateShapePositions.add(start.clone().add(sideDirection));
		gateShapePositions.add(start.clone().subtract(sideDirection));

		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(2).add(h)));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(-2).add(h)));

		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(3).add(h.clone().multiply(2))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(3).add(h.clone().multiply(3))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(3).add(h.clone().multiply(4))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(-3).add(h.clone().multiply(2))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(-3).add(h.clone().multiply(3))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(-3).add(h.clone().multiply(4))));

		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(2).add(h.clone().multiply(5))));
		gateShapePositions.add(start.clone().add(sideDirection.clone().multiply(-2).add(h.clone().multiply(5))));

		gateShapePositions.add(start.clone().add(h.clone().multiply(6)));
		gateShapePositions.add(start.clone().add(sideDirection).add(h.clone().multiply(6)));
		gateShapePositions.add(start.clone().subtract(sideDirection).add(h.clone().multiply(6)));

		return gateShapePositions;
	}

	public ArrayList<Vector> getInsideCoordinates() {
		ArrayList<Vector> WaterPositions = new ArrayList<Vector>();

		Vector direction = getNormalVector();
		Vector h = new Vector(0, 0, 1);
		Vector sideDirection = direction.clone().crossProduct(h);

		Vector start = getPosition().add(direction.clone().multiply(DHD_DISTANCE));
		start.setZ(start.getZ() - 1.0D);
		for (int i = 1; i < 6; i++) {
			WaterPositions.add(start.clone().add(h.clone().multiply(i)));
			WaterPositions.add(start.clone().add(sideDirection).add(h.clone().multiply(i)));
			WaterPositions.add(start.clone().subtract(sideDirection).add(h.clone().multiply(i)));
		}
		for (int i = 1; i < 4; i++) {
			WaterPositions.add(start.clone().add(sideDirection.clone().multiply(2).add(h)).add(h.clone().multiply(i)));
			WaterPositions.add(start.clone().add(sideDirection.clone().multiply(-2).add(h)).add(h.clone().multiply(i)));
		}
		return WaterPositions;
	}

	public void playActivationSound() {
		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

		float volume = MCStargates.configValues.getKawooshSoundVolume();
		double radiusSquared = MCStargates.configValues.getKawooshSoundRadius() * MCStargates.configValues.getKawooshSoundRadius();
		if (radiusSquared > 0.0D) {
			Iterator<? extends Player> itr = players.iterator();
			while (itr.hasNext()) {
				Player p = (Player) itr.next();
				if ((this.loc.getWorld().getUID().equals(p.getLocation().getWorld().getUID()))
						&& (p.getLocation().distanceSquared(this.loc) <= radiusSquared)) {
					volume = (float) (volume * (radiusSquared / p.getLocation().distanceSquared(this.loc)));
					Sound sound = MCStargates.configValues.getKawooshSound();
					p.playSound(this.loc, sound, volume, 1.0F);
				}
			}
		}
	}

	public GateNetwork getNetwork() {
		GateNetwork gn = new GateNetwork("", "");

		for (GateNetwork g : gn.getNetworkList()) {
			if (g.networkstargates.contains(this.name)) {
				return g;
			}
		}
		return null;
	}

	public boolean setNetwork(String networkName) {
		GateNetwork gn = new GateNetwork("", "");
		gn = gn.getNetwork(networkName);
		if ((gn != null) && (!gn.networkstargates.contains(this.name))) {
			gn.addGate(this.name);
			gn.save();
			return true;
		}

		return false;
	}

	public static int getDHD_DISTANCE() {
		return DHD_DISTANCE;
	}

	public static void setDHD_DISTANCE(int dHD_DISTANCE) {
		DHD_DISTANCE = dHD_DISTANCE;
	}

	public static Material getGATE_MATERIAL() {
		return GATE_MATERIAL;
	}

	public static void setGATE_MATERIAL(Material gATE_MATERIAL) {
		GATE_MATERIAL = gATE_MATERIAL;
	}

	public static Material getDHD_MATERIAL() {
		return DHD_MATERIAL;
	}

	public static void setDHD_MATERIAL(Material dHD_MATERIAL) {
		DHD_MATERIAL = dHD_MATERIAL;
	}

	public static Material getCHEVRON_MATERIAL() {
		return CHEVRON_MATERIAL;
	}

	public static void setCHEVRON_MATERIAL(Material cHEVRON_MATERIAL) {
		CHEVRON_MATERIAL = cHEVRON_MATERIAL;
	}

	public static Material getSHIELD_MATERIAL() {
		return SHIELD_MATERIAL;
	}

	public static void setSHIELD_MATERIAL(Material sHIELD_MATERIAL) {
		SHIELD_MATERIAL = sHIELD_MATERIAL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public int getWorldID() {
		return worldID;
	}

	public void setWorldID(int worldID) {
		this.worldID = worldID;
	}

	public boolean isShieldStatus() {
		return shieldStatus;
	}

	public void setShieldStatus(boolean shieldStatus) {
		this.shieldStatus = shieldStatus;
	}

	public boolean isActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(boolean activationStatus) {
		this.activationStatus = activationStatus;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}