package com.DanMan.MCStargates.rings;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.DanMan.MCStargates.main.MCStargates;
import com.DanMan.MCStargates.utils.SignUtils;

public class Ringtransporter {
	Material RINGTRANSPORTER_MATERIAL = MCStargates.configValues.getRingMaterial();
	byte RINGTRANSPORTER_MATERIAL_DATA = MCStargates.configValues.getRingMaterial_data();
	Material RINGTRANSPORTER_GROUND_MATERIAL = MCStargates.configValues.getRingGroundMaterial();
	byte RINGTRANSPORTER_GROUND_MATERIAL_DATA = MCStargates.configValues.getRingGroundMaterial_data();

	Location loc;
	int state_counter = -1;
	int id = 0;

	public Vector getPosition() {
		Vector start = new Vector(this.loc.getX(), this.loc.getZ(), this.loc.getY());
		return start;
	}

	public Location getLocation() {
		return this.loc;
	}

	public Location getLocationNoOffset() {
		int offset = MCStargates.configValues.getRingDistance();
		Vector n = getNormalVector();
		Vector v = getPosition().add(n.multiply(offset));
		Location l = this.loc.clone();
		l.setX(v.getX());
		l.setY(v.getZ());
		l.setZ(v.getY());
		return l;
	}

	public Vector getLocationNoOffsetAsVector() {
		int offset = MCStargates.configValues.getRingDistance();
		Vector n = getNormalVector();
		Vector v = getPosition().add(n.multiply(offset));
		return v;
	}

	public Ringtransporter getPartner() {
		Location it = getLocation().clone();
		Ringtransporter ret = new Ringtransporter();
		ret.loc = it.clone();
		int max = this.loc.getWorld().getMaxHeight();

		for (double i = this.loc.getY(); i < max; i += 1.0D) {
			it.setY(it.getY() + 1.0D);
			Block block = it.getBlock();
			if (block.getType().equals(Material.WALL_SIGN)) {
				Sign s = (Sign) block.getState();
				if (s.getLine(0).equals(ChatColor.GOLD + "[Rings]")) {
					ret.loc = s.getLocation().clone();

					if (getNormalVector().equals(ret.getNormalVector())) {
						return ret;
					}
				}
			}
		}

		it = getLocation().clone();
		for (double i = this.loc.getY(); 0.0D < i; i -= 1.0D) {
			it.setY(it.getY() - 1.0D);
			Block block = it.getBlock();
			if (block.getType().equals(Material.WALL_SIGN)) {
				Sign s = (Sign) block.getState();
				if (s.getLine(0).equals(ChatColor.GOLD + "[Rings]")) {
					ret.loc = s.getLocation().clone();

					if (getNormalVector().equals(ret.getNormalVector())) {
						return ret;
					}
				}
			}
		}

		return null;
	}

	public ArrayList<Double> getInsideBorders() {
		Vector start = getLocationNoOffsetAsVector();
		Vector direction = getNormalVector();
		double x1 = start.getX();
		double x2 = start.getX();
		double y1 = start.getY();
		double y2 = start.getY();
		double z1 = start.getZ() - 1.0D;
		double z2 = start.getZ() + 4.0D;

		if (direction.equals(new Vector(0, 1, 0))) {
			x1 += 1.0D;
			x2 += 5.0D;
			y1 += 1.0D;
			y2 += 5.0D;
		}
		if (direction.equals(new Vector(0, -1, 0))) {
			x1 -= 5.0D;
			x2 -= 1.0D;
			y2 -= 1.0D;
			y1 -= 5.0D;
		}

		if (direction.equals(new Vector(1, 0, 0))) {
			x1 += 1.0D;
			x2 += 5.0D;
			y1 -= 1.0D;
			y2 -= 5.0D;
		}
		if (direction.equals(new Vector(-1, 0, 0))) {
			x1 -= 5.0D;
			x2 -= 1.0D;
			y1 += 1.0D;
			y2 += 5.0D;
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
		ArrayList<Double> ret = new ArrayList<Double>();
		ret.add(Double.valueOf(x1));
		ret.add(Double.valueOf(x2));
		ret.add(Double.valueOf(y1));
		ret.add(Double.valueOf(y2));
		ret.add(Double.valueOf(z1));
		ret.add(Double.valueOf(z2));
		return ret;
	}

	public ArrayList<Entity> getEntitysInside() {
		ArrayList<Entity> allEntitys = new ArrayList<Entity>();

		ArrayList<Double> insideBorders = getInsideBorders();
		Double x1 = (Double) insideBorders.get(0);
		Double x2 = (Double) insideBorders.get(1);
		Double y1 = (Double) insideBorders.get(2);
		Double y2 = (Double) insideBorders.get(3);
		Double z1 = (Double) insideBorders.get(4);
		Double z2 = (Double) insideBorders.get(5);

		for (Entity entity : this.loc.getWorld().getEntities()) {
			if ((y1.doubleValue() <= entity.getLocation().getZ()) && (entity.getLocation().getZ() <= y2.doubleValue())
					&& (x1.doubleValue() <= entity.getLocation().getX())
					&& (entity.getLocation().getX() <= x2.doubleValue())
					&& (z1.doubleValue() < entity.getLocation().getY())
					&& (entity.getLocation().getY() < z2.doubleValue())) {
				allEntitys.add(entity);
			}
		}

		return allEntitys;
	}

	public ArrayList<Block> getBlocksInside() {
		ArrayList<Block> allBlocks = new ArrayList<Block>();

		ArrayList<Double> insideBorders = getInsideBorders();
		Double x1 = (Double) insideBorders.get(0);
		Double x2 = (Double) insideBorders.get(1);
		Double y1 = (Double) insideBorders.get(2);
		Double y2 = (Double) insideBorders.get(3);
		Double z1 = Double.valueOf(((Double) insideBorders.get(4)).doubleValue() + 1.0D);
		Double z2 = (Double) insideBorders.get(5);

		for (Double x = x1; x.doubleValue() <= x2.doubleValue(); x = Double.valueOf(x.doubleValue() + 1.0D)) {
			for (Double y = y1; y.doubleValue() <= y2.doubleValue(); y = Double.valueOf(y.doubleValue() + 1.0D)) {
				for (Double z = z1; z.doubleValue() < z2.doubleValue(); z = Double.valueOf(z.doubleValue() + 1.0D)) {
					Block b = this.loc.getWorld().getBlockAt(x.intValue(), z.intValue(), y.intValue());
					if ((b instanceof Block)) {
						if ((!b.getType().equals(Material.AIR)) && (!b.getType().equals(Material.WALL_SIGN))) {
							allBlocks.add(b);
						}
					}
				}
			}
		}

		return allBlocks;
	}

	public void makeRing(int height) {
		ArrayList<Vector> coordinates = ringshape();
		for (Vector v : coordinates) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() + height, v.getY());
			Block block = location.getBlock();

			block.setType(this.RINGTRANSPORTER_MATERIAL);

			block.setData(this.RINGTRANSPORTER_MATERIAL_DATA);
		}
	}

	public void openGround() {
		ArrayList<Vector> coordinates = ringshape();
		for (Vector v : coordinates) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
			Block block = location.getBlock();
			block.setType(Material.AIR);
		}
	}

	public void closeGround() {
		ArrayList<Vector> coordinates = ringshape();
		for (Vector v : coordinates) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
			Block block = location.getBlock();
			block.setType(this.RINGTRANSPORTER_GROUND_MATERIAL);
			block.setData(this.RINGTRANSPORTER_GROUND_MATERIAL_DATA);
		}
	}

	public boolean checkConstuction() {
		ArrayList<Vector> coordinates = ringshape();
		for (Vector v : coordinates) {
			Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
			Block block = location.getBlock();
			if (!block.getType().equals(this.RINGTRANSPORTER_GROUND_MATERIAL)) {
				return false;
			}

			Location location2 = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 2.0D, v.getY());
			Block block2 = location2.getBlock();
			if (!block2.getType().equals(this.RINGTRANSPORTER_MATERIAL)) {
				return false;
			}
		}
		return true;
	}

	public void cleanRings(int height) {
		ArrayList<Vector> coordinates = ringshape();
		for (int i = 0; i < height; i++) {
			for (Vector v : coordinates) {
				Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() + i, v.getY());
				Block block = location.getBlock();

				block.setType(Material.AIR);
			}
		}
	}

	public Vector getNormalVector() {
		Sign s = (Sign) getLocation().getBlock().getState();
		BlockFace direction = SignUtils.signFacing(s).getOppositeFace();

		Vector vector = null;
		if (direction == BlockFace.NORTH) {
			vector = new Vector(0, -1, 0);
		}
		if (direction == BlockFace.SOUTH) {
			vector = new Vector(0, 1, 0);
		}
		if (direction == BlockFace.EAST) {
			vector = new Vector(1, 0, 0);
		}
		if (direction == BlockFace.WEST) {
			vector = new Vector(-1, 0, 0);
		}
		vector.multiply(-1);
		return vector;
	}

	public Vector getSideVector() {
		Vector n = getNormalVector();
		Vector h = new Vector(0, 0, 1);

		Vector s = n.crossProduct(h);

		return s;
	}

	public ArrayList<Vector> ringshape() {
		Vector start = getLocationNoOffsetAsVector();
		Vector sideDirection = getSideVector();
		Vector topDirection = getNormalVector();
		ArrayList<Vector> ringShapePositions = new ArrayList<Vector>();

		ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(2)));
		ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(3)));

		ringShapePositions
				.add(start.clone().add(sideDirection.clone().multiply(2)).add(topDirection.clone().multiply(5)));
		ringShapePositions
				.add(start.clone().add(sideDirection.clone().multiply(3).add(topDirection.clone().multiply(5))));

		ringShapePositions.add(start.clone().add(topDirection.clone().multiply(2)));
		ringShapePositions.add(start.clone().add(topDirection.clone().multiply(3)));
		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(2)).add(sideDirection.clone().multiply(5)));
		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(3)).add(sideDirection.clone().multiply(5)));

		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(1)).add(sideDirection.clone().multiply(4)));
		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(4)).add(sideDirection.clone().multiply(1)));
		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(1)).add(sideDirection.clone().multiply(1)));
		ringShapePositions
				.add(start.clone().add(topDirection.clone().multiply(4)).add(sideDirection.clone().multiply(4)));

		return ringShapePositions;
	}

	public void makeAnimation(final boolean teleport) {
		if (MCStargates.getInstance() != null) {

			this.id = org.bukkit.Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MCStargates.getInstance(),
					new Runnable() {
						public void run() {
							Ringtransporter.this.state_counter += 1;
							ArrayList<Integer> animationFrame = new ArrayList<Integer>();

							if (Ringtransporter.this.state_counter == 0)
								Ringtransporter.this.openGround();
							if (Ringtransporter.this.state_counter == 1)
								animationFrame = Ringtransporter.this.state1();
							if (Ringtransporter.this.state_counter == 2)
								animationFrame = Ringtransporter.this.state2();
							if (Ringtransporter.this.state_counter == 3)
								animationFrame = Ringtransporter.this.state3();
							if (Ringtransporter.this.state_counter == 4)
								animationFrame = Ringtransporter.this.state4();
							if (Ringtransporter.this.state_counter == 5)
								animationFrame = Ringtransporter.this.state5();
							if (Ringtransporter.this.state_counter == 6)
								animationFrame = Ringtransporter.this.state6();
							if (Ringtransporter.this.state_counter == 7)
								animationFrame = Ringtransporter.this.state7();
							if (Ringtransporter.this.state_counter == 8) {
								animationFrame = Ringtransporter.this.state7();
								if (teleport) {
									Ringtransporter.this.transportEntitys();
									Ringtransporter.this.transportBlocks();
								}
							}
							if (Ringtransporter.this.state_counter == 9)
								animationFrame = Ringtransporter.this.state7();
							if (Ringtransporter.this.state_counter == 10)
								animationFrame = Ringtransporter.this.state6();
							if (Ringtransporter.this.state_counter == 11)
								animationFrame = Ringtransporter.this.state5();
							if (Ringtransporter.this.state_counter == 12)
								animationFrame = Ringtransporter.this.state4();
							if (Ringtransporter.this.state_counter == 13)
								animationFrame = Ringtransporter.this.state3();
							if (Ringtransporter.this.state_counter == 14)
								animationFrame = Ringtransporter.this.state2();
							if (Ringtransporter.this.state_counter == 15)
								animationFrame = Ringtransporter.this.state1();
							if (Ringtransporter.this.state_counter == 16)
								animationFrame = Ringtransporter.this.state8();
							if (Ringtransporter.this.state_counter > 16) {
								Ringtransporter.this.closeGround();
								org.bukkit.Bukkit.getScheduler().cancelTask(Ringtransporter.this.id);
							}

							Ringtransporter.this.cleanRings(4);

							for (Integer i : animationFrame) {
								Ringtransporter.this.makeRing(i.intValue());

							}

						}

					}, 0L, 6L);
		} else {
			System.out.println("kein verweis auf plugin!");
		}
	}

	public void transportEntitys() {
		Ringtransporter Ring1 = this;
		Ringtransporter Ring2 = getPartner();

		if (Ring2 != null) {
			ArrayList<Entity> list1 = Ring1.getEntitysInside();
			ArrayList<Entity> list2 = Ring2.getEntitysInside();

			if (list1.size() != 0) {
				for (Entity entityInRing1 : list1) {
					if ((entityInRing1 instanceof Player)) {
						((Player) entityInRing1).hasPermission("rings.use");
					}

					Location newLoc = new Location(this.loc.getWorld(), entityInRing1.getLocation().getX(),
							Ring2.loc.getY(), entityInRing1.getLocation().getZ());
					entityInRing1.teleport(newLoc);
				}
			}

			if (list2.size() != 0) {
				for (Entity entityInRing2 : list2) {
					if ((entityInRing2 instanceof Player)) {
						((Player) entityInRing2).hasPermission("rings.use");
					}

					Location newLoc2 = new Location(this.loc.getWorld(), entityInRing2.getLocation().getX(),
							Ring1.loc.getY(), entityInRing2.getLocation().getZ());
					entityInRing2.teleport(newLoc2);
				}
			}
		}
	}

	public void transportBlocks() {
		Ringtransporter Ring1 = this;
		Ringtransporter Ring2 = getPartner();
		if (Ring2 != null) {
			ArrayList<Block> list1 = Ring1.getBlocksInside();
			ArrayList<Block> list2 = Ring2.getBlocksInside();

			ArrayList<Material> m1 = new ArrayList<Material>();
			ArrayList<Location> l1 = new ArrayList<Location>();
			ArrayList<Byte> d1 = new ArrayList<Byte>();
			ArrayList<ItemStack[]> i1 = new ArrayList<ItemStack[]>();

			ArrayList<Material> m2 = new ArrayList<Material>();
			ArrayList<Location> l2 = new ArrayList<Location>();
			ArrayList<Byte> d2 = new ArrayList<Byte>();
			ArrayList<ItemStack[]> i2 = new ArrayList<ItemStack[]>();

			ArrayList<Location> tabulist = new ArrayList<Location>();

			for (int i = 0; i < list1.size(); i++) {
				m1.add(((Block) list1.get(i)).getType());
				l1.add(((Block) list1.get(i)).getLocation().clone());
				d1.add(Byte.valueOf(((Block) list1.get(i)).getData()));
				if (((Block) list1.get(i)).getType().equals(Material.CHEST)) {
					Chest c = (Chest) ((Block) list1.get(i)).getState();
					i1.add((ItemStack[]) c.getBlockInventory().getContents().clone());
				} else {
					i1.add(null);
				}
			}

			for (int i = 0; i < list2.size(); i++) {
				m2.add(((Block) list2.get(i)).getType());
				l2.add(((Block) list2.get(i)).getLocation().clone());
				d2.add(Byte.valueOf(((Block) list2.get(i)).getData()));
				if (((Block) list2.get(i)).getType().equals(Material.CHEST)) {
					Chest c = (Chest) ((Block) list2.get(i)).getState();
					i2.add((ItemStack[]) c.getBlockInventory().getContents().clone());
				} else {
					i2.add(null);
				}
			}
			for (int i = 0; i < list1.size(); i++) {
				if (((Block) list1.get(i)).getType().equals(Material.CHEST)) {
					Chest c = (Chest) ((Block) list1.get(i)).getState();
					c.getInventory().clear();
					c.update();
				}
				((Block) list1.get(i)).setType(Material.AIR);
				((Block) list1.get(i)).setData((byte) 0);

				Double blockHeight = Double.valueOf(((Block) list1.get(i)).getY() - Ring1.loc.getY());

				Location newLoc = new Location(this.loc.getWorld(), ((Location) l1.get(i)).getX(),
						Ring2.loc.getY() + blockHeight.doubleValue(), ((Location) l1.get(i)).getZ());
				newLoc.getBlock().setType((Material) m1.get(i));
				newLoc.getBlock().setData(((Byte) d1.get(i)).byteValue());

				if (newLoc.getBlock().getType().equals(Material.CHEST)) {
					Chest c = (Chest) newLoc.getBlock().getState();
					if ((i1.get(i) != null) && (c.getBlockInventory() != null)) {
						c.getBlockInventory().setContents((ItemStack[]) ((ItemStack[]) i1.get(i)).clone());
						c.update();
					}
				}

				tabulist.add(newLoc);
			}

			for (int i = 0; i < list2.size(); i++) {
				if (!tabulist.contains(((Block) list2.get(i)).getLocation())) {
					if (((Block) list2.get(i)).getType().equals(Material.CHEST)) {
						Chest c = (Chest) ((Block) list2.get(i)).getState();
						c.getInventory().clear();
						c.update();
					}
					((Block) list2.get(i)).setType(Material.AIR);
					((Block) list2.get(i)).setData((byte) 0);
				}

				Double blockHeight = Double.valueOf(((Block) list2.get(i)).getY() - Ring2.loc.getY());

				Location newLoc = new Location(this.loc.getWorld(), ((Location) l2.get(i)).getX(),
						Ring1.loc.getY() + blockHeight.doubleValue(), ((Location) l2.get(i)).getZ());
				newLoc.getBlock().setType((Material) m2.get(i));
				newLoc.getBlock().setData(((Byte) d2.get(i)).byteValue());

				if (newLoc.getBlock().getType().equals(Material.CHEST)) {
					Chest c = (Chest) newLoc.getBlock().getState();
					if ((i2.get(i) != null) && (c.getBlockInventory() != null)) {
						c.getBlockInventory().setContents((ItemStack[]) ((ItemStack[]) i2.get(i)).clone());
						c.update();
					}
				}
			}
		}
	}

	public ArrayList<Integer> state1() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(0));
		return coords;
	}

	public ArrayList<Integer> state2() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(1));
		return coords;
	}

	public ArrayList<Integer> state3() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(0));
		coords.add(Integer.valueOf(2));
		return coords;
	}

	public ArrayList<Integer> state4() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(1));
		coords.add(Integer.valueOf(3));
		return coords;
	}

	public ArrayList<Integer> state5() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(0));
		coords.add(Integer.valueOf(2));
		coords.add(Integer.valueOf(3));
		return coords;
	}

	public ArrayList<Integer> state6() {
		ArrayList<Integer> coords = new ArrayList<Integer>();

		coords.add(Integer.valueOf(1));
		coords.add(Integer.valueOf(2));
		coords.add(Integer.valueOf(3));

		return coords;
	}

	public ArrayList<Integer> state7() {
		ArrayList<Integer> coords = new ArrayList<Integer>();
		coords.add(Integer.valueOf(0));
		coords.add(Integer.valueOf(1));
		coords.add(Integer.valueOf(2));
		coords.add(Integer.valueOf(3));

		return coords;
	}

	public ArrayList<Integer> state8() {
		ArrayList<Integer> coords = new ArrayList<Integer>();

		return coords;
	}
}