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
		this.inventory = new Item[36];
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
	
	public Item[][] convertInvTo2D(int height, int width) {
		if(height * width != inventory.length) {
			System.out.println("Dimensions given are not valid factors of inventory size.");
			return null;
		} else {
			Item[][] gridInventory = new Item[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					gridInventory[y][x] = inventory[y * width + x];
				}
			}
			return gridInventory;
		}
	}
	
	public void convertInvTo1D(Item[][] itemGrid) {
		if(itemGrid[0].length * itemGrid.length != inventory.length) {
			System.out.println("Given item grid cannot be translated to player inventory.");
			return;
		} else {
			for(int y = 0; y < itemGrid.length; y++) {
				for(int x = 0; x < itemGrid[y].length; x++) {
					inventory[y * itemGrid[y].length + x] = itemGrid[y][x];
				}
			}
		}
	}
	
	
	/**
	 * Returns Item at index.
	 * @param index The index from which to return the Item.
	 * @return
	 */
	public Item getItemAt(int index) {
		return inventory[index];
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
