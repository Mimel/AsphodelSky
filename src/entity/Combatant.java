package entity;

import item.Catalog;

/**
 * A combatant is an occupant that is able to fight; this implies that they have all the attributes that a typical player has,
 * such as health, momentum, an inventory, and equipment.
 * @author Matt Imel
 */
public abstract class Combatant implements Occupant {
	
	/**
	 * The id of the combatant. Each instance of any enemy has a unique integer id,
	 * with the player having an id of 0.
	 */
	private int id;
	
	/**
	 * The name of the combatant. Can be a uniquish name (e.g. Matt, Joselyn) or
	 * a description (Fighter, Kivian Stag Beetle). 
	 */
	private String name;
	
	/**
	 * An adjective(s) describing the combatant. If the combatant does not
	 * have a title, this field is an empty string.
	 */
	private String title;
	
	/**
	 * The location of the combatant, with regards to the x-position.
	 */
	private int xPos;
	
	/**
	 * The location of the combatant, with regards to the y-position.
	 */
	private int yPos;
	
	/**
	 * The current health of the combatant, as a positive integer. Must
	 * be smaller or equal to the maximum health.
	 * @see maximumHealth
	 */
	private int currentHealth;
	
	/**
	 * The maximum health of the combatant, as a non-zero positive integer.
	 */
	private int maximumHealth;
	
	/**
	 * The inventory of the combatant.
	 */
	private Catalog inventory;
	
	Combatant(int max) {
		this.maximumHealth = max;
		this.currentHealth = max;
		
		this.inventory = new Catalog();
	}
	
	//HUGE TODO, TEMP; id should be unique
	Combatant(int id, String name, String title, int max) {
		this.id = id;
		this.name = name;
		this.title = title;
		
		this.maximumHealth = max;
		this.currentHealth = max;
		
		this.inventory = new Catalog();
	}
	
	/**
	 * Gets the id of the combatant.
	 * @return The id of the combatant.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name of the combatant.
	 * @return The name of the combatant.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the title of the combatant.
	 * @return The title of the combatant.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of the combatant to the specified title.
	 * @param newTitle The new title for the combatant.
	 */
	public void setTitle(String newTitle) {
		title = newTitle;
	}
	
	/**
	 * Gets the x-location of this combatant.
	 * @return The x-location of the combatant.
	 */
	public int getX() {
		return xPos;
	}
	
	/**
	 * Gets the y-location of this combatant.
	 * @return The y-location of the combatant.
	 */
	public int getY() {
		return yPos;
	}
	
	/**
	 * Sets the x-location of the combatant by adding the specified
	 * offset to the current y-location.
	 * @param offset The value to add to the current x-location to make
	 * the new x-location.
	 */
	public void setX(int offset) {
		xPos += offset;
	}
	
	/**
	 * Sets the y-location of the combatant by adding the specified
	 * offset to the current y-location.
	 * @param offset The value to add to the current y-location to make
	 * the new y-location.
	 */
	public void setY(int offset) {
		yPos += offset;
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
