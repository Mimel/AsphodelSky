package grid;

import display.FocusComponent;
import entity.Combatant;
import item.Catalog;
import item.Item;

import java.util.Map;

/**
 * A map of the game, containing a set of tiles and all objects within.
 * 
 * In the grid 
 * #####
 * #...#
 * #..@#
 * #...#
 * #####
 * The player @ is at location map[2][3].
 * 
 * @author Matt Imel
 */
public class CompositeGrid {

	private final Grid<Tile, Tile[]> tiles;

	private final IdSearchableGrid<Combatant, Map.Entry<Point, Combatant>> actors;

	private final Grid<Catalog, Map.Entry<Point, Catalog>> catalogs;

	/**
	 * The name of the map.
	 */
	private final String name;

	private Point focalPoint;

	private boolean isBoundToCombatant;

	private int boundId;

	private final Point MAX_BOUNDS;
	
	/**
	 * The output of the grid.
	 */
	private final FocusComponent gridOutput;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public CompositeGrid(FocusComponent fc) {
		MAX_BOUNDS = new Point(20, 20);

		tiles = new TileGrid(MAX_BOUNDS.x(), MAX_BOUNDS.y());
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();

		this.name = "<NO TITLE>";
		this.focalPoint = new Point(0,0);
		this.isBoundToCombatant = false;
		this.boundId = 0;

		this.gridOutput = fc;
	}

	public String getName() {
		return name;
	}

	public Point getFocus() {
		return focalPoint;
	}

	public void unbind() {
		isBoundToCombatant = false;
	}

	public void bindTo(int id) {
		if(actors.getOccupantById(id) != null) {
			boundId = id;
			isBoundToCombatant = true;
			focalPoint = new Point(actors.getLocationById(id));
		}
	}

	public Catalog getFocusedCatalog() {
		return catalogs.getOccupantAt(focalPoint.x(), focalPoint.y());
	}

	public Combatant getFocusedCombatant() {
		return actors.getOccupantAt(focalPoint.x(), focalPoint.y());
	}

	public Tile getFocusedTile() {
		return tiles.getOccupantAt(focalPoint.x(), focalPoint.y());
	}
	
	/**
	 * Draws the grid, focused on either the player or the manual focus.
	 * This method is preferably called with odd-numbered parameters, though it should handle even-numbered
	 * dimensions well.
	 * @param width The width of the tile array to draw.
	 * @param height The height of the tile array to draw.
	 */
	public void updateGrid(int width, int height) {
		int xFocus = focalPoint.x();
		int yFocus = focalPoint.y();
		
		int xStart = xFocus - width/2;
		int yStart = yFocus - height/2;

		if(xStart < 0) { xStart = 0; }
		else if(xStart > MAX_BOUNDS.x() - width) { xStart = MAX_BOUNDS.x() - width; }

		if(yStart < 0) { yStart = 0; }
		else if(yStart > MAX_BOUNDS.y() - height) { yStart = MAX_BOUNDS.y() - height; }
		
		Iterable<Tile[]> subTiles = tiles.subGrid(xStart, yStart, width, height);
		Iterable<Map.Entry<Point, Combatant>> subActors = actors.subGrid(xStart, yStart, width, height);
		Iterable<Map.Entry<Point, Catalog>> subCatalogs = catalogs.subGrid(xStart, yStart, width, height);
		
		gridOutput.updateGrid(subTiles, subActors, subCatalogs, xFocus, yFocus, xStart, yStart);
	}
	
	/**
	 * Attempts to get a tile within the map at the specified coordinates.
	 * 
	 * @param x The X-coordinate of the tile requested.
	 * @param y The Y-coordinate of the tile requested.
	 * @return The tile if it can be found, or null if it can't.
	 */
	public Tile getTileAt(int x, int y) {
		return tiles.getOccupantAt(x, y);
	}

	public void addCombatant(Combatant c, int x, int y) {
		if(tiles.canOccupy(x, y) && actors.canOccupy(x, y)) {
			actors.placeOccupant(c, x, y);
		}
	}

	public Point getLocationOfCombatant(int id) {
		return actors.getLocationById(id);
	}

	public Combatant getCombatantAt(int x, int y) {
		return actors.getOccupantAt(x, y);
	}

	public Combatant[] getAllCombatants() {
		return actors.getAllOccupants();
	}

	/**
	 * Moves the combatant with given id to a new location on the grid given by the
	 * coordinates given.
	 * @param id The id of the combatant to move.
	 * @param xOffset The X-amount to shift the combatant by.
	 * @param yOffset The Y-amount to shift the combatant by.
	 */
	public void moveCombatant(int id, int xOffset, int yOffset) {
		Point loc = actors.getLocationById(id);
		int newX = loc.x() + xOffset;
		int newY = loc.y() + yOffset;

		if(tiles.canOccupy(newX, newY) && actors.canOccupy(newX, newY)) {
			Combatant c = actors.removeOccuapantById(id);
			actors.placeOccupant(c, newX, newY);

			if(isBoundToCombatant && boundId == id) {
				focalPoint = new Point(newX, newY);
			}
		}
	}

	public boolean isTileOccupiedRelativeTo(int id, int x, int y) {
		Point aLoc = actors.getLocationById(id);
		return (actors.getOccupantAt(aLoc.x() + x, aLoc.y() + y) != null);
	}

	public void removeCombatant(int id) {
		actors.removeOccuapantById(id);
	}
	
	/**
	 * Searches for an entity within the grid, given the id.
	 * @param id The id to look up.
	 * @return The entity if it can be found, or null if it can't.
	 */
	public Combatant getOccupant(int id) {
		return actors.getOccupantById(id);
	}

	/**
	 * Spawns a copy of an item with given id on the given tile.
	 * @param itemId The id of the item to spawn.
	 * @param x The X-coordinate of the tile to spawn the item in.
	 * @param y The Y-coordinate of the tile to spawn the item in.
	 */
	public void addItem(int itemId, int x, int y) {
		if(isValidLocation(x, y)) {
			if(catalogs.getOccupantAt(x, y) == null) {
				catalogs.placeOccupant(new Catalog(), x, y);
			}
			catalogs.getOccupantAt(x, y).insertItem(Item.getItemById(itemId), 1);
		}
	}

	/**
	 * Gets the catalog located on the tile that the given combatant is occupying.
	 * @param combatantId The id of the combatant.
	 * @return The catalog of the tile that the combatant is occupying.
	 */
	public Catalog getItemsOnTile(int combatantId) {
		Point loc = actors.getLocationById(combatantId);
		return catalogs.getOccupantAt(loc.x(), loc.y());
	}

	/**
	 * Removes an item from the given tile.
	 * @param itemId The id of the item to remove.
	 * @param x The x-coordinate of the tile to remove the item from.
	 * @param y The y-coordinate of the tile to remove the item from.
	 */
	public void removeItem(int itemId, int x, int y) {
		Catalog c;
		if((c = catalogs.getOccupantAt(x, y)) != null) {
			c.consumeItem(itemId);
		}
	}

	public void removeItem(int itemId, int numberToRemove, int x, int y) {
		Catalog c;
		if((c = catalogs.getOccupantAt(x, y)) != null) {
			c.consumeItem(itemId, numberToRemove);
		}
	}

	/**
	 * Switches the crosshair location to a tile relative to the one the crosshair is currently on.
	 * If there is no crosshair in the given tile, that tile becomes focused.
	 * Else, the the "isFocused" flag switches between the given tile and the tile plus offsets.
	 * @param xOffset The X-shift where the entity will go.
	 * @param yOffset The Y-shift where the entity will go.
	 */
	public void shiftFocus(int xOffset, int yOffset) {
		int newxPosition = focalPoint.x() + xOffset;
		int newyPosition = focalPoint.y() + yOffset;
		if(isValidLocation(newxPosition, newyPosition)) {
			isBoundToCombatant = false;
			focalPoint = new Point(newxPosition, newyPosition);
		}
	}

	public void shiftFocusToClosestCombatant(int horiz, int vert) {
		Direction h;
		Direction v;

		if(horiz > 0) {
			h = Direction.EAST;
		} else if(horiz < 0) {
			h = Direction.WEST;
		} else {
			h = Direction.CENTER;
		}

		if(vert > 0) {
			v = Direction.SOUTH;
		} else if(vert < 0) {
			v = Direction.NORTH;
		} else {
			v = Direction.CENTER;
		}

		if(getFocusedCombatant() != null) {
			focalPoint = actors.getLocationById(actors.getClosestOccupant(getFocusedCombatant().getId(), h, v).getId());
		}
	}
	
	/**
	 * Checks to see if the given coordinates represent a valid location on the grid.
	 * @param xCoord The X-coordinate.
	 * @param yCoord The Y-coordinate.
	 * @return True if the location exists on the grid, false otherwise.
	 */
	private boolean isValidLocation(int xCoord, int yCoord) {
		return xCoord >= 0 && yCoord >= 0 && yCoord < MAX_BOUNDS.y() && xCoord < MAX_BOUNDS.x();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Tile[] row : tiles) {
			for (Tile space : row) {
				sb.append(space.getTerrain());
			}

			sb.append('\n');
		}
		
		return sb.toString();
	}
}
