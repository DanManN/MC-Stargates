package com.DanMan.MCStargates.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.DanMan.MCStargates.main.MCStargates;
import com.DanMan.MCStargates.stargate.Stargate;

public class StargateFileReader {

	MCStargates plugin;
	
	public StargateFileReader(MCStargates plugin) {
		this.plugin = plugin;
	}
	
	public Stargate getStargate(String name) {
		for (Stargate stargate : getStargateList()) {
			if (stargate.getName().equals(name))
				return stargate;
		}
		return null;
	}

	public boolean saveStargate(Stargate stargate) {
		//System.out.println("Saving Gate");
		if (getStargate(stargate.getName()) != null) {
			return false;
		}

		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter("plugins/MC-Stargates/stargateList.txt", true)),
					true);

			String stargateString = StargateToString(stargate);
			pWriter.println(stargateString);
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

	public boolean updateStargate(Stargate s) {
		ArrayList<Stargate> allGates = getStargateList();
		for (int i = 0; i < allGates.size(); i++) {
			if (allGates.get(i).getName().equals(s.getName())) {
				allGates.set(i, s);
				break;
			}
		}
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter("plugins/MC-Stargates/stargateList.txt", "UTF-8");
			pWriter.println("# Saves all the Stargates:  Name, WorldID, location");
			for (Stargate g : allGates) {
				String stargateString = StargateToString(g);
				pWriter.println(stargateString);
			}
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

	public boolean deleteStargate(Stargate stargate) throws IOException {
		File inputFile = new File("plugins/MC-Stargates/stargateList.txt");

		File tempFile = new File("plugins/MC-Stargates/MC-StargatesTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String currentLine;

		while ((currentLine = reader.readLine()) != null) {
			if ((!currentLine.contains(stargate.getName())) && (!currentLine.startsWith("#"))) {
				writer.write(currentLine);
				writer.newLine();
			}
		}
		reader.close();
		writer.close();

		inputFile.delete();
		tempFile.renameTo(inputFile);

		return true;
	}

	public ArrayList<Stargate> getStargateList() {
		ArrayList<Stargate> GateList = new ArrayList<Stargate>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("plugins/MC-Stargates/stargateList.txt"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.startsWith("#")) {
					Stargate stargate = StringToStargate(sCurrentLine);
					GateList.add(stargate);
				}
			}
			br.close();
			return GateList;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not fetch stargateList.txt!");
			return null;
		}
	}

	public String StargateToString(Stargate s) {
		String ret = s.getName() + ";" + s.getWorldID() + ";" + s.getShieldStatus() + ";" + s.getActivationStatus() + ";"
				+ s.getLocation() + ";" + s.getTarget() + ";" + s.getDirection() + ";" + s.getTask();
		return ret;
	}

	public Stargate StringToStargate(String string) {
		String[] str = string.split(";");
		Stargate s = new Stargate(plugin);

		s.setName(str[0]);
		s.setWorldID(Integer.parseInt(str[1]));
		s.setShieldStatus(Boolean.parseBoolean(str[2]));
		s.setActivationStatus(Boolean.parseBoolean(str[3]));

		double locX = Float.valueOf(str[4].split(",")[1].split("=")[1]).floatValue();
		double locY = Float.valueOf(str[4].split(",")[2].split("=")[1]).floatValue();
		double locZ = Float.valueOf(str[4].split(",")[3].split("=")[1]).floatValue();
		s.setLocation(new org.bukkit.Location((org.bukkit.World) Bukkit.getWorlds().get(s.getWorldID()), locX, locY, locZ));
		s.setTarget(str[5]);
		s.setDirection(str[6]);
		s.setTask(Integer.parseInt(str[7]));

		return s;
	}

	public ArrayList<Stargate> getActiveStartGates() {
		ArrayList<Stargate> GateList = new ArrayList<Stargate>();
		for (Stargate stargate : getStargateList()) {
			if ((stargate.getActivationStatus()) && (!stargate.getTarget().equals("null"))) {
				GateList.add(stargate);
			}
		}
		return GateList;
	}
}
