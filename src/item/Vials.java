package item;

import entity.Player;

/**
 * Vials are randomized consumable items that affect the player, rather than the environment.
 * All vials are stackable and can only be consumed once.
 * 
 * @author Matti
 */
public abstract class Vials extends Item {
	
	public Vials(String t, String d, int x, int y) {
		super(t, d, x, y);
	}
}