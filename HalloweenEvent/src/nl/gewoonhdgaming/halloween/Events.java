package nl.gewoonhdgaming.halloween;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Events implements Listener {
	
	public List<String> cooldown3 = new ArrayList<>();
	public List<String> cooldown = new ArrayList<>();
	public List<String> cooldown2 = new ArrayList<>();
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Arena.getPlayers().contains(p.getName())) {
			e.setCancelled(true);
		}
			
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(Arena.getPlayers().contains(p.getName())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void godMode(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(Arena.getPlayers().contains(p.getName())) {
			p.setHealth(20);
		}
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(Arena.getPlayers().contains(p.getName())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(Arena.getPlayers().contains(p.getName())) {
			e.setCancelled(true);
		}
		}

	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(Arena.getPlayers().contains(p.getName())) {
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Arena.join(p);
		if(Arena.isActivated())
		p.teleport(Arena.getLobby());
	}
	
		@EventHandler
		public void PlayerJoinQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if(!Arena.isActivated()) return;
			if(Arena.getState() == GameState.STARTING) {
				int count = Arena.getPlayers().size() - 1;
				Main.getInstance().broadcast(ChatColor.RED + p.getName() + ChatColor.GREEN + " Heeft de game verlaten(" + count + "/20)");
				Arena.getPlayers().remove(p.getName());
				Arena.reloadScoreboard();
				if(count < 2) 
					Arena.setState(GameState.WAITING);
				
				return;
			}
			if(Team.demonen.contains(p.getName())) {
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
				Team.removeDemonen(p);
			}
			
			if(Team.mensen.contains(p.getName())) {
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
				Team.removeMensen(p);
			}
			
			Arena.getPlayers().remove(p.getName());
			Main.getInstance().broadcast(ChatColor.RED + p.getName() + ChatColor.GREEN + " Heeft de game verlaten(" + Arena.getPlayers().size() + "/20)");
			Arena.reloadScoreboard();
			if(Team.getInstance().getMensen().size() == 0) {
				Arena.stop();
				return;
			}
			if(Team.getInstance().getDemonen().size() == 0) {
				Arena.stop();
				return;
			}
			if(Arena.getPlayers().size() == 0) {
				Arena.stop();
				return;				
			}
		
	}
		@EventHandler
		public void PlayerFood(FoodLevelChangeEvent e) {
			Player p = (Player) e.getEntity();
			p.setFoodLevel(20);
		}
		
		@SuppressWarnings({ "static-access", "deprecation" })
		@EventHandler
		public void onPlayerDamage(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damageTaker = e.getEntity();

		if (damageTaker instanceof Player) {
		    //DamageTaker is a Player
		    Player taker = (Player) damageTaker;
		    if (damager instanceof Player) {
		        //Damage Causer is also a player
		        Player damagerPlayer = (Player) damager;
		        
		        if(Team.getInstance().getTeam(taker) == Team.getInstance().getTeam(damagerPlayer)) {
		        	e.setCancelled(true);
		        	return;
		        }
		        taker.setHealth(20);
		        
		        ItemStack i = new ItemStack(Material.BLAZE_ROD);
				ItemMeta im = i.getItemMeta();
				im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Demoon staf");
				List<String> lore1 = new ArrayList<>();
				lore1.add(ChatColor.GREEN + "Maak iemand een demoon!");
				lore1.add(ChatColor.RED + "Cooldown van 10 secondes");
				im.setLore(lore1);
				i.setItemMeta(im);
		        
		        if(!damagerPlayer.getItemInHand().equals(i)) {
		        	e.setCancelled(true);
		        } else {
		        	
		        	if(cooldown2.contains(damagerPlayer.getName())) {
		        		damagerPlayer.sendMessage(ChatColor.RED + "Je kan nu niet slaan met je staf want je hebt een cooldown");
		        		damagerPlayer.sendTitle(ChatColor.RED + "Je kan nu niet slaan", null);
						return;
					}
		        	
		        	cooldown2.add(damagerPlayer.getName());
		        	
		        	new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown2.remove(damagerPlayer.getName());
							damagerPlayer.sendMessage(ChatColor.AQUA + "Je kan nu iemand slaan met je wand!!");
							
						}
					}.runTaskLater(Main.getInstance(), 200);
		        	
					taker.getInventory().clear();
					taker.removePotionEffect(PotionEffectType.INVISIBILITY);
		    		taker.getInventory().addItem(i);
		    		taker.getInventory().setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (byte) 1));
		    		
		    		Team.getInstance().getTeam(taker).remove(taker.getName());
		    		Team.getInstance().addDemonen(taker);
		    		taker.sendTitle(ChatColor.DARK_RED + "Je bent een demoon", null);
		    		taker.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 1));;
		    		ItemStack i2 = new ItemStack(Material.COMPASS);
		    		ItemMeta im2 = i2.getItemMeta();
		    		im2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Teleporter");
		    		List<String> lore = new ArrayList<>();
		    		lore.add(ChatColor.GREEN + "Teleporteer naar een random speler!");
		    		lore.add(ChatColor.RED + "Cooldown van 60 secondes");
		    		im2.setLore(lore);
		    		i2.setItemMeta(im2);
		    		taker.getInventory().addItem(i2);
		    		taker.sendMessage(ChatColor.DARK_RED + "Je bent in een demoon veranderd! Zorg ervoor dat iedereen een demoon wordt door ze te slaan met je staf!");
		    		if(!(Team.getInstance().mensen.size() < 1))
		    		Main.getInstance().broadcast(ChatColor.AQUA + "Er zijn nog " + Team.mensen.size() + " mensen over");
		    		if(Team.getInstance().mensen.size() < 1) {
				    	Main.getInstance().broadcast(ChatColor.DARK_RED + "De Demonen hebben gewonnen!");
				    	Main.getInstance().broadcastTitle(ChatColor.DARK_RED + "De Demonen hebben gewonnen!", ChatColor.BLACK + "De server wordt herstart");
				    	Arena.setState(GameState.RESETING);
				    	
				    	
				    	new BukkitRunnable() {

							@Override
							public void run() {
								Arena.setState(GameState.RESETING);
								Arena.stop();
							}
				    		
				    	}.runTaskLater(Main.getInstance(), 160);
				    }
		        }
		    }
		    
		}

		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = e.getPlayer();
			if(Team.getInstance().getDemonen().contains(p.getName())) {
				if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
					return;

				LivingEntity le = (LivingEntity) p;
				if(le.isOnGround()) {
					if(!p.getAllowFlight()) {
							
						p.setAllowFlight(true);
						
					}
				}
			}
		}
		
		@SuppressWarnings("deprecation")
		@EventHandler
		public void onAction(PlayerInteractEvent e) {
			if(cooldown.contains(e.getPlayer().getName()))
				return;
			
			ItemStack i2 = new ItemStack(Material.COMPASS);
			ItemMeta im2 = i2.getItemMeta();
			im2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Teleporter");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN + "Teleporteer naar een random speler!");
			lore.add(ChatColor.AQUA + "Cooldown van 60 secondes");
			im2.setLore(lore);
			i2.setItemMeta(im2);
			
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				//INVIS
				if(e.getPlayer().getItemInHand().getType() == Material.WATCH) {
					if(cooldown3.contains(e.getPlayer().getName())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Je kan nu niet onzichtbaar worden want je hebt een cooldown");
						e.getPlayer().sendTitle(ChatColor.RED + "Je kan nu niet onzichtbaar worden", null);
						return;
					}
					cooldown3.add(e.getPlayer().getName());
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
					new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown3.remove(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.AQUA + "Je kan nu weer onzichtbaar worden");
							
						}
					}.runTaskLater(Main.getInstance(), 600);
				}
				//TELEPORTER
				if(e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
					if(cooldown.contains(e.getPlayer().getName())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Je kan nu niet teleporteren want je hebt een cooldown");
						e.getPlayer().sendTitle(ChatColor.RED + "Je kan nu niet teleporteren", null);
						return;
					}
					cooldown.add(e.getPlayer().getName());
					cooldown2.add(e.getPlayer().getName());
					List<String> players1 = new ArrayList<String>();
					for(String s : Arena.getPlayers()) {
						players1.add(s);
					}
					players1.remove(e.getPlayer().getName());
					int random = new Random().nextInt(players1.size());
					Player p = Bukkit.getPlayer(players1.get(random));
					e.getPlayer().teleport(p);
					p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 0.2f);
					p.sendTitle(ChatColor.DARK_RED + "EEN DEMOON", ChatColor.BLACK + "is naar je toe geteleporteerd");
					new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown.remove(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.AQUA + "Je kan nu weer teleporteren!");
							
						}
					}.runTaskLater(Main.getInstance(), 1200);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown2.remove(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.AQUA + "Je kan nu iemand slaan met je wand!");
							
						}
					}.runTaskLater(Main.getInstance(), 200);
				}
			}
		}
		
		@SuppressWarnings("deprecation")
		@EventHandler
		public void onFlight(PlayerToggleFlightEvent e) {
			Player p = e.getPlayer();
			if(!Team.getInstance().getDemonen().contains(p.getName()))
				return;
			e.setCancelled(true);
			p.setFlying(false);
			Location loc = p.getLocation();
			Vector v = loc.getDirection().multiply(1.2f).setY(1.2);
			p.setVelocity(v);
			loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_LAUNCH, 1, 0.2f);
			p.playEffect(p.getLocation(), Effect.PARTICLE_SMOKE, 0);
		}
		
}
