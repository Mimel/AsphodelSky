package entity;

import item.*;

public class Player extends Entity {
	/* Decoration */
	private String title;

	/* Stats */
	private String species;
	private double sightRadius;
	
	/* Inventory */
	private Item[] inventory;
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
		
		this.sightRadius = sightRadius;
		
		this.immobile = false;
		//TEMP
		this.movementSpeed = 1;
		
		//Std. Inventory Size
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
	 * Determines if a used item is consumed; if the item at index "index" is consumable,
	 * decrement the amount of items in that stack. If the amount is 1, remove the item entirely.
	 * @param index The item location in the inventory.
	 */
	public void runConsumptionCheck(int index) {
		if(this.inventory[index].isConsumable()) {
			this.inventory[index] = null;	
		}
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
