package com.DanMan.MCStargates.stargate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.DanMan.MCStargates.utils.StargateFileReader;

public class GateNetwork {
	String name;
	ArrayList<String> networkadmins = new ArrayList<String>();
	ArrayList<String> networkmembers = new ArrayList<String>();
	ArrayList<String> networkstargates = new ArrayList<String>();
	String state = "public";
	String filepath = "plugins/MPStargate/networkList.txt";

	GateNetwork(String founder, String name) {
		this.networkadmins.add(founder);
		this.name = name;
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
		if ((this.state.equals("private")) && (!this.networkmembers.contains(name))
				&& (!this.networkadmins.contains(name))) {
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
		GateNetwork g = new GateNetwork(founder, name);
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
			s = s + (String) g.networkadmins.get(i);
			if (i != g.networkadmins.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		for (int i = 0; i < g.networkmembers.size(); i++) {
			s = s + (String) g.networkmembers.get(i);
			if (i != g.networkmembers.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		for (int i = 0; i < g.networkstargates.size(); i++) {
			s = s + (String) g.networkstargates.get(i);
			if (i != g.networkstargates.size() - 1) {
				s = s + ",";
			}
		}
		s = s + ";";

		s = s + g.state;

		return s;
	}

	public boolean save() {
		File inputFile = new File(filepath);
		File tempFile = new File(filepath + ".temp");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#") || !StringToNetwork(currentLine).name.equals(name)) {
					writer.write(currentLine);
				} else {
					writer.write(NetworkToString(this));
				}
				writer.newLine();
			}
			reader.close();
			writer.close();

			inputFile.delete();
			tempFile.renameTo(inputFile);
			return true;
		} catch (FileNotFoundException e) {
			System.err.println(filepath + " not found");
		} catch (IOException e) {
			System.err.println("Could not write/read " + filepath);
		}
		return false;
	}
	/*
	 * try { BufferedReader br = new BufferedReader(new
	 * FileReader(this.filepath)); BufferedWriter bw = new BufferedWriter(new
	 * FileWriter(this.filepath)); String sCurrentLine; String writeLn = null;
	 * boolean wrote = false; while ((sCurrentLine = br.readLine()) != null) {
	 * if (!sCurrentLine.startsWith("#")) { GateNetwork network =
	 * StringToNetwork(sCurrentLine); if (network.name.equals(this.name)){
	 * writeLn = NetworkToString(this); wrote = true; } else { writeLn =
	 * sCurrentLine; } } else { writeLn = sCurrentLine; } bw.write(writeLn); }
	 * if (!wrote) { bw.write(NetworkToString(this)); } bw.close(); br.close();
	 * return wrote; } catch (IOException e) { e.printStackTrace();
	 * System.err.println("Couldn't write to Networklist!"); return false; }
	 */
	// Byte code:
	// 0: aload_0
	// 1: invokevirtual 200 hauptklassen/GateNetwork:getNetworkList
	// ()Ljava/util/ArrayList;
	// 4: astore_1
	// 5: iconst_0
	// 6: istore_2
	// 7: goto +37 -> 44
	// 10: aload_1
	// 11: iload_2
	// 12: invokevirtual 189 java/util/ArrayList:get (I)Ljava/lang/Object;
	// 15: checkcast 1 hauptklassen/GateNetwork
	// 18: getfield 42 hauptklassen/GateNetwork:name Ljava/lang/String;
	// 21: aload_0
	// 22: getfield 42 hauptklassen/GateNetwork:name Ljava/lang/String;
	// 25: invokevirtual 76 java/lang/String:equals (Ljava/lang/Object;)Z
	// 28: ifeq +13 -> 41
	// 31: aload_1
	// 32: iload_2
	// 33: aload_0
	// 34: invokevirtual 202 java/util/ArrayList:set
	// (ILjava/lang/Object;)Ljava/lang/Object;
	// 37: pop
	// 38: goto +14 -> 52
	// 41: iinc 2 1
	// 44: iload_2
	// 45: aload_1
	// 46: invokevirtual 193 java/util/ArrayList:size ()I
	// 49: if_icmplt -39 -> 10
	// 52: aconst_null
	// 53: astore_2
	// 54: new 206 java/io/PrintWriter
	// 57: dup
	// 58: aload_0
	// 59: getfield 36 hauptklassen/GateNetwork:filepath Ljava/lang/String;
	// 62: ldc -48
	// 64: invokespecial 210 java/io/PrintWriter:<init>
	// (Ljava/lang/String;Ljava/lang/String;)V
	// 67: astore_3
	// 68: aload_3
	// 69: ldc -45
	// 71: invokevirtual 213 java/io/PrintWriter:println
	// (Ljava/lang/String;)V
	// 74: aload_1
	// 75: invokevirtual 214 java/util/ArrayList:iterator
	// ()Ljava/util/Iterator;
	// 78: astore 5
	// 80: goto +29 -> 109
	// 83: aload 5
	// 85: invokeinterface 218 1 0
	// 90: checkcast 1 hauptklassen/GateNetwork
	// 93: astore 4
	// 95: aload_0
	// 96: aload 4
	// 98: invokespecial 224 hauptklassen/GateNetwork:NetworkToString
	// (Lhauptklassen/GateNetwork;)Ljava/lang/String;
	// 101: astore 6
	// 103: aload_3
	// 104: aload 6
	// 106: invokevirtual 213 java/io/PrintWriter:println
	// (Ljava/lang/String;)V
	// 109: aload 5
	// 111: invokeinterface 226 1 0
	// 116: ifne -33 -> 83
	// 119: aload_1
	// 120: aload_0
	// 121: invokevirtual 51 java/util/ArrayList:contains
	// (Ljava/lang/Object;)Z
	// 124: ifne +12 -> 136
	// 127: aload_3
	// 128: aload_0
	// 129: aload_0
	// 130: invokespecial 224 hauptklassen/GateNetwork:NetworkToString
	// (Lhauptklassen/GateNetwork;)Ljava/lang/String;
	// 133: invokevirtual 213 java/io/PrintWriter:println
	// (Ljava/lang/String;)V
	// 136: aload_3
	// 137: invokevirtual 229 java/io/PrintWriter:close ()V
	// 140: goto +40 -> 180
	// 143: astore_3
	// 144: aload_3
	// 145: invokevirtual 117 java/io/IOException:printStackTrace ()V
	// 148: aload_2
	// 149: ifnull +43 -> 192
	// 152: aload_2
	// 153: invokevirtual 230 java/io/PrintWriter:flush ()V
	// 156: aload_2
	// 157: invokevirtual 229 java/io/PrintWriter:close ()V
	// 160: goto +32 -> 192
	// 163: astore 7
	// 165: aload_2
	// 166: ifnull +11 -> 177
	// 169: aload_2
	// 170: invokevirtual 230 java/io/PrintWriter:flush ()V
	// 173: aload_2
	// 174: invokevirtual 229 java/io/PrintWriter:close ()V
	// 177: aload 7
	// 179: athrow
	// 180: aload_2
	// 181: ifnull +11 -> 192
	// 184: aload_2
	// 185: invokevirtual 230 java/io/PrintWriter:flush ()V
	// 188: aload_2
	// 189: invokevirtual 229 java/io/PrintWriter:close ()V
	// 192: iconst_0
	// 193: ireturn
	// Line number table:
	// Java source line #229 -> byte code offset #0
	// Java source line #230 -> byte code offset #5
	// Java source line #231 -> byte code offset #10
	// Java source line #232 -> byte code offset #31
	// Java source line #233 -> byte code offset #38
	// Java source line #230 -> byte code offset #41
	// Java source line #236 -> byte code offset #52
	// Java source line #238 -> byte code offset #54
	// Java source line #239 -> byte code offset #68
	// Java source line #240 -> byte code offset #74
	// Java source line #241 -> byte code offset #95
	// Java source line #242 -> byte code offset #103
	// Java source line #240 -> byte code offset #109
	// Java source line #244 -> byte code offset #119
	// Java source line #245 -> byte code offset #127
	// Java source line #247 -> byte code offset #136
	// Java source line #249 -> byte code offset #140
	// Java source line #250 -> byte code offset #144
	// Java source line #252 -> byte code offset #148
	// Java source line #253 -> byte code offset #152
	// Java source line #254 -> byte code offset #156
	// Java source line #251 -> byte code offset #163
	// Java source line #252 -> byte code offset #165
	// Java source line #253 -> byte code offset #169
	// Java source line #254 -> byte code offset #173
	// Java source line #256 -> byte code offset #177
	// Java source line #252 -> byte code offset #180
	// Java source line #253 -> byte code offset #184
	// Java source line #254 -> byte code offset #188
	// Java source line #258 -> byte code offset #192
	// Local variable table:
	// start length slot name signature
	// 0 194 0 this GateNetwork
	// 4 116 1 allNetworks ArrayList<GateNetwork>
	// 6 39 2 i int
	// 53 136 2 pWriter java.io.PrintWriter
	// 67 70 3 writer java.io.PrintWriter
	// 143 2 3 ioe IOException
	// 93 4 4 network GateNetwork
	// 78 32 5 localIterator java.util.Iterator
	// 101 4 6 networkString String
	// 163 15 7 localObject Object
	// Exception table:
	// from to target type
	// 54 140 143 java/io/IOException
	// 54 148 163 finally
	// }

	public void setName(String string) {
		this.name = string;
		StargateFileReader sfr = new StargateFileReader();
		ArrayList<Stargate> l = sfr.getStargateList();

		for (Stargate s : l) {
			s.updateSign();
		}
	}
}
