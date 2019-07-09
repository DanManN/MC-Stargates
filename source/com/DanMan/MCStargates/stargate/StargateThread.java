package com.DanMan.MCStargates.stargate;

import com.DanMan.MCStargates.main.MCStargates;
import com.DanMan.MCStargates.utils.StargateFileReader;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class StargateThread {
	private Stargate stargate;
	private MCStargates plugin;
	private int threadID = 0;
	private int shutDownTaskID = 0;
	// private int secondsOpen = 0;

	public StargateThread(Stargate s, MCStargates plugin) {
		this.stargate = s;
		this.plugin = plugin;
	}

	public ArrayList<Entity> getNearbyEntities(int distance) {
		ArrayList<Entity> ret = new ArrayList<Entity>();
		java.util.List<Entity> list =
			this.stargate.getLocation().getWorld().getEntities();
		Vector direction = stargate.getNormalVector();
		Vector start = stargate.getPosition().add(
			direction.clone().multiply(stargate.DHD_DISTANCE));

		Location startLoc = stargate.getLocation().clone();
		startLoc.setY(start.getZ());
		startLoc.setX(start.getX());
		startLoc.setZ(start.getY());

		for (Entity e : list) {
			if (e.getWorld().equals(startLoc.getWorld())) {
				if ((!(e instanceof Player)) &&
					(!(e instanceof org.bukkit.entity.Vehicle)) &&
					(e.getLocation().distance(startLoc) < distance)) {
					ret.add(e);
				}
			}
		}
		return ret;
	}

	public boolean checkEntityIsInsideStargate(Entity e) {
		Vector direction = stargate.getNormalVector();
		Vector start = stargate.getPosition().add(
			direction.clone().multiply(stargate.DHD_DISTANCE));

		double x1 = start.getX();
		double x2 = start.getX();
		double y1 = start.getY();
		double y2 = start.getY();
		double z1 = start.getZ();
		double z2 = start.getZ() + 5.0D;

		if (direction.equals(new Vector(0, 1, 0))) {
			x1 -= 3.0D;
			x2 += 3.0D;
			y2 += 1.0D;
		}
		if (direction.equals(new Vector(0, -1, 0))) {
			x1 -= 3.0D;
			x2 += 3.0D;
			y2 += 1.0D;
		}

		if (direction.equals(new Vector(1, 0, 0))) {
			x2 += 1.0D;
			y1 -= 3.0D;
			y2 += 3.0D;
		}
		if (direction.equals(new Vector(-1, 0, 0))) {
			x1 += 1.0D;
			y1 -= 3.0D;
			y2 += 3.0D;
		}
		if (y1 > y2) {
			y1 += y2;
			y2 = y1 - y2;
			y1 -= y2;
		}

		if (x1 > x2) {
			x1 += x2;
			x2 = x1 - x2;
			x1 -= x2;
		}

		if (z1 > z2) {
			z1 += z2;
			z2 = z1 - z2;
			z1 -= z2;
		}

		if ((y1 <= e.getLocation().getZ()) && (e.getLocation().getZ() <= y2) &&
			(x1 <= e.getLocation().getX()) && (e.getLocation().getX() <= x2) &&
			(z1 <= e.getLocation().getY()) && (e.getLocation().getY() <= z2)) {
			return true;
		}

		return false;
	}

	public void teleportEntitysThread() {
		if (plugin != null) {
			threadID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
				plugin, new Runnable() {
					public void run() {
						if ((!StargateThread.this.stargate.getActivationStatus()) ||
							(StargateThread.this.stargate.getTarget() == null)) {
							Bukkit.getScheduler().cancelTask(
								StargateThread.this.threadID);
						}

						for (Entity e : StargateThread.this.getNearbyEntities(10)) {
							if (StargateThread.this.checkEntityIsInsideStargate(e)) {

								StargateFileReader sfr = new StargateFileReader(plugin);
								Stargate target = sfr.getStargate(
									StargateThread.this.stargate.getTarget());

								if (target.getShieldStatus()) {
									// System.out.println("shield active");
									if (plugin.getConfigValues()
											.getIrisNoTeleport()
											.equals("true")) {
										target = StargateThread.this.stargate;
									}
									e.remove();
								} else {
									Vector direction = target.getNormalVector();
									Vector start = target.getPosition().add(
										direction.clone().multiply(
											stargate.DHD_DISTANCE - 1));

									Location newLoc = new Location(
										target.getLocation().getWorld(), start.getX(),
										start.getZ(), start.getY());
									if (target.getLocation().getWorld().equals(
											StargateThread.this.stargate.getLocation()
												.getWorld())) {
										e.teleport(newLoc);

									} else if ((e instanceof Item)) {
										org.bukkit.inventory.ItemStack newItem =
											((Item)e).getItemStack().clone();
										e.remove();
										target.getLocation()
											.getWorld()
											.dropItemNaturally(newLoc, newItem);
									} else {
										e.remove();
										target.getLocation().getWorld().spawnEntity(
											newLoc, e.getType());
									}
								}
							}
						}
					}
				}, 0L, 1L);
		} else {
			System.out.println("null plugin?");
		}
	}

	public void countForShutdown() {
		if (plugin.getConfigValues().getActivationTime() == 0) {
			return;
		}
		if (plugin != null) {
			shutDownTaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
				plugin, new Runnable() {
					public void run() {
						ArrayList<String> args3 = new ArrayList<String>();
						args3.add(stargate.getName());
						args3.add(stargate.getTarget());

						for (Player p :
							 stargate.getLocation().getWorld().getPlayers()) {
							if (p.getLocation().distance(stargate.getLocation()) <
								30.0D)
								p.sendMessage(
									ChatColor.GOLD +
									plugin.language.get("pluginNameChat", "") +
									ChatColor.GREEN +
									plugin.language.get("gateConnectionClosedTimeout",
														args3));
						}
						stargate.stopConnection();
						// Bukkit.getScheduler().cancelTask(StargateThread.this.shutDownTaskID);
					}
				}, plugin.getConfigValues().getActivationTime() * 20);
			stargate.setTask(shutDownTaskID);
		} else {
			System.out.println("null plugin?");
		}
	}
}