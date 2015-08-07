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
	
	private Nature nat;
	private String title;
	private String desc;
	private int xStart;
	private int yStart;
	
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
	
	public void use(Player p1) {
		System.out.print("Not overridden.");
	}

	public enum Nature {
		VIAL;
	}
}
