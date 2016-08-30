/*     */ package hauptklassen;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Bukkit;
/*     */ 
/*     */ public class StargateFileReader
/*     */ {
/*     */   public Stargate getStargate(String name)
/*     */   {
/*  18 */     if (name != null) { try { Object localObject1 = null;Object localObject4 = null;
/*     */         Object localObject3;
/*  20 */         try { BufferedReader br = new BufferedReader(new FileReader("plugins/MPStargate/stargateList.txt")); try { String sCurrentLine; while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine; if (!sCurrentLine.startsWith("#")) { Stargate stargate = StringToStargate(sCurrentLine); if (stargate.name.equals(name)) return stargate; } } return null; } finally { if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) ((Throwable)localObject3).addSuppressed(localThrowable); } return null; } catch (IOException e) { e.printStackTrace();System.out.println("Konnte stargateList.txt nicht finden!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveStargate(Stargate stargate)
/*     */   {
/*  44 */     if (getStargate(stargate.name) != null) {
/*  45 */       return false;
/*     */     }
/*     */     
/*  48 */     PrintWriter pWriter = null;
/*     */     try {
/*  50 */       pWriter = new PrintWriter(new BufferedWriter(new FileWriter(
/*  51 */         "plugins/MPStargate/stargateList.txt", true)), true);
/*     */       
/*  53 */       String stargateString = StargateToString(stargate);
/*  54 */       pWriter.println(stargateString);
/*  55 */       return true;
/*     */     } catch (IOException ioe) {
/*  57 */       ioe.printStackTrace();
/*     */     } finally {
/*  59 */       if (pWriter != null) {
/*  60 */         pWriter.flush();
/*  61 */         pWriter.close();
/*     */       }
/*     */     }
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean updateStargate(Stargate s)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 121	hauptklassen/StargateFileReader:getStargateList	()Ljava/util/ArrayList;
/*     */     //   4: astore_2
/*     */     //   5: iconst_0
/*     */     //   6: istore_3
/*     */     //   7: goto +37 -> 44
/*     */     //   10: aload_2
/*     */     //   11: iload_3
/*     */     //   12: invokevirtual 125	java/util/ArrayList:get	(I)Ljava/lang/Object;
/*     */     //   15: checkcast 40	hauptklassen/Stargate
/*     */     //   18: getfield 39	hauptklassen/Stargate:name	Ljava/lang/String;
/*     */     //   21: aload_1
/*     */     //   22: getfield 39	hauptklassen/Stargate:name	Ljava/lang/String;
/*     */     //   25: invokevirtual 45	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   28: ifeq +13 -> 41
/*     */     //   31: aload_2
/*     */     //   32: iload_3
/*     */     //   33: aload_1
/*     */     //   34: invokevirtual 131	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
/*     */     //   37: pop
/*     */     //   38: goto +14 -> 52
/*     */     //   41: iinc 3 1
/*     */     //   44: iload_3
/*     */     //   45: aload_2
/*     */     //   46: invokevirtual 135	java/util/ArrayList:size	()I
/*     */     //   49: if_icmplt -39 -> 10
/*     */     //   52: aconst_null
/*     */     //   53: astore_3
/*     */     //   54: new 92	java/io/PrintWriter
/*     */     //   57: dup
/*     */     //   58: ldc 20
/*     */     //   60: ldc -117
/*     */     //   62: invokespecial 141	java/io/PrintWriter:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   65: astore 4
/*     */     //   67: aload 4
/*     */     //   69: ldc -112
/*     */     //   71: invokevirtual 111	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   74: aload_2
/*     */     //   75: invokevirtual 146	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   78: astore 6
/*     */     //   80: goto +30 -> 110
/*     */     //   83: aload 6
/*     */     //   85: invokeinterface 150 1 0
/*     */     //   90: checkcast 40	hauptklassen/Stargate
/*     */     //   93: astore 5
/*     */     //   95: aload_0
/*     */     //   96: aload 5
/*     */     //   98: invokevirtual 107	hauptklassen/StargateFileReader:StargateToString	(Lhauptklassen/Stargate;)Ljava/lang/String;
/*     */     //   101: astore 7
/*     */     //   103: aload 4
/*     */     //   105: aload 7
/*     */     //   107: invokevirtual 111	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   110: aload 6
/*     */     //   112: invokeinterface 156 1 0
/*     */     //   117: ifne -34 -> 83
/*     */     //   120: aload 4
/*     */     //   122: invokevirtual 115	java/io/PrintWriter:close	()V
/*     */     //   125: goto +42 -> 167
/*     */     //   128: astore 4
/*     */     //   130: aload 4
/*     */     //   132: invokevirtual 62	java/io/IOException:printStackTrace	()V
/*     */     //   135: aload_3
/*     */     //   136: ifnull +43 -> 179
/*     */     //   139: aload_3
/*     */     //   140: invokevirtual 112	java/io/PrintWriter:flush	()V
/*     */     //   143: aload_3
/*     */     //   144: invokevirtual 115	java/io/PrintWriter:close	()V
/*     */     //   147: goto +32 -> 179
/*     */     //   150: astore 8
/*     */     //   152: aload_3
/*     */     //   153: ifnull +11 -> 164
/*     */     //   156: aload_3
/*     */     //   157: invokevirtual 112	java/io/PrintWriter:flush	()V
/*     */     //   160: aload_3
/*     */     //   161: invokevirtual 115	java/io/PrintWriter:close	()V
/*     */     //   164: aload 8
/*     */     //   166: athrow
/*     */     //   167: aload_3
/*     */     //   168: ifnull +11 -> 179
/*     */     //   171: aload_3
/*     */     //   172: invokevirtual 112	java/io/PrintWriter:flush	()V
/*     */     //   175: aload_3
/*     */     //   176: invokevirtual 115	java/io/PrintWriter:close	()V
/*     */     //   179: iconst_0
/*     */     //   180: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #69	-> byte code offset #0
/*     */     //   Java source line #70	-> byte code offset #5
/*     */     //   Java source line #71	-> byte code offset #10
/*     */     //   Java source line #72	-> byte code offset #31
/*     */     //   Java source line #73	-> byte code offset #38
/*     */     //   Java source line #70	-> byte code offset #41
/*     */     //   Java source line #76	-> byte code offset #52
/*     */     //   Java source line #78	-> byte code offset #54
/*     */     //   Java source line #79	-> byte code offset #58
/*     */     //   Java source line #78	-> byte code offset #62
/*     */     //   Java source line #80	-> byte code offset #67
/*     */     //   Java source line #81	-> byte code offset #74
/*     */     //   Java source line #82	-> byte code offset #95
/*     */     //   Java source line #83	-> byte code offset #103
/*     */     //   Java source line #81	-> byte code offset #110
/*     */     //   Java source line #85	-> byte code offset #120
/*     */     //   Java source line #88	-> byte code offset #125
/*     */     //   Java source line #89	-> byte code offset #130
/*     */     //   Java source line #91	-> byte code offset #135
/*     */     //   Java source line #92	-> byte code offset #139
/*     */     //   Java source line #93	-> byte code offset #143
/*     */     //   Java source line #90	-> byte code offset #150
/*     */     //   Java source line #91	-> byte code offset #152
/*     */     //   Java source line #92	-> byte code offset #156
/*     */     //   Java source line #93	-> byte code offset #160
/*     */     //   Java source line #95	-> byte code offset #164
/*     */     //   Java source line #91	-> byte code offset #167
/*     */     //   Java source line #92	-> byte code offset #171
/*     */     //   Java source line #93	-> byte code offset #175
/*     */     //   Java source line #97	-> byte code offset #179
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	181	0	this	StargateFileReader
/*     */     //   0	181	1	s	Stargate
/*     */     //   4	71	2	allGates	ArrayList<Stargate>
/*     */     //   6	39	3	i	int
/*     */     //   53	123	3	pWriter	PrintWriter
/*     */     //   65	56	4	writer	PrintWriter
/*     */     //   128	3	4	ioe	IOException
/*     */     //   93	4	5	g	Stargate
/*     */     //   78	33	6	localIterator	java.util.Iterator
/*     */     //   101	5	7	stargateString	String
/*     */     //   150	15	8	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   54	125	128	java/io/IOException
/*     */     //   54	135	150	finally
/*     */   }
/*     */   
/*     */   public boolean deleteStargate(Stargate stargate)
/*     */     throws IOException
/*     */   {
/* 102 */     File inputFile = new File("plugins/MPStargate/stargateList.txt");
/*     */     
/* 104 */     File tempFile = new File("plugins/MPStargate/MPStargateTempFile.txt");
/*     */     
/* 106 */     BufferedReader reader = new BufferedReader(new FileReader(inputFile));
/* 107 */     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
/*     */     
/*     */     String currentLine;
/*     */     
/* 111 */     while ((currentLine = reader.readLine()) != null) {
/*     */       String currentLine;
/* 113 */       if ((!currentLine.contains(stargate.name)) && 
/* 114 */         (!currentLine.startsWith("#")))
/*     */       {
/*     */ 
/* 117 */         writer.write(currentLine);
/* 118 */         writer.newLine();
/*     */       } }
/* 120 */     reader.close();
/* 121 */     writer.close();
/*     */     
/* 123 */     inputFile.delete();
/* 124 */     tempFile.renameTo(inputFile);
/*     */     
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   public ArrayList<Stargate> getStargateList() {
/* 130 */     ArrayList<Stargate> GateList = new ArrayList();
/* 131 */     try { Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(
/* 132 */           "plugins/MPStargate/stargateList.txt"));
/*     */         try
/*     */         {
/*     */           String sCurrentLine;
/* 136 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/* 137 */             if (!sCurrentLine.startsWith("#")) {
/* 138 */               Stargate stargate = StringToStargate(sCurrentLine);
/* 139 */               GateList.add(stargate);
/*     */             }
/*     */           }
/* 142 */           return GateList;
/*     */         } finally {
/* 144 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*     */       
/* 148 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 145 */       e.printStackTrace();
/* 146 */       System.out.println("Konnte stargateList.txt nicht finden!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String StargateToString(Stargate s)
/*     */   {
/* 155 */     String ret = s.name + ";" + s.worldID + ";" + s.shieldStatus + ";" + s.activationStatus + ";" + s.loc + ";" + s.target + ";" + s.direction;
/* 156 */     return ret;
/*     */   }
/*     */   
/*     */   public Stargate StringToStargate(String string) {
/* 160 */     String[] str = string.split(";");
/* 161 */     Stargate s = new Stargate();
/*     */     
/* 163 */     s.name = str[0];
/* 164 */     s.worldID = Integer.parseInt(str[1]);
/* 165 */     s.shieldStatus = Boolean.parseBoolean(str[2]);
/* 166 */     s.activationStatus = Boolean.parseBoolean(str[3]);
/*     */     
/* 168 */     double locX = Float.valueOf(str[4].split(",")[1].split("=")[1]).floatValue();
/* 169 */     double locY = Float.valueOf(str[4].split(",")[2].split("=")[1]).floatValue();
/* 170 */     double locZ = Float.valueOf(str[4].split(",")[3].split("=")[1]).floatValue();
/* 171 */     s.loc = new org.bukkit.Location((org.bukkit.World)Bukkit.getWorlds().get(s.worldID), locX, locY, locZ);
/*     */     
/* 173 */     s.target = str[5];
/*     */     
/* 175 */     s.direction = str[6];
/*     */     
/* 177 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Stargate> getActivStartGates()
/*     */   {
/* 183 */     ArrayList<Stargate> GateList = new ArrayList();
/* 184 */     try { Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(
/* 185 */           "plugins/MPStargate/stargateList.txt"));
/*     */         try
/*     */         {
/*     */           String sCurrentLine;
/* 189 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/* 190 */             if (!sCurrentLine.startsWith("#")) {
/* 191 */               Stargate stargate = StringToStargate(sCurrentLine);
/* 192 */               if ((stargate.activationStatus) && (!stargate.target.equals("null"))) {
/* 193 */                 GateList.add(stargate);
/*     */               }
/*     */             }
/*     */           }
/* 197 */           return GateList;
/*     */         } finally {
/* 199 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*     */       
/* 203 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 200 */       e.printStackTrace();
/* 201 */       System.out.println("Konnte stargateList.txt nicht finden!");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/StargateFileReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */