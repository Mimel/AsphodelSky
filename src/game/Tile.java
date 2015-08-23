package game;

import item.*;

public class Tile {
	public static final int tileSize = 36;
	public static final int maxInventorySize = 10;
	
	private char tileRep;
	private boolean impassable;
	private int revealStatus;
	
	/**
	 * The current items on the ground on this tile.
	 * TODO: Accompany for more than one item on a tile.
	 */
	private StackableItem[] itemsOnGround;
	
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
		
		this.itemsOnGround = new StackableItem[Tile.maxInventorySize];
	}
	
	public char getRep() { return tileRep; }
	public void setRep(char newRep) { tileRep = newRep; }
	
	public int getRevealed() { return revealStatus; }
	public void setRevealed(int newReveal) { revealStatus = newReveal; }
	
	public boolean isImpassable() { return impassable; }
	
	public boolean hasItems() {
		return itemsOnGround[0] != null;
	}
	
	/**
	 * Returns the top item on the Tile's inventory stack. Unlike the popItem method, this method does not delete
	 * the item in question.
	 * @return The Item furthest from the origin of the itemsOnGround array. 
	 */
	public StackableItem peekItem() {
		for(int x = 0; x < itemsOnGround.length; x++) {
			if(itemsOnGround[x] == null) {
				if(x == 0) {
					System.out.println("There are no items on this tile.");
					return null;
				}
				return itemsOnGround[x - 1];
			}
		}
		return itemsOnGround[itemsOnGround.length - 1];
	}
	
	/**
	 * Pops an item from the array, in the manner that the archetypical stack model does. Stores the top item
	 * in a temp variable, removes the top item, and removes the Item assigned to the temp variable.
	 * @return The popped item.
	 */
	public StackableItem popItem() {
		for(int x = 0; x < itemsOnGround.length; x++) {
			if(itemsOnGround[x] == null) {
				if(x == 0) {
					System.out.println("There are no items on this tile.");
					return null;
				}
				StackableItem temp = itemsOnGround[x - 1];
				itemsOnGround[x - 1] = null;
				return temp;
			}
		}
		StackableItem temp = itemsOnGround[itemsOnGround.length - 1];
		itemsOnGround[itemsOnGround.length - 1] = null;
		return temp;
	}
	
	/**
	 * Pushes an item onto the Tile's inventory stack.
	 * @param i The Item being pushed on the stack.
	 */
	public void pushOntoInv(StackableItem i) {
		for(int x = 0; x < itemsOnGround.length; x++) {
			if(itemsOnGround[x] == null) {
				itemsOnGround[x] = i;
				return;
			} else if(itemsOnGround[x].getItem().equals(i.getItem())) {
				itemsOnGround[x].adjustItemAmount(i.getAmount());
			}
		}
		System.out.println("Maximum amount of items on tiles.");
	}
}
