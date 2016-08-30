package com.DanMan.stargate;

import java.io.PrintStream;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class StargateThread {
	Stargate stargate;
	int threadID = 0;
	int shoutDownTaskID = 0;
	int secondsOpen = 0;

	StargateThread(Stargate s) {
		this.stargate = s;
	}

	public ArrayList<Entity> getNearbyEntities(int distance) {
		ArrayList<Entity> ret = new ArrayList();
		java.util.List<Entity> list = this.stargate.loc.getWorld().getEntities();
		Vector direction = this.stargate.getNormalVector();
		Vector start = this.stargate.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE));

		Location startLoc = this.stargate.loc.clone();
		startLoc.setY(start.getZ());
		startLoc.setX(start.getX());
		startLoc.setZ(start.getY());

		for (Entity e : list) {
			if (e.getWorld().equals(startLoc.getWorld())) {
				if ((!(e instanceof Player)) && (!(e instanceof org.bukkit.entity.Vehicle))
						&& (e.getLocation().distance(startLoc) < distance)) {
					ret.add(e);
				}
			}
		}
		return ret;
	}

	public boolean checkEntityIsInsideStargate(Entity e) {
		Vector direction = this.stargate.getNormalVector();
		Vector start = this.stargate.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE));

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

		if ((y1 <= e.getLocation().getZ()) && (e.getLocation().getZ() <= y2) && (x1 <= e.getLocation().getX())
				&& (e.getLocation().getX() <= x2) && (z1 <= e.getLocation().getY()) && (e.getLocation().getY() <= z2)) {
			return true;
		}

		return false;
	}

	public void teleportEntitysThread() {
		if (MCStargates.getInstance() != null) {
			this.threadID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MCStargates.getInstance(),
					new Runnable() {
						public void run() {
							if ((!StargateThread.this.stargate.activationStatus)
									|| (StargateThread.this.stargate.target == null)) {
								Bukkit.getScheduler().cancelTask(StargateThread.this.threadID);
							}

							for (Entity e : StargateThread.this.getNearbyEntities(10)) {
								if (StargateThread.this.checkEntityIsInsideStargate(e)) {

									StargateFileReader sfr = new StargateFileReader();
									Stargate target = sfr.getStargate(StargateThread.this.stargate.target);

									if (target.shieldStatus) {
										System.out.println("shield activ");
										if (MCStargates.configValues.IrisNoTeleport.equals("true")) {
											target = StargateThread.this.stargate;
										}
										e.remove();
									} else {
										Vector direction = target.getNormalVector();
										Vector start = target.getPosition()
												.add(direction.clone().multiply(Stargate.DHD_DISTANCE - 1));

										Location newLoc = new Location(target.loc.getWorld(), start.getX(),
												start.getZ(), start.getY());
										if (target.loc.getWorld().equals(StargateThread.this.stargate.loc.getWorld())) {
											e.teleport(newLoc);

										} else if ((e instanceof Item)) {
											org.bukkit.inventory.ItemStack newItem = ((Item) e).getItemStack().clone();
											e.remove();
											target.loc.getWorld().dropItemNaturally(newLoc, newItem);
										} else {
											e.remove();
											target.loc.getWorld().spawnEntity(newLoc, e.getType());
										}

									}

								}

							}
						}
					}, 0L, 2L);
		} else {
			System.out.println("kein verweis auf plugin!");
		}
	}

	public void countForShutdown() {
		if (MCStargates.getInstance() != null) {
			this.shoutDownTaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCStargates.getInstance(),
					new Runnable() {
						public void run() {
							if (MCStargates.configValues.activationTime == 0) {
								Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
							}
							if (!StargateThread.this.stargate.activationStatus) {
								Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
							}

							ArrayList<String> args3 = new ArrayList();
							args3.add(StargateThread.this.stargate.name);
							args3.add(StargateThread.this.stargate.target);

							for (Player p : StargateThread.this.stargate.loc.getWorld().getPlayers()) {
								if (p.getLocation().distance(StargateThread.this.stargate.loc) < 30.0D)
									p.sendMessage(
											ChatColor.GOLD + MCStargates.language.get("pluginNameChat", "") + ChatColor.GREEN
													+ MCStargates.language.get("gateConnectionClosedTimeout", args3));
							}
							StargateThread.this.stargate.stopConnection();
							Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
						}

					}, MCStargates.configValues.activationTime * 20);
		} else {
			System.out.println("kein verweis auf plugin!");
		}
	}
}