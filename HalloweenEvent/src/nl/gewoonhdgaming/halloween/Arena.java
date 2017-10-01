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
	
	
	private static List<String> players = new ArrayList<>();
	
	
	private static int minPlayers = 2;
	private static int maxPlayers = 20;
	private static Location lobby;
	private static Location spawn1;
	private static Location spawn2;
	private static int countdown = 15;
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
	
		p.setScoreboard(board);
		
		for(String s : players) {
			Player r = Bukkit.getPlayer(s);
			r.setScoreboard(board);
		}
	
	}
	
	public static void scoreboardCreation() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("HalloweenEvent", "");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HalloweenEvent");
		
		Score filler1 = obj.getScore(ChatColor.GRAY + "");
		filler1.setScore(5);
		int num = Arena.getPlayers().size() + 1;
		Score status = obj.getScore(ChatColor.RED + "Beschikbaar");
		status.setScore(5);
		Score status2 = obj.getScore(ChatColor.YELLOW + "van 31 Okt t/m 11 Nov!");
		status2.setScore(5);

		Score filler2 = obj.getScore("");
		filler2.setScore(1);

		Score serverName = obj.getScore(ChatColor.AQUA + "GewoonHDGaming");
		serverName.setScore(0);
}
	
	public static void countdown() {
		if(players.size() == 1)
			return;
		if(state != GameState.WAITING)
			return;
		Main.getInstance().broadcast(ChatColor.AQUA + "Minimum spelers is behaald! Aftellen is begonnen!");
		state = GameState.STARTING;
		new BukkitRunnable() {
			
			
			@Override
			public void run() {
				countdown = countdown -1;
				if(players.size() < 1) {
					Main.getInstance().broadcast(ChatColor.RED + "Te weinig spelers, aftellen gestopt.");
					cancel();
					state = GameState.WAITING;
					return;
				}
				if(countdown == 60 | countdown == 30 | countdown == 15 | countdown == 10 | countdown == 5 | countdown == 4 | countdown == 3 | countdown == 2 | countdown == 1) {
					Main.getInstance().broadcast(ChatColor.DARK_RED + "De game start in " + countdown + " seconde(s).");
				}
				for(String s : players) {
					Player p = Bukkit.getPlayer(s);
					p.setLevel(countdown);
				}
				if(countdown == 1) {
					cancel();
					start();
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 20);
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
		im.setLore(lore1);
		i.setItemMeta(im);
		r.getInventory().addItem(i);
		
		lore1.add(ChatColor.GREEN + "Maak iemand een demoon!");
		lore1.add(ChatColor.AQUA + "Cooldown van 10 secondes");
		
		r.getInventory().setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (byte) 1));
		r.sendMessage(ChatColor.DARK_RED + "Je bent een demoon! Je moet ervoor zorgen dat iedereen een demoon wordt door ze te slaan met je demonen staf!");
		r.sendTitle(ChatColor.DARK_RED + "Je bent een demoon", null);
		r.teleport(spawn2);
		r.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 301, 1));
		ItemStack i2 = new ItemStack(Material.COMPASS);
		ItemMeta im2 = i2.getItemMeta();
		im2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Teleporter");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN + "Teleporteer naar een random speler!");
		lore.add(ChatColor.AQUA + "Cooldown van 60 secondes");
		im2.setLore(lore);
		i2.setItemMeta(im2);
		r.getInventory().addItem(i2);
		
		//SPELERS TOEVOEGEN
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName() != n) {
				
			Team.getInstance().addMensen(p);
			p.teleport(spawn1);
			p.sendMessage(ChatColor.GREEN + "Je bent een mens! Je moet zo lang mogelijk niet geslagen worden door een demoon met zijn demonen staf!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
			}
			}
		
		Main.getInstance().broadcast(ChatColor.GREEN + "De game eindigd in 300 secondes!");
		
		if(state != GameState.STARTING)
			return;
		state = GameState.INGAME;
			
		new BukkitRunnable() {

			@Override
			public void run() {
				if(state == GameState.RESETING) {
					cancel();
					return;
				}
				
				if(duur == 0) {
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
				Main.getInstance().broadcast(ChatColor.GREEN + "De mensen hebben gewonnen! De server wordt herstart!");
				
			}
			
		}.runTaskLater(Main.getInstance(), 100);
	}
	
	public static void stop() {
		Main.getInstance().getLogger().info("STOPPING");
		state= GameState.RESETING;
		duur = 300;
		countdown = 15;
		
		if(players.size() != 0) {
		
			for(String s : players) {
				Player p = Bukkit.getPlayer(s);

				Team.removeTeam(p);
				p.performCommand("lobby");
			}
		}
		players.removeAll(players);
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
