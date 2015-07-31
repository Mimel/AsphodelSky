package game;

public class Tile {
	public static int tileSize = 36;
	
	private char tileRep;
	private boolean impassable;
	private int revealStatus;
	
	Tile(char tileRep) {
		this.tileRep = tileRep;
		this.revealStatus = 0;
		
		switch(this.tileRep) {
		case 'X':
			impassable = true;
			break;
		case '0':
			impassable = false;
			break;
		default:
			impassable = true;
			break;
		}
	}
	
	public char getRep() { return tileRep; }
	public void setRep(char newRep) { tileRep = newRep; }
	
	public int getRevealed() { return revealStatus; }
	public void setRevealed(int newReveal) { revealStatus = newReveal; }
	
	public boolean isImpassable() { return impassable; }
}
