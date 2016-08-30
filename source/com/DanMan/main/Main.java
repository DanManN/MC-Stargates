/*      */ package hauptklassen;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Logger;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.BlockFace;
/*      */ import org.bukkit.block.BlockState;
/*      */ import org.bukkit.command.Command;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.EntityType;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.entity.Vehicle;
/*      */ import org.bukkit.event.EventHandler;
/*      */ import org.bukkit.event.EventPriority;
/*      */ import org.bukkit.event.block.Action;
/*      */ import org.bukkit.event.block.BlockBreakEvent;
/*      */ import org.bukkit.event.block.BlockFromToEvent;
/*      */ import org.bukkit.event.block.BlockPistonExtendEvent;
/*      */ import org.bukkit.event.block.BlockPistonRetractEvent;
/*      */ import org.bukkit.event.block.BlockRedstoneEvent;
/*      */ import org.bukkit.event.block.SignChangeEvent;
/*      */ import org.bukkit.event.entity.EntityExplodeEvent;
/*      */ import org.bukkit.event.player.PlayerInteractEvent;
/*      */ import org.bukkit.event.player.PlayerJoinEvent;
/*      */ import org.bukkit.event.player.PlayerMoveEvent;
/*      */ import org.bukkit.event.vehicle.VehicleMoveEvent;
/*      */ import org.bukkit.inventory.Inventory;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.util.Vector;
/*      */ 
/*      */ public class Main extends org.bukkit.plugin.java.JavaPlugin implements org.bukkit.event.Listener
/*      */ {
/*      */   static Main instance;
/*   47 */   public Map<String, String> START_GATES = new java.util.HashMap();
/*      */   public static ConfigFileReader configValues;
/*      */   public static LanguageFileReader language;
/*      */   
/*   51 */   public void onEnable() { getLogger().info("Mistpilz Mod has been enabled!");
/*      */     
/*   53 */     String version = getDescription().getVersion();
/*   54 */     System.out.println("    0000");
/*   55 */     System.out.println("  0      0");
/*   56 */     System.out.println("0          0");
/*   57 */     System.out.println("0MPStargate0");
/*   58 */     System.out.println("0   " + version + "   0");
/*   59 */     System.out.println("  0      0");
/*   60 */     System.out.println("    0000");
/*      */     
/*      */ 
/*   63 */     getServer().getPluginManager().registerEvents(this, this);
/*   64 */     checkConficFiles();
/*   65 */     checkDefaultLanguageFile();
/*      */     
/*   67 */     configValues = new ConfigFileReader();
/*   68 */     language = new LanguageFileReader(configValues.Language);
/*   69 */     instance = this;
/*      */   }
/*      */   
/*      */   public void onDisable() {
/*   73 */     instance = null;
/*   74 */     getLogger().info("Mistpilz Mod has been disabled.");
/*      */     
/*      */ 
/*   77 */     if (configValues.activationTime != 0) {
/*   78 */       StargateFileReader sfr = new StargateFileReader();
/*      */       
/*   80 */       ArrayList<Stargate> activeGates = sfr.getActivStartGates();
/*   81 */       for (Stargate s : activeGates) {
/*   82 */         s.stopConnection();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Main getInstance() {
/*   88 */     return instance;
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler
/*      */   public void onPlayerJoin(PlayerJoinEvent e)
/*      */   {
/*   95 */     Player p = e.getPlayer();
/*   96 */     PlayerDataReader pdr = new PlayerDataReader(p);
/*   97 */     if (!pdr.checkForPlayerData()) {
/*   98 */       pdr.createPlayerData();
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void defineStargate(SignChangeEvent sign) throws Exception
/*      */   {
/*  105 */     Player player = sign.getPlayer();
/*  106 */     if (player.hasPermission("stargate.register"))
/*      */     {
/*  108 */       if ((sign.getLine(0).equalsIgnoreCase("[Stargate]")) && 
/*  109 */         (!sign.getLine(1).equals(""))) {
/*  110 */         String gateName = sign.getLine(1);
/*      */         
/*  112 */         org.bukkit.block.Sign s = (org.bukkit.block.Sign)sign.getBlock().getState();
/*  113 */         SignUtils su = new SignUtils();
/*      */         
/*  115 */         Stargate stargate = new Stargate();
/*  116 */         stargate.setLocation(s.getLocation());
/*      */         
/*  118 */         stargate.direction = SignUtils.signFacing(s).getOppositeFace();
/*      */         
/*  120 */         if (stargate.checkGateShape()) {
/*  121 */           stargate.name = gateName;
/*      */           
/*  123 */           StargateFileReader sfr = new StargateFileReader();
/*      */           try {
/*  125 */             if (sfr.getStargate(stargate.name) == null) {
/*  126 */               sfr.saveStargate(stargate);
/*  127 */               sign.setLine(0, ChatColor.GOLD + "[Stargate]");
/*      */               
/*  129 */               stargate.makeGateShape(Boolean.valueOf(true));
/*      */               
/*  131 */               if (player.hasPermission("network.create")) {
/*  132 */                 String networkName = "";
/*  133 */                 if (!sign.getLine(3).equals("")) {
/*  134 */                   networkName = sign.getLine(3);
/*      */                   
/*  136 */                   GateNetwork emptyNetwork = new GateNetwork(player.getName(), networkName);
/*      */                   
/*      */ 
/*  139 */                   if (emptyNetwork.getNetwork(networkName) == null) {
/*  140 */                     emptyNetwork.addGate(stargate.name);
/*  141 */                     emptyNetwork.save();
/*      */                   }
/*      */                   else {
/*  144 */                     GateNetwork existingNetwork = emptyNetwork.getNetwork(networkName);
/*  145 */                     if (existingNetwork.hasAdmin(player.getName())) {
/*  146 */                       existingNetwork.addGate(stargate.name);
/*  147 */                       existingNetwork.save();
/*      */                     }
/*      */                   }
/*  150 */                   stargate.updateSign();
/*      */                 }
/*      */               }
/*      */               else {
/*  154 */                 player.sendMessage(
/*  155 */                   ChatColor.GOLD + language.get("pluginNameChat", "") + 
/*  156 */                   ChatColor.RED + 
/*  157 */                   language.get("networkNoPermissionCreate", ""));
/*      */               }
/*      */               
/*      */ 
/*  161 */               sign.getPlayer().sendMessage(
/*  162 */                 ChatColor.GOLD + language.get("pluginNameChat", "") + 
/*  163 */                 ChatColor.GREEN + 
/*  164 */                 language.get("gateCreated", stargate.name));
/*      */             }
/*      */             else {
/*  167 */               sign.setLine(0, ChatColor.RED + "Stargate");
/*  168 */               sign.getPlayer().sendMessage(
/*  169 */                 ChatColor.GOLD + language.get("pluginNameChat", "") + 
/*  170 */                 ChatColor.RED + 
/*  171 */                 language.get("gateNameExists", ""));
/*      */             }
/*      */           } catch (Exception e) {
/*  174 */             e.printStackTrace();
/*  175 */             sign.setLine(0, ChatColor.RED + "Stargate");
/*  176 */             sign.getPlayer().sendMessage(
/*  177 */               ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  178 */               language.get("gateCreationFailed", ""));
/*      */           }
/*      */         } else {
/*  181 */           sign.setLine(0, ChatColor.RED + "Stargate");
/*  182 */           sign.getPlayer().sendMessage(
/*  183 */             ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  184 */             language.get("gateNotArround", ""));
/*      */         }
/*      */       }
/*      */     } else {
/*  188 */       player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler(priority=EventPriority.HIGHEST)
/*      */   public void defineRingtransporter(SignChangeEvent sign) throws Exception {
/*  194 */     Player player = sign.getPlayer();
/*  195 */     if (sign.getLine(0).equalsIgnoreCase("[Rings]")) {
/*  196 */       if (player.hasPermission("rings.build")) {
/*  197 */         Ringtransporter rt = new Ringtransporter();
/*  198 */         rt.loc = sign.getBlock().getLocation().clone();
/*  199 */         if (rt.checkConstuction()) {
/*  200 */           sign.setLine(0, ChatColor.GOLD + "[Rings]");
/*      */         }
/*      */       } else {
/*  203 */         sign.setLine(0, "Rings");
/*  204 */         player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void redstoneSignalInStargate(BlockRedstoneEvent event)
/*      */   {
/*  212 */     Block b = event.getBlock();
/*      */     
/*  214 */     if (b.getType().equals(Material.WALL_SIGN)) {
/*  215 */       org.bukkit.block.Sign sign = (org.bukkit.block.Sign)b.getState();
/*  216 */       if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]"))
/*      */       {
/*      */ 
/*  219 */         StargateFileReader sfr = new StargateFileReader();
/*  220 */         Stargate s = sfr.getStargate(sign.getLine(1));
/*      */         
/*      */         GateNetwork gn;
/*      */         
/*  224 */         if ((gn = s.getNetwork()) != null) {
/*  225 */           if (gn.state.equals("private")) {
/*  226 */             if (event.getNewCurrent() > 0) {
/*  227 */               s.activateShield();
/*      */             }
/*  229 */             if (event.getNewCurrent() == 0) {
/*  230 */               s.deactivateShield();
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  235 */           if (event.getNewCurrent() > 0) {
/*  236 */             s.activateShield();
/*      */           }
/*  238 */           if (event.getNewCurrent() == 0) {
/*  239 */             s.deactivateShield();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  244 */       if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Rings]")) {
/*  245 */         Ringtransporter rt = new Ringtransporter();
/*  246 */         rt.loc = sign.getLocation().clone();
/*  247 */         Ringtransporter target = rt.getPartner();
/*      */         
/*  249 */         if (target != null)
/*      */         {
/*  251 */           if ((event.getNewCurrent() > 0) && 
/*  252 */             (rt.checkConstuction()) && (target.checkConstuction())) {
/*  253 */             rt.makeAnimation(true);
/*  254 */             target.makeAnimation(false);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @EventHandler
/*      */   public void leftClickGateSign(PlayerInteractEvent e)
/*      */   {
/*  266 */     if (e.getPlayer().hasPermission("stargate.close")) {
/*  267 */       if ((e.getAction() == Action.LEFT_CLICK_BLOCK) && 
/*  268 */         (e.getClickedBlock().getType() == Material.WALL_SIGN))
/*      */       {
/*  270 */         Block block = e.getClickedBlock();
/*  271 */         org.bukkit.block.Sign sign = (org.bukkit.block.Sign)block.getState();
/*      */         
/*  273 */         if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
/*  274 */           String gateName = sign.getLine(1);
/*  275 */           StargateFileReader sfr = new StargateFileReader();
/*      */           try {
/*  277 */             Stargate s = sfr.getStargate(gateName);
/*  278 */             if ((s == null) || 
/*  279 */               (!s.activationStatus) || 
/*  280 */               (s.target.equals("null")))
/*      */               return;
/*      */             GateNetwork gn;
/*  283 */             if (((gn = s.getNetwork()) != null) && 
/*  284 */               (!gn.allowsPlayer(e.getPlayer().getName()))) {
/*  285 */               e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  286 */                 language.get("networkPlayerNotAllowed", ""));
/*  287 */               return;
/*      */             }
/*      */             
/*      */ 
/*  291 */             s.stopConnection();
/*  292 */             ArrayList<String> args = new ArrayList();
/*  293 */             args.add(s.name);
/*  294 */             args.add(s.target);
/*  295 */             e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/*  296 */               language.get("gateConnectionClosed", args));
/*      */ 
/*      */           }
/*      */           catch (Exception localException) {}
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  308 */       e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  309 */         language.get("permissionNotAllowed", ""));
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void rightClickSign(PlayerInteractEvent e)
/*      */   {
/*  316 */     if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && 
/*  317 */       (e.getClickedBlock().getType() == Material.WALL_SIGN))
/*      */     {
/*  319 */       Block block = e.getClickedBlock();
/*  320 */       org.bukkit.block.Sign sign = (org.bukkit.block.Sign)block.getState();
/*      */       
/*      */ 
/*  323 */       if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
/*  324 */         e.setCancelled(true);
/*  325 */         String gateName = sign.getLine(1);
/*      */         
/*  327 */         StargateFileReader sfr = new StargateFileReader();
/*      */         try
/*      */         {
/*  330 */           Stargate s = sfr.getStargate(gateName);
/*  331 */           if (s != null) {
/*  332 */             if ((!s.checkGateShape()) || (!s.checkSign())) {
/*  333 */               ArrayList<String> args = new ArrayList();
/*  334 */               args.add(s.name);
/*  335 */               e.getPlayer().sendMessage(
/*  336 */                 ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  337 */                 language.get("gateDestroyed", args));
/*      */             }
/*      */             else {
/*  340 */               this.START_GATES.put(e.getPlayer().getName(), s.name);
/*      */               
/*  342 */               if (!s.target.equals("null")) {
/*  343 */                 ArrayList<String> args = new ArrayList();
/*  344 */                 args.add(s.name);
/*  345 */                 args.add(s.target);
/*  346 */                 e.getPlayer().sendMessage(
/*  347 */                   ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/*  348 */                   language.get("gateSelectedHasTarget", args));
/*      */               }
/*      */               else {
/*  351 */                 e.getPlayer().sendMessage(
/*  352 */                   ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/*  353 */                   language.get("gateSelected", s.name));
/*      */               }
/*      */               
/*  356 */               if (e.getPlayer().hasPermission("stargate.discover")) {
/*  357 */                 PlayerDataReader pdr = new PlayerDataReader(e.getPlayer());
/*  358 */                 if (!pdr.knowsGate(gateName)) {
/*  359 */                   pdr.saveStargate(gateName);
/*  360 */                   e.getPlayer().sendMessage(
/*  361 */                     ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/*  362 */                     language.get("gateNewAddress", s.name));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception localException) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  373 */       if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Rings]")) {
/*  374 */         e.setCancelled(true);
/*  375 */         if (e.getPlayer().hasPermission("rings.activate")) {
/*  376 */           Ringtransporter rt = new Ringtransporter();
/*  377 */           rt.loc = sign.getLocation().clone();
/*  378 */           Ringtransporter target = rt.getPartner();
/*      */           
/*  380 */           if (target != null)
/*      */           {
/*  382 */             if ((rt.checkConstuction()) && (target.checkConstuction()))
/*      */             {
/*  384 */               rt.makeAnimation(true);
/*  385 */               target.makeAnimation(false);
/*      */             }
/*      */             else {
/*  388 */               e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  389 */                 language.get("ringsConstructionDamaged", ""));
/*      */             }
/*      */           }
/*      */           else {
/*  393 */             e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  394 */               language.get("ringsNoTarget", ""));
/*      */           }
/*      */         }
/*      */         else {
/*  398 */           e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  399 */             language.get("permissionNotAllowed", ""));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @EventHandler
/*      */   public void moveInKawoosh(PlayerMoveEvent event)
/*      */   {
/*  409 */     if (event.getTo().getBlock().hasMetadata("Kawoosh")) {
/*  410 */       Player player = event.getPlayer();
/*  411 */       event.getFrom().add(new Vector(1, 1, 1));
/*  412 */       player.setHealth(0.0D);
/*  413 */       player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  414 */         language.get("playerKilledByKawoosh", ""));
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void vehicleInStargate(VehicleMoveEvent e)
/*      */   {
/*  421 */     Entity entity = e.getVehicle().getPassenger();
/*      */     
/*      */ 
/*  424 */     if (entity == null) {
/*  425 */       Stargate s = getHorizonEntityIsInside(e.getVehicle());
/*  426 */       if (s != null) {
/*  427 */         StargateFileReader sfr = new StargateFileReader();
/*  428 */         Stargate target = sfr.getStargate(s.target);
/*  429 */         if (target != null) {
/*  430 */           if (target.shieldStatus) {
/*  431 */             if (configValues.IrisNoTeleport.equals("true")) {
/*  432 */               target = s;
/*      */             }
/*      */             else {
/*  435 */               e.getVehicle().remove();
/*  436 */               return;
/*      */             }
/*      */           }
/*  439 */           Vector direction = target.getNormalVector();
/*  440 */           Vector start = target.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE - 1));
/*  441 */           Location newLoc = new Location((World)Bukkit.getWorlds().get(target.worldID), start.getX(), start.getZ(), start.getY());
/*  442 */           e.getVehicle().teleport(newLoc);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*  448 */     else if ((entity instanceof Player)) {
/*  449 */       Stargate s = getHorizonEntityIsInside(entity);
/*  450 */       if (s != null) {
/*  451 */         Player p = (Player)entity;
/*  452 */         PlayerMoveEvent pEvent = new PlayerMoveEvent(p, e.getFrom(), e.getTo());
/*  453 */         playerInStargate(pEvent);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  458 */       Stargate s = getHorizonEntityIsInside(entity);
/*  459 */       if (s != null) {
/*  460 */         StargateFileReader sfr = new StargateFileReader();
/*  461 */         Stargate target = sfr.getStargate(s.target);
/*  462 */         if (target != null) {
/*  463 */           if (target.shieldStatus) {
/*  464 */             if (configValues.IrisNoTeleport.equals("true")) {
/*  465 */               target = s;
/*      */             }
/*      */             else {
/*  468 */               e.getVehicle().remove();
/*  469 */               return;
/*      */             }
/*      */           }
/*  472 */           Vector direction = target.getNormalVector();
/*  473 */           Vector start = target.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE - 1));
/*  474 */           Location newLoc = new Location((World)Bukkit.getWorlds().get(target.worldID), start.getX(), start.getZ(), start.getY());
/*  475 */           e.getVehicle().eject();
/*      */           
/*  477 */           e.getVehicle().teleport(newLoc);
/*  478 */           entity.teleport(newLoc);
/*  479 */           e.getVehicle().setPassenger(entity);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler
/*      */   public void playerInStargate(PlayerMoveEvent event)
/*      */   {
/*  491 */     Player player = event.getPlayer();
/*  492 */     if (player.hasPermission("stargate.use")) {
/*  493 */       Stargate s = getHorizonEntityIsInside(player);
/*  494 */       if (s != null)
/*      */       {
/*  496 */         StargateFileReader sfr = new StargateFileReader();
/*  497 */         Stargate target = sfr.getStargate(s.target);
/*      */         
/*  499 */         if ((configValues.IrisNoTeleport.equals("true")) && 
/*  500 */           (target.shieldStatus)) {
/*  501 */           target = s;
/*      */         }
/*      */         
/*      */ 
/*  505 */         if ((s.getNetwork() != null) && 
/*  506 */           (!s.getNetwork().allowsPlayer(player.getName())) && 
/*  507 */           (configValues.networkTeleportUnallowedPlayers == 0)) {
/*  508 */           target = s;
/*  509 */           event.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  510 */             language.get("networkPlayerNotAllowed", ""));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  516 */         if (target == null) {
/*  517 */           return;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  522 */         Vector direction = target.getNormalVector();
/*  523 */         Vector start = target.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE - 1));
/*  524 */         Location newLoc = new Location((World)Bukkit.getWorlds().get(target.worldID), start.getX(), start.getZ(), start.getY());
/*      */         
/*      */ 
/*      */ 
/*  528 */         if (direction.equals(new Vector(1, 0, 0))) {
/*  529 */           newLoc.setYaw(90.0F);
/*      */         }
/*      */         
/*  532 */         if (direction.equals(new Vector(0, 1, 0))) {
/*  533 */           newLoc.setYaw(180.0F);
/*      */         }
/*      */         
/*  536 */         if (direction.equals(new Vector(-1, 0, 0))) {
/*  537 */           newLoc.setYaw(-90.0F);
/*      */         }
/*      */         
/*  540 */         if (direction.equals(new Vector(0, -1, 0))) {
/*  541 */           newLoc.setYaw(0.0F);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  546 */         for (Player players : Bukkit.getOnlinePlayers())
/*      */         {
/*  548 */           players.hidePlayer(player);
/*      */         }
/*      */         
/*      */ 
/*  552 */         Entity vehicle = player.getVehicle();
/*  553 */         if (vehicle != null) {
/*  554 */           vehicle.eject();
/*  555 */           vehicle.teleport(newLoc);
/*      */         }
/*      */         
/*  558 */         player.teleport(newLoc);
/*      */         
/*  560 */         if ((player.getVehicle() == null) && (vehicle != null)) {
/*  561 */           vehicle.setPassenger(player);
/*      */         }
/*      */         
/*      */ 
/*  565 */         if ((player.teleport(newLoc)) && (target.shieldStatus))
/*      */         {
/*      */ 
/*  568 */           if ((player.getHealth() - configValues.IrisDamage <= 0.0D) && (configValues.IrisDestroyInventory == 1)) {
/*  569 */             player.getInventory().clear();
/*      */           }
/*      */           
/*  572 */           player.setHealth(player.getHealth() - configValues.IrisDamage);
/*      */           
/*  574 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  575 */             language.get("playerDamagedByIris", ""));
/*      */         }
/*      */         
/*      */ 
/*  579 */         for (Player players : Bukkit.getOnlinePlayers())
/*      */         {
/*  581 */           players.showPlayer(player);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @EventHandler
/*      */   public void onBlockFromTo(BlockFromToEvent event)
/*      */   {
/*  593 */     Block block = event.getBlock();
/*  594 */     if ((block.hasMetadata("PortalWater")) && 
/*  595 */       (((org.bukkit.metadata.MetadataValue)block.getMetadata("PortalWater").get(0)).asString().equals("true")))
/*      */     {
/*  597 */       int id = block.getTypeId();
/*  598 */       if ((id == 8) || (id == 9)) {
/*  599 */         event.setCancelled(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void destroyStargateBlock(BlockBreakEvent event)
/*      */     throws IOException
/*      */   {
/*  608 */     if ((event.getBlock().getType().equals(Stargate.GATE_MATERIAL)) || 
/*  609 */       (event.getBlock().getType().equals(Stargate.DHD_MATERIAL)) || 
/*  610 */       (event.getBlock().getType().equals(Stargate.CHEVRON_MATERIAL)))
/*      */     {
/*  612 */       if (event.getBlock().hasMetadata("StargateBlock")) {
/*  613 */         event.setCancelled(true);
/*      */       }
/*      */     }
/*  616 */     if (event.getBlock().getType().equals(Material.WALL_SIGN)) {
/*  617 */       org.bukkit.block.Sign sign = (org.bukkit.block.Sign)event.getBlock().getState();
/*  618 */       if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
/*  619 */         event.setCancelled(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void PistonStargateBlockExtend(BlockPistonExtendEvent event) throws IOException
/*      */   {
/*  627 */     for (int i = 0; i < event.getBlocks().size(); i++) {
/*  628 */       if (((Block)event.getBlocks().get(i)).hasMetadata("StargateBlock")) {
/*  629 */         event.setCancelled(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void PistonStargateBlockRetract(BlockPistonRetractEvent event) throws IOException
/*      */   {
/*  637 */     if (event.getBlock().hasMetadata("StargateBlock")) {
/*  638 */       event.setCancelled(true);
/*      */     }
/*      */   }
/*      */   
/*      */   @EventHandler
/*      */   public void ExplodeStargateBlock(EntityExplodeEvent event)
/*      */     throws IOException
/*      */   {
/*  646 */     if (event.getEntityType().equals(EntityType.PRIMED_TNT)) {
/*  647 */       for (int i = 0; i < event.blockList().size(); i++) {
/*  648 */         if ((((Block)event.blockList().get(i)).hasMetadata("StargateBlock")) || 
/*  649 */           (((Block)event.blockList().get(i)).hasMetadata("PortalWater"))) {
/*  650 */           event.blockList().remove(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  659 */   String commandprefix = "mpgates";
/*  660 */   String commandActivate = "to";
/*  661 */   String commandDeactivate = "close";
/*  662 */   String commandRepair = "repair";
/*  663 */   String commandBuild = "build";
/*  664 */   String commandRemove = "remove";
/*  665 */   String commandShield = "shield";
/*  666 */   String commandGatelist = "list";
/*  667 */   String commandNetwork = "network";
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*      */   {
/*  673 */     if (cmd.getName().equalsIgnoreCase(this.commandprefix))
/*      */     {
/*  675 */       if (!(sender instanceof Player)) return true;
/*  676 */       Player player = Bukkit.getPlayer(sender.getName());
/*      */       
/*  678 */       if (args.length == 0)
/*      */       {
/*      */ 
/*      */ 
/*  682 */         return true; }
/*  683 */       if (args[0] != null)
/*      */       {
/*  685 */         if ((args[0].equalsIgnoreCase("help")) || (args[0].equalsIgnoreCase("?"))) {
/*  686 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + " Help:");
/*  687 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandBuild + " <north/south/...> (creativmode only)");
/*  688 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandActivate + " <targetname>");
/*  689 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandDeactivate);
/*  690 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandRemove + " (unregisters from database)");
/*  691 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandShield);
/*  692 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandGatelist);
/*  693 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandRepair + " <gatename>");
/*  694 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " add <networkname>");
/*  695 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " remove");
/*  696 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " op/deop <playername>");
/*  697 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " allow/unallow <playername>");
/*  698 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " private/public");
/*  699 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /" + this.commandprefix + " " + this.commandNetwork + " setName <new networkname>");
/*  700 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " To register a Stargate, place a sign at the front of the DHD: First line \"[Stargate]\", second line <Gatename> (optional: fourth line <networkname>.");
/*  701 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " Distance DHD->Stargate: " + (configValues.DHD_Distance - 1));
/*      */           
/*  703 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  708 */         if (args[0].equalsIgnoreCase(this.commandBuild))
/*      */         {
/*  710 */           if (!player.hasPermission("stargate.command.build")) {
/*  711 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + " You dont have permission to do this.");return true;
/*      */           }
/*  713 */           if (args.length == 2) {
/*  714 */             if ((args[1].equalsIgnoreCase("NORTH")) || (args[1].equalsIgnoreCase("EAST")) || (args[1].equalsIgnoreCase("SOUTH")) || (args[1].equalsIgnoreCase("WEST"))) {
/*  715 */               String skydirection = args[1].toUpperCase();
/*  716 */               Stargate s = new Stargate();
/*  717 */               s.setLocation(Bukkit.getPlayer(sender.getName()).getLocation());
/*  718 */               s.direction = skydirection;
/*  719 */               s.makeGateShape(Boolean.valueOf(false));
/*  720 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("gateBuild", ""));
/*      */             }
/*      */             else {
/*  723 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("commandWrongArguments", ""));
/*  724 */               return true;
/*      */             }
/*      */           }
/*      */           else {
/*  728 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("commandWrongArguments", ""));
/*  729 */             return true;
/*      */           }
/*  731 */           return true;
/*      */         }
/*      */         
/*      */ 
/*  735 */         if (args[0].equalsIgnoreCase(this.commandRemove)) {
/*  736 */           if (!player.hasPermission("stargate.command.remove")) {
/*  737 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*  738 */             return true;
/*      */           }
/*  740 */           StargateFileReader sfr = new StargateFileReader();
/*  741 */           String startGate = (String)this.START_GATES.get(sender.getName());
/*  742 */           if (startGate != null) {
/*  743 */             Stargate s = sfr.getStargate(startGate);
/*  744 */             if (s == null) {
/*  745 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotInDatabase", ""));
/*  746 */               return true;
/*      */             }
/*      */             
/*  749 */             if ((s.getNetwork() != null) && 
/*  750 */               (!s.getNetwork().allowsPlayer(player.getName()))) {
/*  751 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*  752 */               return true;
/*      */             }
/*      */             
/*      */             try
/*      */             {
/*  757 */               s.stopConnectionFromBothSides();
/*  758 */               sfr.deleteStargate(s);
/*  759 */               s.makeGateShape(Boolean.valueOf(false));
/*  760 */               s.loc.getBlock().setType(Material.AIR);
/*      */               
/*  762 */               GateNetwork gn = new GateNetwork("", "");
/*  763 */               ArrayList<GateNetwork> l = gn.getNetworkList();
/*  764 */               for (GateNetwork network : l) {
/*  765 */                 network.removeGate(s.name);
/*      */               }
/*  767 */               return true;
/*      */             } catch (IOException e) {
/*  769 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotDeleted", ""));
/*  770 */               return true;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  775 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("playerHasToSelectGate", ""));
/*  776 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  782 */         if (args[0].equalsIgnoreCase(this.commandDeactivate)) {
/*  783 */           if (!player.hasPermission("stargate.command.deactivate")) {
/*  784 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*  785 */             return true;
/*      */           }
/*  787 */           StargateFileReader sfr = new StargateFileReader();
/*  788 */           String startGate = (String)this.START_GATES.get(sender.getName());
/*  789 */           if (startGate != null) {
/*  790 */             Stargate s = sfr.getStargate(startGate);
/*      */             
/*  792 */             if ((s.activationStatus) && (!s.target.equals("null")))
/*      */             {
/*  794 */               if ((s.getNetwork() != null) && 
/*  795 */                 (!s.getNetwork().allowsPlayer(player.getName()))) {
/*  796 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  797 */                   language.get("networkPlayerNotAllowed", ""));
/*  798 */                 return true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  803 */               ArrayList<String> args3 = new ArrayList();
/*  804 */               args3.add(s.name);
/*  805 */               args3.add(s.target);
/*  806 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("gateConnectionClosed", args3));
/*      */               
/*  808 */               s.stopConnection();
/*  809 */               return true;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  816 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateConnectionNotClosed", ""));
/*  817 */             return true;
/*      */           }
/*      */           
/*      */ 
/*  821 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("playerHasToSelectGate", ""));
/*  822 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  828 */         if (args[0].equalsIgnoreCase(this.commandRepair)) {
/*  829 */           if (!player.hasPermission("stargate.command.repair")) {
/*  830 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*  831 */             return true;
/*      */           }
/*  833 */           if ((args.length > 0) && (args[1] != null)) {
/*  834 */             StargateFileReader sfr = new StargateFileReader();
/*  835 */             Stargate s = sfr.getStargate(args[1]);
/*  836 */             if (s != null) {
/*  837 */               if (!s.checkGateShape())
/*      */               {
/*  839 */                 s.deactivate();
/*  840 */                 s.deactivateShield();
/*  841 */                 s.stopConnectionFromBothSides();
/*  842 */                 s.makeGateShape(Boolean.valueOf(true));
/*  843 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("gateRepaired", ""));
/*      */               }
/*      */               
/*  846 */               if (!s.checkSign())
/*      */               {
/*  848 */                 Block b = s.loc.getBlock();
/*  849 */                 b.setType(Material.WALL_SIGN);
/*  850 */                 org.bukkit.block.Sign sign = (org.bukkit.block.Sign)b.getState();
/*      */                 
/*  852 */                 BlockState state = b.getState();
/*      */                 
/*      */ 
/*  855 */                 if (s.direction.equalsIgnoreCase("NORTH")) {
/*  856 */                   ((org.bukkit.material.Sign)state.getData()).setFacingDirection(BlockFace.SOUTH);
/*      */                 }
/*  858 */                 if (s.direction.equalsIgnoreCase("SOUTH")) {
/*  859 */                   ((org.bukkit.material.Sign)state.getData()).setFacingDirection(BlockFace.EAST);
/*      */                 }
/*  861 */                 if (s.direction.equalsIgnoreCase("EAST")) {
/*  862 */                   ((org.bukkit.material.Sign)state.getData()).setFacingDirection(BlockFace.WEST);
/*      */                 }
/*  864 */                 if (s.direction.equalsIgnoreCase("WEST")) {
/*  865 */                   ((org.bukkit.material.Sign)state.getData()).setFacingDirection(BlockFace.EAST);
/*      */                 }
/*  867 */                 state.update();
/*      */                 
/*  869 */                 sign.setLine(0, ChatColor.GOLD + "[Stargate]");
/*  870 */                 sign.setLine(1, s.name);
/*  871 */                 sign.update();
/*  872 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("signRepaired", ""));
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  877 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotRepairedNotInDatabase", ""));
/*  878 */               return true;
/*      */             }
/*      */           }
/*      */           else {
/*  882 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("commandMissingGatename", ""));
/*  883 */             return true;
/*      */           }
/*  885 */           return true;
/*      */         }
/*      */         
/*      */         Material m;
/*  889 */         if (args[0].equalsIgnoreCase(this.commandActivate)) {
/*  890 */           if (!player.hasPermission("stargate.command.activate")) {
/*  891 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/*  892 */             return true;
/*      */           }
/*  894 */           if (args[1] != null) {
/*  895 */             StargateFileReader sfr = new StargateFileReader();
/*  896 */             String startGate = (String)this.START_GATES.get(sender.getName());
/*  897 */             if (startGate != null) {
/*  898 */               Stargate s = sfr.getStargate(startGate);
/*  899 */               if (s != null)
/*      */               {
/*  901 */                 if (!s.activationStatus) {
/*  902 */                   s.target = args[1];
/*      */                   
/*      */ 
/*  905 */                   if ((s.getNetwork() != null) && (sfr.getStargate(s.target).getNetwork() != null))
/*      */                   {
/*  907 */                     if (!s.getNetwork().name.equals(sfr.getStargate(s.target).getNetwork().name)) {
/*  908 */                       player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  909 */                         language.get("networkGatesNotInSameNetwork", ""));
/*  910 */                       return true;
/*      */                     }
/*  912 */                     if (!s.getNetwork().allowsPlayer(player.getName())) {
/*  913 */                       player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  914 */                         language.get("networkPlayerNotAllowed", ""));
/*  915 */                       return true;
/*      */                     }
/*      */                   }
/*      */                   
/*  919 */                   if (((s.getNetwork() == null) && (sfr.getStargate(s.target).getNetwork() != null)) || (
/*  920 */                     (s.getNetwork() != null) && (sfr.getStargate(s.target).getNetwork() == null))) {
/*  921 */                     player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/*  922 */                       language.get("networkGatesNotInSameNetwork", ""));
/*  923 */                     return true;
/*      */                   }
/*      */                   
/*      */ 
/*  927 */                   if (!s.loc.getWorld().equals(sfr.getStargate(s.target).loc.getWorld())) {
/*  928 */                     int connectionPaid = 0;
/*  929 */                     if (!configValues.InterWorldConnectionCosts.equals("none")) {
/*  930 */                       String[] costs = configValues.InterWorldConnectionCosts.split(",");
/*  931 */                       m = Material.getMaterial(Integer.parseInt(costs[0]));
/*  932 */                       Object inv = player.getInventory();
/*  933 */                       for (int i = 0; i < ((Inventory)inv).getSize(); i++) {
/*  934 */                         ItemStack itm = ((Inventory)inv).getItem(i);
/*  935 */                         if ((itm != null) && 
/*  936 */                           (itm.getType().equals(m)) && 
/*  937 */                           (((Inventory)inv).getItem(i).getAmount() - Integer.parseInt(costs[1]) >= 0)) {
/*  938 */                           player.getInventory().removeItem(new ItemStack[] { new ItemStack(m, Integer.parseInt(costs[1])) });
/*  939 */                           connectionPaid = 1;
/*  940 */                           break;
/*      */                         }
/*      */                         
/*      */                       }
/*      */                     }
/*      */                     else
/*      */                     {
/*  947 */                       System.out.println("no costs");
/*  948 */                       connectionPaid = 1;
/*      */                     }
/*  950 */                     if (connectionPaid == 0) {
/*  951 */                       String costName = Material.getMaterial(Integer.parseInt(configValues.InterWorldConnectionCosts.split(",")[0])).toString();
/*  952 */                       player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gatePaymentMissing", costName));
/*  953 */                       return true;
/*      */                     }
/*      */                   }
/*      */                   
/*      */ 
/*  958 */                   if (!s.connectToTarget()) {
/*  959 */                     player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateConnectionFailed", ""));
/*  960 */                     return true;
/*      */                   }
/*      */                   
/*  963 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("gateNewConnection", s.target));
/*      */                   
/*      */ 
/*  966 */                   if (player.hasPermission("stargate.discover")) {
/*  967 */                     PlayerDataReader pdr = new PlayerDataReader(player);
/*  968 */                     if (!pdr.knowsGate(s.target)) {
/*  969 */                       pdr.saveStargate(s.target);
/*  970 */                       player.sendMessage(
/*  971 */                         ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/*  972 */                         language.get("gateNewAddress", s.target));
/*      */                     }
/*      */                   }
/*  975 */                   return true;
/*      */                 }
/*      */                 
/*  978 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateAlreadyActive", ""));
/*  979 */                 return true;
/*      */               }
/*      */               
/*  982 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotInDatabase", ""));
/*  983 */               return true;
/*      */             }
/*  985 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("playerHasToSelectGate", ""));
/*  986 */             return true;
/*      */           }
/*      */           
/*      */ 
/*  990 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("commandWrongArguments", ""));
/*  991 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  998 */         if (args[0].equalsIgnoreCase(this.commandShield)) {
/*  999 */           if (!player.hasPermission("stargate.command.shield")) {
/* 1000 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/* 1001 */             return true;
/*      */           }
/* 1003 */           String startGate = (String)this.START_GATES.get(sender.getName());
/* 1004 */           if (startGate != null) {
/* 1005 */             StargateFileReader sfr = new StargateFileReader();
/* 1006 */             Stargate s = sfr.getStargate(startGate);
/* 1007 */             if (s != null) {
/* 1008 */               if (s.getNetwork() != null)
/*      */               {
/* 1010 */                 if ((!s.getNetwork().allowsPlayer(player.getName())) || (s.getNetwork().isPublic())) {
/* 1011 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + 
/* 1012 */                     language.get("networkPlayerNotAllowed", ""));
/* 1013 */                   return true;
/*      */                 }
/*      */               }
/* 1016 */               s.switchShield();
/* 1017 */               player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("shieldStatusSwitched", ""));
/* 1018 */               return true;
/*      */             }
/*      */             
/* 1021 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotInDatabase", ""));
/*      */           }
/*      */           else
/*      */           {
/* 1025 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("playerHasToSelectGate", ""));
/*      */           }
/* 1027 */           return true;
/*      */         }
/*      */         
/*      */         Stargate s;
/*      */         
/* 1032 */         if (args[0].equalsIgnoreCase(this.commandGatelist)) {
/* 1033 */           if (!player.hasPermission("stargate.command.gatelist")) {
/* 1034 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/* 1035 */             return false;
/*      */           }
/* 1037 */           player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + language.get("chatGateList", ""));
/*      */           
/* 1039 */           StargateFileReader sfr = new StargateFileReader();
/* 1040 */           PlayerDataReader pdr = new PlayerDataReader(player);
/* 1041 */           ArrayList<String> knownGates = pdr.getKnownStargates();
/* 1042 */           ArrayList<Stargate> gateList = new ArrayList();
/*      */           
/* 1044 */           for (String name : knownGates) {
/* 1045 */             gateList.add(sfr.getStargate(name));
/*      */           }
/*      */           
/* 1048 */           if (gateList.size() == 0) {
/* 1049 */             player.sendMessage(ChatColor.RED + language.get("chatNoStargatesExist", ""));
/* 1050 */             return true;
/*      */           }
/*      */           
/* 1053 */           for (m = gateList.iterator(); m.hasNext();) { s = (Stargate)m.next();
/* 1054 */             Object args1 = new ArrayList();
/* 1055 */             ((ArrayList)args1).add(s.name);
/* 1056 */             ((ArrayList)args1).add(s.loc.getBlockX());
/* 1057 */             ((ArrayList)args1).add(s.loc.getBlockZ());
/* 1058 */             ((ArrayList)args1).add(s.loc.getBlockY());
/* 1059 */             if (s.activationStatus) {
/* 1060 */               ((ArrayList)args1).add(language.get("chatGateListActivated", ""));
/*      */             }
/*      */             else {
/* 1063 */               ((ArrayList)args1).add("");
/*      */             }
/* 1065 */             player.sendMessage(ChatColor.GREEN + language.get("chatGateListEntry", (ArrayList)args1));
/*      */           }
/* 1067 */           return true;
/*      */         }
/*      */         
/* 1070 */         if (args[0].equalsIgnoreCase(this.commandNetwork)) {
/* 1071 */           if (!player.hasPermission("network.commands")) {
/* 1072 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("permissionNotAllowed", ""));
/* 1073 */             return false;
/*      */           }
/* 1075 */           String startGate = (String)this.START_GATES.get(sender.getName());
/*      */           
/* 1077 */           if (startGate != null) {
/* 1078 */             StargateFileReader sfr = new StargateFileReader();
/* 1079 */             Stargate s = sfr.getStargate(startGate);
/* 1080 */             if (s != null)
/*      */             {
/*      */ 
/* 1083 */               if ((args[1] != null) && 
/* 1084 */                 (args[1].equalsIgnoreCase("info")) && 
/* 1085 */                 (s.getNetwork() != null))
/*      */               {
/* 1087 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + s.getNetworkName());
/* 1088 */                 player.sendMessage(ChatColor.GREEN + s.getNetwork().state);
/*      */                 
/*      */ 
/* 1091 */                 player.sendMessage(ChatColor.GREEN + "Admins: " + ChatColor.DARK_PURPLE + s.getNetwork().networkadmins);
/*      */                 
/*      */ 
/* 1094 */                 if (s.getNetwork().isPrivate()) {
/* 1095 */                   player.sendMessage(ChatColor.GREEN + "Member: " + ChatColor.YELLOW + s.getNetwork().networkmembers);
/*      */                 }
/*      */                 
/* 1098 */                 for (String gatename : s.getNetwork().networkstargates) {
/* 1099 */                   PlayerDataReader pdr = new PlayerDataReader(player);
/* 1100 */                   Object knownGates = pdr.getKnownStargates();
/* 1101 */                   if ((((ArrayList)knownGates).contains(gatename)) || (s.getNetwork().hasAdmin(player.getName()))) {
/* 1102 */                     player.sendMessage(ChatColor.GREEN + gatename);
/*      */                   }
/*      */                 }
/* 1105 */                 return true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1111 */               if ((s.getNetwork() != null) && 
/* 1112 */                 (!s.getNetwork().hasAdmin(player.getName()))) {
/* 1113 */                 player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + 
/* 1114 */                   ChatColor.RED + language.get("networkPlayerNotAdmin", ""));
/* 1115 */                 return true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1120 */               if (args[1] != null)
/*      */               {
/* 1122 */                 if ((args[1].equals("remove")) && 
/* 1123 */                   (s.getNetwork() != null)) {
/* 1124 */                   GateNetwork net = s.getNetwork();
/* 1125 */                   net.removeGate(s.name);
/* 1126 */                   net.save();
/* 1127 */                   s.updateSign();
/* 1128 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1129 */                     language.get("networkRemovedGate", ""));
/* 1130 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1135 */                 if ((args[1].equals("add")) && 
/* 1136 */                   (args[2] != null) && 
/* 1137 */                   (s.getNetwork() == null)) {
/* 1138 */                   GateNetwork net = new GateNetwork(player.getName(), args[2]);
/*      */                   
/* 1140 */                   if (net.getNetwork(args[2]) == null) {
/* 1141 */                     net.addGate(s.name);
/* 1142 */                     net.save();
/*      */                   } else {
/* 1144 */                     s.setNetwork(args[2]);
/*      */                   }
/*      */                   
/* 1147 */                   s.updateSign();
/*      */                   
/* 1149 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1150 */                     language.get("networkAddedGate", ""));
/* 1151 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1157 */                 if ((args[1].equals("public")) && 
/* 1158 */                   (s.getNetwork() != null)) {
/* 1159 */                   GateNetwork net = s.getNetwork();
/* 1160 */                   net.makePublic();
/* 1161 */                   net.save();
/* 1162 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1163 */                     language.get("networkSetPublic", net.name));
/* 1164 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/* 1168 */                 if ((args[1].equals("private")) && 
/* 1169 */                   (s.getNetwork() != null)) {
/* 1170 */                   GateNetwork net = s.getNetwork();
/* 1171 */                   net.makePrivate();
/* 1172 */                   net.save();
/* 1173 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1174 */                     language.get("networkSetPrivate", net.name));
/* 1175 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1180 */                 if ((args[1].equals("op")) && 
/* 1181 */                   (args[2] != null) && 
/* 1182 */                   (s.getNetwork() != null)) {
/* 1183 */                   GateNetwork net = s.getNetwork();
/* 1184 */                   net.addAdmin(args[2]);
/* 1185 */                   net.save();
/* 1186 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1187 */                     language.get("networkOpedPlayer", args[2]));
/* 1188 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1193 */                 if ((args[1].equals("deop")) && 
/* 1194 */                   (args[2] != null) && 
/* 1195 */                   (s.getNetwork() != null)) {
/* 1196 */                   GateNetwork net = s.getNetwork();
/* 1197 */                   net.removeAdmin(args[2]);
/* 1198 */                   net.addMember(args[2]);
/* 1199 */                   net.save();
/* 1200 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1201 */                     language.get("networkDeopedPlayer", args[2]));
/* 1202 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1208 */                 if ((args[1].equals("allow")) && 
/* 1209 */                   (args[2] != null) && 
/* 1210 */                   (s.getNetwork() != null)) {
/* 1211 */                   GateNetwork net = s.getNetwork();
/* 1212 */                   net.addMember(args[2]);
/* 1213 */                   net.save();
/* 1214 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1215 */                     language.get("networkAllowPlayer", args[2]));
/* 1216 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1221 */                 if ((args[1].equals("unallow")) && 
/* 1222 */                   (args[2] != null) && 
/* 1223 */                   (s.getNetwork() != null)) {
/* 1224 */                   GateNetwork net = s.getNetwork();
/* 1225 */                   net.removeMember(args[2]);
/* 1226 */                   net.save();
/* 1227 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1228 */                     language.get("networkUnallowedPlayer", args[2]));
/* 1229 */                   return true;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1235 */                 if ((args[1].equalsIgnoreCase("setName")) && 
/* 1236 */                   (args[2] != null) && 
/* 1237 */                   (!args[2].equals("")) && 
/* 1238 */                   (s.getNetwork() != null)) {
/* 1239 */                   GateNetwork net = s.getNetwork();
/* 1240 */                   net.setName(args[2]);
/* 1241 */                   net.save();
/* 1242 */                   player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + 
/* 1243 */                     language.get("networkSetName", args[2]));
/* 1244 */                   return true;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1253 */               return true;
/*      */             }
/*      */             
/* 1256 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("gateNotInDatabase", ""));
/*      */           }
/*      */           else {
/* 1259 */             player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("playerHasToSelectGate", ""));
/*      */           }
/* 1261 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1269 */         player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED + language.get("commandWrongArguments", ""));
/* 1270 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1274 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void checkConficFiles()
/*      */   {
/* 1282 */     File stargateList = new File("plugins/MPStargate/stargateList.txt");
/* 1283 */     File mpgatesConfig = new File("plugins/MPStargate/mpgatesConfig.txt");
/* 1284 */     File NetworkList = new File("plugins/MPStargate/networkList.txt");
/*      */     
/*      */ 
/* 1287 */     File parent = stargateList.getParentFile();
/* 1288 */     if ((!parent.exists()) && (!parent.mkdirs())) {
/* 1289 */       throw new IllegalStateException("Couldn't create dir: " + parent);
/*      */     }
/* 1291 */     File parent2 = mpgatesConfig.getParentFile();
/* 1292 */     if ((!parent2.exists()) && (!parent2.mkdirs())) {
/* 1293 */       throw new IllegalStateException("Couldn't create dir: " + parent2);
/*      */     }
/* 1295 */     File parent3 = NetworkList.getParentFile();
/* 1296 */     if ((!parent3.exists()) && (!parent3.mkdirs())) {
/* 1297 */       throw new IllegalStateException("Couldn't create dir: " + parent3);
/*      */     }
/*      */     
/*      */ 
/* 1301 */     if (!stargateList.exists()) {
/*      */       try {
/* 1303 */         PrintWriter writer = new PrintWriter(
/* 1304 */           "plugins/MPStargate/stargateList.txt", "UTF-8");
/* 1305 */         writer.println("# Saves all the Stargates: Name, worldID, shieldStatus, activationStatus, location, target, direction");
/* 1306 */         writer.close();
/*      */       }
/*      */       catch (Exception e) {
/* 1309 */         System.out.println("[WARNING] Couldn't create stargateListe.txt!");
/*      */       }
/*      */     }
/*      */     
/* 1313 */     if (!NetworkList.exists()) {
/*      */       try {
/* 1315 */         PrintWriter writer = new PrintWriter(
/* 1316 */           "plugins/MPStargate/networkList.txt", "UTF-8");
/* 1317 */         writer.println("# Saves all the Networks of a world");
/* 1318 */         writer.close();
/*      */       }
/*      */       catch (Exception e) {
/* 1321 */         System.out.println("[WARNING] Couldn't create networkList.txt!");
/*      */       }
/*      */     }
/*      */     
/* 1325 */     if (!mpgatesConfig.exists()) {
/*      */       try {
/* 1327 */         PrintWriter writer = new PrintWriter(
/* 1328 */           "plugins/MPStargate/mpgatesConfig.txt", "UTF-8");
/* 1329 */         writer.println("# Config File - Everything else is coming soon ;-)");
/* 1330 */         writer.println("# For materialsettings, just use solid blocks and the official names for the materials");
/* 1331 */         writer.println("# Deleting this file and restarting the plugin will give you a version with default-values.");
/* 1332 */         writer.println("# ATTENTION!! Changes could cause the plugin to have troubles finding already made stargates.");
/* 1333 */         writer.println("activationTime: 38 # seconds gate stays open (0 = infinite)");
/* 1334 */         writer.println("DHDMaterial: OBSIDIAN");
/* 1335 */         writer.println("GateMaterial: OBSIDIAN");
/* 1336 */         writer.println("ChevronMaterial: REDSTONE_BLOCK");
/* 1337 */         writer.println("ShieldMaterial: STONE");
/* 1338 */         writer.println("DHDDistance: 9 #Distance of the !sign! (not the DHD-block) to the gate, the DHD-block is one less.");
/* 1339 */         writer.println("KawooshSound: ENDERDRAGON_GROWL");
/* 1340 */         writer.println("KawooshSoundRadius: 20");
/* 1341 */         writer.println("KawooshSoundVolume: 10");
/* 1342 */         writer.println("IrisDamage: 18 #in hitpoints. One heart equals 2 hitpoints");
/* 1343 */         writer.println("IrisDestroyInventory: 1 # 0 false, 1 true, if player dies, inventory goes with him");
/* 1344 */         writer.println("IrisNoTeleport: false #if targets shield is activated, teleport back to starting-gate");
/* 1345 */         writer.println("Language: en #country-code, e.g. klingon if your languagepack is named language_klingon.txt");
/* 1346 */         writer.println("RingMaterial: 44,0 #MaterialID,ByteData");
/* 1347 */         writer.println("RingGroundMaterial: 98,3 #MaterialID,ByteData");
/* 1348 */         writer.println("RingDistance: 0 #Offset Sign to Ringtransporter");
/* 1349 */         writer.println("InterWorldConnectionCosts: 264,1 #ItemID, amount");
/*      */         
/* 1351 */         writer.println("NetworkTeleportUnallowedPlayers: 1");
/*      */         
/*      */ 
/* 1354 */         writer.close();
/*      */       }
/*      */       catch (Exception e) {
/* 1357 */         System.out.println("[WARNING] Couldn't create mpgatesConfig.txt!");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void checkDefaultLanguageFile()
/*      */   {
/* 1365 */     File stargateLanguageFile = new File("plugins/MPStargate/language_en.txt");
/*      */     
/* 1367 */     File parent = stargateLanguageFile.getParentFile();
/* 1368 */     if ((!parent.exists()) && (!parent.mkdirs())) {
/* 1369 */       throw new IllegalStateException("Couldn't create dir: " + parent);
/*      */     }
/*      */     
/*      */ 
/* 1373 */     if (!stargateLanguageFile.exists()) {
/*      */       try {
/* 1375 */         PrintWriter writer = new PrintWriter(
/* 1376 */           "plugins/MPStargate/language_en.txt", "UTF-8");
/* 1377 */         writer.println("# Language File English");
/* 1378 */         writer.println("# You can change everything behind the \":\". The number of %s should not be changed!");
/* 1379 */         writer.println("# Deleting this file and restarting the plugin will give you a version with default-values.");
/* 1380 */         writer.println("# ATTENTION!! Dont change the Values in front of the \":\". This will result in Error-Messages.");
/* 1381 */         writer.println("chatGateListActivated: \", ACTIVATED\"");
/* 1382 */         writer.println("chatGateListEntry: \"%s (%s, %s, %s) %s\"\t#Arguments: Name, positions(x,z,y), if activated: chatGateListActivated");
/* 1383 */         writer.println("chatGateList: \"Gatelist\"");
/* 1384 */         writer.println("chatNoStargatesExist: \"No existing Stargates!\"");
/* 1385 */         writer.println("commandWrongArguments: \"Wrong number or type of arguments!\"");
/* 1386 */         writer.println("commandMissingGatename: \"Last Argument <Gatename> missed!\"");
/* 1387 */         writer.println("gateAlreadyActive: \"Stargate was already activated!\"");
/* 1388 */         writer.println("gateBuild: \"You build a stargate-shape.\"");
/* 1389 */         writer.println("gateConnectionClosed: \"You closed the connection %s -> %s.\"\t#Arguments: Startgate, Targetgate");
/* 1390 */         writer.println("gateConnectionClosedTimeout: \"The connection %s -> %s closed automatically. Maybe it timed out.\"\t#Arguments: Startgate, Targetgate");
/* 1391 */         writer.println("gateConnectionFailed: \"Stargate-connection failed!\"");
/* 1392 */         writer.println("gateConnectionNotClosed: \"Didn't closed connection.\"");
/* 1393 */         writer.println("gateCreated: \"Stargate %s was created!\"\t\t\t#Arguments: The Stargates name");
/* 1394 */         writer.println("gateCreationFailed: \"Couldn't create Stargate!\"");
/* 1395 */         writer.println("gateDestroyed: \"%s was destoryed. You should repair it!\"\t\t#Arguments: The Stargates name");
/* 1396 */         writer.println("gateNameExists: \"Stargate wasnt created. Name already exists!\"");
/* 1397 */         writer.println("gateNewAddress: \"You found the unknown Stargate-address %s.\"");
/* 1398 */         writer.println("gateNewConnection: \"Connected Stargate to %s.\"");
/* 1399 */         writer.println("gateNotArround: \"Couldn't find a Stargate nearby!\"");
/* 1400 */         writer.println("gateNotInDatabase: \"An Error occured. Stargate wasn't found in the database!\"");
/* 1401 */         writer.println("gateNotDeleted: \"An Error occured. Stargate wasn't deleted!\"");
/* 1402 */         writer.println("gateNotRepairedNotInDatabase: \"Stargate couldn't be repaired. Name not found!\"");
/* 1403 */         writer.println("gatePaymentMissing: \"To activate a connection to an other world, you have to activate an aditional Chevron. For this, you need %s.\"\t#Arguments: A Materials name");
/* 1404 */         writer.println("gateRepaired: \"Stargate was repaired!\"");
/* 1405 */         writer.println("gateSelected: \"%s selected.\"\t\t\t\t#Arguments: The Stargates name");
/* 1406 */         writer.println("gateSelectedHasTarget: \"%s (-> %s) selected.\"\t\t\t#Arguments: The Stargates name and the name of the targetgate");
/* 1407 */         writer.println("networkAddedGate: \"You added this Gate to the network %s.\"");
/* 1408 */         writer.println("networkAllowPlayer: \"You allowed %s to use this network now.\"");
/* 1409 */         writer.println("networkDeopPlayer: \"You made %s a normal user, not an opperator.\"");
/* 1410 */         writer.println("networkNoPermissionCreate: \"You don't have permission to create new networks. Gate was created without network.\"");
/* 1411 */         writer.println("networkOpPlayer: \"You made %s an opperator of this network.\"");
/* 1412 */         writer.println("networkPlayerNotAllowed: \"You aren't a member or admin, so you aren't allowed to use this network.\"");
/* 1413 */         writer.println("networkPlayerNotAdmin: \"You aren't a admin of this network.\"");
/* 1414 */         writer.println("networkRemovedGate: \"You deleted this Gate from the network %s.\"");
/* 1415 */         writer.println("networkSetName: \"You changed this networks name to %s.\"");
/* 1416 */         writer.println("networkSetPrivate: \"You made the network %s a private one. Use the allow-command to allow players to use it.\"");
/* 1417 */         writer.println("networkSetPublic: \"You made the network %s a public one. Everyone can use it now.\"");
/* 1418 */         writer.println("networkUnallowPlayer: \"You took the permission from %s to use this Gates network.\"");
/* 1419 */         writer.println("networkGatesNotInSameNetwork: \"Both gates have to be inside the same network or both without a network.\"");
/* 1420 */         writer.println("permissionNotAllowed: \"You dont have permission to do this.\"");
/* 1421 */         writer.println("pluginNameChat: \"[Stargate] \"");
/* 1422 */         writer.println("playerHasToSelectGate: \"You need to select a Stargate!\"");
/* 1423 */         writer.println("playerKilledByKawoosh: \"You were dematerialized by the KAWOOOOOSH! Don't stay so close next time.\"");
/* 1424 */         writer.println("playerDamagedByIris: \"You got a bitchslap from Iris straight in your face. Next time, try to walk through the gate backwards.\"");
/* 1425 */         writer.println("ringsConstructionDamaged: \"The Rings or the target rings seem to be damaged\"");
/* 1426 */         writer.println("ringsNoTarget: \"There seems to be no rings around, that could work as receiver.\"");
/* 1427 */         writer.println("shieldStatusSwitched: \"Shield was activated/deactivated.\"");
/* 1428 */         writer.println("signRepaired: \"Sign was repaired!\"");
/* 1429 */         writer.println("wrongCommand: \"Wrong command or invalid quantity of arguments. Try %s help\"\t\t\t\t#Arguments: the commands prefix");
/*      */         
/*      */ 
/*      */ 
/* 1433 */         writer.close();
/*      */       }
/*      */       catch (Exception e) {
/* 1436 */         System.out.println("[WARNING] Couldn't create mpgatesConfig.txt!");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ArrayList<Double> swapper(double y1, double y2)
/*      */   {
/* 1444 */     y1 += y2;
/* 1445 */     y2 = y1 - y2;
/* 1446 */     y1 -= y2;
/* 1447 */     ArrayList<Double> ret = new ArrayList();
/* 1448 */     ret.add(Double.valueOf(y1));
/* 1449 */     ret.add(Double.valueOf(y2));
/* 1450 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */   public static Stargate getHorizonEntityIsInside(Entity entity)
/*      */   {
/* 1456 */     StargateFileReader sfr = new StargateFileReader();
/* 1457 */     ArrayList<Stargate> stargateList = sfr.getActivStartGates();
/*      */     
/* 1459 */     for (Stargate s : stargateList) {
/* 1460 */       Vector direction = s.getNormalVector();
/* 1461 */       Vector start = s.getPosition().add(direction.clone().multiply(Stargate.DHD_DISTANCE));
/*      */       
/*      */ 
/*      */ 
/* 1465 */       double x1 = start.getX();
/* 1466 */       double x2 = start.getX();
/* 1467 */       double y1 = start.getY();
/* 1468 */       double y2 = start.getY();
/* 1469 */       double z1 = start.getZ();
/* 1470 */       double z2 = start.getZ() + 5.0D;
/*      */       
/*      */ 
/* 1473 */       if (direction.equals(new Vector(0, 1, 0))) {
/* 1474 */         x1 -= 3.0D;x2 += 3.0D;y2 += 1.0D;
/*      */       }
/* 1476 */       if (direction.equals(new Vector(0, -1, 0))) {
/* 1477 */         x1 -= 3.0D;x2 += 3.0D;y2 += 1.0D;
/*      */       }
/*      */       
/*      */ 
/* 1481 */       if (direction.equals(new Vector(1, 0, 0))) {
/* 1482 */         x2 += 1.0D;y1 -= 3.0D;y2 += 3.0D;
/*      */       }
/* 1484 */       if (direction.equals(new Vector(-1, 0, 0))) {
/* 1485 */         x1 += 1.0D;y1 -= 3.0D;y2 += 3.0D;
/*      */       }
/* 1487 */       if (y1 > y2) {
/* 1488 */         y1 += y2;
/* 1489 */         y2 = y1 - y2;
/* 1490 */         y1 -= y2;
/*      */       }
/*      */       
/* 1493 */       if (x1 > x2) {
/* 1494 */         x1 += x2;
/* 1495 */         x2 = x1 - x2;
/* 1496 */         x1 -= x2;
/*      */       }
/*      */       
/* 1499 */       if (z1 > z2) {
/* 1500 */         z1 += z2;
/* 1501 */         z2 = z1 - z2;
/* 1502 */         z1 -= z2;
/*      */       }
/*      */       
/*      */ 
/* 1506 */       if ((y1 <= entity.getLocation().getZ()) && (entity.getLocation().getZ() <= y2) && 
/* 1507 */         (x1 <= entity.getLocation().getX()) && (entity.getLocation().getX() <= x2) && 
/* 1508 */         (z1 <= entity.getLocation().getY()) && (entity.getLocation().getY() <= z2)) {
/* 1509 */         return s;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1514 */     return null;
/*      */   }
/*      */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */