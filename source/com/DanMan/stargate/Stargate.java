/*     */ package hauptklassen;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.metadata.FixedMetadataValue;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Stargate implements Listener
/*     */ {
/*  23 */   public static int DHD_DISTANCE = Main.configValues.DHD_Distance;
/*  24 */   public static Material GATE_MATERIAL = Main.configValues.GateMaterial;
/*  25 */   public static Material DHD_MATERIAL = Main.configValues.DHDMaterial;
/*  26 */   public static Material CHEVRON_MATERIAL = Main.configValues.ChevronMaterial;
/*  27 */   public static Material SHIELD_MATERIAL = Main.configValues.ShieldMaterial;
/*     */   
/*     */   String name;
/*     */   Location loc;
/*  31 */   int worldID = 0;
/*  32 */   boolean shieldStatus = false;
/*  33 */   boolean activationStatus = false;
/*  34 */   String target = null;
/*  35 */   String direction = null;
/*     */   
/*     */   public Vector getPosition() {
/*  38 */     return new Vector(this.loc.getX(), this.loc.getZ(), this.loc.getY());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setLocation(Location loc)
/*     */   {
/*  47 */     this.loc = loc;
/*  48 */     this.worldID = Bukkit.getWorlds().indexOf(loc.getWorld());
/*  49 */     return true;
/*     */   }
/*     */   
/*     */   public boolean activate() {
/*  53 */     if (checkGateShape()) {
/*  54 */       activateChevrons();
/*  55 */       if (!this.shieldStatus) {
/*  56 */         Kawoosh k = new Kawoosh(this);
/*  57 */         k.makeKawoosh();
/*  58 */         playActivationSound();
/*     */       }
/*     */       
/*     */ 
/*  62 */       this.activationStatus = true;
/*  63 */       StargateFileReader sfr = new StargateFileReader();
/*  64 */       sfr.updateStargate(this);
/*  65 */       return true;
/*     */     }
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean deactivate() {
/*  71 */     if (!this.shieldStatus) {
/*  72 */       fillGate(Material.AIR);
/*     */     }
/*  74 */     deactivateChevrons();
/*  75 */     this.activationStatus = false;
/*  76 */     this.target = "null";
/*  77 */     StargateFileReader sfr = new StargateFileReader();
/*  78 */     sfr.updateStargate(this);
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean connectToTarget() {
/*  83 */     if (this.target != null) {
/*  84 */       if (!this.target.equals(this.name)) {
/*  85 */         StargateFileReader sfr = new StargateFileReader();
/*  86 */         Stargate s = sfr.getStargate(this.target);
/*  87 */         if (s != null) {
/*  88 */           if ((!this.activationStatus) && 
/*  89 */             (!s.activationStatus)) {
/*  90 */             if ((checkGateShape()) && (s.checkGateShape())) {
/*  91 */               if (compareNetworkName(s.getNetworkName()))
/*     */               {
/*     */ 
/*  94 */                 activate();
/*  95 */                 updateSign();
/*  96 */                 StargateThread sT = new StargateThread(this);
/*  97 */                 sT.teleportEntitysThread();
/*  98 */                 sT.countForShutdown();
/*     */                 
/* 100 */                 s.activate();
/*     */                 
/* 102 */                 return true;
/*     */               }
/*     */               
/* 105 */               return false;
/*     */             }
/*     */             
/* 108 */             return false;
/*     */           }
/*     */           
/* 111 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 115 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 119 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 123 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getNetworkName()
/*     */   {
/* 129 */     if (getNetwork() == null) {
/* 130 */       return null;
/*     */     }
/* 132 */     return getNetwork().name;
/*     */   }
/*     */   
/*     */   public boolean compareNetworkName(String str2) {
/* 136 */     String str1 = getNetworkName();
/* 137 */     return str1 == null ? false : str2 == null ? true : str1.equals(str2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean stopConnection()
/*     */   {
/* 145 */     if (this.target != null) {
/* 146 */       StargateFileReader sfr = new StargateFileReader();
/* 147 */       Stargate s = sfr.getStargate(this.target);
/* 148 */       if (s != null) {
/* 149 */         s.deactivate();
/* 150 */         deactivate();
/* 151 */         updateSign();
/*     */       }
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean stopConnectionFromBothSides()
/*     */   {
/* 162 */     StargateFileReader sfr = new StargateFileReader();
/*     */     
/* 164 */     if (this.target == null) {
/* 165 */       for (Stargate s : sfr.getStargateList()) {
/* 166 */         if (s.target.equals(this)) {
/* 167 */           s.stopConnection();
/* 168 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 173 */       stopConnection();
/* 174 */       return true;
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector getNormalVector()
/*     */   {
/* 187 */     Vector vector = null;
/* 188 */     if (this.direction.equals("NORTH")) {
/* 189 */       vector = new Vector(0, -1, 0);
/*     */     }
/* 191 */     if (this.direction.equals("SOUTH")) {
/* 192 */       vector = new Vector(0, 1, 0);
/*     */     }
/* 194 */     if (this.direction.equals("EAST")) {
/* 195 */       vector = new Vector(1, 0, 0);
/*     */     }
/* 197 */     if (this.direction.equals("WEST")) {
/* 198 */       vector = new Vector(-1, 0, 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     return vector;
/*     */   }
/*     */   
/*     */   public void makeGateShape(Boolean unbreakable) {
/* 212 */     Location aloc = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ());
/*     */     
/* 214 */     Vector DHDPos = new Vector(aloc.getX(), aloc.getZ(), aloc.getY());
/* 215 */     DHDPos.add(getNormalVector());
/*     */     
/* 217 */     aloc.setX(DHDPos.getX());
/* 218 */     aloc.setY(DHDPos.getZ());
/* 219 */     aloc.setZ(DHDPos.getY());
/* 220 */     Block DHDblock = aloc.getBlock();
/* 221 */     DHDblock.setType(DHD_MATERIAL);
/* 222 */     if (unbreakable.booleanValue()) {
/* 223 */       DHDblock.setMetadata("StargateBlock", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */ 
/*     */     }
/* 226 */     else if (DHDblock.hasMetadata("StargateBlock")) {
/* 227 */       DHDblock.removeMetadata("StargateBlock", Main.getInstance());
/*     */     }
/*     */     
/* 230 */     ArrayList<Vector> coordinates = getRingCoordinates();
/* 231 */     for (Vector v : coordinates)
/*     */     {
/* 233 */       Location location = new Location(this.loc.getWorld(), 
/* 234 */         v.getX(), v.getZ(), v.getY());
/* 235 */       Block block = location.getBlock();
/*     */       
/* 237 */       block.setType(GATE_MATERIAL);
/* 238 */       if (unbreakable.booleanValue()) {
/* 239 */         block.setMetadata("StargateBlock", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */ 
/*     */       }
/* 242 */       else if (block.hasMetadata("StargateBlock")) {
/* 243 */         block.removeMetadata("StargateBlock", Main.getInstance());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateSign()
/*     */   {
/* 251 */     Block b = this.loc.getBlock();
/* 252 */     b.setType(Material.WALL_SIGN);
/* 253 */     Sign sign = (Sign)b.getState();
/*     */     
/* 255 */     if ((this.target != null) && (!this.target.equals("null"))) {
/* 256 */       sign.setLine(2, "->" + this.target);
/*     */     }
/*     */     else {
/* 259 */       sign.setLine(2, "");
/*     */     }
/*     */     
/* 262 */     if (getNetwork() != null) {
/* 263 */       sign.setLine(3, getNetwork().name);
/*     */     } else {
/* 265 */       sign.setLine(3, "");
/*     */     }
/* 267 */     sign.update();
/*     */   }
/*     */   
/*     */   public boolean checkSign()
/*     */   {
/* 272 */     Block b = this.loc.getBlock();
/* 273 */     b.setType(Material.WALL_SIGN);
/* 274 */     Sign sign = (Sign)b.getState();
/*     */     
/* 276 */     if (!sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
/* 277 */       return false;
/*     */     }
/* 279 */     if (!sign.getLine(1).equalsIgnoreCase(this.name)) {
/* 280 */       return false;
/*     */     }
/*     */     
/* 283 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkGateShape()
/*     */   {
/* 288 */     Location loc = this.loc.clone();
/*     */     
/* 290 */     Vector DHDPos = new Vector(loc.getX(), loc.getZ(), loc.getY());
/* 291 */     DHDPos.add(getNormalVector());
/*     */     
/* 293 */     loc.setX(DHDPos.getX());
/* 294 */     loc.setY(DHDPos.getZ());
/* 295 */     loc.setZ(DHDPos.getY());
/* 296 */     Block DHDblock = loc.getBlock();
/*     */     
/* 298 */     if (!DHDblock.getType().equals(DHD_MATERIAL)) {
/* 299 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 303 */     ArrayList<Vector> gateShapePositions = getRingCoordinates();
/* 304 */     for (Vector v : gateShapePositions)
/*     */     {
/* 306 */       Location location = new Location(this.loc.getWorld(), 
/* 307 */         v.getX(), v.getZ(), v.getY());
/* 308 */       Block b = location.getBlock();
/*     */       
/* 310 */       if ((!b.getType().equals(GATE_MATERIAL)) && 
/* 311 */         (!b.getType().equals(CHEVRON_MATERIAL)))
/*     */       {
/*     */ 
/* 314 */         return false;
/*     */       }
/*     */     }
/* 317 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void activateShield()
/*     */   {
/* 325 */     if (!this.shieldStatus) {
/* 326 */       this.shieldStatus = true;
/* 327 */       StargateFileReader sfr = new StargateFileReader();
/* 328 */       sfr.updateStargate(this);
/* 329 */       fillGate(SHIELD_MATERIAL);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deactivateShield() {
/* 334 */     if (this.shieldStatus) {
/* 335 */       this.shieldStatus = false;
/* 336 */       StargateFileReader sfr = new StargateFileReader();
/* 337 */       sfr.updateStargate(this);
/* 338 */       if (this.activationStatus) {
/* 339 */         fillGate(Material.WATER);
/*     */       } else {
/* 341 */         fillGate(Material.AIR);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void switchShield()
/*     */   {
/* 348 */     if (!this.shieldStatus) {
/* 349 */       activateShield();
/*     */     } else {
/* 351 */       deactivateShield();
/*     */     }
/*     */   }
/*     */   
/*     */   public void fillGate(Material m)
/*     */   {
/* 357 */     ArrayList<Vector> array = getInsideCoordinates();
/* 358 */     for (int i = 0; i < array.size(); i++)
/*     */     {
/* 360 */       Vector v = (Vector)array.get(i);
/* 361 */       Location location = new Location((World)Bukkit.getWorlds().get(this.worldID), 
/* 362 */         v.getX(), v.getZ(), v.getY());
/* 363 */       Block block = location.getBlock();
/* 364 */       block.setType(m);
/* 365 */       if (m.equals(Material.WATER)) {
/* 366 */         block.setMetadata("PortalWater", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */ 
/*     */       }
/* 369 */       else if (block.hasMetadata("PortalWater")) {
/* 370 */         block.removeMetadata("PortalWater", Main.getInstance());
/*     */       }
/*     */       
/*     */ 
/* 374 */       if (m.equals(Main.configValues.ShieldMaterial)) {
/* 375 */         block.setMetadata("StargateBlock", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */ 
/*     */       }
/* 378 */       else if (block.hasMetadata("StargateBlock")) {
/* 379 */         block.removeMetadata("StargateBlock", Main.getInstance());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeKawoosh(boolean b)
/*     */   {
/* 389 */     Kawoosh k = new Kawoosh(this);
/* 390 */     k.makeKawoosh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void activateChevrons()
/*     */   {
/* 397 */     ArrayList<Vector> array = getRingCoordinates();
/* 398 */     for (int i = 0; i < array.size(); i++) {
/* 399 */       if ((i == 0) || (i == 3) || (i == 4) || (i == 6) || (i == 9) || (i == 11) || 
/* 400 */         (i == 12) || (i == 13)) {
/* 401 */         Vector v = (Vector)array.get(i);
/* 402 */         Location location = new Location(
/* 403 */           (World)Bukkit.getWorlds().get(this.worldID), v.getX(), v.getZ(), v.getY());
/* 404 */         Block block = location.getBlock();
/* 405 */         block.setType(CHEVRON_MATERIAL);
/* 406 */         block.setMetadata("StargateBlock", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deactivateChevrons()
/*     */   {
/* 413 */     ArrayList<Vector> array = getRingCoordinates();
/* 414 */     for (Vector v : array) {
/* 415 */       Location location = new Location((World)Bukkit.getWorlds().get(this.worldID), 
/* 416 */         v.getX(), v.getZ(), v.getY());
/* 417 */       Block block = location.getBlock();
/* 418 */       block.setType(GATE_MATERIAL);
/* 419 */       block.setMetadata("StargateBlock", new FixedMetadataValue(Main.getInstance(), "true"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayList<Vector> getRingCoordinates()
/*     */   {
/* 427 */     Vector direction = getNormalVector();
/* 428 */     Vector h = new Vector(0, 0, 1);
/* 429 */     Vector sideDirection = direction.clone().crossProduct(h);
/*     */     
/* 431 */     Vector start = getPosition().add(direction.clone().multiply(DHD_DISTANCE));
/* 432 */     start.setZ(start.clone().getZ() - 1.0D);
/*     */     
/*     */ 
/*     */ 
/* 436 */     ArrayList<Vector> gateShapePositions = new ArrayList();
/*     */     
/*     */ 
/* 439 */     gateShapePositions.add(start.clone());
/* 440 */     gateShapePositions.add(start.clone().add(sideDirection));
/* 441 */     gateShapePositions.add(start.clone().subtract(sideDirection));
/*     */     
/* 443 */     gateShapePositions.add(start.clone().add(
/* 444 */       sideDirection.clone().multiply(2).add(h)));
/* 445 */     gateShapePositions.add(start.clone().add(
/* 446 */       sideDirection.clone().multiply(-2).add(h)));
/*     */     
/* 448 */     gateShapePositions.add(start.clone().add(
/* 449 */       sideDirection.clone().multiply(3).add(h.clone().multiply(2))));
/* 450 */     gateShapePositions.add(start.clone().add(
/* 451 */       sideDirection.clone().multiply(3).add(h.clone().multiply(3))));
/* 452 */     gateShapePositions.add(start.clone().add(
/* 453 */       sideDirection.clone().multiply(3).add(h.clone().multiply(4))));
/* 454 */     gateShapePositions.add(start.clone().add(
/* 455 */       sideDirection.clone().multiply(-3).add(h.clone().multiply(2))));
/* 456 */     gateShapePositions.add(start.clone().add(
/* 457 */       sideDirection.clone().multiply(-3).add(h.clone().multiply(3))));
/* 458 */     gateShapePositions.add(start.clone().add(
/* 459 */       sideDirection.clone().multiply(-3).add(h.clone().multiply(4))));
/*     */     
/* 461 */     gateShapePositions.add(start.clone().add(
/* 462 */       sideDirection.clone().multiply(2).add(h.clone().multiply(5))));
/* 463 */     gateShapePositions.add(start.clone().add(
/* 464 */       sideDirection.clone().multiply(-2).add(h.clone().multiply(5))));
/*     */     
/* 466 */     gateShapePositions.add(start.clone().add(h.clone().multiply(6)));
/* 467 */     gateShapePositions.add(start.clone().add(sideDirection)
/* 468 */       .add(h.clone().multiply(6)));
/* 469 */     gateShapePositions.add(start.clone().subtract(sideDirection)
/* 470 */       .add(h.clone().multiply(6)));
/*     */     
/* 472 */     return gateShapePositions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Vector> getInsideCoordinates()
/*     */   {
/* 480 */     ArrayList<Vector> WaterPositions = new ArrayList();
/*     */     
/* 482 */     Vector direction = getNormalVector();
/* 483 */     Vector h = new Vector(0, 0, 1);
/* 484 */     Vector sideDirection = direction.clone().crossProduct(h);
/*     */     
/* 486 */     Vector start = getPosition().add(direction.clone().multiply(DHD_DISTANCE));
/* 487 */     start.setZ(start.getZ() - 1.0D);
/* 488 */     for (int i = 1; i < 6; i++) {
/* 489 */       WaterPositions.add(start.clone().add(h.clone().multiply(i)));
/* 490 */       WaterPositions.add(start.clone().add(sideDirection)
/* 491 */         .add(h.clone().multiply(i)));
/* 492 */       WaterPositions.add(start.clone().subtract(sideDirection)
/* 493 */         .add(h.clone().multiply(i)));
/*     */     }
/* 495 */     for (int i = 1; i < 4; i++) {
/* 496 */       WaterPositions.add(start.clone()
/* 497 */         .add(sideDirection.clone().multiply(2).add(h))
/* 498 */         .add(h.clone().multiply(i)));
/* 499 */       WaterPositions.add(start.clone()
/* 500 */         .add(sideDirection.clone().multiply(-2).add(h))
/* 501 */         .add(h.clone().multiply(i)));
/*     */     }
/* 503 */     return WaterPositions;
/*     */   }
/*     */   
/*     */   public void playActivationSound()
/*     */   {
/* 508 */     Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
/*     */     
/*     */ 
/* 511 */     float volume = Main.configValues.KawooshSoundVolume;
/* 512 */     double radiusSquared = Main.configValues.KawooshSoundRadius * Main.configValues.KawooshSoundRadius;
/* 513 */     if (radiusSquared > 0.0D) {
/* 514 */       Iterator<? extends Player> itr = players.iterator();
/* 515 */       while (itr.hasNext()) {
/* 516 */         Player p = (Player)itr.next();
/* 517 */         if ((this.loc.getWorld().getUID().equals(p.getLocation().getWorld().getUID())) && 
/* 518 */           (p.getLocation().distanceSquared(this.loc) <= radiusSquared))
/*     */         {
/* 520 */           volume = (float)(volume * (radiusSquared / p.getLocation().distanceSquared(this.loc)));
/* 521 */           Sound sound = Main.configValues.KawooshSound;
/* 522 */           p.playSound(this.loc, sound, volume, 1.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GateNetwork getNetwork()
/*     */   {
/* 534 */     GateNetwork gn = new GateNetwork("", "");
/*     */     
/* 536 */     for (GateNetwork g : gn.getNetworkList()) {
/* 537 */       if (g.networkstargates.contains(this.name)) {
/* 538 */         return g;
/*     */       }
/*     */     }
/* 541 */     return null;
/*     */   }
/*     */   
/*     */   public boolean setNetwork(String networkName)
/*     */   {
/* 546 */     GateNetwork gn = new GateNetwork("", "");
/* 547 */     gn = gn.getNetwork(networkName);
/* 548 */     if ((gn != null) && 
/* 549 */       (!gn.networkstargates.contains(this.name))) {
/* 550 */       gn.addGate(this.name);
/* 551 */       gn.save();
/* 552 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 556 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/Stargate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */