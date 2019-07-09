package com.DanMan.MCStargates.stargate;

import com.DanMan.MCStargates.main.MCStargates;
import com.DanMan.MCStargates.utils.StargateFileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GateNetwork {
	public String name;
	public ArrayList<String> networkadmins = new ArrayList<String>();
	public ArrayList<String> networkmembers = new ArrayList<String>();
	public ArrayList<String> networkstargates = new ArrayList<String>();
	public String state = "public";
	String filepath = "plugins/MC-Stargates/networkList.txt";
	private MCStargates plugin;

	public GateNetwork(String founder, String name, MCStargates plugin) {
		this.networkadmins.add(founder);
		this.name = name;
		this.plugin = plugin;
	}

	public boolean addAdmin(String p) {
		if (!this.networkadmins.contains(p)) {
			this.networkadmins.add(p);
			return true;
		}
		return false;
	}

	public boolean removeAdmin(String p) {
		if (this.networkadmins.contains(p)) {
			this.networkadmins.remove(p);
			return true;
		}
		return false;
	}

	public boolean addMember(String p) {
		if (!this.networkmembers.contains(p)) {
			this.networkmembers.add(p);
			return true;
		}
		return false;
	}

	public boolean removeMember(String p) {
		if (this.networkmembers.contains(p)) {
			this.networkmembers.remove(p);
			return true;
		}
		return false;
	}

	public boolean addGate(String s) {
		if (!this.networkstargates.contains(s)) {
			this.networkstargates.add(s);
			return true;
		}
		return false;
	}

	public boolean removeGate(String s) {
		if (this.networkstargates.contains(s)) {
			this.networkstargates.remove(s);
			return true;
		}
		return false;
	}

	public boolean isPrivate() {
		if (this.state.equalsIgnoreCase("private")) {
			return true;
		}
		return false;
	}

	public boolean isPublic() {
		if (this.state.equalsIgnoreCase("public")) {
			return true;
		}
		return false;
	}

	public boolean makePrivate() {
		if (this.state.equals("public")) {
			this.state = "private";
			return true;
		}
		return false;
	}

	public boolean makePublic() {
		if (this.state.equals("private")) {
			this.state = "public";
			return true;
		}
		return false;
	}

	public boolean allowsPlayer(String name) {
		if ((this.state.equals("private")) && (!this.networkmembers.contains(name)) &&
			(!this.networkadmins.contains(name))) {
			return false;
		}

		return true;
	}

	public boolean hasAdmin(String name) {
		if (!this.networkadmins.contains(name)) {
			return false;
		}
		return true;
	}

	public ArrayList<GateNetwork> getNetworkList() {
		ArrayList<GateNetwork> networkList = new ArrayList<GateNetwork>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filepath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.startsWith("#")) {
					GateNetwork network = StringToNetwork(sCurrentLine);
					networkList.add(network);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't fetch Networklist!");
		}
		return networkList;
	}

	public GateNetwork getNetwork(String name) {
		ArrayList<GateNetwork> networkList = getNetworkList();
		for (GateNetwork network : networkList) {
			if (network.name.equals(name))
				return network;
		}
		return null;
	}

	private GateNetwork StringToNetwork(String s) {
		if (s.equals("")) {
			return null;
		}
		String[] a = s.split(";");
		String name = a[0];
		String[] admins = a[1].split(",");
		String[] members = a[2].split(",");
		String[] gates = a[3].split(",");
		String state = a[4];

		String founder = admins[0];
		GateNetwork g = new GateNetwork(founder, name, plugin);
		String[] arrayOfString1;
		int j = (arrayOfString1 = admins).length;
		for (int i = 0; i < j; i++) {
			String admin = arrayOfString1[i];
			g.addAdmin(admin);
		}
		j = (arrayOfString1 = members).length;
		for (int i = 0; i < j; i++) {
			String member = arrayOfString1[i];
			g.addMember(member);
		}

		j = (arrayOfString1 = gates).length;
		for (int i = 0; i < j; i++) {
			String gate = arrayOfString1[i];
			g.addGate(gate);
		}
		g.state = state;

		return g;
	}

	private String NetworkToString(GateNetwork g) {
		String s = "";
		s = s + this.name + ";";
		for (int i = 0; i < g.networkadmins.size(); i++) {
			s = s + (String)g.networkadmins.get(i);
			if (i != g.networkadmins.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		for (int i = 0; i < g.networkmembers.size(); i++) {
			s = s + (String)g.networkmembers.get(i);
			if (i != g.networkmembers.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		for (int i = 0; i < g.networkstargates.size(); i++) {
			s = s + (String)g.networkstargates.get(i);
			if (i != g.networkstargates.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		s = s + g.state;

		return s;
	}

	public boolean save() {
		ArrayList<GateNetwork> allNetworks = getNetworkList();
		for (int i = 0; i < allNetworks.size(); ++i) {
			if (((GateNetwork)allNetworks.get(i)).name.equals(this.name)) {
				allNetworks.set(i, this);
				break;
			}
		}
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(this.filepath, "UTF-8");
			pWriter.println(
				"# Saves all the Networks:  name, admins, members, stargates");
			for (GateNetwork network : allNetworks) {
				String networkString = NetworkToString(network);
				pWriter.println(networkString);
			}
			if (!(allNetworks.contains(this))) {
				pWriter.println(NetworkToString(this));
			}
			pWriter.close();
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

	public void setName(String string) {
		this.name = string;
		StargateFileReader sfr = new StargateFileReader(plugin);
		ArrayList<Stargate> l = sfr.getStargateList();

		for (Stargate s : l) {
			s.updateSign();
		}
	}
}
