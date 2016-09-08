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
		System.out.println("Saving Gate");
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

	public boolean updateStargate(Stargate stargate) {
		File inputFile = new File("plugins/MC-Stargates/stargateList.txt");
		File tempFile = new File("plugins/MC-Stargates/MC-StargatesTempFile.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));

			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#") || !currentLine.contains(" " + stargate.getName() + ";")) {
					writer.write(currentLine);
				} else {
					writer.write(StargateToString(stargate));
				}
				writer.newLine();
			}
			reader.close();
			writer.close();

			inputFile.delete();
			tempFile.renameTo(inputFile);
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("stargateList.txt not found");
		} catch (IOException e) {
			System.err.println("Could not write/read stargateList.txt");
		}
		return false;
	}
	// Byte code:
	// 0: aload_0
	// 1: invokevirtual 121 hauptklassen/StargateFileReader:getStargateList
	// ()Ljava/util/ArrayList;
	// 4: astore_2
	// 5: iconst_0
	// 6: istore_3
	// 7: goto +37 -> 44
	// 10: aload_2
	// 11: iload_3
	// 12: invokevirtual 125 java/util/ArrayList:get (I)Ljava/lang/Object;
	// 15: checkcast 40 hauptklassen/Stargate
	// 18: getfield 39 hauptklassen/Stargate:name Ljava/lang/String;
	// 21: aload_1
	// 22: getfield 39 hauptklassen/Stargate:name Ljava/lang/String;
	// 25: invokevirtual 45 java/lang/String:equals (Ljava/lang/Object;)Z
	// 28: ifeq +13 -> 41
	// 31: aload_2
	// 32: iload_3
	// 33: aload_1
	// 34: invokevirtual 131 java/util/ArrayList:set
	// (ILjava/lang/Object;)Ljava/lang/Object;
	// 37: pop
	// 38: goto +14 -> 52
	// 41: iinc 3 1
	// 44: iload_3
	// 45: aload_2
	// 46: invokevirtual 135 java/util/ArrayList:size ()I
	// 49: if_icmplt -39 -> 10
	// 52: aconst_null
	// 53: astore_3
	// 54: new 92 java/io/PrintWriter
	// 57: dup
	// 58: ldc 20
	// 60: ldc -117
	// 62: invokespecial 141 java/io/PrintWriter:<init>
	// (Ljava/lang/String;Ljava/lang/String;)V
	// 65: astore 4
	// 67: aload 4
	// 69: ldc -112
	// 71: invokevirtual 111 java/io/PrintWriter:println
	// (Ljava/lang/String;)V
	// 74: aload_2
	// 75: invokevirtual 146 java/util/ArrayList:iterator
	// ()Ljava/util/Iterator;
	// 78: astore 6
	// 80: goto +30 -> 110
	// 83: aload 6
	// 85: invokeinterface 150 1 0
	// 90: checkcast 40 hauptklassen/Stargate
	// 93: astore 5
	// 95: aload_0
	// 96: aload 5
	// 98: invokevirtual 107
	// hauptklassen/StargateFileReader:StargateToString
	// (Lhauptklassen/Stargate;)Ljava/lang/String;
	// 101: astore 7
	// 103: aload 4
	// 105: aload 7
	// 107: invokevirtual 111 java/io/PrintWriter:println
	// (Ljava/lang/String;)V
	// 110: aload 6
	// 112: invokeinterface 156 1 0
	// 117: ifne -34 -> 83
	// 120: aload 4
	// 122: invokevirtual 115 java/io/PrintWriter:close ()V
	// 125: goto +42 -> 167
	// 128: astore 4
	// 130: aload 4
	// 132: invokevirtual 62 java/io/IOException:printStackTrace ()V
	// 135: aload_3
	// 136: ifnull +43 -> 179
	// 139: aload_3
	// 140: invokevirtual 112 java/io/PrintWriter:flush ()V
	// 143: aload_3
	// 144: invokevirtual 115 java/io/PrintWriter:close ()V
	// 147: goto +32 -> 179
	// 150: astore 8
	// 152: aload_3
	// 153: ifnull +11 -> 164
	// 156: aload_3
	// 157: invokevirtual 112 java/io/PrintWriter:flush ()V
	// 160: aload_3
	// 161: invokevirtual 115 java/io/PrintWriter:close ()V
	// 164: aload 8
	// 166: athrow
	// 167: aload_3
	// 168: ifnull +11 -> 179
	// 171: aload_3
	// 172: invokevirtual 112 java/io/PrintWriter:flush ()V
	// 175: aload_3
	// 176: invokevirtual 115 java/io/PrintWriter:close ()V
	// 179: iconst_0
	// 180: ireturn
	// Line number table:
	// Java source line #69 -> byte code offset #0
	// Java source line #70 -> byte code offset #5
	// Java source line #71 -> byte code offset #10
	// Java source line #72 -> byte code offset #31
	// Java source line #73 -> byte code offset #38
	// Java source line #70 -> byte code offset #41
	// Java source line #76 -> byte code offset #52
	// Java source line #78 -> byte code offset #54
	// Java source line #79 -> byte code offset #58
	// Java source line #78 -> byte code offset #62
	// Java source line #80 -> byte code offset #67
	// Java source line #81 -> byte code offset #74
	// Java source line #82 -> byte code offset #95
	// Java source line #83 -> byte code offset #103
	// Java source line #81 -> byte code offset #110
	// Java source line #85 -> byte code offset #120
	// Java source line #88 -> byte code offset #125
	// Java source line #89 -> byte code offset #130
	// Java source line #91 -> byte code offset #135
	// Java source line #92 -> byte code offset #139
	// Java source line #93 -> byte code offset #143
	// Java source line #90 -> byte code offset #150
	// Java source line #91 -> byte code offset #152
	// Java source line #92 -> byte code offset #156
	// Java source line #93 -> byte code offset #160
	// Java source line #95 -> byte code offset #164
	// Java source line #91 -> byte code offset #167
	// Java source line #92 -> byte code offset #171
	// Java source line #93 -> byte code offset #175
	// Java source line #97 -> byte code offset #179
	// Local variable table:
	// start length slot name signature
	// 0 181 0 this StargateFileReader
	// 0 181 1 s Stargate
	// 4 71 2 allGates ArrayList<Stargate>
	// 6 39 3 i int
	// 53 123 3 pWriter PrintWriter
	// 65 56 4 writer PrintWriter
	// 128 3 4 ioe IOException
	// 93 4 5 g Stargate
	// 78 33 6 localIterator java.util.Iterator
	// 101 5 7 stargateString String
	// 150 15 8 localObject Object
	// Exception table:
	// from to target type
	// 54 125 128 java/io/IOException
	// 54 135 150 finally
	// }

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
			System.out.println("Could not fetch stargateList.txt!");
			return null;
		}
	}

	public String StargateToString(Stargate s) {
		String ret = " " + s.getName() + ";" + s.getWorldID() + ";" + s.getShieldStatus() + ";" + s.getActivationStatus() + ";"
				+ s.getLocation() + ";" + s.getTarget() + ";" + s.getDirection();
		return ret;
	}

	public Stargate StringToStargate(String string) {
		String[] str = string.substring(1).split(";");
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

		s.setDirection(BlockFace.valueOf(str[6]));

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
