/*     */ package hauptklassen;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Ringtransporter
/*     */ {
/*  20 */   Material RINGTRANSPORTER_MATERIAL = Main.configValues.RingMaterial;
/*  21 */   byte RINGTRANSPORTER_MATERIAL_DATA = Main.configValues.RingMaterial_data;
/*  22 */   Material RINGTRANSPORTER_GROUND_MATERIAL = Main.configValues.RingGroundMaterial;
/*  23 */   byte RINGTRANSPORTER_GROUND_MATERIAL_DATA = Main.configValues.RingGroundMaterial_data;
/*     */   
/*     */   Location loc;
/*  26 */   int state_counter = -1;
/*  27 */   int id = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector getPosition()
/*     */   {
/*  35 */     Vector start = new Vector(this.loc.getX(), this.loc.getZ(), this.loc.getY());
/*  36 */     return start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  44 */     return this.loc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Location getLocationNoOffset()
/*     */   {
/*  53 */     int offset = Main.configValues.RingDistance;
/*  54 */     Vector n = getNormalVector();
/*  55 */     Vector v = getPosition().add(n.multiply(offset));
/*  56 */     Location l = this.loc.clone();
/*  57 */     l.setX(v.getX());
/*  58 */     l.setY(v.getZ());
/*  59 */     l.setZ(v.getY());
/*  60 */     return l;
/*     */   }
/*     */   
/*     */   public Vector getLocationNoOffsetAsVector()
/*     */   {
/*  65 */     int offset = Main.configValues.RingDistance;
/*  66 */     Vector n = getNormalVector();
/*  67 */     Vector v = getPosition().add(n.multiply(offset));
/*  68 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Ringtransporter getPartner()
/*     */   {
/*  79 */     Location it = getLocation().clone();
/*  80 */     Ringtransporter ret = new Ringtransporter();
/*  81 */     ret.loc = it.clone();
/*  82 */     int max = this.loc.getWorld().getMaxHeight();
/*     */     
/*  84 */     for (double i = this.loc.getY(); i < max; i += 1.0D) {
/*  85 */       it.setY(it.getY() + 1.0D);
/*  86 */       Block block = it.getBlock();
/*  87 */       if (block.getType().equals(Material.WALL_SIGN)) {
/*  88 */         Sign s = (Sign)block.getState();
/*  89 */         if (s.getLine(0).equals(ChatColor.GOLD + "[Rings]")) {
/*  90 */           ret.loc = s.getLocation().clone();
/*     */           
/*     */ 
/*     */ 
/*  94 */           if (getNormalVector().equals(ret.getNormalVector())) {
/*  95 */             return ret;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 102 */     it = getLocation().clone();
/* 103 */     for (double i = this.loc.getY(); 0.0D < i; i -= 1.0D)
/*     */     {
/* 105 */       it.setY(it.getY() - 1.0D);
/* 106 */       Block block = it.getBlock();
/* 107 */       if (block.getType().equals(Material.WALL_SIGN)) {
/* 108 */         Sign s = (Sign)block.getState();
/* 109 */         if (s.getLine(0).equals(ChatColor.GOLD + "[Rings]")) {
/* 110 */           ret.loc = s.getLocation().clone();
/*     */           
/*     */ 
/* 113 */           if (getNormalVector().equals(ret.getNormalVector())) {
/* 114 */             return ret;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 121 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Double> getInsideBorders()
/*     */   {
/* 130 */     Vector start = getLocationNoOffsetAsVector();
/* 131 */     Vector direction = getNormalVector();
/* 132 */     double x1 = start.getX();
/* 133 */     double x2 = start.getX();
/* 134 */     double y1 = start.getY();
/* 135 */     double y2 = start.getY();
/* 136 */     double z1 = start.getZ() - 1.0D;
/* 137 */     double z2 = start.getZ() + 4.0D;
/*     */     
/*     */ 
/* 140 */     if (direction.equals(new Vector(0, 1, 0))) {
/* 141 */       x1 += 1.0D;x2 += 5.0D;y1 += 1.0D;y2 += 5.0D;
/*     */     }
/* 143 */     if (direction.equals(new Vector(0, -1, 0))) {
/* 144 */       x1 -= 5.0D;x2 -= 1.0D;y2 -= 1.0D;y1 -= 5.0D;
/*     */     }
/*     */     
/*     */ 
/* 148 */     if (direction.equals(new Vector(1, 0, 0))) {
/* 149 */       x1 += 1.0D;x2 += 5.0D;y1 -= 1.0D;y2 -= 5.0D;
/*     */     }
/* 151 */     if (direction.equals(new Vector(-1, 0, 0))) {
/* 152 */       x1 -= 5.0D;x2 -= 1.0D;y1 += 1.0D;y2 += 5.0D;
/*     */     }
/* 154 */     if (y1 > y2) {
/* 155 */       y1 += y2;
/* 156 */       y2 = y1 - y2;
/* 157 */       y1 -= y2;
/*     */     }
/*     */     
/* 160 */     if (x1 > x2) {
/* 161 */       x1 += x2;
/* 162 */       x2 = x1 - x2;
/* 163 */       x1 -= x2;
/*     */     }
/*     */     
/* 166 */     if (z1 > z2) {
/* 167 */       z1 += z2;
/* 168 */       z2 = z1 - z2;
/* 169 */       z1 -= z2;
/*     */     }
/* 171 */     ArrayList<Double> ret = new ArrayList();
/* 172 */     ret.add(Double.valueOf(x1));
/* 173 */     ret.add(Double.valueOf(x2));
/* 174 */     ret.add(Double.valueOf(y1));
/* 175 */     ret.add(Double.valueOf(y2));
/* 176 */     ret.add(Double.valueOf(z1));
/* 177 */     ret.add(Double.valueOf(z2));
/* 178 */     return ret;
/*     */   }
/*     */   
/*     */   public ArrayList<Entity> getEntitysInside()
/*     */   {
/* 183 */     ArrayList<Entity> allEntitys = new ArrayList();
/*     */     
/* 185 */     ArrayList<Double> insideBorders = getInsideBorders();
/* 186 */     Double x1 = (Double)insideBorders.get(0);
/* 187 */     Double x2 = (Double)insideBorders.get(1);
/* 188 */     Double y1 = (Double)insideBorders.get(2);
/* 189 */     Double y2 = (Double)insideBorders.get(3);
/* 190 */     Double z1 = (Double)insideBorders.get(4);
/* 191 */     Double z2 = (Double)insideBorders.get(5);
/*     */     
/* 193 */     for (Entity entity : this.loc.getWorld().getEntities()) {
/* 194 */       if ((y1.doubleValue() <= entity.getLocation().getZ()) && (entity.getLocation().getZ() <= y2.doubleValue()) && 
/* 195 */         (x1.doubleValue() <= entity.getLocation().getX()) && (entity.getLocation().getX() <= x2.doubleValue()) && 
/* 196 */         (z1.doubleValue() < entity.getLocation().getY()) && (entity.getLocation().getY() < z2.doubleValue())) {
/* 197 */         allEntitys.add(entity);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 203 */     return allEntitys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayList<Block> getBlocksInside()
/*     */   {
/* 210 */     ArrayList<Block> allBlocks = new ArrayList();
/*     */     
/* 212 */     ArrayList<Double> insideBorders = getInsideBorders();
/* 213 */     Double x1 = (Double)insideBorders.get(0);
/* 214 */     Double x2 = (Double)insideBorders.get(1);
/* 215 */     Double y1 = (Double)insideBorders.get(2);
/* 216 */     Double y2 = (Double)insideBorders.get(3);
/* 217 */     Double z1 = Double.valueOf(((Double)insideBorders.get(4)).doubleValue() + 1.0D);
/* 218 */     Double z2 = (Double)insideBorders.get(5);
/*     */     
/*     */ 
/* 221 */     for (Double x = x1; x.doubleValue() <= x2.doubleValue(); x = Double.valueOf(x.doubleValue() + 1.0D)) {
/* 222 */       for (Double y = y1; y.doubleValue() <= y2.doubleValue(); y = Double.valueOf(y.doubleValue() + 1.0D)) {
/* 223 */         for (Double z = z1; z.doubleValue() < z2.doubleValue(); z = Double.valueOf(z.doubleValue() + 1.0D)) {
/* 224 */           Block b = this.loc.getWorld().getBlockAt(x.intValue(), z.intValue(), y.intValue());
/* 225 */           if ((b instanceof Block))
/*     */           {
/* 227 */             if ((!b.getType().equals(Material.AIR)) && (!b.getType().equals(Material.WALL_SIGN))) {
/* 228 */               allBlocks.add(b);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 235 */     return allBlocks;
/*     */   }
/*     */   
/*     */   public void makeRing(int height) {
/* 239 */     ArrayList<Vector> coordinates = ringshape();
/* 240 */     for (Vector v : coordinates) {
/* 241 */       Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() + height, v.getY());
/* 242 */       Block block = location.getBlock();
/*     */       
/* 244 */       block.setType(this.RINGTRANSPORTER_MATERIAL);
/*     */       
/* 246 */       block.setData(this.RINGTRANSPORTER_MATERIAL_DATA);
/*     */     }
/*     */   }
/*     */   
/*     */   public void openGround() {
/* 251 */     ArrayList<Vector> coordinates = ringshape();
/* 252 */     for (Vector v : coordinates) {
/* 253 */       Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
/* 254 */       Block block = location.getBlock();
/* 255 */       block.setType(Material.AIR);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeGround() {
/* 260 */     ArrayList<Vector> coordinates = ringshape();
/* 261 */     for (Vector v : coordinates) {
/* 262 */       Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
/* 263 */       Block block = location.getBlock();
/* 264 */       block.setType(this.RINGTRANSPORTER_GROUND_MATERIAL);
/* 265 */       block.setData(this.RINGTRANSPORTER_GROUND_MATERIAL_DATA);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean checkConstuction() {
/* 270 */     ArrayList<Vector> coordinates = ringshape();
/* 271 */     for (Vector v : coordinates)
/*     */     {
/* 273 */       Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 1.0D, v.getY());
/* 274 */       Block block = location.getBlock();
/* 275 */       if (!block.getType().equals(this.RINGTRANSPORTER_GROUND_MATERIAL)) { return false;
/*     */       }
/*     */       
/* 278 */       Location location2 = new Location(this.loc.getWorld(), v.getX(), v.getZ() - 2.0D, v.getY());
/* 279 */       Block block2 = location2.getBlock();
/* 280 */       if (!block2.getType().equals(this.RINGTRANSPORTER_MATERIAL)) { return false;
/*     */       }
/*     */     }
/* 283 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void cleanRings(int height)
/*     */   {
/* 289 */     ArrayList<Vector> coordinates = ringshape();
/* 290 */     for (int i = 0; i < height; i++) {
/* 291 */       for (Vector v : coordinates) {
/* 292 */         Location location = new Location(this.loc.getWorld(), v.getX(), v.getZ() + i, v.getY());
/* 293 */         Block block = location.getBlock();
/*     */         
/* 295 */         block.setType(Material.AIR);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Vector getNormalVector() {
/* 301 */     Sign s = (Sign)getLocation().getBlock().getState();
/* 302 */     String direction = SignUtils.signFacing(s).getOppositeFace();
/*     */     
/* 304 */     Vector vector = null;
/* 305 */     if (direction.equals("NORTH")) {
/* 306 */       vector = new Vector(0, -1, 0);
/*     */     }
/* 308 */     if (direction.equals("SOUTH")) {
/* 309 */       vector = new Vector(0, 1, 0);
/*     */     }
/* 311 */     if (direction.equals("EAST")) {
/* 312 */       vector = new Vector(1, 0, 0);
/*     */     }
/* 314 */     if (direction.equals("WEST")) {
/* 315 */       vector = new Vector(-1, 0, 0);
/*     */     }
/* 317 */     vector.multiply(-1);
/* 318 */     return vector;
/*     */   }
/*     */   
/*     */   public Vector getSideVector() {
/* 322 */     Vector n = getNormalVector();
/* 323 */     Vector h = new Vector(0, 0, 1);
/*     */     
/* 325 */     Vector s = n.crossProduct(h);
/*     */     
/* 327 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Vector> ringshape()
/*     */   {
/* 333 */     Vector start = getLocationNoOffsetAsVector();
/* 334 */     Vector sideDirection = getSideVector();
/* 335 */     Vector topDirection = getNormalVector();
/* 336 */     ArrayList<Vector> ringShapePositions = new ArrayList();
/*     */     
/*     */ 
/* 339 */     ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(2)));
/* 340 */     ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(3)));
/*     */     
/*     */ 
/* 343 */     ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(2)).add(topDirection.clone().multiply(5)));
/* 344 */     ringShapePositions.add(start.clone().add(sideDirection.clone().multiply(3).add(topDirection.clone().multiply(5))));
/*     */     
/*     */ 
/* 347 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(2)));
/* 348 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(3)));
/* 349 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(2)).add(sideDirection.clone().multiply(5)));
/* 350 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(3)).add(sideDirection.clone().multiply(5)));
/*     */     
/*     */ 
/* 353 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(1)).add(sideDirection.clone().multiply(4)));
/* 354 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(4)).add(sideDirection.clone().multiply(1)));
/* 355 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(1)).add(sideDirection.clone().multiply(1)));
/* 356 */     ringShapePositions.add(start.clone().add(topDirection.clone().multiply(4)).add(sideDirection.clone().multiply(4)));
/*     */     
/*     */ 
/* 359 */     return ringShapePositions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeAnimation(final boolean teleport)
/*     */   {
/* 369 */     if (Main.getInstance() != null)
/*     */     {
/*     */ 
/* 372 */       this.id = org.bukkit.Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 376 */           Ringtransporter.this.state_counter += 1;
/* 377 */           ArrayList<Integer> animationFrame = new ArrayList();
/*     */           
/* 379 */           if (Ringtransporter.this.state_counter == 0) Ringtransporter.this.openGround();
/* 380 */           if (Ringtransporter.this.state_counter == 1) animationFrame = Ringtransporter.this.state1();
/* 381 */           if (Ringtransporter.this.state_counter == 2) animationFrame = Ringtransporter.this.state2();
/* 382 */           if (Ringtransporter.this.state_counter == 3) animationFrame = Ringtransporter.this.state3();
/* 383 */           if (Ringtransporter.this.state_counter == 4) animationFrame = Ringtransporter.this.state4();
/* 384 */           if (Ringtransporter.this.state_counter == 5) animationFrame = Ringtransporter.this.state5();
/* 385 */           if (Ringtransporter.this.state_counter == 6) animationFrame = Ringtransporter.this.state6();
/* 386 */           if (Ringtransporter.this.state_counter == 7) animationFrame = Ringtransporter.this.state7();
/* 387 */           if (Ringtransporter.this.state_counter == 8) {
/* 388 */             animationFrame = Ringtransporter.this.state7();
/* 389 */             if (teleport) {
/* 390 */               Ringtransporter.this.transportEntitys();
/* 391 */               Ringtransporter.this.transportBlocks();
/*     */             }
/*     */           }
/* 394 */           if (Ringtransporter.this.state_counter == 9) animationFrame = Ringtransporter.this.state7();
/* 395 */           if (Ringtransporter.this.state_counter == 10) animationFrame = Ringtransporter.this.state6();
/* 396 */           if (Ringtransporter.this.state_counter == 11) animationFrame = Ringtransporter.this.state5();
/* 397 */           if (Ringtransporter.this.state_counter == 12) animationFrame = Ringtransporter.this.state4();
/* 398 */           if (Ringtransporter.this.state_counter == 13) animationFrame = Ringtransporter.this.state3();
/* 399 */           if (Ringtransporter.this.state_counter == 14) animationFrame = Ringtransporter.this.state2();
/* 400 */           if (Ringtransporter.this.state_counter == 15) animationFrame = Ringtransporter.this.state1();
/* 401 */           if (Ringtransporter.this.state_counter == 16) animationFrame = Ringtransporter.this.state8();
/* 402 */           if (Ringtransporter.this.state_counter > 16) {
/* 403 */             Ringtransporter.this.closeGround();
/* 404 */             org.bukkit.Bukkit.getScheduler().cancelTask(Ringtransporter.this.id);
/*     */           }
/*     */           
/*     */ 
/* 408 */           Ringtransporter.this.cleanRings(4);
/*     */           
/* 410 */           for (Integer i : animationFrame) {
/* 411 */             Ringtransporter.this.makeRing(i.intValue());
/*     */ 
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */ 
/* 418 */       }, 0L, 6L);
/*     */     }
/*     */     else {
/* 421 */       System.out.println("kein verweis auf plugin!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void transportEntitys()
/*     */   {
/* 428 */     Ringtransporter Ring1 = this;
/* 429 */     Ringtransporter Ring2 = getPartner();
/*     */     
/* 431 */     if (Ring2 != null)
/*     */     {
/* 433 */       ArrayList<Entity> list1 = Ring1.getEntitysInside();
/* 434 */       ArrayList<Entity> list2 = Ring2.getEntitysInside();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 440 */       if (list1.size() != 0) {
/* 441 */         for (Entity entityInRing1 : list1) {
/* 442 */           if ((entityInRing1 instanceof Player)) {
/* 443 */             ((Player)entityInRing1).hasPermission("rings.use");
/*     */           }
/*     */           
/*     */ 
/* 447 */           Location newLoc = new Location(this.loc.getWorld(), entityInRing1.getLocation().getX(), Ring2.loc.getY(), entityInRing1.getLocation().getZ());
/* 448 */           entityInRing1.teleport(newLoc);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 453 */       if (list2.size() != 0) {
/* 454 */         for (Entity entityInRing2 : list2) {
/* 455 */           if ((entityInRing2 instanceof Player)) {
/* 456 */             ((Player)entityInRing2).hasPermission("rings.use");
/*     */           }
/*     */           
/*     */ 
/* 460 */           Location newLoc2 = new Location(this.loc.getWorld(), entityInRing2.getLocation().getX(), Ring1.loc.getY(), entityInRing2.getLocation().getZ());
/* 461 */           entityInRing2.teleport(newLoc2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void transportBlocks()
/*     */   {
/* 469 */     Ringtransporter Ring1 = this;
/* 470 */     Ringtransporter Ring2 = getPartner();
/* 471 */     if (Ring2 != null) {
/* 472 */       ArrayList<Block> list1 = Ring1.getBlocksInside();
/* 473 */       ArrayList<Block> list2 = Ring2.getBlocksInside();
/*     */       
/*     */ 
/* 476 */       ArrayList<Material> m1 = new ArrayList();
/* 477 */       ArrayList<Location> l1 = new ArrayList();
/* 478 */       ArrayList<Byte> d1 = new ArrayList();
/* 479 */       ArrayList<ItemStack[]> i1 = new ArrayList();
/*     */       
/* 481 */       ArrayList<Material> m2 = new ArrayList();
/* 482 */       ArrayList<Location> l2 = new ArrayList();
/* 483 */       ArrayList<Byte> d2 = new ArrayList();
/* 484 */       ArrayList<ItemStack[]> i2 = new ArrayList();
/*     */       
/*     */ 
/*     */ 
/* 488 */       ArrayList<Location> tabulist = new ArrayList();
/*     */       
/*     */ 
/* 491 */       for (int i = 0; i < list1.size(); i++) {
/* 492 */         m1.add(((Block)list1.get(i)).getType());
/* 493 */         l1.add(((Block)list1.get(i)).getLocation().clone());
/* 494 */         d1.add(Byte.valueOf(((Block)list1.get(i)).getData()));
/* 495 */         if (((Block)list1.get(i)).getType().equals(Material.CHEST)) {
/* 496 */           Chest c = (Chest)((Block)list1.get(i)).getState();
/* 497 */           i1.add((ItemStack[])c.getBlockInventory().getContents().clone());
/*     */         }
/*     */         else {
/* 500 */           i1.add(null);
/*     */         }
/*     */       }
/*     */       
/* 504 */       for (int i = 0; i < list2.size(); i++) {
/* 505 */         m2.add(((Block)list2.get(i)).getType());
/* 506 */         l2.add(((Block)list2.get(i)).getLocation().clone());
/* 507 */         d2.add(Byte.valueOf(((Block)list2.get(i)).getData()));
/* 508 */         if (((Block)list2.get(i)).getType().equals(Material.CHEST)) {
/* 509 */           Chest c = (Chest)((Block)list2.get(i)).getState();
/* 510 */           i2.add((ItemStack[])c.getBlockInventory().getContents().clone());
/* 511 */         } else { i2.add(null);
/*     */         }
/*     */       }
/* 514 */       for (int i = 0; i < list1.size(); i++)
/*     */       {
/* 516 */         if (((Block)list1.get(i)).getType().equals(Material.CHEST)) {
/* 517 */           Chest c = (Chest)((Block)list1.get(i)).getState();
/* 518 */           c.getInventory().clear();
/* 519 */           c.update();
/*     */         }
/* 521 */         ((Block)list1.get(i)).setType(Material.AIR);
/* 522 */         ((Block)list1.get(i)).setData((byte)0);
/*     */         
/*     */ 
/* 525 */         Double blockHeight = Double.valueOf(((Block)list1.get(i)).getY() - Ring1.loc.getY());
/*     */         
/* 527 */         Location newLoc = new Location(this.loc.getWorld(), ((Location)l1.get(i)).getX(), Ring2.loc.getY() + blockHeight.doubleValue(), ((Location)l1.get(i)).getZ());
/* 528 */         newLoc.getBlock().setType((Material)m1.get(i));
/* 529 */         newLoc.getBlock().setData(((Byte)d1.get(i)).byteValue());
/*     */         
/* 531 */         if (newLoc.getBlock().getType().equals(Material.CHEST)) {
/* 532 */           Chest c = (Chest)newLoc.getBlock().getState();
/* 533 */           if ((i1.get(i) != null) && 
/* 534 */             (c.getBlockInventory() != null)) {
/* 535 */             c.getBlockInventory().setContents((ItemStack[])((ItemStack[])i1.get(i)).clone());
/* 536 */             c.update();
/*     */           }
/*     */         }
/*     */         
/* 540 */         tabulist.add(newLoc);
/*     */       }
/*     */       
/* 543 */       for (int i = 0; i < list2.size(); i++)
/*     */       {
/* 545 */         if (!tabulist.contains(((Block)list2.get(i)).getLocation())) {
/* 546 */           if (((Block)list2.get(i)).getType().equals(Material.CHEST)) {
/* 547 */             Chest c = (Chest)((Block)list2.get(i)).getState();
/* 548 */             c.getInventory().clear();
/* 549 */             c.update();
/*     */           }
/* 551 */           ((Block)list2.get(i)).setType(Material.AIR);
/* 552 */           ((Block)list2.get(i)).setData((byte)0);
/*     */         }
/*     */         
/* 555 */         Double blockHeight = Double.valueOf(((Block)list2.get(i)).getY() - Ring2.loc.getY());
/*     */         
/* 557 */         Location newLoc = new Location(this.loc.getWorld(), ((Location)l2.get(i)).getX(), Ring1.loc.getY() + blockHeight.doubleValue(), ((Location)l2.get(i)).getZ());
/* 558 */         newLoc.getBlock().setType((Material)m2.get(i));
/* 559 */         newLoc.getBlock().setData(((Byte)d2.get(i)).byteValue());
/*     */         
/* 561 */         if (newLoc.getBlock().getType().equals(Material.CHEST)) {
/* 562 */           Chest c = (Chest)newLoc.getBlock().getState();
/* 563 */           if ((i2.get(i) != null) && 
/* 564 */             (c.getBlockInventory() != null)) {
/* 565 */             c.getBlockInventory().setContents((ItemStack[])((ItemStack[])i2.get(i)).clone());
/* 566 */             c.update();
/*     */           }
/*     */         }
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
/*     */   public ArrayList<Integer> state1()
/*     */   {
/* 583 */     ArrayList<Integer> coords = new ArrayList();
/* 584 */     coords.add(Integer.valueOf(0));
/* 585 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state2() {
/* 589 */     ArrayList<Integer> coords = new ArrayList();
/* 590 */     coords.add(Integer.valueOf(1));
/* 591 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state3() {
/* 595 */     ArrayList<Integer> coords = new ArrayList();
/* 596 */     coords.add(Integer.valueOf(0));
/* 597 */     coords.add(Integer.valueOf(2));
/* 598 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state4() {
/* 602 */     ArrayList<Integer> coords = new ArrayList();
/* 603 */     coords.add(Integer.valueOf(1));
/* 604 */     coords.add(Integer.valueOf(3));
/* 605 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state5() {
/* 609 */     ArrayList<Integer> coords = new ArrayList();
/* 610 */     coords.add(Integer.valueOf(0));
/* 611 */     coords.add(Integer.valueOf(2));
/* 612 */     coords.add(Integer.valueOf(3));
/* 613 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state6() {
/* 617 */     ArrayList<Integer> coords = new ArrayList();
/*     */     
/* 619 */     coords.add(Integer.valueOf(1));
/* 620 */     coords.add(Integer.valueOf(2));
/* 621 */     coords.add(Integer.valueOf(3));
/*     */     
/* 623 */     return coords;
/*     */   }
/*     */   
/* 626 */   public ArrayList<Integer> state7() { ArrayList<Integer> coords = new ArrayList();
/* 627 */     coords.add(Integer.valueOf(0));
/* 628 */     coords.add(Integer.valueOf(1));
/* 629 */     coords.add(Integer.valueOf(2));
/* 630 */     coords.add(Integer.valueOf(3));
/*     */     
/* 632 */     return coords;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> state8() {
/* 636 */     ArrayList<Integer> coords = new ArrayList();
/*     */     
/* 638 */     return coords;
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/Ringtransporter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */