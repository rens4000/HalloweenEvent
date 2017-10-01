package nl.gewoonhdgaming.halloween;

public enum GameState {
	
	WAITING(true, "Waiting"), STARTING(true, "Starting"), INGAME(false, "Ingame"), RESETING(false, "Reseting");
	
	private boolean joinable;
	private String text;
	
	GameState(boolean joinable, String text) {
		this.joinable = joinable;
		this.text = text;
	}
	
	public boolean isJoinable() {
		return joinable;
	}
	
	public String getText() {
		return text;
	}

}
