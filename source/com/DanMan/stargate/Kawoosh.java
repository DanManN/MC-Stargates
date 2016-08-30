/*     */ package hauptklassen;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Kawoosh
/*     */ {
/*     */   Stargate stargate;
/*  14 */   int id = 0;
/*  15 */   int state_counter = 0;
/*     */   
/*     */   Kawoosh(Stargate s)
/*     */   {
/*  19 */     this.stargate = s;
/*     */   }
/*     */   
/*     */   public void makeKawoosh()
/*     */   {
/*  24 */     if (Main.getInstance() != null)
/*     */     {
/*     */ 
/*  27 */       this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
/*     */       {
/*     */         public void run() {
/*  30 */           Vector normal = Kawoosh.this.stargate.getNormalVector();
/*  31 */           Vector h = new Vector(0, 0, 1);
/*  32 */           Vector side = normal.clone().crossProduct(h);
/*  33 */           Vector start = Kawoosh.this.stargate.getPosition().add(normal.clone().multiply(Stargate.DHD_DISTANCE));
/*     */           
/*  35 */           Kawoosh.this.state_counter += 1;
/*     */           
/*     */ 
/*     */ 
/*  39 */           ArrayList<Vector> kawooshCoords = new ArrayList();
/*  40 */           if (Kawoosh.this.state_counter == 1) kawooshCoords = Kawoosh.this.state1();
/*  41 */           if (Kawoosh.this.state_counter == 2) kawooshCoords = Kawoosh.this.state2();
/*  42 */           if (Kawoosh.this.state_counter == 3) kawooshCoords = Kawoosh.this.state3();
/*  43 */           if (Kawoosh.this.state_counter == 4) kawooshCoords = Kawoosh.this.state4();
/*  44 */           if (Kawoosh.this.state_counter == 5) kawooshCoords = Kawoosh.this.state5();
/*  45 */           if (Kawoosh.this.state_counter == 6) kawooshCoords = Kawoosh.this.state6();
/*  46 */           if (Kawoosh.this.state_counter == 7) kawooshCoords = Kawoosh.this.state7();
/*  47 */           if (Kawoosh.this.state_counter == 8) kawooshCoords = Kawoosh.this.state8();
/*  48 */           if (Kawoosh.this.state_counter == 9) kawooshCoords = Kawoosh.this.state9();
/*  49 */           if (Kawoosh.this.state_counter == 10) kawooshCoords = Kawoosh.this.state10();
/*  50 */           if (Kawoosh.this.state_counter == 11) kawooshCoords = Kawoosh.this.state4();
/*  51 */           if (Kawoosh.this.state_counter == 12) kawooshCoords = Kawoosh.this.state3();
/*  52 */           if (Kawoosh.this.state_counter > 12) { Bukkit.getScheduler().cancelTask(Kawoosh.this.id);
/*     */           }
/*     */           
/*  55 */           Kawoosh.this.cleanKawoosh();
/*     */           
/*  57 */           for (Vector v : kawooshCoords)
/*     */           {
/*     */ 
/*  60 */             Vector k = start.clone().add(side.clone().multiply(v.getX())).add(h.clone().multiply(v.getY())).subtract(normal.clone().multiply(v.getZ()));
/*  61 */             Location location = new Location(
/*  62 */               (org.bukkit.World)Bukkit.getWorlds().get(Kawoosh.this.stargate.worldID), k.getX(), k.getZ(), k.getY());
/*  63 */             Block block = location.getBlock();
/*     */             
/*  65 */             block.setType(Material.STATIONARY_WATER);
/*     */             
/*  67 */             block.setMetadata("PortalWater", new org.bukkit.metadata.FixedMetadataValue(Main.getInstance(), "true"));
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  79 */       }, 0L, 3L);
/*     */     }
/*     */     else {
/*  82 */       System.out.println("kein verweis auf plugin!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Vector> state1()
/*     */   {
/*  89 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*  91 */     coords.add(new Vector(0, 0, 0));
/*  92 */     coords.add(new Vector(2, 2, 0));
/*  93 */     coords.add(new Vector(-2, 2, 0));
/*  94 */     coords.add(new Vector(0, 4, 0));
/*     */     
/*  96 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state2()
/*     */   {
/* 101 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/* 103 */     coords.add(new Vector(0, 0, 0));
/* 104 */     coords.add(new Vector(1, 0, 0));
/* 105 */     coords.add(new Vector(-1, 0, 0));
/*     */     
/* 107 */     coords.add(new Vector(1, 1, 0));
/* 108 */     coords.add(new Vector(1, 3, 0));
/* 109 */     coords.add(new Vector(-1, 3, 0));
/* 110 */     coords.add(new Vector(-1, 1, 0));
/* 111 */     coords.add(new Vector(2, 1, 0));
/* 112 */     coords.add(new Vector(2, 2, 0));
/* 113 */     coords.add(new Vector(-2, 1, 0));
/* 114 */     coords.add(new Vector(-2, 2, 0));
/* 115 */     coords.add(new Vector(2, 3, 0));
/* 116 */     coords.add(new Vector(-2, 3, 0));
/*     */     
/* 118 */     coords.add(new Vector(0, 4, 0));
/* 119 */     coords.add(new Vector(1, 4, 0));
/* 120 */     coords.add(new Vector(-1, 4, 0));
/*     */     
/* 122 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state3()
/*     */   {
/* 127 */     ArrayList<Vector> coords = new ArrayList();
/* 128 */     coords.add(new Vector(0, 0, 0));
/* 129 */     coords.add(new Vector(1, 0, 0));
/* 130 */     coords.add(new Vector(-1, 0, 0));
/*     */     
/* 132 */     coords.add(new Vector(0, 1, 0));
/* 133 */     coords.add(new Vector(0, 2, 0));
/* 134 */     coords.add(new Vector(0, 3, 0));
/* 135 */     coords.add(new Vector(1, 2, 0));
/* 136 */     coords.add(new Vector(-1, 2, 0));
/* 137 */     coords.add(new Vector(1, 3, 0));
/* 138 */     coords.add(new Vector(-1, 3, 0));
/*     */     
/* 140 */     coords.add(new Vector(1, 1, 0));
/* 141 */     coords.add(new Vector(-1, 1, 0));
/* 142 */     coords.add(new Vector(2, 1, 0));
/* 143 */     coords.add(new Vector(2, 2, 0));
/* 144 */     coords.add(new Vector(-2, 1, 0));
/* 145 */     coords.add(new Vector(-2, 2, 0));
/* 146 */     coords.add(new Vector(2, 3, 0));
/* 147 */     coords.add(new Vector(-2, 3, 0));
/*     */     
/* 149 */     coords.add(new Vector(0, 4, 0));
/* 150 */     coords.add(new Vector(1, 4, 0));
/* 151 */     coords.add(new Vector(-1, 4, 0));
/*     */     
/* 153 */     return coords;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Vector> state4()
/*     */   {
/* 159 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */ 
/* 162 */     coords.add(new Vector(0, 1, 1));
/* 163 */     coords.add(new Vector(1, 2, 1));
/* 164 */     coords.add(new Vector(-1, 2, 1));
/* 165 */     coords.add(new Vector(0, 3, 1));
/* 166 */     coords.add(new Vector(0, 2, 2));
/*     */     
/* 168 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state5()
/*     */   {
/* 173 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */     int h;
/* 176 */     for (int s = 0; s < 3; s++) {
/* 177 */       for (h = 1; h < 4; h++) {
/* 178 */         coords.add(new Vector(s - 1, h, 1));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 183 */     for (Vector v : state4()) {
/* 184 */       coords.add(v.clone().add(new Vector(0, 0, 1)));
/*     */     }
/*     */     
/* 187 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state6()
/*     */   {
/* 192 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */     int h;
/* 195 */     for (int s = 0; s < 3; s++) {
/* 196 */       for (h = 1; h < 4; h++) {
/* 197 */         coords.add(new Vector(s - 1, h, 1));
/* 198 */         coords.add(new Vector(s - 1, h, 2));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 204 */     for (Vector v : state4()) {
/* 205 */       coords.add(v.clone().add(new Vector(0, 0, 2)));
/*     */     }
/*     */     
/*     */ 
/* 209 */     coords.add(new Vector(0, 0, 1));
/* 210 */     coords.add(new Vector(0, 0, 2));
/* 211 */     coords.add(new Vector(-2, 2, 1));
/* 212 */     coords.add(new Vector(-2, 2, 2));
/* 213 */     coords.add(new Vector(2, 2, 1));
/* 214 */     coords.add(new Vector(2, 2, 2));
/* 215 */     coords.add(new Vector(0, 4, 1));
/* 216 */     coords.add(new Vector(0, 4, 2));
/*     */     
/* 218 */     coords.add(new Vector(0, 2, 5));
/*     */     
/* 220 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state7()
/*     */   {
/* 225 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */ 
/* 228 */     for (Vector v : state6()) {
/* 229 */       coords.add(v.clone().add(new Vector(0, 0, 1)));
/*     */     }
/*     */     
/* 232 */     coords.remove(new Vector(0, 2, 6));
/*     */     
/*     */ 
/* 235 */     for (Vector v2 : state4()) {
/* 236 */       coords.add(v2.clone());
/*     */     }
/* 238 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state8()
/*     */   {
/* 243 */     ArrayList<Vector> coords = new ArrayList();
/* 244 */     for (Vector v : state7()) {
/* 245 */       coords.add(v.clone().add(new Vector(0, 0, 2)));
/*     */     }
/*     */     
/*     */ 
/* 249 */     for (Vector v : state4()) {
/* 250 */       coords.add(v.clone().add(new Vector(0, 0, 0)));
/* 251 */       coords.add(v.clone().add(new Vector(0, 0, 1)));
/*     */     }
/*     */     
/* 254 */     return coords;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Vector> state9()
/*     */   {
/* 260 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */ 
/* 263 */     coords.add(new Vector(0, 2, 1));
/* 264 */     coords.add(new Vector(0, 2, 2));
/* 265 */     coords.add(new Vector(0, 2, 3));
/* 266 */     coords.add(new Vector(0, 2, 7));
/*     */     
/* 268 */     for (Vector v : state4()) {
/* 269 */       coords.add(v.clone().add(new Vector(0, 0, 3)));
/* 270 */       coords.add(v.clone().add(new Vector(0, 0, 4)));
/*     */     }
/*     */     
/* 273 */     coords.add(new Vector(1, 2, 6));
/* 274 */     coords.add(new Vector(-1, 2, 6));
/*     */     
/* 276 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Vector> state10()
/*     */   {
/* 281 */     ArrayList<Vector> coords = new ArrayList();
/*     */     
/*     */ 
/* 284 */     coords.add(new Vector(0, 2, 1));
/* 285 */     coords.add(new Vector(0, 2, 2));
/*     */     
/*     */ 
/* 288 */     for (Vector v : state4()) {
/* 289 */       coords.add(v.clone().add(new Vector(0, 0, 2)));
/* 290 */       coords.add(v.clone().add(new Vector(0, 0, 3)));
/*     */     }
/*     */     
/* 293 */     return coords;
/*     */   }
/*     */   
/*     */   public void cleanKawoosh() {
/* 297 */     Vector normal = this.stargate.getNormalVector();
/* 298 */     ArrayList<Vector> vecs = this.stargate.getInsideCoordinates();
/* 299 */     int i; for (Iterator localIterator = vecs.iterator(); localIterator.hasNext(); 
/* 300 */         i < 8)
/*     */     {
/* 299 */       Vector v = (Vector)localIterator.next();
/* 300 */       i = 1; continue;
/* 301 */       Vector k = v.clone().subtract(normal.clone().multiply(i));
/* 302 */       Location location = new Location(
/* 303 */         (org.bukkit.World)Bukkit.getWorlds().get(this.stargate.worldID), k.getX(), k.getZ(), k.getY());
/* 304 */       Block block = location.getBlock();
/*     */       
/* 306 */       if (!block.hasMetadata("StargateBlock"))
/*     */       {
/*     */ 
/* 309 */         block.setType(Material.AIR);
/*     */         
/* 311 */         if (block.hasMetadata("PortalWater")) {
/* 312 */           block.removeMetadata("PortalWater", Main.getInstance());
/*     */         }
/* 314 */         if (block.hasMetadata("Kawoosh")) {
/* 315 */           block.removeMetadata("Kawoosh", Main.getInstance());
/*     */         }
/*     */       }
/* 300 */       i++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/Kawoosh.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */