/*     */ package hauptklassen;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class GateNetwork
/*     */ {
/*     */   String name;
/*  12 */   ArrayList<String> networkadmins = new ArrayList();
/*  13 */   ArrayList<String> networkmembers = new ArrayList();
/*  14 */   ArrayList<String> networkstargates = new ArrayList();
/*  15 */   String state = "public";
/*  16 */   String filepath = "plugins/MPStargate/networkList.txt";
/*     */   
/*     */   GateNetwork(String founder, String name) {
/*  19 */     this.networkadmins.add(founder);
/*  20 */     this.name = name;
/*     */   }
/*     */   
/*     */   public boolean addAdmin(String p) {
/*  24 */     if (!this.networkadmins.contains(p)) {
/*  25 */       this.networkadmins.add(p);
/*  26 */       return true;
/*     */     }
/*  28 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeAdmin(String p) {
/*  32 */     if (this.networkadmins.contains(p)) {
/*  33 */       this.networkadmins.remove(p);
/*  34 */       return true;
/*     */     }
/*  36 */     return false;
/*     */   }
/*     */   
/*     */   public boolean addMember(String p) {
/*  40 */     if (!this.networkmembers.contains(p)) {
/*  41 */       this.networkmembers.add(p);
/*  42 */       return true;
/*     */     }
/*  44 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeMember(String p) {
/*  48 */     if (this.networkmembers.contains(p)) {
/*  49 */       this.networkmembers.remove(p);
/*  50 */       return true;
/*     */     }
/*  52 */     return false;
/*     */   }
/*     */   
/*     */   public boolean addGate(String s) {
/*  56 */     if (!this.networkstargates.contains(s)) {
/*  57 */       this.networkstargates.add(s);
/*  58 */       return true;
/*     */     }
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeGate(String s) {
/*  64 */     if (this.networkstargates.contains(s)) {
/*  65 */       this.networkstargates.remove(s);
/*  66 */       return true;
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPrivate() {
/*  72 */     if (this.state.equalsIgnoreCase("private")) {
/*  73 */       return true;
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */   
/*  78 */   public boolean isPublic() { if (this.state.equalsIgnoreCase("public")) {
/*  79 */       return true;
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public boolean makePrivate() {
/*  85 */     if (this.state.equals("public")) {
/*  86 */       this.state = "private";
/*  87 */       return true;
/*     */     }
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public boolean makePublic() {
/*  93 */     if (this.state.equals("private")) {
/*  94 */       this.state = "public";
/*  95 */       return true;
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   public boolean allowsPlayer(String name) {
/* 101 */     if ((this.state.equals("private")) && 
/* 102 */       (!this.networkmembers.contains(name)) && 
/* 103 */       (!this.networkadmins.contains(name))) {
/* 104 */       return false;
/*     */     }
/*     */     
/* 107 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasAdmin(String name) {
/* 111 */     if (!this.networkadmins.contains(name)) {
/* 112 */       return false;
/*     */     }
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public ArrayList<GateNetwork> getNetworkList()
/*     */   {
/* 119 */     ArrayList<GateNetwork> networkList = new ArrayList();
/* 120 */     try { Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(this.filepath));
/*     */         try
/*     */         {
/*     */           String sCurrentLine;
/* 124 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/* 125 */             if (!sCurrentLine.startsWith("#")) {
/* 126 */               GateNetwork network = StringToNetwork(sCurrentLine);
/* 127 */               networkList.add(network);
/*     */             }
/*     */           }
/* 130 */           return networkList;
/*     */         } finally {
/* 132 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*     */       
/* 136 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 133 */       e.printStackTrace();
/* 134 */       System.out.println("Konnte networkList.txt nicht finden!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GateNetwork getNetwork(String name)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(this.filepath));
/*     */         try
/*     */         {
/*     */           String sCurrentLine;
/* 150 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/* 151 */             if (!sCurrentLine.startsWith("#")) {
/* 152 */               GateNetwork network = StringToNetwork(sCurrentLine);
/* 153 */               if (network.name.equals(name)) {
/* 154 */                 return network;
/*     */               }
/*     */             }
/*     */           }
/* 158 */           return null;
/*     */         } finally {
/* 160 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*     */       
/* 164 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 161 */       e.printStackTrace();
/* 162 */       System.out.println("Konnte networkList.txt nicht finden!");
/*     */     }
/*     */   }
/*     */   
/*     */   private GateNetwork StringToNetwork(String s)
/*     */   {
/* 168 */     if (s.equals("")) {
/* 169 */       return null;
/*     */     }
/* 171 */     String[] a = s.split(";");
/* 172 */     String name = a[0];
/* 173 */     String[] admins = a[1].split(",");
/* 174 */     String[] members = a[2].split(",");
/* 175 */     String[] gates = a[3].split(",");
/* 176 */     String state = a[4];
/*     */     
/* 178 */     String founder = admins[0];
/* 179 */     GateNetwork g = new GateNetwork(founder, name);
/*     */     String[] arrayOfString1;
/* 181 */     int j = (arrayOfString1 = admins).length; for (int i = 0; i < j; i++) { String admin = arrayOfString1[i];
/* 182 */       g.addAdmin(admin);
/*     */     }
/* 184 */     j = (arrayOfString1 = members).length; for (i = 0; i < j; i++) { String member = arrayOfString1[i];
/* 185 */       g.addMember(member);
/*     */     }
/*     */     
/* 188 */     j = (arrayOfString1 = gates).length; for (i = 0; i < j; i++) { String gate = arrayOfString1[i];
/* 189 */       g.addGate(gate);
/*     */     }
/* 191 */     g.state = state;
/*     */     
/* 193 */     return g;
/*     */   }
/*     */   
/*     */   private String NetworkToString(GateNetwork g) {
/* 197 */     String s = "";
/* 198 */     s = s + this.name + ";";
/* 199 */     for (int i = 0; i < g.networkadmins.size(); i++) {
/* 200 */       s = s + (String)g.networkadmins.get(i);
/* 201 */       if (i != g.networkadmins.size() - 1) {
/* 202 */         s = s + ",";
/*     */       }
/*     */     }
/* 205 */     s = s + ";";
/*     */     
/* 207 */     for (int i = 0; i < g.networkmembers.size(); i++) {
/* 208 */       s = s + (String)g.networkmembers.get(i);
/* 209 */       if (i != g.networkmembers.size() - 1) {
/* 210 */         s = s + ",";
/*     */       }
/*     */     }
/* 213 */     s = s + ";";
/*     */     
/* 215 */     for (int i = 0; i < g.networkstargates.size(); i++) {
/* 216 */       s = s + (String)g.networkstargates.get(i);
/* 217 */       if (i != g.networkstargates.size() - 1) {
/* 218 */         s = s + ",";
/*     */       }
/*     */     }
/* 221 */     s = s + ";";
/*     */     
/* 223 */     s = s + g.state;
/*     */     
/* 225 */     return s;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean save()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 200	hauptklassen/GateNetwork:getNetworkList	()Ljava/util/ArrayList;
/*     */     //   4: astore_1
/*     */     //   5: iconst_0
/*     */     //   6: istore_2
/*     */     //   7: goto +37 -> 44
/*     */     //   10: aload_1
/*     */     //   11: iload_2
/*     */     //   12: invokevirtual 189	java/util/ArrayList:get	(I)Ljava/lang/Object;
/*     */     //   15: checkcast 1	hauptklassen/GateNetwork
/*     */     //   18: getfield 42	hauptklassen/GateNetwork:name	Ljava/lang/String;
/*     */     //   21: aload_0
/*     */     //   22: getfield 42	hauptklassen/GateNetwork:name	Ljava/lang/String;
/*     */     //   25: invokevirtual 76	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   28: ifeq +13 -> 41
/*     */     //   31: aload_1
/*     */     //   32: iload_2
/*     */     //   33: aload_0
/*     */     //   34: invokevirtual 202	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
/*     */     //   37: pop
/*     */     //   38: goto +14 -> 52
/*     */     //   41: iinc 2 1
/*     */     //   44: iload_2
/*     */     //   45: aload_1
/*     */     //   46: invokevirtual 193	java/util/ArrayList:size	()I
/*     */     //   49: if_icmplt -39 -> 10
/*     */     //   52: aconst_null
/*     */     //   53: astore_2
/*     */     //   54: new 206	java/io/PrintWriter
/*     */     //   57: dup
/*     */     //   58: aload_0
/*     */     //   59: getfield 36	hauptklassen/GateNetwork:filepath	Ljava/lang/String;
/*     */     //   62: ldc -48
/*     */     //   64: invokespecial 210	java/io/PrintWriter:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   67: astore_3
/*     */     //   68: aload_3
/*     */     //   69: ldc -45
/*     */     //   71: invokevirtual 213	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   74: aload_1
/*     */     //   75: invokevirtual 214	java/util/ArrayList:iterator	()Ljava/util/Iterator;
/*     */     //   78: astore 5
/*     */     //   80: goto +29 -> 109
/*     */     //   83: aload 5
/*     */     //   85: invokeinterface 218 1 0
/*     */     //   90: checkcast 1	hauptklassen/GateNetwork
/*     */     //   93: astore 4
/*     */     //   95: aload_0
/*     */     //   96: aload 4
/*     */     //   98: invokespecial 224	hauptklassen/GateNetwork:NetworkToString	(Lhauptklassen/GateNetwork;)Ljava/lang/String;
/*     */     //   101: astore 6
/*     */     //   103: aload_3
/*     */     //   104: aload 6
/*     */     //   106: invokevirtual 213	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   109: aload 5
/*     */     //   111: invokeinterface 226 1 0
/*     */     //   116: ifne -33 -> 83
/*     */     //   119: aload_1
/*     */     //   120: aload_0
/*     */     //   121: invokevirtual 51	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
/*     */     //   124: ifne +12 -> 136
/*     */     //   127: aload_3
/*     */     //   128: aload_0
/*     */     //   129: aload_0
/*     */     //   130: invokespecial 224	hauptklassen/GateNetwork:NetworkToString	(Lhauptklassen/GateNetwork;)Ljava/lang/String;
/*     */     //   133: invokevirtual 213	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   136: aload_3
/*     */     //   137: invokevirtual 229	java/io/PrintWriter:close	()V
/*     */     //   140: goto +40 -> 180
/*     */     //   143: astore_3
/*     */     //   144: aload_3
/*     */     //   145: invokevirtual 117	java/io/IOException:printStackTrace	()V
/*     */     //   148: aload_2
/*     */     //   149: ifnull +43 -> 192
/*     */     //   152: aload_2
/*     */     //   153: invokevirtual 230	java/io/PrintWriter:flush	()V
/*     */     //   156: aload_2
/*     */     //   157: invokevirtual 229	java/io/PrintWriter:close	()V
/*     */     //   160: goto +32 -> 192
/*     */     //   163: astore 7
/*     */     //   165: aload_2
/*     */     //   166: ifnull +11 -> 177
/*     */     //   169: aload_2
/*     */     //   170: invokevirtual 230	java/io/PrintWriter:flush	()V
/*     */     //   173: aload_2
/*     */     //   174: invokevirtual 229	java/io/PrintWriter:close	()V
/*     */     //   177: aload 7
/*     */     //   179: athrow
/*     */     //   180: aload_2
/*     */     //   181: ifnull +11 -> 192
/*     */     //   184: aload_2
/*     */     //   185: invokevirtual 230	java/io/PrintWriter:flush	()V
/*     */     //   188: aload_2
/*     */     //   189: invokevirtual 229	java/io/PrintWriter:close	()V
/*     */     //   192: iconst_0
/*     */     //   193: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #229	-> byte code offset #0
/*     */     //   Java source line #230	-> byte code offset #5
/*     */     //   Java source line #231	-> byte code offset #10
/*     */     //   Java source line #232	-> byte code offset #31
/*     */     //   Java source line #233	-> byte code offset #38
/*     */     //   Java source line #230	-> byte code offset #41
/*     */     //   Java source line #236	-> byte code offset #52
/*     */     //   Java source line #238	-> byte code offset #54
/*     */     //   Java source line #239	-> byte code offset #68
/*     */     //   Java source line #240	-> byte code offset #74
/*     */     //   Java source line #241	-> byte code offset #95
/*     */     //   Java source line #242	-> byte code offset #103
/*     */     //   Java source line #240	-> byte code offset #109
/*     */     //   Java source line #244	-> byte code offset #119
/*     */     //   Java source line #245	-> byte code offset #127
/*     */     //   Java source line #247	-> byte code offset #136
/*     */     //   Java source line #249	-> byte code offset #140
/*     */     //   Java source line #250	-> byte code offset #144
/*     */     //   Java source line #252	-> byte code offset #148
/*     */     //   Java source line #253	-> byte code offset #152
/*     */     //   Java source line #254	-> byte code offset #156
/*     */     //   Java source line #251	-> byte code offset #163
/*     */     //   Java source line #252	-> byte code offset #165
/*     */     //   Java source line #253	-> byte code offset #169
/*     */     //   Java source line #254	-> byte code offset #173
/*     */     //   Java source line #256	-> byte code offset #177
/*     */     //   Java source line #252	-> byte code offset #180
/*     */     //   Java source line #253	-> byte code offset #184
/*     */     //   Java source line #254	-> byte code offset #188
/*     */     //   Java source line #258	-> byte code offset #192
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	194	0	this	GateNetwork
/*     */     //   4	116	1	allNetworks	ArrayList<GateNetwork>
/*     */     //   6	39	2	i	int
/*     */     //   53	136	2	pWriter	java.io.PrintWriter
/*     */     //   67	70	3	writer	java.io.PrintWriter
/*     */     //   143	2	3	ioe	IOException
/*     */     //   93	4	4	network	GateNetwork
/*     */     //   78	32	5	localIterator	java.util.Iterator
/*     */     //   101	4	6	networkString	String
/*     */     //   163	15	7	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   54	140	143	java/io/IOException
/*     */     //   54	148	163	finally
/*     */   }
/*     */   
/*     */   public void setName(String string)
/*     */   {
/* 264 */     this.name = string;
/* 265 */     StargateFileReader sfr = new StargateFileReader();
/* 266 */     ArrayList<Stargate> l = sfr.getStargateList();
/*     */     
/* 268 */     for (Stargate s : l) {
/* 269 */       s.updateSign();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/GateNetwork.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */