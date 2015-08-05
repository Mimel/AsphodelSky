package item;

/**
 * The superclass for everything that can be stored into an inventory. All pickups and collectibles
 * must be derived from this class.
 * @author Matti
 */
public abstract class Item {
	/** The name of the item, used for display purposes. */
	private String title;
	
	/** The description of the item and its purposes, for display. */
	private String desc;
	
	/** On the tileset, the beginning X coordinate of the image for the item. */
	private int xStart;
	
	/** On the tileset, the beginning Y coordinate of the image for the item. */
	private int yStart;
	
	public Item(String t, String d, int x, int y) {
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
	
	public int getX() {
		return xStart; 
	}
	
	public int getY() {
		return yStart;
	}
}
