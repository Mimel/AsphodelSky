package game;

import java.util.ArrayList;
import java.util.List;

import item.*;

/**
 * Represents a unit of ground/wall on the map. Tiles can have varying properties based on their type, but all tiles
 * have some interaction with the entities and items.
 * @author Mimel
 */
public class Tile {
	/** The dimension in pixels the tile must be drawn. Used for both the standard for the map and inventory. */
	public static final int tileSize = 36;
	
	/** The size of the tile's inventory, in unique items. */
	public static final int maxInventorySize = 10;
	
	/** A character that determines the type of the tile. */
	private char tileRep;
	
	/** Whether or not a tile can be walked through. */
	private boolean impassable;
	
	/** 
	 * Whether or not a tile is revealed. Must be a number ranging from 0-2;
	 * 0 represents an unseen tile.
	 * 1 represents an unseen tile that was once seen.
	 * 2 represents a currently seen tile. 
	 */
	private int revealStatus;
	
	/**
	 * The current items on the ground on this tile.
	 * TODO: Accompany for more than one item on a tile.
	 */
	private List<Item> itemsOnGround;
	
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
		
		this.itemsOnGround = new ArrayList<Item>();
	}
	
	public char getRep() { return tileRep; }
	public void setRep(char newRep) { tileRep = newRep; }
	
	public int getRevealed() { return revealStatus; }
	public void setRevealed(int newReveal) { revealStatus = newReveal; }
	
	public boolean isImpassable() { return impassable; }
	
	/**
	 * Returns whether or not a Tile has items on it.
	 * @return The absence of items returns false, anything else returns true.
	 */
	public boolean hasItems() {
		return !itemsOnGround.isEmpty();
	}
	
	/**
	 * Returns the top item on the Tile's inventory list. Unlike the popItem method, this method does not delete
	 * the item in question.
	 * @return The Item furthest from the origin of the itemsOnGround array. 
	 */
	public Item peekItem() {
		for(int x = 0; x < itemsOnGround.size(); x++) {
			if(itemsOnGround.get(x) == null) {
				if(x == 0) {
					System.out.println("There are no items on this tile.");
					return null;
				}
				return itemsOnGround.get(x - 1);
			}
		}
		return itemsOnGround.get(itemsOnGround.size() - 1);
	}
	
	/**
	 * Pops an item from the array, in the manner that the archetypical stack model does. Stores the top item
	 * in a temp variable, removes the top item, and removes the Item assigned to the temp variable.
	 * @return The popped item.
	 */
	public Item popItem() {
		for(int x = 0; x < itemsOnGround.size(); x++) {
			if(itemsOnGround.get(x) == null) {
				if(x == 0) {
					System.out.println("There are no items on this tile.");
					return null;
				}
				Item temp = itemsOnGround.get(x - 1);
				itemsOnGround.remove(x - 1);
				return temp;
			}
		}
		Item temp = itemsOnGround.get(itemsOnGround.size() - 1);
		itemsOnGround.remove(itemsOnGround.size() - 1);
		return temp;
	}
	
	/**
	 * Pushes an item onto the Tile's inventory stack.
	 * @param i The Item being pushed on the stack.
	 */
	public void pushOntoInv(Item i) {
		for(int x = 0; x < itemsOnGround.size(); x++) {
			if(itemsOnGround.get(x) == null) {
				itemsOnGround.set(x, i);
				return;
			}
		}
		itemsOnGround.add(i);
	}
}
