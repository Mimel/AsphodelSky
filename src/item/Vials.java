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
	
	/**
	 * Effects after use. Vials only affect the player, so there is only
	 * the one parameter.
	 */
	public abstract void use(Player p1);
}

/** 
 * Description of the Vial.
 */
class HealthVial extends Vials {
	public HealthVial() {
		super("Health Vial", "Restores a small portion of your total health.", 0, 0);
	}
	
	@Override
	public void use(Player p1) {
		
	}
}