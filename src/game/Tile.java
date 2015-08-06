package game;

import item.Item;

public class Tile {
	public static int tileSize = 36;
	
	private char tileRep;
	private boolean impassable;
	private int revealStatus;
	
	/**
	 * The current items on the ground on this tile.
	 * TODO: Accompany for more than one item on a tile.
	 */
	private Item itemsOnGround;
	
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
		
		this.itemsOnGround = null;
	}
	
	public char getRep() { return tileRep; }
	public void setRep(char newRep) { tileRep = newRep; }
	
	public int getRevealed() { return revealStatus; }
	public void setRevealed(int newReveal) { revealStatus = newReveal; }
	
	public boolean isImpassable() { return impassable; }
	
	public boolean hasItems() {
		return itemsOnGround != null;
	}
	
	public Item getItems() {
		return itemsOnGround;
	}
	
	/**
	 * Pushes an item onto the Tile's inventory stack.
	 * @param i
	 */
	public void pushOntoInv(Item i) {
		itemsOnGround = i;
	}
}
