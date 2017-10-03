package nl.gewoonhdgaming.halloween;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

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
	public static void removeMensen(Player p) {
		mensen.remove(p.getName());
	}
	public static void removeDemonen(Player p) {
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
