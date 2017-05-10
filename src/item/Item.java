package item;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import entity.Combatant;

/**
 * A template for creating an item. All final properties are tied to the class the item is in (Say, Vial or Head),
 * while the non-final properties are tied to each individual item.
 * 
 * The ID is an exception to this.
 * 
 * @see Catalog
 * @author Matt Imel
 *
 */
public abstract class Item implements Comparable<Item> {
	
	/**
	 * The width of an item, in accordance to the tileset.
	 */
	public static final int ITEM_WIDTH = 48;
	
	/**
	 * Keeps track of the current id number when a new item is created.
	 * Thread-safe, on the off-chance that the individual item catalogs are created concurrently.
	 */
	private static final AtomicInteger AUTO_INCR_ID = new AtomicInteger(0);
	
	/**
	 * The id of the item. Each item's id is unique.
	 */
	private final int ID;
	
	/**
	 * The tileset the image belongs to. Each category must have a singluar image
	 * used to represent it's images.
	 */
	private final Image TILESET;
	
	/**
	 * The X-offset from the tileset used to determine where the image set begins.
	 */
	private final int X_OFFSET;
	
	/**
	 * The Y-offset from the tileset used to determine where the image set begins.
	 */
	private final int Y_OFFSET;
	
	/**
	 * The name of the item.
	 */
	protected String name;
	
	/**
	 * A visual description of the item.
	 */
	protected String descVis;

	/**
	 * A description of the item shown on use.
	 */
	protected String descUse;
	
	/**
	 * The margin (in the context of the X variable) needed to reach the coordinate that begins the item's image.
	 */
	protected int xMargin;
	
	/**
	 * The margin (in the context of the Y variable) needed to reach the coordinate that begins the item's image.
	 */
	protected int yMargin;
	
	/**
	 * Creates the properties of an item set, where all properties hold true for all instances of that set.
	 * @param tileset The tileset to use.
	 * @param xoff The X-offset.
	 * @param yoff The Y-offset.
	 */
	protected Item(String tileset, int xoff, int yoff) {
		this.ID = AUTO_INCR_ID.incrementAndGet();
		
		Image tempimg;	
		try {
			tempimg = ImageIO.read(new File(tileset));
		} catch(IOException ioe) {
			System.out.println("What?");
			tempimg = null;
		}
		
		this.TILESET = tempimg;

		this.X_OFFSET = xoff;
		this.Y_OFFSET = yoff;
	}
	
	public Image getTileset() {
		return TILESET;
	}
	
	public int getXOffset() {
		return X_OFFSET;
	}
	
	public int getYOffset() {
		return Y_OFFSET;
	}
	
	/**
	 * Gets the id of the item.
	 * @return The id.
	 */
	public int getId() {
		return ID;
	}
	
	/**
	 * Gets the name of the item.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the visual description of the item.
	 * @return The visual description.
	 */
	public String getVisualDescription() {
		return descVis;
	}
	
	/**
	 * Gets the use description of the item
	 * @return The usage description.
	 */
	public String getUseDescription() {
		return descUse;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getXMargin() {
		return xMargin;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getYMargin() {
		return yMargin;
	}
	
	/**
	 * Uses the item.
	 */
	public abstract void use();
	
	/**
	 * Uses the item that affects the user.
	 */
	public abstract void use(Combatant user);
	
	/**
	 * Compares two items by id.
	 */
	@Override
	public int compareTo(Item i) {
		return ((Integer) this.ID).compareTo((Integer) i.ID);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
