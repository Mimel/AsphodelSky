package item;

import entity.Player;

/**
 * Denote the attributes of each item within each category that
 * extends this class. Note that all subclasses of this must be classes
 * that have private constructors and self-referential, static, final instances
 * of the class within them. As horrible as this sounds, this is to give the class
 * an enum-like feel, without the implicit extension of the Java.Enum class,
 * so that class can extend this one instead.
 * @author Matt Imel
 */
public abstract class Item {
	
	//NOTE: All final globals are attributed to the item CATEGORY, rather
	//than the individual items themselves. All globals that are not final
	//are attributed to each individual item. For example, tileset is used
	//by all items within a category so it's final; while a title is used
	//by an individual item, and is therefore not final.
	
	/** The file location of the tileset used by this item category */
	protected final String TILESET;
	
	/** 
	 * The X offset from the origin where the specific region created
	 * by the item category starts. This is meant to prevent the usage
	 * of multiple different images per item category.
	 */
	protected final int X_OFFSET;
	
	/**
	 * The Y offset from the origin where the specific region
	 * created by the item category starts.
	 */
	protected final int Y_OFFSET;
	
	//NOTE: Because an items stackability(?) is determined by whatever class it is in,
	//the stackable property is a final global, as unintuitive as that may seem.
	/** Determines whether multiples of items occupy the same inventory slot. */
	protected final boolean STACKABLE;
	
	/** The name of the item. */
	protected String title;
	
	/** The description of the item. */
	protected String desc;
	
	/** X Location of the item in the tileset relative to the tileset origin plus the x offset. */
	protected int xItemLoc;
	
	/** Y Location of the item in the tileset relative to the tileset origin plus the y offset. */
	protected int yItemLoc;
	
	protected Item(String fileLoc, int xOff, int yOff) {
		this.TILESET = fileLoc;
		this.X_OFFSET = xOff;
		this.Y_OFFSET = yOff;
		
		if(this instanceof Equippable) {
			this.STACKABLE = false;
		} else {
			this.STACKABLE = true;
		}
	}
	
	public String getTileset() {
		return TILESET;
	}
	
	public int getXOffset() {
		return X_OFFSET;
	}
	
	public int getYOffset() {
		return Y_OFFSET;
	}
	
	public boolean isStackable() {
		return STACKABLE;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public int getXLocationInTileset() {
		return xItemLoc;
	}
	
	public int getYLocationInTileset() {
		return yItemLoc;
	}
	
	/**
	 * Determines whether or not an item can be used.
	 * @param p1 The Player.
	 * @return Whether or not an item can be used.
	 */
	public abstract boolean isUsable(Player p1);
	
	/**
	 * The effect that happens when an item is used. For all consumable items, this is akin
	 * to the initial consumption. For equipment, this is akin to equipping the item.
	 * @param p1 The Player.
	 * @return A message describing the effect.
	 */
	public abstract String use(Player p1);
	
	/**
	 * An effect that happens every set interval of time. Whenever that interval is hit,
	 * this method is called.
	 * @param p1 The Player.
	 * @return A message describing the effect.
	 */
	public abstract String step(Player p1);
	
	/**
	 * An effect that happens after an item is used. For equipment, this happens
	 * after removing it from yourself.
	 * @param p1 The player.
	 * @return A message describing the effect.
	 */
	public abstract String die(Player p1);
}
