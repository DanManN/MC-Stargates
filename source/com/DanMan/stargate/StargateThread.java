/*     */ package hauptklassen;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class StargateThread
/*     */ {
/*     */   Stargate stargate;
/*  19 */   int threadID = 0;
/*  20 */   int shoutDownTaskID = 0;
/*  21 */   int secondsOpen = 0;
/*     */   
/*     */   StargateThread(Stargate s)
/*     */   {
/*  25 */     this.stargate = s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Entity> getNearbyEntities(int distance)
/*     */   {
/*  36 */     ArrayList<Entity> ret = new ArrayList();
/*  37 */     java.util.List<Entity> list = this.stargate.loc.getWorld().getEntities();
/*  38 */     Vector direction = this.stargate.getNormalVector();
/*  39 */     Vector start = this.stargate.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE));
/*     */     
/*  41 */     Location startLoc = this.stargate.loc.clone();
/*  42 */     startLoc.setY(start.getZ());startLoc.setX(start.getX());startLoc.setZ(start.getY());
/*     */     
/*  44 */     for (Entity e : list) {
/*  45 */       if (e.getWorld().equals(startLoc.getWorld()))
/*     */       {
/*  47 */         if ((!(e instanceof Player)) && (!(e instanceof org.bukkit.entity.Vehicle)) && 
/*  48 */           (e.getLocation().distance(startLoc) < distance)) {
/*  49 */           ret.add(e);
/*     */         }
/*     */       }
/*     */     }
/*  53 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkEntityIsInsideStargate(Entity e)
/*     */   {
/*  60 */     Vector direction = this.stargate.getNormalVector();
/*  61 */     Vector start = this.stargate.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE));
/*     */     
/*     */ 
/*     */ 
/*  65 */     double x1 = start.getX();
/*  66 */     double x2 = start.getX();
/*  67 */     double y1 = start.getY();
/*  68 */     double y2 = start.getY();
/*  69 */     double z1 = start.getZ();
/*  70 */     double z2 = start.getZ() + 5.0D;
/*     */     
/*     */ 
/*  73 */     if (direction.equals(new Vector(0, 1, 0))) {
/*  74 */       x1 -= 3.0D;x2 += 3.0D;y2 += 1.0D;
/*     */     }
/*  76 */     if (direction.equals(new Vector(0, -1, 0))) {
/*  77 */       x1 -= 3.0D;x2 += 3.0D;y2 += 1.0D;
/*     */     }
/*     */     
/*     */ 
/*  81 */     if (direction.equals(new Vector(1, 0, 0))) {
/*  82 */       x2 += 1.0D;y1 -= 3.0D;y2 += 3.0D;
/*     */     }
/*  84 */     if (direction.equals(new Vector(-1, 0, 0))) {
/*  85 */       x1 += 1.0D;y1 -= 3.0D;y2 += 3.0D;
/*     */     }
/*  87 */     if (y1 > y2) {
/*  88 */       y1 += y2;
/*  89 */       y2 = y1 - y2;
/*  90 */       y1 -= y2;
/*     */     }
/*     */     
/*  93 */     if (x1 > x2) {
/*  94 */       x1 += x2;
/*  95 */       x2 = x1 - x2;
/*  96 */       x1 -= x2;
/*     */     }
/*     */     
/*  99 */     if (z1 > z2) {
/* 100 */       z1 += z2;
/* 101 */       z2 = z1 - z2;
/* 102 */       z1 -= z2;
/*     */     }
/*     */     
/*     */ 
/* 106 */     if ((y1 <= e.getLocation().getZ()) && (e.getLocation().getZ() <= y2) && 
/* 107 */       (x1 <= e.getLocation().getX()) && (e.getLocation().getX() <= x2) && 
/* 108 */       (z1 <= e.getLocation().getY()) && (e.getLocation().getY() <= z2)) {
/* 109 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   public void teleportEntitysThread()
/*     */   {
/* 118 */     if (Main.getInstance() != null)
/*     */     {
/* 120 */       this.threadID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 124 */           if ((!StargateThread.this.stargate.activationStatus) || (StargateThread.this.stargate.target == null)) {
/* 125 */             Bukkit.getScheduler().cancelTask(StargateThread.this.threadID);
/*     */           }
/*     */           
/* 128 */           for (Entity e : StargateThread.this.getNearbyEntities(10)) {
/* 129 */             if (StargateThread.this.checkEntityIsInsideStargate(e))
/*     */             {
/*     */ 
/* 132 */               StargateFileReader sfr = new StargateFileReader();
/* 133 */               Stargate target = sfr.getStargate(StargateThread.this.stargate.target);
/*     */               
/*     */ 
/* 136 */               if (target.shieldStatus) {
/* 137 */                 System.out.println("shield activ");
/* 138 */                 if (Main.configValues.IrisNoTeleport.equals("true")) {
/* 139 */                   target = StargateThread.this.stargate;
/*     */                 }
/* 141 */                 e.remove();
/*     */               }
/*     */               else {
/* 144 */                 Vector direction = target.getNormalVector();
/* 145 */                 Vector start = target.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE - 1));
/*     */                 
/* 147 */                 Location newLoc = new Location(target.loc.getWorld(), start.getX(), start.getZ(), start.getY());
/* 148 */                 if (target.loc.getWorld().equals(StargateThread.this.stargate.loc.getWorld())) {
/* 149 */                   e.teleport(newLoc);
/*     */ 
/*     */                 }
/* 152 */                 else if ((e instanceof Item)) {
/* 153 */                   org.bukkit.inventory.ItemStack newItem = ((Item)e).getItemStack().clone();
/* 154 */                   e.remove();
/* 155 */                   target.loc.getWorld().dropItemNaturally(newLoc, newItem);
/*     */                 }
/*     */                 else {
/* 158 */                   e.remove();
/* 159 */                   target.loc.getWorld().spawnEntity(newLoc, e.getType());
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */         }
/* 168 */       }, 0L, 2L);
/*     */     }
/*     */     else {
/* 171 */       System.out.println("kein verweis auf plugin!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void countForShutdown() {
/* 176 */     if (Main.getInstance() != null) {
/* 177 */       this.shoutDownTaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 181 */           if (Main.configValues.activationTime == 0) {
/* 182 */             Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
/*     */           }
/* 184 */           if (!StargateThread.this.stargate.activationStatus) {
/* 185 */             Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
/*     */           }
/*     */           
/* 188 */           ArrayList<String> args3 = new ArrayList();
/* 189 */           args3.add(StargateThread.this.stargate.name);
/* 190 */           args3.add(StargateThread.this.stargate.target);
/*     */           
/* 192 */           for (Player p : StargateThread.this.stargate.loc.getWorld().getPlayers()) {
/* 193 */             if (p.getLocation().distance(StargateThread.this.stargate.loc) < 30.0D)
/* 194 */               p.sendMessage(ChatColor.GOLD + Main.language.get("pluginNameChat", "") + ChatColor.GREEN + Main.language.get("gateConnectionClosedTimeout", args3));
/*     */           }
/* 196 */           StargateThread.this.stargate.stopConnection();
/* 197 */           Bukkit.getScheduler().cancelTask(StargateThread.this.shoutDownTaskID);
/*     */         }
/*     */         
/* 200 */       }, Main.configValues.activationTime * 20);
/*     */     } else {
/* 202 */       System.out.println("kein verweis auf plugin!");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/StargateThread.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */