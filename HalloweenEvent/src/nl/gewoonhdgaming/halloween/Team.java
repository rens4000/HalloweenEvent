package nl.gewoonhdgaming.halloween;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Team {
	
	private static Team instance = new Team();
	
	public static List<String> mensen = new ArrayList<>();
	
	public static List<String> demonen = new ArrayList<>();
	
	public void addMensen(Player p) {
		mensen.add(p.getName());
	}
	public void addDemonen(Player p) {
		demonen.add(p.getName());
	}
	public void removeMensen(Player p) {
		mensen.remove(p.getName());
	}
	public void removeDemonen(Player p) {
		demonen.remove(p.getName());
	}
	
	public static Team getInstance() {
		return instance;
	}
	public List<String> getMensen() {
		return mensen;
	}
	public List<String> getDemonen() {
		return demonen;
	}
	
	public static void removeTeam(Player p) {
		if(getTeam(p) == mensen) {
			mensen.remove(p.getName());
			p.removePotionEffect(PotionEffectType.SPEED);
			return;
		}
		if(getTeam(p) == demonen) {
			demonen.remove(p.getName());
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			return;
		}
	}
	
	public static List<String> getTeam(Player p) {
		if(mensen.contains(p.getName())) {
			return mensen;
		}
		if(demonen.contains(p.getName())) {
			return demonen;
		}
		return null;
	}
	

}
