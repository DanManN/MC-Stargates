/*     */ package hauptklassen;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.material.MaterialData;
/*     */ 
/*     */ public class ConfigFileReader
/*     */ {
/*  15 */   int activationTime = 0;
/*  16 */   Material DHDMaterial = Material.OBSIDIAN;
/*  17 */   Material GateMaterial = Material.OBSIDIAN;
/*  18 */   Material ChevronMaterial = Material.REDSTONE_BLOCK;
/*  19 */   Material ShieldMaterial = Material.STONE;
/*  20 */   int DHD_Distance = 9;
/*  21 */   int KawooshSoundRadius = 20;
/*  22 */   int KawooshSoundVolume = 10;
/*  23 */   int IrisDamage = 18;
/*  24 */   int IrisDestroyInventory = 1;
/*  25 */   String IrisNoTeleport = "false";
/*  26 */   String Language = "en";
/*  27 */   Material RingMaterial = Material.getMaterial(44);
/*  28 */   byte RingMaterial_data = 0;
/*  29 */   Material RingGroundMaterial = Material.SMOOTH_BRICK.getNewData((byte)3).getItemType();
/*  30 */   byte RingGroundMaterial_data = 3;
/*  31 */   int RingDistance = 2;
/*  32 */   Sound KawooshSound = Sound.ENDERDRAGON_GROWL;
/*     */   
/*  34 */   String InterWorldConnectionCosts = "none";
/*     */   
/*     */ 
/*     */   int networkTeleportUnallowedPlayers;
/*     */   
/*     */ 
/*  40 */   Map<String, String> VALUES = new HashMap();
/*     */   
/*     */ 
/*     */   public boolean getConfig()
/*     */   {
/*     */     try
/*     */     {
/*  47 */       Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(
/*  48 */           "plugins/MPStargate/mpgatesConfig.txt"));
/*     */         try {
/*     */           String sCurrentLine;
/*  51 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/*  52 */             if (!sCurrentLine.startsWith("#"))
/*     */             {
/*  54 */               String[] a = sCurrentLine.split("#")[0].split(":");
/*  55 */               String key = a[0].trim();
/*  56 */               String value = a[1].trim();
/*     */               
/*  58 */               this.VALUES.put(key, value);
/*     */             }
/*     */           }
/*  61 */           return true;
/*     */         } finally {
/*  63 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*     */         }
/*     */       }
/*  66 */       return false;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  64 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   ConfigFileReader()
/*     */   {
/*  71 */     if (getConfig()) {
/*  72 */       if ((this.VALUES.get("activationTime") != null) && 
/*  73 */         (Integer.parseInt((String)this.VALUES.get("activationTime")) >= 0)) {
/*  74 */         this.activationTime = Integer.parseInt(
/*  75 */           (String)this.VALUES.get("activationTime"));
/*     */       }
/*     */       
/*     */ 
/*  79 */       if (Material.getMaterial((String)this.VALUES.get("DHDMaterial")) != null) {
/*  80 */         if (Material.getMaterial((String)this.VALUES.get("DHDMaterial")).isSolid())
/*     */         {
/*  82 */           if (!Material.getMaterial((String)this.VALUES.get("DHDMaterial")).hasGravity()) {
/*  83 */             this.DHDMaterial = Material.getMaterial(
/*  84 */               (String)this.VALUES.get("DHDMaterial"));
/*     */           }
/*     */         }
/*     */       } else {
/*  88 */         System.out.println("[WARNING] Wrong Material for DHD in configfile!");
/*     */       }
/*     */       
/*  91 */       if (Material.getMaterial((String)this.VALUES.get("GateMaterial")) != null) {
/*  92 */         if (Material.getMaterial((String)this.VALUES.get("GateMaterial")).isSolid())
/*     */         {
/*  94 */           if (!Material.getMaterial((String)this.VALUES.get("GateMaterial")).hasGravity()) {
/*  95 */             this.GateMaterial = Material.getMaterial(
/*  96 */               (String)this.VALUES.get("GateMaterial"));
/*     */           }
/*     */         }
/*     */       } else {
/* 100 */         System.out.println("[WARNING] Wrong Material for Stargate in configfile!");
/*     */       }
/*     */       
/* 103 */       if (Material.getMaterial((String)this.VALUES.get("ChevronMaterial")) != null)
/*     */       {
/* 105 */         if (Material.getMaterial((String)this.VALUES.get("ChevronMaterial")).isSolid())
/*     */         {
/* 107 */           if (!Material.getMaterial((String)this.VALUES.get("ChevronMaterial")).hasGravity()) {
/* 108 */             this.ChevronMaterial = Material.getMaterial(
/* 109 */               (String)this.VALUES.get("ChevronMaterial"));
/*     */           }
/*     */         }
/*     */       } else {
/* 113 */         System.out.println("[WARNING] Wrong Material for Chevrons in configfile!");
/*     */       }
/*     */       
/* 116 */       if ((Material.getMaterial((String)this.VALUES.get(this.ShieldMaterial)) != null) && 
/* 117 */         (Material.getMaterial((String)this.VALUES.get(this.ShieldMaterial)).isSolid())) {
/* 118 */         this.ShieldMaterial = Material.getMaterial(
/* 119 */           (String)this.VALUES.get(this.ShieldMaterial));
/*     */       }
/*     */       
/*     */ 
/* 123 */       if ((this.VALUES.get("DHDDistance") != null) && 
/* 124 */         (Integer.parseInt((String)this.VALUES.get("DHDDistance")) >= 8)) {
/* 125 */         this.DHD_Distance = Integer.parseInt((String)this.VALUES.get("DHDDistance"));
/*     */       }
/*     */       
/* 128 */       if ((this.VALUES.get("KawooshSoundRadius") != null) && 
/* 129 */         (Integer.parseInt((String)this.VALUES.get("KawooshSoundRadius")) >= 0)) {
/* 130 */         this.KawooshSoundRadius = Integer.parseInt(
/* 131 */           (String)this.VALUES.get("KawooshSoundRadius"));
/*     */       }
/*     */       
/*     */ 
/* 135 */       if ((this.VALUES.get("KawooshSoundVolume") != null) && 
/* 136 */         (Integer.parseInt((String)this.VALUES.get("KawooshSoundVolume")) >= 0)) {
/* 137 */         this.KawooshSoundVolume = Integer.parseInt(
/* 138 */           (String)this.VALUES.get("KawooshSoundVolume"));
/*     */       }
/*     */       
/*     */ 
/* 142 */       if ((this.VALUES.get("IrisDamage") != null) && 
/* 143 */         (Integer.parseInt((String)this.VALUES.get("IrisDamage")) >= 0)) {
/* 144 */         this.IrisDamage = Integer.parseInt((String)this.VALUES.get("IrisDamage"));
/*     */       }
/*     */       
/*     */ 
/* 148 */       if ((this.VALUES.get("IrisDestroyInventory") != null) && (
/* 149 */         (Integer.parseInt((String)this.VALUES.get("IrisDestroyInventory")) == 0) || (Integer.parseInt((String)this.VALUES.get("IrisDestroyInventory")) == 1))) {
/* 150 */         this.IrisDestroyInventory = Integer.parseInt((String)this.VALUES.get("IrisDamage"));
/*     */       }
/*     */       
/*     */ 
/* 154 */       if ((this.VALUES.get("Language") != null) && 
/* 155 */         (((String)this.VALUES.get("Language")).length() > 0)) {
/* 156 */         this.Language = ((String)this.VALUES.get("Language"));
/*     */       }
/*     */       
/*     */ 
/* 160 */       if ((this.VALUES.get("IrisNoTeleport") != null) && 
/* 161 */         (((String)this.VALUES.get("IrisNoTeleport")).length() > 0)) {
/* 162 */         this.IrisNoTeleport = ((String)this.VALUES.get("IrisNoTeleport"));
/*     */       }
/*     */       
/* 165 */       if ((this.VALUES.get("NetworkTeleportUnallowedPlayers") != null) && 
/* 166 */         (Integer.parseInt((String)this.VALUES.get("NetworkTeleportUnallowedPlayers")) >= 0)) {
/* 167 */         this.networkTeleportUnallowedPlayers = Integer.parseInt((String)this.VALUES.get("NetworkTeleportUnallowedPlayers"));
/*     */       }
/*     */       
/*     */ 
/* 171 */       if (this.VALUES.get("RingMaterial") != null)
/*     */       {
/* 173 */         if (((String)this.VALUES.get("RingMaterial")).length() > 0) {
/* 174 */           String[] s = ((String)this.VALUES.get("RingMaterial")).split(",");
/* 175 */           if (s.length > 0) {
/* 176 */             this.RingMaterial = Material.getMaterial(Integer.parseInt(s[0]));
/* 177 */             this.RingMaterial_data = Byte.parseByte(s[1]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 183 */       if ((this.VALUES.get("RingGroundMaterial") != null) && 
/* 184 */         (((String)this.VALUES.get("RingGroundMaterial")).length() > 0)) {
/* 185 */         String[] value = ((String)this.VALUES.get("RingGroundMaterial")).split(",");
/* 186 */         if (value.length > 0) {
/* 187 */           this.RingGroundMaterial = Material.getMaterial(Integer.parseInt(value[0]));
/* 188 */           this.RingGroundMaterial_data = Byte.parseByte(value[1]);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 194 */       if (this.VALUES.get("RingsDistance") != null)
/*     */       {
/* 196 */         if ((Integer.parseInt((String)this.VALUES.get("RingDistance")) > -2) || (Integer.parseInt((String)this.VALUES.get("RingDistance")) <= -5)) {
/* 197 */           this.RingDistance = Integer.parseInt((String)this.VALUES.get("RingDistance"));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 203 */       if (this.VALUES.get("InterWorldConnectionCosts") != null)
/*     */       {
/* 205 */         String s = (String)this.VALUES.get("InterWorldConnectionCosts");
/* 206 */         String[] s2 = s.split(",");
/* 207 */         if (s2.length == 2) {
/* 208 */           if ((Integer.parseInt(s2[0]) >= 0) && (Integer.parseInt(s2[1]) >= 0)) {
/* 209 */             this.InterWorldConnectionCosts = ((String)this.VALUES.get("InterWorldConnectionCosts"));
/*     */           }
/*     */         } else {
/* 212 */           this.InterWorldConnectionCosts = "none";
/*     */         }
/*     */       }
/*     */       
/* 216 */       if ((this.VALUES.get("KawooshSound") != null) && 
/* 217 */         (Sound.valueOf((String)this.VALUES.get("KawooshSound")) != null)) {
/* 218 */         this.KawooshSound = Sound.valueOf((String)this.VALUES.get("KawooshSound"));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/ConfigFileReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */