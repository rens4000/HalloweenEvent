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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Events implements Listener {
	
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
	
		@SuppressWarnings("static-access")
		@EventHandler
		public void PlayerJoinQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if(!Arena.isActivated()) return;
			if(Team.getInstance().getMensen().size() < 1) {
				Arena.stop();
				return;
			}
			if(Team.getInstance().getDemonen().size() < 1) {
				Arena.stop();
				return;
			}
			if(Arena.getPlayers().size() < 1) {
				Arena.stop();
				return;				
			}
			Team.getInstance().getTeam(p).remove(p.getName());
			Arena.getPlayers().remove(p.getName());
			int n = Arena.getPlayers().size() - 1;
			Main.getInstance().broadcast(ChatColor.RED + p.getName() + ChatColor.GREEN + " Heeft de game verlaten(" + n + "/20)");
			
		
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
				i.setItemMeta(im);
		        
		        if(!damagerPlayer.getItemInHand().equals(i)) {
		        	e.setCancelled(true);
		        } else {
		        	
		        	if(cooldown2.contains(damagerPlayer.getName())) {
		        		damager.sendMessage(ChatColor.RED + "Je hebt nog een (compass)cooldown!");
		        		e.setCancelled(true);
		        		return;
		        	}
		        	
		    		taker.getInventory().addItem(i);
		    		taker.getInventory().setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (byte) 1));
		    		Team.getInstance().getTeam(taker).remove(taker.getName());
		    		Team.getInstance().addDemonen(taker);
		    		taker.sendMessage(ChatColor.DARK_RED + "Je bent in een demoon veranderd! Zorg ervoor dat iedereen een demoon wordt door ze te slaan met je staf!");
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
				if(p.isOnGround()) {
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
				if(e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
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
					new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown.remove(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.AQUA + "Cooldown afgelopen!");
							
						}
					}.runTaskLater(Main.getInstance(), 1200);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							cooldown2.remove(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.AQUA + "Je kan nu iemand slaan met je wand!!");
							
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
