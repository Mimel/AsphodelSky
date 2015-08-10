package item;

import entity.Player;

/**
 * The superclass for everything that can be stored into an inventory. All pickups and collectibles
 * must be derived from this class.
 * @author Matti
 */
public enum Item {
	HEALING_VIAL(Nature.VIAL, "Healing Vial", "Restores a small portion of your maximum health", 0, 0) {
		public void use(Player p1) {
			System.out.println("Overridden!");
		}
	},
	
	ENERGY_VIAL(Nature.VIAL, "Energy Vial", "Restores a small portion of your maximum energy", 0, 36) {
		public void use(Player p1) {
			System.out.println("This is an energy vial!");
		}
	};
	
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
	
	/** The amount of similar items an item has. As confusing as this is, this class also stores multiples of items.
	 * The default is 1. If stackable is true, this can exceed 1.
	 * @see stackable
	 */
	private int amount;
	
	private Item(Nature n, String t, String d, int x, int y) {
		this.nat = n;
		this.title = t;
		this.desc = d;
		this.xStart = x;
		this.yStart = y;
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

	public enum Nature {
		VIAL;
	}
}
