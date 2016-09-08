package com.DanMan.MCStargates.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.DanMan.MCStargates.rings.Ringtransporter;
import com.DanMan.MCStargates.stargate.GateNetwork;
import com.DanMan.MCStargates.stargate.Stargate;
import com.DanMan.MCStargates.utils.ConfigFileReader;
import com.DanMan.MCStargates.utils.LanguageFileReader;
import com.DanMan.MCStargates.utils.PlayerDataReader;
import com.DanMan.MCStargates.utils.SignUtils;
import com.DanMan.MCStargates.utils.StargateFileReader;

public class MCStargates extends JavaPlugin implements Listener {
	public Map<String, String> START_GATES = new java.util.HashMap();
	private ConfigFileReader configValues;
	public LanguageFileReader language;

	public void onEnable() {
		String version = getDescription().getVersion();
		System.out.println("    000000");
		System.out.println("  0        0");
		System.out.println("0            0");
		System.out.println("0MC-Stargates0");
		System.out.println("0    " + version + "     0");
		System.out.println("  0        0");
		System.out.println("    000000");

		getServer().getPluginManager().registerEvents(this, this);
		checkConficFiles();
		checkDefaultLanguageFile();

		configValues = new ConfigFileReader();
		language = new LanguageFileReader(configValues.getLanguage());
		getLogger().info("MC-Stargates: Enabled");
	}

	public void onDisable() {
		if (configValues.getActivationTime() != 0) {
			StargateFileReader sfr = new StargateFileReader(this);

			ArrayList<Stargate> activeGates = sfr.getActiveStartGates();
			for (Stargate s : activeGates) {
				s.stopConnection();
			}
		}
		getLogger().info("MC-Stargates: Disabled");
	}

	public ConfigFileReader getConfigValues() {
		return configValues;
	}

	public void setConfigValues(ConfigFileReader configValues) {
		this.configValues = configValues;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerDataReader pdr = new PlayerDataReader(p, this);
		if (!pdr.checkForPlayerData()) {
			pdr.createPlayerData();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void defineStargate(SignChangeEvent sign) throws Exception {
		Player player = sign.getPlayer();
		if (player.hasPermission("stargate.register")) {
			if ((sign.getLine(0).equalsIgnoreCase("[Stargate]")) && (!sign.getLine(1).equals(""))) {
				String gateName = sign.getLine(1);

				Sign s = (Sign) sign.getBlock().getState();
				SignUtils su = new SignUtils();

				Stargate stargate = new Stargate(this);
				stargate.setLocation(s.getLocation());

				stargate.setDirection(SignUtils.signFacing(s).getOppositeFace());

				if (stargate.checkGateShape()) {
					stargate.setName(gateName);

					StargateFileReader sfr = new StargateFileReader(this);
					try {
						if (sfr.getStargate(stargate.getName()) == null) {
							sfr.saveStargate(stargate);
							sign.setLine(0, ChatColor.GOLD + "[Stargate]");

							stargate.makeGateShape(Boolean.valueOf(true));

							if (player.hasPermission("network.create")) {
								String networkName = "";
								if (!sign.getLine(3).equals("")) {
									networkName = sign.getLine(3);

									GateNetwork emptyNetwork = new GateNetwork(player.getName(), networkName, this);

									if (emptyNetwork.getNetwork(networkName) == null) {
										emptyNetwork.addGate(stargate.getName());
										emptyNetwork.save();
									} else {
										GateNetwork existingNetwork = emptyNetwork.getNetwork(networkName);
										if (existingNetwork.hasAdmin(player.getName())) {
											existingNetwork.addGate(stargate.getName());
											existingNetwork.save();
										}
									}
									stargate.updateSign();
								}
							} else {
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
										+ language.get("networkNoPermissionCreate", ""));
							}

							sign.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
									+ ChatColor.GREEN + language.get("gateCreated", stargate.getName()));
						} else {
							sign.setLine(0, ChatColor.RED + "Stargate");
							sign.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
									+ ChatColor.RED + language.get("gateNameExists", ""));
						}
					} catch (Exception e) {
						e.printStackTrace();
						sign.setLine(0, ChatColor.RED + "Stargate");
						sign.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("gateCreationFailed", ""));
					}
				} else {
					sign.setLine(0, ChatColor.RED + "Stargate");
					sign.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("gateNotArround", ""));
				}
			}
		} else {
			player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
					+ language.get("permissionNotAllowed", ""));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void defineRingtransporter(SignChangeEvent sign) throws Exception {
		Player player = sign.getPlayer();
		if (sign.getLine(0).equalsIgnoreCase("[Rings]")) {
			if (player.hasPermission("rings.build")) {
				Ringtransporter rt = new Ringtransporter(this);
				rt.setLocation(sign.getBlock().getLocation());
				if (rt.checkConstuction()) {
					sign.setLine(0, ChatColor.GOLD + "[Rings]");
				}
			} else {
				sign.setLine(0, "Rings");
				player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
						+ language.get("permissionNotAllowed", ""));
			}
		}
	}

	@EventHandler
	public void redstoneSignalInStargate(BlockRedstoneEvent event) {
		Block b = event.getBlock();

		if (b.getType().equals(Material.WALL_SIGN)) {
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
			if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {

				StargateFileReader sfr = new StargateFileReader(this);
				Stargate s = sfr.getStargate(sign.getLine(1));

				GateNetwork gn;

				if ((gn = s.getNetwork()) != null) {
					if (gn.state.equals("private")) {
						if (event.getNewCurrent() > 0) {
							s.activateShield();
						}
						if (event.getNewCurrent() == 0) {
							s.deactivateShield();
						}
					}
				} else {
					if (event.getNewCurrent() > 0) {
						s.activateShield();
					}
					if (event.getNewCurrent() == 0) {
						s.deactivateShield();
					}
				}
			}

			if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Rings]")) {
				Ringtransporter rt = new Ringtransporter(this);
				rt.setLocation(sign.getLocation());
				Ringtransporter target = rt.getPartner();

				if (target != null) {
					if ((event.getNewCurrent() > 0) && (rt.checkConstuction()) && (target.checkConstuction())) {
						rt.makeAnimation(true);
						target.makeAnimation(false);
					}
				}
			}
		}
	}

	@EventHandler
	public void leftClickGateSign(PlayerInteractEvent e) {
		if (e.getPlayer().hasPermission("stargate.close")) {
			if ((e.getAction() == Action.LEFT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
				Block block = e.getClickedBlock();
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();

				if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
					String gateName = sign.getLine(1);
					StargateFileReader sfr = new StargateFileReader(this);
					try {
						Stargate s = sfr.getStargate(gateName);
						if ((s == null) || (!s.getActivationStatus()) || (s.getTarget().equals("null")))
							return;
						GateNetwork gn;
						if (((gn = s.getNetwork()) != null) && (!gn.allowsPlayer(e.getPlayer().getName()))) {
							e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
									+ ChatColor.RED + language.get("networkPlayerNotAllowed", ""));
							return;
						}

						s.stopConnection();
						ArrayList<String> args = new ArrayList();
						args.add(s.getName());
						args.add(s.getTarget());
						e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
								+ language.get("gateConnectionClosed", args));

					} catch (Exception localException) {
					}
				}

			}

		} else {
			e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
					+ language.get("permissionNotAllowed", ""));
		}
	}

	@EventHandler
	public void rightClickSign(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
			Block block = e.getClickedBlock();
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();

			if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
				e.setCancelled(true);
				String gateName = sign.getLine(1);

				StargateFileReader sfr = new StargateFileReader(this);
				try {
					Stargate s = sfr.getStargate(gateName);
					if (s != null) {
						if ((!s.checkGateShape()) || (!s.checkSign())) {
							ArrayList<String> args = new ArrayList();
							args.add(s.getName());
							e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
									+ ChatColor.RED + language.get("gateDestroyed", args));
						} else {
							this.START_GATES.put(e.getPlayer().getName(), s.getName());

							if (!s.getTarget().equals("null")) {
								ArrayList<String> args = new ArrayList();
								args.add(s.getName());
								args.add(s.getTarget());
								e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
										+ ChatColor.GREEN + language.get("gateSelectedHasTarget", args));
							} else {
								e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
										+ ChatColor.GREEN + language.get("gateSelected", s.getName()));
							}

							if (e.getPlayer().hasPermission("stargate.discover")) {
								PlayerDataReader pdr = new PlayerDataReader(e.getPlayer(),this);
								if (!pdr.knowsGate(gateName)) {
									pdr.saveStargate(gateName);
									e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("gateNewAddress", s.getName()));
								}
							}
						}
					}
				} catch (Exception localException) {
				}
			}

			if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Rings]")) {
				e.setCancelled(true);
				if (e.getPlayer().hasPermission("rings.activate")) {
					Ringtransporter rt = new Ringtransporter(this);
					rt.setLocation(sign.getLocation());
					Ringtransporter target = rt.getPartner();

					if (target != null) {
						if ((rt.checkConstuction()) && (target.checkConstuction())) {
							rt.makeAnimation(true);
							target.makeAnimation(false);
						} else {
							e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
									+ ChatColor.RED + language.get("ringsConstructionDamaged", ""));
						}
					} else {
						e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("ringsNoTarget", ""));
					}
				} else {
					e.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("permissionNotAllowed", ""));
				}
			}
		}
	}

	@EventHandler
	public void moveInKawoosh(PlayerMoveEvent event) {
		if (event.getTo().getBlock().hasMetadata("Kawoosh")) {
			Player player = event.getPlayer();
			event.getFrom().add(new Vector(1, 1, 1));
			player.setHealth(0.0D);
			player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
					+ language.get("playerKilledByKawoosh", ""));
		}
	}

	@EventHandler
	public void vehicleInStargate(VehicleMoveEvent e) {
		Entity entity = e.getVehicle().getPassenger();

		if (entity == null) {
			Stargate s = getHorizonEntityIsInside(e.getVehicle());
			if (s != null) {
				StargateFileReader sfr = new StargateFileReader(this);
				Stargate target = sfr.getStargate(s.getTarget());
				if (target != null) {
					if (target.getShieldStatus()) {
						if (configValues.getIrisNoTeleport().equals("true")) {
							target = s;
						} else {
							e.getVehicle().remove();
							return;
						}
					}
					Vector direction = target.getNormalVector();
					Vector start = target.getPosition().add(direction.clone().multiply(s.DHD_DISTANCE - 1));
					Location newLoc = new Location((World) Bukkit.getWorlds().get(target.getWorldID()), start.getX(),
							start.getZ(), start.getY());
					e.getVehicle().teleport(newLoc);
				}

			}

		} else if ((entity instanceof Player)) {
			Stargate s = getHorizonEntityIsInside(entity);
			if (s != null) {
				Player p = (Player) entity;
				PlayerMoveEvent pEvent = new PlayerMoveEvent(p, e.getFrom(), e.getTo());
				playerInStargate(pEvent);
			}
		} else {
			Stargate s = getHorizonEntityIsInside(entity);
			if (s != null) {
				StargateFileReader sfr = new StargateFileReader(this);
				Stargate target = sfr.getStargate(s.getTarget());
				if (target != null) {
					if (target.getShieldStatus()) {
						if (configValues.getIrisNoTeleport().equals("true")) {
							target = s;
						} else {
							e.getVehicle().remove();
							return;
						}
					}
					Vector direction = target.getNormalVector();
					Vector start = target.getPosition().add(direction.clone().multiply(s.DHD_DISTANCE - 1));
					Location newLoc = new Location((World) Bukkit.getWorlds().get(target.getWorldID()), start.getX(),
							start.getZ(), start.getY());
					e.getVehicle().eject();

					e.getVehicle().teleport(newLoc);
					entity.teleport(newLoc);
					e.getVehicle().setPassenger(entity);
				}
			}
		}
	}

	@EventHandler
	public void playerInStargate(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("stargate.use")) {
			Stargate s = getHorizonEntityIsInside(player);
			if (s != null) {
				StargateFileReader sfr = new StargateFileReader(this);
				Stargate target = sfr.getStargate(s.getTarget());

				if ((configValues.getIrisNoTeleport().equals("true")) && (target.getShieldStatus())) {
					target = s;
				}

				if ((s.getNetwork() != null) && (!s.getNetwork().allowsPlayer(player.getName()))
						&& (configValues.getNetworkTeleportUnallowedPlayers() == 0)) {
					target = s;
					event.getPlayer().sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("networkPlayerNotAllowed", ""));
				}

				if (target == null) {
					return;
				}

				Vector direction = target.getNormalVector();
				Vector start = target.getPosition().add(direction.clone().multiply(s.DHD_DISTANCE - 1));
				Location newLoc = new Location((World) Bukkit.getWorlds().get(target.getWorldID()), start.getX(),
						start.getZ(), start.getY());

				if (direction.equals(new Vector(1, 0, 0))) {
					newLoc.setYaw(90.0F);
				}

				if (direction.equals(new Vector(0, 1, 0))) {
					newLoc.setYaw(180.0F);
				}

				if (direction.equals(new Vector(-1, 0, 0))) {
					newLoc.setYaw(-90.0F);
				}

				if (direction.equals(new Vector(0, -1, 0))) {
					newLoc.setYaw(0.0F);
				}

				for (Player players : Bukkit.getOnlinePlayers()) {
					players.hidePlayer(player);
				}

				Entity vehicle = player.getVehicle();
				if (vehicle != null) {
					vehicle.eject();
					vehicle.teleport(newLoc);
				}

				player.teleport(newLoc);

				if ((player.getVehicle() == null) && (vehicle != null)) {
					vehicle.setPassenger(player);
				}

				if ((player.teleport(newLoc)) && (target.getShieldStatus())) {

					if ((player.getHealth() - configValues.getIrisDamage() <= 0.0D)
							&& (configValues.getIrisDestroyInventory() == 1)) {
						player.getInventory().clear();
					}

					player.setHealth(player.getHealth() - configValues.getIrisDamage());

					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("playerDamagedByIris", ""));
				}

				for (Player players : Bukkit.getOnlinePlayers()) {
					players.showPlayer(player);
				}
			}
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		Block block = event.getBlock();
		if ((block.hasMetadata("PortalWater"))
				&& (((org.bukkit.metadata.MetadataValue) block.getMetadata("PortalWater").get(0)).asString()
						.equals("true"))) {
			int id = block.getTypeId();
			if ((id == 8) || (id == 9)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void destroyStargateBlock(BlockBreakEvent event) throws IOException {
		if ((event.getBlock().getType().equals(getConfigValues().getGateMaterial()))
				|| (event.getBlock().getType().equals(getConfigValues().getDHDMaterial()))
				|| (event.getBlock().getType().equals(getConfigValues().getChevronMaterial()))) {
			if (event.getBlock().hasMetadata("StargateBlock")) {
				event.setCancelled(true);
			}
		}
		if (event.getBlock().getType().equals(Material.WALL_SIGN)) {
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getBlock().getState();
			if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Stargate]")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PistonStargateBlockExtend(BlockPistonExtendEvent event) throws IOException {
		for (int i = 0; i < event.getBlocks().size(); i++) {
			if (((Block) event.getBlocks().get(i)).hasMetadata("StargateBlock")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PistonStargateBlockRetract(BlockPistonRetractEvent event) throws IOException {
		if (event.getBlock().hasMetadata("StargateBlock")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void ExplodeStargateBlock(EntityExplodeEvent event) throws IOException {
		if (event.getEntityType().equals(EntityType.PRIMED_TNT)) {
			for (int i = 0; i < event.blockList().size(); i++) {
				if ((((Block) event.blockList().get(i)).hasMetadata("StargateBlock"))
						|| (((Block) event.blockList().get(i)).hasMetadata("PortalWater"))) {
					event.blockList().remove(i);
				}
			}
		}
	}

	String commandprefix = "mpgates";
	String commandActivate = "to";
	String commandDeactivate = "close";
	String commandRepair = "repair";
	String commandBuild = "build";
	String commandRemove = "remove";
	String commandShield = "shield";
	String commandGatelist = "list";
	String commandNetwork = "network";

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase(this.commandprefix)) {
			if (!(sender instanceof Player))
				return true;
			Player player = Bukkit.getPlayer(sender.getName());

			if (args.length == 0) {

				return true;
			}
			if (args[0] != null) {
				if ((args[0].equalsIgnoreCase("help")) || (args[0].equalsIgnoreCase("?"))) {
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + " Help:");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandBuild + " <north/south/...> (creativmode only)");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandActivate + " <targetname>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandDeactivate);
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandRemove + " (unregisters from database)");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandShield);
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandGatelist);
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandRepair + " <gatename>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " add <networkname>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " remove");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " op/deop <playername>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " allow/unallow <playername>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " private/public");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN + " /"
							+ this.commandprefix + " " + this.commandNetwork + " setName <new networkname>");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
							+ " To register a Stargate, place a sign at the front of the DHD: First line \"[Stargate]\", second line <Gatename> (optional: fourth line <networkname>.");
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
							+ " Distance DHD->Stargate: " + (configValues.getDHD_Distance() - 1));

					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandBuild)) {
					if (!player.hasPermission("stargate.command.build")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ " You dont have permission to do this.");
						return true;
					}
					if (args.length == 2) {
						if ((args[1].equalsIgnoreCase("NORTH")) || (args[1].equalsIgnoreCase("EAST"))
								|| (args[1].equalsIgnoreCase("SOUTH")) || (args[1].equalsIgnoreCase("WEST"))) {
							BlockFace skydirection = BlockFace.valueOf(args[1].toUpperCase());
							Stargate s = new Stargate(this);
							s.setLocation(Bukkit.getPlayer(sender.getName()).getLocation());
							s.setDirection(skydirection);
							s.makeGateShape(Boolean.valueOf(false));
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
									+ language.get("gateBuild", ""));
						} else {
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("commandWrongArguments", ""));
							return true;
						}
					} else {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("commandWrongArguments", ""));
						return true;
					}
					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandRemove)) {
					if (!player.hasPermission("stargate.command.remove")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return true;
					}
					StargateFileReader sfr = new StargateFileReader(this);
					String startGate = (String) this.START_GATES.get(sender.getName());
					if (startGate != null) {
						Stargate s = sfr.getStargate(startGate);
						if (s == null) {
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("gateNotInDatabase", ""));
							return true;
						}

						if ((s.getNetwork() != null) && (!s.getNetwork().allowsPlayer(player.getName()))) {
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("permissionNotAllowed", ""));
							return true;
						}

						try {
							s.stopConnectionFromBothSides();
							sfr.deleteStargate(s);
							s.makeGateShape(Boolean.valueOf(false));
							s.getLocation().getBlock().setType(Material.AIR);

							GateNetwork gn = new GateNetwork("", "", this);
							ArrayList<GateNetwork> l = gn.getNetworkList();
							for (GateNetwork network : l) {
								network.removeGate(s.getName());
							}
							return true;
						} catch (IOException e) {
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("gateNotDeleted", ""));
							return true;
						}
					}

					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("playerHasToSelectGate", ""));
					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandDeactivate)) {
					if (!player.hasPermission("stargate.command.deactivate")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return true;
					}
					StargateFileReader sfr = new StargateFileReader(this);
					String startGate = (String) this.START_GATES.get(sender.getName());
					if (startGate != null) {
						Stargate s = sfr.getStargate(startGate);

						if ((s.getActivationStatus()) && (!s.getTarget().equals("null"))) {
							if ((s.getNetwork() != null) && (!s.getNetwork().allowsPlayer(player.getName()))) {
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
										+ language.get("networkPlayerNotAllowed", ""));
								return true;
							}

							ArrayList<String> args3 = new ArrayList();
							args3.add(s.getName());
							args3.add(s.getTarget());
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
									+ language.get("gateConnectionClosed", args3));

							s.stopConnection();
							return true;
						}

						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("gateConnectionNotClosed", ""));
						return true;
					}

					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("playerHasToSelectGate", ""));
					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandRepair)) {
					if (!player.hasPermission("stargate.command.repair")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return true;
					}
					if ((args.length > 0) && (args[1] != null)) {
						StargateFileReader sfr = new StargateFileReader(this);
						Stargate s = sfr.getStargate(args[1]);
						if (s != null) {
							if (!s.checkGateShape()) {
								s.deactivate();
								s.deactivateShield();
								s.stopConnectionFromBothSides();
								s.makeGateShape(Boolean.valueOf(true));
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
										+ language.get("gateRepaired", ""));
							}

							if (!s.checkSign()) {
								Block b = s.getLocation().getBlock();
								b.setType(Material.WALL_SIGN);
								org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();

								BlockState state = b.getState();

								if (s.getDirection() == BlockFace.NORTH) {
									((org.bukkit.material.Sign) state.getData()).setFacingDirection(BlockFace.SOUTH);
								}
								if (s.getDirection() == BlockFace.SOUTH) {
									((org.bukkit.material.Sign) state.getData()).setFacingDirection(BlockFace.EAST);
								}
								if (s.getDirection() == BlockFace.EAST) {
									((org.bukkit.material.Sign) state.getData()).setFacingDirection(BlockFace.WEST);
								}
								if (s.getDirection() == BlockFace.WEST) {
									((org.bukkit.material.Sign) state.getData()).setFacingDirection(BlockFace.EAST);
								}
								state.update();

								sign.setLine(0, ChatColor.GOLD + "[Stargate]");
								sign.setLine(1, s.getName());
								sign.update();
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
										+ language.get("signRepaired", ""));
							}
						} else {
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("gateNotRepairedNotInDatabase", ""));
							return true;
						}
					} else {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("commandMissingGatename", ""));
						return true;
					}
					return true;
				}

				Material m;
				if (args[0].equalsIgnoreCase(this.commandActivate)) {
					if (!player.hasPermission("stargate.command.activate")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return true;
					}
					if (args[1] != null) {
						StargateFileReader sfr = new StargateFileReader(this);
						String startGate = (String) this.START_GATES.get(sender.getName());
						if (startGate != null) {
							Stargate s = sfr.getStargate(startGate);
							if (s != null) {
								if (!s.getActivationStatus()) {
									s.setTarget(args[1]);
									if (sfr.getStargate(s.getTarget()) == null) {
										player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
													+ ChatColor.RED + language.get("gateNotInDatabase", ""));
										return true;
									}

									if ((s.getNetwork() != null) && (sfr.getStargate(s.getTarget()).getNetwork() != null)) {
										if (!s.getNetwork().name.equals(sfr.getStargate(s.getTarget()).getNetwork().name)) {
											player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
													+ ChatColor.RED + language.get("networkGatesNotInSameNetwork", ""));
											return true;
										}
										if (!s.getNetwork().allowsPlayer(player.getName())) {
											player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
													+ ChatColor.RED + language.get("networkPlayerNotAllowed", ""));
											return true;
										}
									}

									if (((s.getNetwork() == null) && (sfr.getStargate(s.getTarget()).getNetwork() != null))
										|| ((s.getNetwork() != null) && (sfr.getStargate(s.getTarget()).getNetwork() == null))) {
										
										player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
												+ ChatColor.RED + language.get("networkGatesNotInSameNetwork", ""));
										return true;
									}

									if (!s.getLocation().getWorld().equals(sfr.getStargate(s.getTarget()).getLocation().getWorld())) {
										int connectionPaid = 0;
										if (!configValues.getInterWorldConnectionCosts().equals("none")) {
											String[] costs = configValues.getInterWorldConnectionCosts().split(",");
											m = Material.getMaterial(Integer.parseInt(costs[0]));
											Object inv = player.getInventory();
											for (int i = 0; i < ((Inventory) inv).getSize(); i++) {
												ItemStack itm = ((Inventory) inv).getItem(i);
												if ((itm != null) && (itm.getType().equals(m))
														&& (((Inventory) inv).getItem(i).getAmount()
																- Integer.parseInt(costs[1]) >= 0)) {
													player.getInventory().removeItem(new ItemStack[] {
															new ItemStack(m, Integer.parseInt(costs[1])) });
													connectionPaid = 1;
													break;
												}

											}
										} else {
											System.out.println("no costs");
											connectionPaid = 1;
										}
										if (connectionPaid == 0) {
											String costName = Material
													.getMaterial(Integer.parseInt(
															configValues.getInterWorldConnectionCosts().split(",")[0]))
													.toString();
											player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
													+ ChatColor.RED + language.get("gatePaymentMissing", costName));
											return true;
										}
									}

									if (!s.connectToTarget()) {
										player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
												+ ChatColor.RED + language.get("gateConnectionFailed", ""));
										return true;
									}

									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("gateNewConnection", s.getTarget()));

									if (player.hasPermission("stargate.discover")) {
										PlayerDataReader pdr = new PlayerDataReader(player,this);
										if (!pdr.knowsGate(s.getTarget())) {
											pdr.saveStargate(s.getTarget());
											player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
													+ ChatColor.GREEN + language.get("gateNewAddress", s.getTarget()));
										}
									}
									return true;
								}

								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
										+ language.get("gateAlreadyActive", ""));
								return true;
							}

							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
									+ language.get("gateNotInDatabase", ""));
							return true;
						}
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("playerHasToSelectGate", ""));
						return true;
					}

					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
							+ language.get("commandWrongArguments", ""));
					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandShield)) {
					if (!player.hasPermission("stargate.command.shield")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return true;
					}
					String startGate = (String) this.START_GATES.get(sender.getName());
					if (startGate != null) {
						StargateFileReader sfr = new StargateFileReader(this);
						Stargate s = sfr.getStargate(startGate);
						if (s != null) {
							if (s.getNetwork() != null) {
								if ((!s.getNetwork().allowsPlayer(player.getName())) || (s.getNetwork().isPublic())) {
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.RED + language.get("networkPlayerNotAllowed", ""));
									return true;
								}
							}
							s.switchShield();
							player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
									+ language.get("shieldStatusSwitched", ""));
							return true;
						}

						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("gateNotInDatabase", ""));
					} else {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("playerHasToSelectGate", ""));
					}
					return true;
				}

				Stargate s;

				if (args[0].equalsIgnoreCase(this.commandGatelist)) {
					if (!player.hasPermission("stargate.command.gatelist")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return false;
					}
					player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
							+ language.get("chatGateList", ""));

					StargateFileReader sfr = new StargateFileReader(this);
					PlayerDataReader pdr = new PlayerDataReader(player,this);
					ArrayList<String> knownGates = pdr.getKnownStargates();
					ArrayList<Stargate> gateList = new ArrayList();

					for (String name : knownGates) {
						gateList.add(sfr.getStargate(name));
					}

					if (gateList.size() == 0) {
						player.sendMessage(ChatColor.RED + language.get("chatNoStargatesExist", ""));
						return true;
					}

					Iterator<Stargate> sm = gateList.iterator();
					while (sm.hasNext()) {
						s = (Stargate) sm.next();
						Object args1 = new ArrayList();
						((ArrayList) args1).add(s.getName());
						((ArrayList) args1).add(s.getLocation().getBlockX());
						((ArrayList) args1).add(s.getLocation().getBlockZ());
						((ArrayList) args1).add(s.getLocation().getBlockY());
						if (s.getActivationStatus()) {
							((ArrayList) args1).add(language.get("chatGateListActivated", ""));
						} else {
							((ArrayList) args1).add("");
						}
						player.sendMessage(ChatColor.GREEN + language.get("chatGateListEntry", (ArrayList) args1));
					}
					return true;
				}

				if (args[0].equalsIgnoreCase(this.commandNetwork)) {
					if (!player.hasPermission("network.commands")) {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("permissionNotAllowed", ""));
						return false;
					}
					String startGate = (String) this.START_GATES.get(sender.getName());

					if (startGate != null) {
						StargateFileReader sfr = new StargateFileReader(this);
						s = sfr.getStargate(startGate);
						if (s != null) {

							if ((args[1] != null) && (args[1].equalsIgnoreCase("info")) && (s.getNetwork() != null)) {
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.GREEN
										+ s.getNetworkName());
								player.sendMessage(ChatColor.GREEN + s.getNetwork().state);

								player.sendMessage(ChatColor.GREEN + "Admins: " + ChatColor.DARK_PURPLE
										+ s.getNetwork().networkadmins);

								if (s.getNetwork().isPrivate()) {
									player.sendMessage(ChatColor.GREEN + "Member: " + ChatColor.YELLOW
											+ s.getNetwork().networkmembers);
								}

								for (String gatename : s.getNetwork().networkstargates) {
									PlayerDataReader pdr = new PlayerDataReader(player,this);
									Object knownGates = pdr.getKnownStargates();
									if ((((ArrayList) knownGates).contains(gatename))
											|| (s.getNetwork().hasAdmin(player.getName()))) {
										player.sendMessage(ChatColor.GREEN + gatename);
									}
								}
								return true;
							}

							if ((s.getNetwork() != null) && (!s.getNetwork().hasAdmin(player.getName()))) {
								player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
										+ language.get("networkPlayerNotAdmin", ""));
								return true;
							}

							if (args[1] != null) {
								if ((args[1].equals("remove")) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.removeGate(s.getName());
									net.save();
									s.updateSign();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkRemovedGate", ""));
									return true;
								}

								if ((args[1].equals("add")) && (args[2] != null) && (s.getNetwork() == null)) {
									GateNetwork net = new GateNetwork(player.getName(), args[2], this);

									if (net.getNetwork(args[2]) == null) {
										net.addGate(s.getName());
										net.save();
									} else {
										s.setNetwork(args[2]);
									}

									s.updateSign();

									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkAddedGate", ""));
									return true;
								}

								if ((args[1].equals("public")) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.makePublic();
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkSetPublic", net.name));
									return true;
								}

								if ((args[1].equals("private")) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.makePrivate();
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkSetPrivate", net.name));
									return true;
								}

								if ((args[1].equals("op")) && (args[2] != null) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.addAdmin(args[2]);
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkOpedPlayer", args[2]));
									return true;
								}

								if ((args[1].equals("deop")) && (args[2] != null) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.removeAdmin(args[2]);
									net.addMember(args[2]);
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkDeopedPlayer", args[2]));
									return true;
								}

								if ((args[1].equals("allow")) && (args[2] != null) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.addMember(args[2]);
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkAllowPlayer", args[2]));
									return true;
								}

								if ((args[1].equals("unallow")) && (args[2] != null) && (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.removeMember(args[2]);
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkUnallowedPlayer", args[2]));
									return true;
								}

								if ((args[1].equalsIgnoreCase("setName")) && (args[2] != null) && (!args[2].equals(""))
										&& (s.getNetwork() != null)) {
									GateNetwork net = s.getNetwork();
									net.setName(args[2]);
									net.save();
									player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "")
											+ ChatColor.GREEN + language.get("networkSetName", args[2]));
									return true;
								}
							}

							return true;
						}

						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("gateNotInDatabase", ""));
					} else {
						player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
								+ language.get("playerHasToSelectGate", ""));
					}
					return true;
				}

			} else {

				player.sendMessage(ChatColor.GOLD + language.get("pluginNameChat", "") + ChatColor.RED
						+ language.get("commandWrongArguments", ""));
				return true;
			}
		}

		return true;
	}

	public void checkConficFiles() {
		File stargateList = new File("plugins/MC-Stargates/stargateList.txt");
		File mpgatesConfig = new File("plugins/MC-Stargates/mpgatesConfig.txt");
		File NetworkList = new File("plugins/MC-Stargates/networkList.txt");

		File parent = stargateList.getParentFile();
		if ((!parent.exists()) && (!parent.mkdirs())) {
			throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		File parent2 = mpgatesConfig.getParentFile();
		if ((!parent2.exists()) && (!parent2.mkdirs())) {
			throw new IllegalStateException("Couldn't create dir: " + parent2);
		}
		File parent3 = NetworkList.getParentFile();
		if ((!parent3.exists()) && (!parent3.mkdirs())) {
			throw new IllegalStateException("Couldn't create dir: " + parent3);
		}

		if (!stargateList.exists()) {
			try {
				PrintWriter writer = new PrintWriter("plugins/MC-Stargates/stargateList.txt", "UTF-8");
				writer.println(
						"# Saves all the Stargates: Name, worldID, shieldStatus, activationStatus, location, target, direction");
				writer.close();
			} catch (Exception e) {
				System.out.println("[WARNING] Couldn't create stargateListe.txt!");
			}
		}

		if (!NetworkList.exists()) {
			try {
				PrintWriter writer = new PrintWriter("plugins/MC-Stargates/networkList.txt", "UTF-8");
				writer.println("# Saves all the Networks of a world");
				writer.close();
			} catch (Exception e) {
				System.out.println("[WARNING] Couldn't create networkList.txt!");
			}
		}

		if (!mpgatesConfig.exists()) {
			try {
				PrintWriter writer = new PrintWriter("plugins/MC-Stargates/mpgatesConfig.txt", "UTF-8");
				writer.println("# Config File - Everything else is coming soon ;-)");
				writer.println(
						"# For materialsettings, just use solid blocks and the official names for the materials");
				writer.println(
						"# Deleting this file and restarting the plugin will give you a version with default-values.");
				writer.println(
						"# ATTENTION!! Changes could cause the plugin to have troubles finding already made stargates.");
				writer.println("activationTime: 38 # seconds gate stays open (0 = infinite)");
				writer.println("DHDMaterial: OBSIDIAN");
				writer.println("GateMaterial: OBSIDIAN");
				writer.println("ChevronMaterial: REDSTONE_BLOCK");
				writer.println("ShieldMaterial: STONE");
				writer.println(
						"DHDDistance: 9 #Distance of the !sign! (not the DHD-block) to the gate, the DHD-block is one less.");
				writer.println("KawooshSound: ENTITY_ENDERDRAGON_GROWL");
				writer.println("KawooshSoundRadius: 20");
				writer.println("KawooshSoundVolume: 10");
				writer.println("IrisDamage: 18 #in hitpoints. One heart equals 2 hitpoints");
				writer.println("IrisDestroyInventory: 1 # 0 false, 1 true, if player dies, inventory goes with him");
				writer.println("IrisNoTeleport: false #if targets shield is activated, teleport back to starting-gate");
				writer.println(
						"Language: en #country-code, e.g. klingon if your languagepack is named language_klingon.txt");
				writer.println("RingMaterial: 44,0 #MaterialID,ByteData");
				writer.println("RingGroundMaterial: 98,3 #MaterialID,ByteData");
				writer.println("RingDistance: 0 #Offset Sign to Ringtransporter");
				writer.println("InterWorldConnectionCosts: 264,1 #ItemID, amount");

				writer.println("NetworkTeleportUnallowedPlayers: 1");

				writer.close();
			} catch (Exception e) {
				System.out.println("[WARNING] Couldn't create mpgatesConfig.txt!");
			}
		}
	}

	public void checkDefaultLanguageFile() {
		File stargateLanguageFile = new File("plugins/MC-Stargates/language_en.txt");

		File parent = stargateLanguageFile.getParentFile();
		if ((!parent.exists()) && (!parent.mkdirs())) {
			throw new IllegalStateException("Couldn't create dir: " + parent);
		}

		if (!stargateLanguageFile.exists()) {
			try {
				PrintWriter writer = new PrintWriter("plugins/MC-Stargates/language_en.txt", "UTF-8");
				writer.println("# Language File English");
				writer.println("# You can change everything behind the \":\". The number of %s should not be changed!");
				writer.println(
						"# Deleting this file and restarting the plugin will give you a version with default-values.");
				writer.println(
						"# ATTENTION!! Dont change the Values in front of the \":\". This will result in Error-Messages.");
				writer.println("chatGateListActivated: \", ACTIVATED\"");
				writer.println(
						"chatGateListEntry: \"%s (%s, %s, %s) %s\"\t#Arguments: Name, positions(x,z,y), if activated: chatGateListActivated");
				writer.println("chatGateList: \"Gatelist\"");
				writer.println("chatNoStargatesExist: \"No existing Stargates!\"");
				writer.println("commandWrongArguments: \"Wrong number or type of arguments!\"");
				writer.println("commandMissingGatename: \"Last Argument <Gatename> missed!\"");
				writer.println("gateAlreadyActive: \"Stargate was already activated!\"");
				writer.println("gateBuild: \"You build a stargate-shape.\"");
				writer.println(
						"gateConnectionClosed: \"You closed the connection %s -> %s.\"\t#Arguments: Startgate, Targetgate");
				writer.println(
						"gateConnectionClosedTimeout: \"The connection %s -> %s closed automatically. Maybe it timed out.\"\t#Arguments: Startgate, Targetgate");
				writer.println("gateConnectionFailed: \"Stargate-connection failed!\"");
				writer.println("gateConnectionNotClosed: \"Didn't closed connection.\"");
				writer.println("gateCreated: \"Stargate %s was created!\"\t\t\t#Arguments: The Stargates name");
				writer.println("gateCreationFailed: \"Couldn't create Stargate!\"");
				writer.println(
						"gateDestroyed: \"%s was destoryed. You should repair it!\"\t\t#Arguments: The Stargates name");
				writer.println("gateNameExists: \"Stargate wasnt created. Name already exists!\"");
				writer.println("gateNewAddress: \"You found the unknown Stargate-address %s.\"");
				writer.println("gateNewConnection: \"Connected Stargate to %s.\"");
				writer.println("gateNotArround: \"Couldn't find a Stargate nearby!\"");
				writer.println("gateNotInDatabase: \"An Error occured. Stargate wasn't found in the database!\"");
				writer.println("gateNotDeleted: \"An Error occured. Stargate wasn't deleted!\"");
				writer.println("gateNotRepairedNotInDatabase: \"Stargate couldn't be repaired. Name not found!\"");
				writer.println(
						"gatePaymentMissing: \"To activate a connection to an other world, you have to activate an aditional Chevron. For this, you need %s.\"\t#Arguments: A Materials name");
				writer.println("gateRepaired: \"Stargate was repaired!\"");
				writer.println("gateSelected: \"%s selected.\"\t\t\t\t#Arguments: The Stargates name");
				writer.println(
						"gateSelectedHasTarget: \"%s (-> %s) selected.\"\t\t\t#Arguments: The Stargates name and the name of the targetgate");
				writer.println("networkAddedGate: \"You added this Gate to the network %s.\"");
				writer.println("networkAllowPlayer: \"You allowed %s to use this network now.\"");
				writer.println("networkDeopPlayer: \"You made %s a normal user, not an opperator.\"");
				writer.println(
						"networkNoPermissionCreate: \"You don't have permission to create new networks. Gate was created without network.\"");
				writer.println("networkOpPlayer: \"You made %s an opperator of this network.\"");
				writer.println(
						"networkPlayerNotAllowed: \"You aren't a member or admin, so you aren't allowed to use this network.\"");
				writer.println("networkPlayerNotAdmin: \"You aren't a admin of this network.\"");
				writer.println("networkRemovedGate: \"You deleted this Gate from the network %s.\"");
				writer.println("networkSetName: \"You changed this networks name to %s.\"");
				writer.println(
						"networkSetPrivate: \"You made the network %s a private one. Use the allow-command to allow players to use it.\"");
				writer.println("networkSetPublic: \"You made the network %s a public one. Everyone can use it now.\"");
				writer.println("networkUnallowPlayer: \"You took the permission from %s to use this Gates network.\"");
				writer.println(
						"networkGatesNotInSameNetwork: \"Both gates have to be inside the same network or both without a network.\"");
				writer.println("permissionNotAllowed: \"You dont have permission to do this.\"");
				writer.println("pluginNameChat: \"[Stargate] \"");
				writer.println("playerHasToSelectGate: \"You need to select a Stargate!\"");
				writer.println(
						"playerKilledByKawoosh: \"You were dematerialized by the KAWOOOOOSH! Don't stay so close next time.\"");
				writer.println(
						"playerDamagedByIris: \"You got a bitchslap from Iris straight in your face. Next time, try to walk through the gate backwards.\"");
				writer.println("ringsConstructionDamaged: \"The Rings or the target rings seem to be damaged\"");
				writer.println("ringsNoTarget: \"There seems to be no rings around, that could work as receiver.\"");
				writer.println("shieldStatusSwitched: \"Shield was activated/deactivated.\"");
				writer.println("signRepaired: \"Sign was repaired!\"");
				writer.println(
						"wrongCommand: \"Wrong command or invalid quantity of arguments. Try %s help\"\t\t\t\t#Arguments: the commands prefix");

				writer.close();
			} catch (Exception e) {
				System.out.println("[WARNING] Couldn't create mpgatesConfig.txt!");
			}
		}
	}

	public ArrayList<Double> swapper(double y1, double y2) {
		y1 += y2;
		y2 = y1 - y2;
		y1 -= y2;
		ArrayList<Double> ret = new ArrayList();
		ret.add(Double.valueOf(y1));
		ret.add(Double.valueOf(y2));
		return ret;
	}

	public Stargate getHorizonEntityIsInside(Entity entity) {
		StargateFileReader sfr = new StargateFileReader(this);
		ArrayList<Stargate> stargateList = sfr.getActiveStartGates();

		for (Stargate s : stargateList) {
			Vector direction = s.getNormalVector();
			Vector start = s.getPosition().add(direction.clone().multiply(s.DHD_DISTANCE));

			double x1 = start.getX();
			double x2 = start.getX();
			double y1 = start.getY();
			double y2 = start.getY();
			double z1 = start.getZ();
			double z2 = start.getZ() + 5.0D;

			if (direction.equals(new Vector(0, 1, 0))) {
				x1 -= 3.0D;
				x2 += 3.0D;
				y2 += 1.0D;
			}
			if (direction.equals(new Vector(0, -1, 0))) {
				x1 -= 3.0D;
				x2 += 3.0D;
				y2 += 1.0D;
			}

			if (direction.equals(new Vector(1, 0, 0))) {
				x2 += 1.0D;
				y1 -= 3.0D;
				y2 += 3.0D;
			}
			if (direction.equals(new Vector(-1, 0, 0))) {
				x1 += 1.0D;
				y1 -= 3.0D;
				y2 += 3.0D;
			}
			if (y1 > y2) {
				y1 += y2;
				y2 = y1 - y2;
				y1 -= y2;
			}

			if (x1 > x2) {
				x1 += x2;
				x2 = x1 - x2;
				x1 -= x2;
			}

			if (z1 > z2) {
				z1 += z2;
				z2 = z1 - z2;
				z1 -= z2;
			}

			if ((y1 <= entity.getLocation().getZ()) && (entity.getLocation().getZ() <= y2)
					&& (x1 <= entity.getLocation().getX()) && (entity.getLocation().getX() <= x2)
					&& (z1 <= entity.getLocation().getY()) && (entity.getLocation().getY() <= z2)) {
				return s;
			}
		}

		return null;
	}
}
