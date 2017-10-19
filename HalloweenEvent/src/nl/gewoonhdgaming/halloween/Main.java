package nl.gewoonhdgaming.halloween;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private static Events events;
	
	@Override
	public void onEnable() {
		events = new Events();
		Arena.scoreboardCreation();
		PluginManager pm = getServer().getPluginManager();
	instance = this;
	loadArena();
	pm.registerEvents(new Events(), this);
	
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static Events getEvents() {
		return events;
	}
	
	public void broadcast(String msg) {
		for(Player p : getServer().getOnlinePlayers()) {
			p.sendMessage(msg);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void broadcastTitle(String msg, String msg2) {
		for(Player p : getServer().getOnlinePlayers()) {
			p.sendTitle(msg, msg2);
		}
	}
	
	public void loadArena() {
		if(getConfig().contains("Arena.active")) {
			Arena.setActivated(getConfig().getBoolean("Arena.active"));
			getLogger().info(ChatColor.GREEN + "CONFIG: Arena.active exists!");
		}
		if(getConfig().contains("Arena.spawn1")) {
			Location l = new Location(Bukkit.getWorld(getConfig().getString("Arena.spawn1.world")), getConfig().getInt("Arena.spawn1.x"), getConfig().getInt("Arena.spawn1.y"), getConfig().getInt("Arena.spawn1.z"));
			getLogger().info(ChatColor.GREEN + "CONFIG: Arena.spawn1 exists!");
			Arena.setSpawn1(l);
		}
		if(getConfig().contains("Arena.spawn2")) {
			Location l = new Location(Bukkit.getWorld(getConfig().getString("Arena.spawn2.world")), getConfig().getInt("Arena.spawn2.x"), getConfig().getInt("Arena.spawn2.y"), getConfig().getInt("Arena.spawn2.z"));
			getLogger().info(ChatColor.GREEN + "CONFIG: Arena.spawn2 exists!");
			Arena.setSpawn2(l);
		}
		if(getConfig().contains("Arena.lobby")) {
			Location l = new Location(Bukkit.getWorld(getConfig().getString("Arena.lobby.world")), getConfig().getInt("Arena.lobby.x"), getConfig().getInt("Arena.lobby.y"), getConfig().getInt("Arena.lobby.z"));
			getLogger().info(ChatColor.GREEN + "CONFIG: Arena.lobby exists!");
			Arena.setLobby(l);
		}
		Arena.setState(GameState.WAITING);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("halloween")) {
			Player p = (Player) sender;
			if(!p.hasPermission("GHG.Admin")) {
				return false;
			}
			
			if(args.length == 0) {
				p.sendMessage(ChatColor.DARK_RED + "HalloweenEvent");
				p.sendMessage(ChatColor.WHITE + "/halloween setspawn");
				p.sendMessage(ChatColor.WHITE + "/halloweensetspawn2");
				p.sendMessage(ChatColor.WHITE + "/halloween setlobby");
				p.sendMessage(ChatColor.WHITE + "/halloween toggle");

			}
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("setspawn")) {
					Location l = p.getLocation();
					getConfig().set("Arena.spawn1.world", p.getLocation().getWorld().getName());
					getConfig().set("Arena.spawn1.x", p.getLocation().getX());
					getConfig().set("Arena.spawn1.y", p.getLocation().getY());
					getConfig().set("Arena.spawn1.z", p.getLocation().getZ());
					p.sendMessage("Spawn aangepast");
					Arena.setSpawn1(l);
					saveConfig();
				}
				if(args[0].equalsIgnoreCase("setspawn2")) {
					Location l = p.getLocation();
					getConfig().set("Arena.spawn2.world", p.getLocation().getWorld().getName());
					getConfig().set("Arena.spawn2.x", p.getLocation().getX());
					getConfig().set("Arena.spawn2.y", p.getLocation().getY());
					getConfig().set("Arena.spawn2.z", p.getLocation().getZ());
					p.sendMessage("Spawn aangepas2t");

					Arena.setSpawn2(l);
					saveConfig();
				}
				
				if(args[0].equalsIgnoreCase("setlobby")) {
					Location l = p.getLocation();
					getConfig().set("Arena.lobby.world", p.getLocation().getWorld().getName());
					getConfig().set("Arena.lobby.x", p.getLocation().getX());
					getConfig().set("Arena.lobby.y", p.getLocation().getY());
					getConfig().set("Arena.lobby.z", p.getLocation().getZ());
					p.sendMessage("lobby aangepast");

					Arena.setLobby(l);
					saveConfig();
				}
				if(args[0].equalsIgnoreCase("toggle")) {
					
					getConfig().set("Arena.active", !Arena.isActivated());
					Arena.setActivated(!Arena.isActivated());
					saveConfig();
					p.sendMessage("De arena is gezet naar " + Arena.isActivated());
				}
				if(args[0].equalsIgnoreCase("he")) {
					String target = Bukkit.getServer().getPlayer(args[1]).getName();
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "AudioSystem" + ChatColor.WHITE + "] De halloween audio is herstart voor de speler");
					String audiocommand = "ac " + "send" + " " + target + " " + "halloween";
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), audiocommand);
				}
				if(args[0].equalsIgnoreCase("play")) {
					String target = Bukkit.getServer().getPlayer(args[1]).getName();
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "AudioSystem" + ChatColor.WHITE + "] De audio is herstart voor de speler");
					String audiocommand = "ac " + "send" + " " + target + " " + args[2];
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), audiocommand);
				}
				if(args[0].equalsIgnoreCase("audiolist")) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "AudioSystem" + ChatColor.WHITE + "] We halen de playerlist op....");
					String audiocommand = "ac " + "list" + " " + "players";
					Bukkit.dispatchCommand(p, audiocommand);
				}
				if(args[0].equalsIgnoreCase("audioadmin")) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "AudioSystem" + ChatColor.WHITE + "] AudioClient V3 Intergrated by Boykev");
				}
			}
		}
		
		
		return false;
	}

}
