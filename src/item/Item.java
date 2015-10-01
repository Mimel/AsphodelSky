package item;

import com.google.common.collect.ArrayListMultimap;

import entity.Player;
import game.FlavorText;
import game.Tile;
import game.Toolbox;

/**
 * The superclass for everything that can be stored into an inventory. All pickups and collectibles
 * must be derived from this class.
 * @author Matti
 */
public enum Item {
	HEALING_VIAL(0, Nature.VIAL, "Healing Vial", "Restores a small portion of your maximum health.", 0, 0) {
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, ItemTrigger> almm) {
			if(!this.isUsable(p1)) {
				return new FlavorText("Health is already at a maximum.", 'r');
			} else {
				int difference = p1.getCurrHP();
				p1.adjustCurrentHealth(6);
				if(p1.getCurrHP() > p1.getMaxHP()) {
					p1.equalizeHealth();
				}	
				difference = -(difference - p1.getCurrHP());
				addDie(almm, this, time, 200);
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
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, ItemTrigger> almm) {
			System.out.println("This is an energy vial!");
			return new FlavorText("Energy restored, I guess?", 'b');
		}
		
		public boolean isUsable(Player p1) {
			return p1.getCurrEP() != p1.getMaxEP();
		}
	},
	
	HASTE_VIAL(2, Nature.VIAL, "Haste Vial", "Increases your current speed, and slowly returns it to the regular speed.", 72, 0) {
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, ItemTrigger> almm) {
			int newSpeed = 5;
			int difference = p1.getMovementSpeed() - newSpeed;
			
			p1.setMovementSpeed(newSpeed);
			
			int interval = 40;
			addFade(almm, this, time, interval, difference);
			addDie(almm, this, time, interval*difference);
			return new FlavorText("You feel time slow down around you.", 'b');
		}
		
		public boolean isUsable(Player p1) {
			return true;
		}
		
		public FlavorText fade(Player p1) {
			p1.setMovementSpeed(p1.getMovementSpeed() + 1);
			return new FlavorText("You feel time speed up around you...", 'p');
		}
		
		public FlavorText die(Player p1) {
			return new FlavorText("The passage of time resumes at a normal pace.", 'b');
		}
	},
	
	POISON_VIAL(3, Nature.VIAL, "Poison Vial", "Gradually reduces your health.", 108, 0) {
		public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, ItemTrigger> almm) {
			int numberOfTicks = Toolbox.rollDice(7, 6);
			addFade(almm, this, time, p1.getMovementSpeed(), numberOfTicks);
			addDie(almm, this, time, p1.getMovementSpeed()*numberOfTicks);
			return new FlavorText("You feel a malignant force within you...", 'b');
		}
		
		public boolean isUsable(Player p1) {
			return true;
		}
		
		public FlavorText fade(Player p1) {
			if(Toolbox.rollDice(1, 5) == 1) {
				p1.adjustCurrentHealth(-1);
				return new FlavorText("You convulse.", 'r');
			} else {
				return null;
			}
			
		}
		
		public FlavorText die(Player p1) {
			return new FlavorText("You feel the poison exit your system.", 'g');
		}
	};
	
	/**
	 * When this is paired with an Item in the itemEventQueue, call fade() at that given time. 
	 */
	public static final int ON_ELAPSE_FADE = 0;
	
	/**
	 * When this is paired with an Item in the itemEventQueue, call die() at that given time. 
	 */
	public static final int ON_ELAPSE_DIE = 1;
	
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
	public FlavorText use(Player p1, Tile[][] currentMap, int time, ArrayListMultimap<Integer, ItemTrigger> almm) {
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
	 * Provides a residual effect that is called per every given increment of time. 
	 */
	public FlavorText fade(Player p1) {
		return new FlavorText("This item doesn't have a fade; this is an error.", 'r');
	}
	
	/**
	 * Provides an additional time-activated effect to an item; this method is called whenever 
	 * @param p1 The Player.
	 * @return The message on time-activation.
	 */
	public FlavorText die(Player p1) {
		return new FlavorText("This item doesn't have closure; this is an error.", 'r');
	}
	
	/**
	 * Adds a fade effect a given number of times between a given time interval. Every time the itemEventQueue lapses over a fade,
	 * the item's overridden fade() is called.
	 * @param almm The item event queue.
	 * @param i The Item.
	 * @param time The current time.
	 * @param interval The time interval over which to space the intervals.
	 * @param numberOfIntervals The number of intervals the fade is applied.
	 */
	private static void addFade(ArrayListMultimap<Integer, ItemTrigger> almm, Item i, int time, int interval, int numberOfIntervals) {
		for(int x = 1; x <= numberOfIntervals; x++) {
			almm.put(time + (interval * x), new ItemTrigger(i, ON_ELAPSE_FADE));
		}
	}
	
	/**
	 * Adds a die effect after a given interval of time. The die() method is called after that given interval, usually signifying the end of the item's use.
	 * @param almm The item event queue.
	 * @param i The Item.
	 * @param time The current time.
	 * @param interval The given interval, after which the die() method is called.
	 */
	private static void addDie(ArrayListMultimap<Integer, ItemTrigger> almm, Item i, int time, int interval) {
		almm.put(time + interval, new ItemTrigger(i, ON_ELAPSE_DIE));
	}
	
	
	/**
	 * A sub-enum used to classify items.
	 */
	private enum Nature {
		VIAL;
	}
}
