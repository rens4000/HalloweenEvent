package nl.gewoonhdgaming.halloween;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

@SuppressWarnings("unused")
public class Arena {
	
	
	private static final List<String> players = new ArrayList<>();
	
	
	private static int minPlayers = 2;
	private static int maxPlayers = 20;
	private static Location lobby;
	private static Location spawn1;
	private static Location spawn2;
	private static int countdown = 30;
	private  static int duur = 300;
	private static boolean activated = false;
	private static GameState state = GameState.WAITING;
	
	private static Scoreboard board;
	private static Objective obj;
	
	public static void join(Player p) {
		if(!state.isJoinable()) {
			p.kickPlayer(ChatColor.RED + "De arena is al begonnen!(" + state.getText() + ")");
			return;
		}
		if(!activated) {
			p.sendMessage(ChatColor.RED + "Er is een fout opgetreden! Geef deze code door aan een developer: ARENA_NOT_ACTIVE_LN37");
		}
		int num = Arena.getPlayers().size() + 1;
		Main.getInstance().broadcast(ChatColor.RED + p.getName() + ChatColor.GREEN + " Heeft de game gejoined(" + num + "/20)");
		p.getInventory().clear();
		players.add(p.getName());
		p.teleport(lobby);
		countdown();
		p.setGameMode(GameMode.SURVIVAL);
	
		reloadScoreboard();
	
	}
	
	public static void scoreboardCreation() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("HalloweenEvent", "");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HalloweenEvent");
		
		final Score filler1 = obj.getScore(ChatColor.GRAY + "");
		filler1.setScore(5);
		final int num = Arena.getPlayers().size();
		final Score status = obj.getScore(ChatColor.RED + "Beschikbaar");
		status.setScore(5);
		final Score status2 = obj.getScore(ChatColor.YELLOW + "van 21 Okt t/m 11 Nov!");
		status2.setScore(5);
		
		final Score player = obj.getScore(ChatColor.AQUA + "Spelers: " + num + "/20");
		player.setScore(4);

		final Score filler2 = obj.getScore("");
		filler2.setScore(1);

		final Score serverName = obj.getScore(ChatColor.AQUA + "GewoonHDGaming");
		serverName.setScore(0);
}
	
	public static void countdown() {
		if(players.size() == 1)
			return;
		if(state != GameState.WAITING) {
			countdown = 30;
			for(String s : players) {
				Player p = Bukkit.getPlayer(s);
				p.setLevel(0);
			}
			return;
		}
		Main.getInstance().broadcast(ChatColor.AQUA + "Minimum spelers is behaald! Aftellen is begonnen!");
		state = GameState.STARTING;
		new BukkitRunnable() {
			
			
			@Override
			public void run() {
				countdown = countdown -1;
				if(state != GameState.STARTING) {
					Main.getInstance().broadcast(ChatColor.RED + "Te weinig spelers, het aftellen is gestopt.");
					countdown = 15;
					for(String s : players) {
						Player p = Bukkit.getPlayer(s);
						p.setLevel(0);
					}
					state = GameState.WAITING;
					cancel();
					return;
				}
				if(countdown == 60 | countdown == 30 | countdown == 15 | countdown == 10 | countdown == 5 | countdown == 4 | countdown == 3 | countdown == 2 | countdown == 1) {
					Main.getInstance().broadcast(ChatColor.DARK_RED + "De game start in " + countdown + " seconde(s).");
				}
				for(String s : players) {
					Player p = Bukkit.getPlayer(s);
					p.setLevel(countdown);
				}
				if(countdown == 0) {
					cancel();
					start();
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	public static void reloadScoreboard() {
		scoreboardCreation();
		for(int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			p.setScoreboard(board);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void start() {
		int random = new Random().nextInt(players.size());
		
		String n = players.get(random);
		Player r = Bukkit.getPlayer(n);
		Team.getInstance().addDemonen(r);
		ItemStack i = new ItemStack(Material.BLAZE_ROD);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Demoon staf");
		List<String> lore1 = new ArrayList<>();
		lore1.add(ChatColor.GREEN + "Maak iemand een demoon!");
		lore1.add(ChatColor.RED + "Cooldown van 10 secondes");
		im.setLore(lore1);
		i.setItemMeta(im);
		r.getInventory().addItem(i);
		
		
		
		r.getInventory().setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (byte) 1));
		r.sendMessage(ChatColor.DARK_RED + "Je bent een demoon! Je moet ervoor zorgen dat iedereen een demoon wordt door ze te slaan met je demonen staf!");
		r.sendTitle(ChatColor.DARK_RED + "Je bent een demoon", null);
		r.teleport(spawn2);
		r.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 1));
		ItemStack i2 = new ItemStack(Material.COMPASS);
		ItemMeta im2 = i2.getItemMeta();
		im2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Teleporter");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Teleporteer naar een random speler!");
		lore.add(ChatColor.RED + "Cooldown van 60 secondes");
		im2.setLore(lore);
		i2.setItemMeta(im2);
		r.getInventory().addItem(i2);
		
		//CLOCK
		ItemStack i3 = new ItemStack(Material.WATCH);
		ItemMeta im3 = i3.getItemMeta();
		im3.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Onzichtbaarheids spreuk");
		List<String> lore3 = new ArrayList<>();
		lore3.add(ChatColor.GREEN + "Maak je zelf onzichtbaar voor 15 secondes!");
		lore3.add(ChatColor.RED + "Cooldown van 30 secondes");
		im3.setLore(lore3);
		i3.setItemMeta(im3);
		
		//SPELERS TOEVOEGEN
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName() != n) {
				
			Team.getInstance().addMensen(p);
			p.teleport(spawn1);
			p.sendMessage(ChatColor.GREEN + "Je bent een mens! Je moet zo lang mogelijk niet geslagen worden door een demoon met zijn demonen staf!");
			p.getInventory().addItem(i3);
			
			}
		}
		
		Main.getInstance().broadcast(ChatColor.GREEN + "De game eindigd in 300 secondes!");
		
		if(state != GameState.STARTING)
			return;
		state = GameState.INGAME;
			
		new BukkitRunnable() {

			@Override
			public void run() {
				if(state != GameState.INGAME) {
					for(String s : players) {
						Player p = Bukkit.getPlayer(s);
						p.setLevel(0);
					}
					duur = 300;
					cancel();
					return;
				}
				
				if(duur == 0) {
					Main.getInstance().broadcast(ChatColor.GREEN + "De mensen hebben gewonnen! De server wordt herstart!");
					stopwait();
				}
				duur = duur - 1;
				for(String s : players) {
					Player p = Bukkit.getPlayer(s);
					p.setLevel(duur);
				}
				if(Team.getInstance().getDemonen().size() < 0) {
					for(String s : players) {
						Player p = Bukkit.getPlayer(s);
						p.setLevel(0);
					}
					cancel();
				}
			}
			
			

			
			
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
	}
	
	private static void stopwait() {
		new BukkitRunnable() {

			@Override
			public void run() {
				stop();
			}
			
		}.runTaskLater(Main.getInstance(), 10);
	}
	
	public static void stop() {
		Main.getInstance().getLogger().info("STOPPING");
		state= GameState.RESETING;
		duur = 300;
		countdown = 30;
		
		for(int i = 0; i < Team.demonen.size(); i++) {
			Player p = Bukkit.getPlayer(Team.demonen.get(i));
			Team.removeDemonen(p);
			players.remove(p.getName());
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			p.performCommand("lobby");
		}
		for(int i = 0; i < Team.mensen.size(); i++) {
			Player p = Bukkit.getPlayer(Team.mensen.get(i));
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			Team.removeMensen(p);
			players.remove(p.getName());
			p.performCommand("lobby");
		}
		for(int i = 0; i < players.size(); i++) {
			Player p = Bukkit.getPlayer(players.get(i));
			players.remove(p.getName());
			if(p.isOnline()) p.performCommand("lobby");
		}
		for(int i = 0; i < Main.getEvents().cooldown.size(); i++) {
			Main.getEvents().cooldown.remove(i);
		}
		for(int i = 0; i < Main.getEvents().cooldown2.size(); i++) {
			Main.getEvents().cooldown2.remove(i);
		}
		for(int i = 0; i < Main.getEvents().cooldown3.size(); i++) {
			Main.getEvents().cooldown3.remove(i);
		}
		state = GameState.WAITING;	
		Main.getInstance().getLogger().info("WAITING");
	}
	
	public static Location getLobby() {
		return lobby;
	}

	public static void setLobby(Location lobby) {
		Arena.lobby = lobby;
	}

	public static Location getSpawn1() {
		return spawn1;
	}

	public static void setSpawn1(Location spawn1) {
		Arena.spawn1 = spawn1;
	}

	public static Location getSpawn2() {
		return spawn2;
	}

	public static void setSpawn2(Location spawn2) {
		Arena.spawn2 = spawn2;
	}

	public static boolean isActivated() {
		return activated;
	}

	public static void setActivated(boolean activated) {
		Arena.activated = activated;
	}

	public static GameState getState() {
		return state;
	}

	public static void setState(GameState state) {
		Arena.state = state;
	}

	public static List<String> getPlayers() {
		return players;
}
	
	public GameState state() {
		return state;
	}
	
	

}
