package item;

import entity.Player;

/**
 * The superclass for everything that can be stored into an inventory. All pickups and collectibles
 * must be derived from this class.
 * @author Matti
 */
public enum Item {
	
	/**
	 * 
	 */
	HEALING_VIAL(1, Nature.VIAL, "Healing Vial", "Restores a small portion of your maximum health", 0, 0) {
		public void use(Player p1) {
			p1.takeDamage(-6);
			if(p1.getCurrHP() > p1.getMaxHP()) {
				p1.equalizeHealth();
			}
		}
	},
	
	/**
	 * 
	 */
	ENERGY_VIAL(2, Nature.VIAL, "Energy Vial", "Restores a small portion of your maximum energy", 0, 36) {
		public void use(Player p1) {
			System.out.println("This is an energy vial!");
		}
	};
	
	/** The type of Item. All items have a Nature, drawn out of the Nature enum. */
	private Nature nat;
	
	/** The numerical ID of the item. */
	private int id;
	
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
	
	/** The amount of similar items an item has. As confusing as this is, this class also stores multiples of items.
	 * The default is 1. If stackable is true, this can exceed 1.
	 * @see stackable
	 */
	private int amount;
	
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
		this.amount = 1;
		
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
	
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Adds the int number to the itemCount. A negative number represents the removal of items.
	 * @param number Number of items to add/discount.
	 */
	public void adjustItemCount(int number) {
		amount += number;
	}
	
	public boolean natureIs(Nature n) {
		return nat == n;
	}
	
	/**
	 * The use method for items that only affect the player. Can be overridden in the predefined fields.
	 * @param p1 The player.
	 */
	public void use(Player p1) {
		System.out.print("Not overridden.");
	}
	
	/**
	 * A sub-enum used to classify items.
	 */
	private enum Nature {
		VIAL;
	}
}
