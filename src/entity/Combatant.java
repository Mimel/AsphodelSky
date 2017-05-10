package entity;

import item.Catalog;

/**
 * A combatant is an occupant that is able to fight; this implies that they have all the attributes that a typical player has,
 * such as health, momentum, an inventory, and equipment.
 * @author Matt Imel
 */
public abstract class Combatant implements Occupant {
	
	private int currentHealth;
	
	private int maximumHealth;
	
	private Catalog inventory;
	
	Combatant(int max) {
		this.maximumHealth = max;
		this.currentHealth = max;
		
		this.inventory = new Catalog();
	}
	
	/**
	 * Retrieves the current health of the combatant.
	 * @return The current health of the combatant.
	 * @see getMaxHealth
	 */
	public int getHealth() {
		return currentHealth;
	}
	
	/**
	 * Retrieves the maximum health of the combatant.
	 * @return The maximum health of the combatant.
	 */
	public int getMaxHealth() {
		return maximumHealth;
	}
	
	/**
	 * Decreases the amount of current health by a given amount.
	 * @param healthLoss The amount of health to subtract.
	 */
	public void decreaseHealthBy(int healthLoss) {
		if(healthLoss > currentHealth) {
			currentHealth = 0;
		} else {
			currentHealth -= healthLoss;
		}
	}
	
	/**
	 * Increases the amount of current health by a given amount.
	 * @param healthGain The amount of health to add.
	 */
	public void increaseHealthBy(int healthGain) {
		if(currentHealth + healthGain > maximumHealth) {
			currentHealth = maximumHealth;
		} else {
			currentHealth += healthGain;
		}
	}
	
	/**
	 * Retrieves the inventory of the combatant.
	 * @return The inventory of the combatant.
	 */
	public Catalog getInventory() {
		return inventory;
	}
}
