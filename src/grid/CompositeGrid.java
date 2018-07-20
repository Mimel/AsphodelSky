package grid;

import entity.Combatant;
import entity.Player;
import item.Catalog;
import item.Item;

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

	private final Grid<Tile> tiles;

	private final IdSearchableGrid<Combatant> actors;

	private final Grid<Catalog> catalogs;

	/**
	 * The name of the map.
	 */
	private final String name;

	private Point focalPoint;

	private boolean isBoundToCombatant;

	private int boundId;

	private final Point MAX_BOUNDS;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public CompositeGrid() {
		MAX_BOUNDS = new Point(20, 20);

		tiles = new TileGrid(MAX_BOUNDS.x(), MAX_BOUNDS.y());
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();

		this.name = "<NO TITLE>";
		this.focalPoint = new Point(0,0);
		this.isBoundToCombatant = false;
		this.boundId = 0;
	}

	public CompositeGrid(String name, int width, int height) {
		this.name = name;
		MAX_BOUNDS = new Point(width, height);

		tiles = new TileGrid(MAX_BOUNDS.x(), MAX_BOUNDS.y());
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();

		this.focalPoint = new Point(0,0);
		this.isBoundToCombatant = false;
		this.boundId = 0;
	}

	public int getNumberOfColumns() {
		return MAX_BOUNDS.x();
	}

	public int getNumberOfRows() {
		return MAX_BOUNDS.y();
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
	 * Attempts to get a tile within the map at the specified coordinates.
	 * 
	 * @param x The X-coordinate of the tile requested.
	 * @param y The Y-coordinate of the tile requested.
	 * @return The tile if it can be found, or null if it can't.
	 */
	public Tile getTileAt(int x, int y) {
		return tiles.getOccupantAt(x, y);
	}

	public void setTileAt(int x, int y, char terrain) {
		tiles.placeOccupant(new Tile(terrain), x, y);
	}

	public void addCombatant(Combatant c, int x, int y) {
		if(tiles.canOccupy(x, y) && actors.canOccupy(x, y)) {
			actors.placeOccupant(c, x, y);
		}
	}

	public boolean doesCombatantExist(Combatant c) {
		return actors.getOccupantById(c.getId()) != null;
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
	 * @param combatantToMove The combatant to move.
	 * @param xOffset The X-amount to shift the combatant by.
	 * @param yOffset The Y-amount to shift the combatant by.
	 */
	public void moveCombatant(Combatant combatantToMove, int xOffset, int yOffset) {
		Point loc = actors.getLocationById(combatantToMove.getId());
		int newX = loc.x() + xOffset;
		int newY = loc.y() + yOffset;

		if(tiles.canOccupy(newX, newY) && actors.canOccupy(newX, newY)) {
			Combatant c = actors.removeOccuapantById(combatantToMove.getId());
			actors.placeOccupant(c, newX, newY);

			if(isBoundToCombatant && boundId == combatantToMove.getId()) {
				focalPoint = new Point(newX, newY);
			}
		}
	}

	public boolean isTileOccupiedRelativeTo(int id, int x, int y) {
		Point aLoc = actors.getLocationById(id);
		return (actors.getOccupantAt(aLoc.x() + x, aLoc.y() + y) != null);
	}

	public void killCombatant(Combatant combatantToKill) {
		Point loc = actors.getLocationById(combatantToKill.getId());
		Combatant killedCombatant = actors.removeOccuapantById(combatantToKill.getId());
		catalogs.placeOccupant(killedCombatant.getInventory(), loc.x(), loc.y());
	}

	/**
	 * Searches for an entity within the grid, given the id.
	 * @param c The combatant to look up.
	 * @return The entity if it can be found, or null if it can't.
	 */
	public Combatant getCombatant(Combatant c) {
		return actors.getOccupantById(c.getId());
	}

	public Player getPlayer() {
		return (Player)actors.getOccupantById(Player.PLAYER_ID);
	}

	/**
	 * Spawns a copy of an item with given id on the given tile.
	 * @param item The item to spawn.
	 * @param placeToAdd The tile to spawn the item in.
	 */
	public void addItem(Item item, Point placeToAdd) {
		if(isValidLocation(placeToAdd.x(), placeToAdd.y())) {
			catalogs.placeOccupant(new Catalog(), placeToAdd.x(), placeToAdd.y());
			catalogs.getOccupantAt(placeToAdd.x(), placeToAdd.y()).insertItem(item, 1);
		}
	}

	public void addCatalog(Catalog catalog, int x, int y) {
		catalogs.placeOccupant(catalog, x, y);
	}

	/**
	 * Gets the catalog located on the tile that the given combatant is occupying.
	 * @param c The combatant.
	 * @return The catalog of the tile that the combatant is occupying.
	 */
	public Catalog getItemsOnTileWithCombatant(Combatant c) {
		Point loc = actors.getLocationById(c.getId());
		return catalogs.getOccupantAt(loc.x(), loc.y());
	}

	public Catalog getItemsOnTile(int x, int y) {
		return catalogs.getOccupantAt(x, y);
	}

	/**
	 * Removes an item from the given tile.
	 * @param itemToRemove The item to remove.
	 * @param x The x-coordinate of the tile to remove the item from.
	 * @param y The y-coordinate of the tile to remove the item from.
	 */
	public void removeItem(Item itemToRemove, int x, int y) {
		Catalog c;
		if((c = catalogs.getOccupantAt(x, y)) != null) {
			c.consumeItem(itemToRemove);
		}
	}

	public void removeItem(Item itemToRemove, int numberToRemove, int x, int y) {
		Catalog c;
		if((c = catalogs.getOccupantAt(x, y)) != null) {
			c.consumeItem(itemToRemove, numberToRemove);
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

	public String getGridRepresentation() {
		return getName() + '\n' + MAX_BOUNDS.x() + ',' + MAX_BOUNDS.y() + '\n' +
				tiles + "&CATALOGS\n" +
				catalogs + "&ACTORS\n" +
				actors;
	}
}
