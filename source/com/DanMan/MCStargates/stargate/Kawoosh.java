package com.DanMan.MCStargates.stargate;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.DanMan.MCStargates.main.MCStargates;

public class Kawoosh {
	private Stargate stargate;
	private int id = 0;
	private int state_counter = 0;
	private MCStargates plugin;
	
	public Kawoosh(Stargate s, MCStargates plugin) {
		this.stargate = s;
		this.plugin = plugin;
	}

	public void makeKawoosh() {
		if (plugin != null) {

			this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				public void run() {
					Vector normal = Kawoosh.this.stargate.getNormalVector();
					Vector h = new Vector(0, 0, 1);
					Vector side = normal.clone().crossProduct(h);
					Vector start = Kawoosh.this.stargate.getPosition()
							.add(normal.clone().multiply(stargate.DHD_DISTANCE));

					Kawoosh.this.state_counter += 1;

					ArrayList<Vector> kawooshCoords = new ArrayList<Vector>();
					if (Kawoosh.this.state_counter == 1)
						kawooshCoords = Kawoosh.this.state1();
					if (Kawoosh.this.state_counter == 2)
						kawooshCoords = Kawoosh.this.state2();
					if (Kawoosh.this.state_counter == 3)
						kawooshCoords = Kawoosh.this.state3();
					if (Kawoosh.this.state_counter == 4)
						kawooshCoords = Kawoosh.this.state4();
					if (Kawoosh.this.state_counter == 5)
						kawooshCoords = Kawoosh.this.state5();
					if (Kawoosh.this.state_counter == 6)
						kawooshCoords = Kawoosh.this.state6();
					if (Kawoosh.this.state_counter == 7)
						kawooshCoords = Kawoosh.this.state7();
					if (Kawoosh.this.state_counter == 8)
						kawooshCoords = Kawoosh.this.state8();
					if (Kawoosh.this.state_counter == 9)
						kawooshCoords = Kawoosh.this.state9();
					if (Kawoosh.this.state_counter == 10)
						kawooshCoords = Kawoosh.this.state10();
					if (Kawoosh.this.state_counter == 11)
						kawooshCoords = Kawoosh.this.state4();
					if (Kawoosh.this.state_counter == 12)
						kawooshCoords = Kawoosh.this.state3();
					if (Kawoosh.this.state_counter > 12) {
						Bukkit.getScheduler().cancelTask(Kawoosh.this.id);
					}

					Kawoosh.this.cleanKawoosh();

					for (Vector v : kawooshCoords) {

						Vector k = start.clone().add(side.clone().multiply(v.getX())).add(h.clone().multiply(v.getY()))
								.subtract(normal.clone().multiply(v.getZ()));
						Location location = new Location(
								(org.bukkit.World) Bukkit.getWorlds().get(Kawoosh.this.stargate.getWorldID()), k.getX(),
								k.getZ(), k.getY());
						Block block = location.getBlock();

						block.setType(Material.STATIONARY_WATER);

						block.setMetadata("PortalWater",
								new org.bukkit.metadata.FixedMetadataValue(plugin, "true"));

					}

				}

			}, 0L, 3L);
		} else {
			System.out.println("null plugin?");
		}
	}

	public ArrayList<Vector> state1() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		coords.add(new Vector(0, 0, 0));
		coords.add(new Vector(2, 2, 0));
		coords.add(new Vector(-2, 2, 0));
		coords.add(new Vector(0, 4, 0));

		return coords;
	}

	public ArrayList<Vector> state2() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		coords.add(new Vector(0, 0, 0));
		coords.add(new Vector(1, 0, 0));
		coords.add(new Vector(-1, 0, 0));

		coords.add(new Vector(1, 1, 0));
		coords.add(new Vector(1, 3, 0));
		coords.add(new Vector(-1, 3, 0));
		coords.add(new Vector(-1, 1, 0));
		coords.add(new Vector(2, 1, 0));
		coords.add(new Vector(2, 2, 0));
		coords.add(new Vector(-2, 1, 0));
		coords.add(new Vector(-2, 2, 0));
		coords.add(new Vector(2, 3, 0));
		coords.add(new Vector(-2, 3, 0));

		coords.add(new Vector(0, 4, 0));
		coords.add(new Vector(1, 4, 0));
		coords.add(new Vector(-1, 4, 0));

		return coords;
	}

	public ArrayList<Vector> state3() {
		ArrayList<Vector> coords = new ArrayList<Vector>();
		coords.add(new Vector(0, 0, 0));
		coords.add(new Vector(1, 0, 0));
		coords.add(new Vector(-1, 0, 0));

		coords.add(new Vector(0, 1, 0));
		coords.add(new Vector(0, 2, 0));
		coords.add(new Vector(0, 3, 0));
		coords.add(new Vector(1, 2, 0));
		coords.add(new Vector(-1, 2, 0));
		coords.add(new Vector(1, 3, 0));
		coords.add(new Vector(-1, 3, 0));

		coords.add(new Vector(1, 1, 0));
		coords.add(new Vector(-1, 1, 0));
		coords.add(new Vector(2, 1, 0));
		coords.add(new Vector(2, 2, 0));
		coords.add(new Vector(-2, 1, 0));
		coords.add(new Vector(-2, 2, 0));
		coords.add(new Vector(2, 3, 0));
		coords.add(new Vector(-2, 3, 0));

		coords.add(new Vector(0, 4, 0));
		coords.add(new Vector(1, 4, 0));
		coords.add(new Vector(-1, 4, 0));

		return coords;
	}

	public ArrayList<Vector> state4() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		coords.add(new Vector(0, 1, 1));
		coords.add(new Vector(1, 2, 1));
		coords.add(new Vector(-1, 2, 1));
		coords.add(new Vector(0, 3, 1));
		coords.add(new Vector(0, 2, 2));

		return coords;
	}

	public ArrayList<Vector> state5() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		int h;
		for (int s = 0; s < 3; s++) {
			for (h = 1; h < 4; h++) {
				coords.add(new Vector(s - 1, h, 1));
			}
		}

		for (Vector v : state4()) {
			coords.add(v.clone().add(new Vector(0, 0, 1)));
		}

		return coords;
	}

	public ArrayList<Vector> state6() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		int h;
		for (int s = 0; s < 3; s++) {
			for (h = 1; h < 4; h++) {
				coords.add(new Vector(s - 1, h, 1));
				coords.add(new Vector(s - 1, h, 2));
			}
		}

		for (Vector v : state4()) {
			coords.add(v.clone().add(new Vector(0, 0, 2)));
		}

		coords.add(new Vector(0, 0, 1));
		coords.add(new Vector(0, 0, 2));
		coords.add(new Vector(-2, 2, 1));
		coords.add(new Vector(-2, 2, 2));
		coords.add(new Vector(2, 2, 1));
		coords.add(new Vector(2, 2, 2));
		coords.add(new Vector(0, 4, 1));
		coords.add(new Vector(0, 4, 2));

		coords.add(new Vector(0, 2, 5));

		return coords;
	}

	public ArrayList<Vector> state7() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		for (Vector v : state6()) {
			coords.add(v.clone().add(new Vector(0, 0, 1)));
		}

		coords.remove(new Vector(0, 2, 6));

		for (Vector v2 : state4()) {
			coords.add(v2.clone());
		}
		return coords;
	}

	public ArrayList<Vector> state8() {
		ArrayList<Vector> coords = new ArrayList<Vector>();
		for (Vector v : state7()) {
			coords.add(v.clone().add(new Vector(0, 0, 2)));
		}

		for (Vector v : state4()) {
			coords.add(v.clone().add(new Vector(0, 0, 0)));
			coords.add(v.clone().add(new Vector(0, 0, 1)));
		}

		return coords;
	}

	public ArrayList<Vector> state9() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		coords.add(new Vector(0, 2, 1));
		coords.add(new Vector(0, 2, 2));
		coords.add(new Vector(0, 2, 3));
		coords.add(new Vector(0, 2, 7));

		for (Vector v : state4()) {
			coords.add(v.clone().add(new Vector(0, 0, 3)));
			coords.add(v.clone().add(new Vector(0, 0, 4)));
		}

		coords.add(new Vector(1, 2, 6));
		coords.add(new Vector(-1, 2, 6));

		return coords;
	}

	public ArrayList<Vector> state10() {
		ArrayList<Vector> coords = new ArrayList<Vector>();

		coords.add(new Vector(0, 2, 1));
		coords.add(new Vector(0, 2, 2));

		for (Vector v : state4()) {
			coords.add(v.clone().add(new Vector(0, 0, 2)));
			coords.add(v.clone().add(new Vector(0, 0, 3)));
		}

		return coords;
	}

	public void cleanKawoosh() {
     		Vector normal = this.stargate.getNormalVector();
    		ArrayList<Vector> vecs = this.stargate.getInsideCoordinates();
    		for (int i = 1; i < 8; i++)  {
       			Vector k = normal.clone().multiply(-i);
			//System.out.println(k.toString());
			for (Vector iv : vecs) {
				Vector v = iv.clone().add(k);
				//System.out.println(v.toString());
       				Location location = new Location((World) Bukkit.getWorlds().get(this.stargate.getWorldID()), v.getX(), v.getZ(), v.getY());
       				Block block = location.getBlock();
       
       				if (!block.hasMetadata("StargateBlock")) {
         				block.setType(Material.AIR);
         				if (block.hasMetadata("PortalWater")) {
           					block.removeMetadata("PortalWater", plugin);
         				}
         				if (block.hasMetadata("Kawoosh")) {
           					block.removeMetadata("Kawoosh", plugin);
         				}
       				}
			}
     		}
   	}
}
