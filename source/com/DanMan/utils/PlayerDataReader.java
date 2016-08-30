/*     */ package hauptklassen;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class PlayerDataReader
/*     */ {
/*     */   Player player;
/*     */   String filepath;
/*     */   
/*     */   PlayerDataReader(Player p)
/*     */   {
/*  20 */     this.player = p;
/*  21 */     this.filepath = ("plugins/MPStargate/players/" + p.getName() + ".txt");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean createPlayerData()
/*     */   {
/*  27 */     if (!checkForPlayerData()) {
/*  28 */       File playerData = new File(this.filepath);
/*     */       
/*     */ 
/*  31 */       File parent = playerData.getParentFile();
/*  32 */       if ((!parent.exists()) && (!parent.mkdirs())) {
/*  33 */         throw new IllegalStateException("Couldn't create dir: " + parent);
/*     */       }
/*  35 */       if (!playerData.exists()) {
/*     */         try {
/*  37 */           PrintWriter writer = new PrintWriter(
/*  38 */             this.filepath, "UTF-8");
/*  39 */           writer.println("# Saves all the Stargates a player discovered");
/*  40 */           writer.close();
/*     */         }
/*     */         catch (Exception e) {
/*  43 */           System.out.println("[WARNING] Couldn't create " + this.player.getName() + ".txt!");
/*     */         }
/*     */       }
/*     */     }
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public boolean checkForPlayerData()
/*     */   {
/*  52 */     File playerData = new File(this.filepath);
/*  53 */     if (!playerData.exists()) {
/*  54 */       return false;
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<String> getKnownStargates()
/*     */   {
/*  62 */     ArrayList<String> ret = new ArrayList();
/*  63 */     try { Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(
/*  64 */           this.filepath));
/*     */         try {
/*     */           String sCurrentLine;
/*  67 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/*  68 */             if (!sCurrentLine.startsWith("#"))
/*     */             {
/*  70 */               String a = sCurrentLine.split("#")[0];
/*  71 */               ret.add(a);
/*     */             }
/*     */           }
/*  74 */           for (String s : ret) {
/*  75 */             StargateFileReader sfr = new StargateFileReader();
/*  76 */             if (sfr.getStargate(s) == null) {
/*  77 */               ret.remove(s);
/*     */             }
/*     */           }
/*  80 */           return ret;
/*     */         } finally {
/*  82 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*  85 */       return null;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  83 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean knowsGate(String name)
/*     */   {
/*  89 */     ArrayList<String> l = getKnownStargates();
/*  90 */     if (l != null) {
/*  91 */       for (String s : l) {
/*  92 */         if (s.equals(name)) {
/*  93 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean saveStargate(String name)
/*     */   {
/* 103 */     if (getKnownStargates().contains(name)) {
/* 104 */       return false;
/*     */     }
/*     */     
/* 107 */     PrintWriter pWriter = null;
/*     */     try {
/* 109 */       pWriter = new PrintWriter(new java.io.BufferedWriter(new FileWriter(
/* 110 */         this.filepath, true)), true);
/*     */       
/* 112 */       pWriter.println(name);
/* 113 */       return true;
/*     */     } catch (IOException ioe) {
/* 115 */       ioe.printStackTrace();
/*     */     } finally {
/* 117 */       if (pWriter != null) {
/* 118 */         pWriter.flush();
/* 119 */         pWriter.close();
/*     */       }
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/PlayerDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */