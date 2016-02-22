package entity;

import item.*;
import org.javatuples.Pair;

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
	private ItemAmountPair[] inventory;
	
	/** The size of the inventory array. Usually conforming to the PlayerInfo part of the GUI. */
	private final int INVENTORY_SIZE = 36;
	
	private Accessory e_Acce;
	private Helm e_Helm;
	private Neckwear e_Neck;
	private Torso e_Body;
	private Weapon e_Weap;
	private Offhand e_Offh;
	private Legs e_Legs;
	private Feet e_Feet;
	
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
		this.inventory = new ItemAmountPair[INVENTORY_SIZE];
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
	
	public ItemAmountPair[] getInventory() {
		return inventory;
	}
	
	/**
	 * Pushes an Item onto the inventory, in the way that a stack architecture does.
	 * TODO: Handle case when inventory is full.
	 * @param i The Item being pushed onto the inventory array.
	 */
	public void pushToInventory(Item i, int amt) {
		for(int x = 0; x < inventory.length; x++) {
			if(inventory[x].getItem().equals(i) && i.isStackable()) {
				inventory[x].addItems(amt);
				return;
			} else if(inventory[x] == null) {
				inventory[x] = new ItemAmountPair(i, amt);
				return;
			}
		}
	}
	
	/**
	 * Pushes an Item onto the inventory, in the way that a stack architecture does.
	 * TODO: Handle case when inventory is full.
	 * @param i The Item being pushed onto the inventory array.
	 */
	public void pushToInventory(ItemAmountPair iap) {
		for(int x = 0; x < inventory.length; x++) {
			if(inventory[x] == null) {
				inventory[x] = iap;
				return;
			} else if(inventory[x].equals(iap) && iap.getItem().isStackable()) {
				inventory[x].addItems(iap.getAmount());
				return;
			}	
		}
	}
	
	public void decrementOrRemoveItem(int pos) {
		if(inventory[pos].getAmount() <= 1) {
			removeItemFromInventory(pos);
		} else {
			inventory[pos].decrementItem();
		}
	}
	
	/**
	 * Removes an item from the inventory.
	 * @param pos 	The position in inventory to remove the item from.
	 */
	public void removeItemFromInventory(int pos) {
		inventory[pos] = null;
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
