package com.DanMan.MCStargates.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PlayerDataReader {
	Player player;
	String filepath;

	PlayerDataReader(Player p) {
		this.player = p;
		this.filepath = ("plugins/MPStargate/players/" + p.getName() + ".txt");
	}

	public boolean createPlayerData() {
		if (!checkForPlayerData()) {
			File playerData = new File(this.filepath);

			File parent = playerData.getParentFile();
			if ((!parent.exists()) && (!parent.mkdirs())) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			if (!playerData.exists()) {
				try {
					PrintWriter writer = new PrintWriter(this.filepath, "UTF-8");
					writer.println("# Saves all the Stargates a player discovered");
					writer.close();
				} catch (Exception e) {
					System.out.println("[WARNING] Couldn't create " + this.player.getName() + ".txt!");
				}
			}
		}
		return false;
	}

	public boolean checkForPlayerData() {
		File playerData = new File(this.filepath);
		if (!playerData.exists()) {
			return false;
		}
		return true;
	}

	public ArrayList<String> getKnownStargates() {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filepath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.startsWith("#")) {
					String a = sCurrentLine.split("#")[0];
					ret.add(a);
				}
			}
			for (String s : ret) {
				StargateFileReader sfr = new StargateFileReader();
				if (sfr.getStargate(s) == null) {
					ret.remove(s);
				}
			}
			br.close();
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean knowsGate(String name) {
		ArrayList<String> l = getKnownStargates();
		if (l != null) {
			for (String s : l) {
				if (s.equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean saveStargate(String name) {
		if (getKnownStargates().contains(name)) {
			return false;
		}

		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(new java.io.BufferedWriter(new FileWriter(this.filepath, true)), true);

			pWriter.println(name);
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (pWriter != null) {
				pWriter.flush();
				pWriter.close();
			}
		}
		return false;
	}
}