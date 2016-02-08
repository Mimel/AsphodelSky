package entity;

import item.*;

/**
 * This class represents the player itself. This is a special derivation of the Entity which the user controls. 
 * @author Mimel
 */
public class Player extends Entity {
	/** An adjective representing the player, based on the skills possessed by him/her/it. */
	private String title;

	/** Meant for color currently, but will go back on TODO */
	private String species;
	
	/** The radius of vision the player has. */
	private double sightRadius;
	
	/** The inventory the player has. */
	private Item[] inventory;
	
	/** The size of the inventory array. Usually conforming to the PlayerInfo part of the GUI. */
	private final int inventorySize = 36;
	
	public Player(String name, int xCoord, int yCoord, double sightRadius) {
		this.name = name;
		this.title = "Testy";
		this.species = "Human";
		
		
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		
		//TEMP
		this.maxHP = 20;
		this.currHP = this.maxHP;
		
		this.maxEP = 5;
		this.currEP = this.maxEP;
		
		this.str = 3;
		this.fin = 3;
		this.mnd = 3;
		
		this.sightRadius = sightRadius;
		this.immobile = false;
		//END TEMP
		
		this.movementSpeed = 10;
		this.inventory = new Item[inventorySize];
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	
	public double getSightRadius() {
		return sightRadius;
	}

	public void setSightRadius(double sightRadius) {
		this.sightRadius = sightRadius;
	}
	
	public Item[] getInventory() {
		return inventory;
	}
	
	/**
	 * Pushes an Item onto the inventory, in the way that a stack architecture does.
	 * @param i The Item being pushed onto the inventory array.
	 */
	public void pushToInventory(Item i) {
		for(int x = 0; x < inventory.length; x++) {
			if(inventory[x] == null) {
				inventory[x] = i;
				return;
			}
		}
	}

	/**
	 * This method manages the player's "move" command, which is triggered using the QWE/ASD/ZCX keys or the 1-9 numpad
	 * keys. This method has two distinct outcomes; the player is translated to an adjacent tile, or the player interacts
	 * with another entity. In the case below
	 */
	@Override
	public void move(int xShift, int yShift) {
		this.xCoord += xShift;
		this.yCoord += yShift;
	}
}
