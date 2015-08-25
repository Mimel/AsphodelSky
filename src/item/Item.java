package item;

import com.google.common.collect.ArrayListMultimap;

import entity.Player;
import game.FlavorText;
import game.Tile;

/**
 * The superclass for everything that can be stored into an inventory. All pickups and collectibles
 * must be derived from this class.
 * @author Matti
 */
public enum Item {
	HEALING_VIAL(0, Nature.VIAL, "Healing Vial", "Restores a small portion of your maximum health.", 0, 0) {
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, Item> tmm) {
			if(!this.isUsable(p1)) {
				return new FlavorText("Health is already at a maximum.", 'r');
			} else {
				int difference = p1.getCurrHP();
				p1.adjustCurrentHealth(6);
				if(p1.getCurrHP() > p1.getMaxHP()) {
					p1.equalizeHealth();
				}
				difference = -(difference - p1.getCurrHP());
				tmm.put(time + 20, this);
				return new FlavorText(difference + " health restored.", 'g');
			}
		}
		
		public boolean isUsable(Player p1) {
			return p1.getCurrHP() != p1.getMaxHP();
		}
		
		public FlavorText die(Player p1) {
			p1.adjustCurrentHealth(-7);
			return new FlavorText("You lost 7 health!", 'b');
		}
	},
	
	ENERGY_VIAL(1, Nature.VIAL, "Energy Vial", "Restores a small portion of your maximum energy.", 36, 0) {
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, Item> tmm) {
			System.out.println("This is an energy vial!");
			tmm.put(time + 20, this);
			return new FlavorText("Energy restored, I guess?", 'b');
		}
	};
	
	/** Numeric ID; Each item has a different, unique id. */
	private int id;
	
	/** The type of Item. All items have a Nature, drawn out of the Nature enum. */
	private Nature nat;
	
	/** The name of the Item. */
	private String title;
	
	/** The description of the Item, providing information about it and its usage. */
	private String desc;
	
	/** The X-coordinate of the upper-left corner of the item image in the Item tileset. */
	private int xStart;
	
	/** The Y-coordinate of the upper-left corner of the item image in the Item tileset. */
	private int yStart;
	
	/** Whether or not an item can have multiples occupy the same spot in an inventory. */
	private boolean stackable;
	
	/**
	 * Determines whether or not the item will be removed or decremented from inventory when used.
	 */
	private boolean consumable;
	
	private Item(int id, Nature n, String t, String d, int x, int y) {
		this.id = id;
		this.nat = n;
		this.title = t;
		this.desc = d;
		this.xStart = x;
		this.yStart = y;
		
		if(nat == Nature.VIAL) {
			this.stackable = true;
			this.consumable = true;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDesc() {
		return desc;
	}

	public int getxStart() {
		return xStart;
	}

	public int getyStart() {
		return yStart;
	}
	
	public boolean isStackable() {
		return stackable == true;
	}
	
	public boolean isConsumable() {
		return consumable == true;
	}
	
	/**
	 * Compares item with a specific Nature.
	 * @param n A Nature.
	 * @return The similarity between the parameter and object's Nature.
	 */
	public boolean natureIs(Nature n) {
		return nat == n;
	}
	
	/**
	 * The use method for items that only affect the player. Can be overridden in the predefined fields.
	 * @param p1 The player.
	 * @return The message associated with the usage of the item.
	 */
	public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, Item> tmm) {
		return new FlavorText("Oops! Error!", 'r');
	}
	
	/**
	 * Determines whether or not the item in question can be used; Returns true if it can,
	 * false if it can't
	 * @param p1 The player.
	 * @return Whether or not the item can be used.
	 */
	public boolean isUsable(Player p1) {
		return false;
	}
	
	/**
	 * Provides an additional time-activated effect to an item; this method is called whenever 
	 * @param p1
	 * @return
	 */
	public FlavorText die(Player p1) {
		return new FlavorText("This item doesn't have closure; this is an error.", 'r');
	}
	
	/**
	 * A sub-enum used to classify items.
	 */
	private enum Nature {
		VIAL;
	}
}
