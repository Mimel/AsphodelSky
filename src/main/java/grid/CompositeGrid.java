package grid;

import entity.Combatant;
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

	public final Point MAX_BOUNDS;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public CompositeGrid() {
		MAX_BOUNDS = new Point(20, 20);

		tiles = new TileGrid(MAX_BOUNDS.x(), MAX_BOUNDS.y());
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();

		this.name = "<NO TITLE>";
	}

	public CompositeGrid(String name, int width, int height) {
		this.name = name;
		MAX_BOUNDS = new Point(width, height);

		tiles = new TileGrid(MAX_BOUNDS.x(), MAX_BOUNDS.y());
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();
	}

	public int getNumberOfColumns() {
		return MAX_BOUNDS.x();
	}

	public int getNumberOfRows() {
		return MAX_BOUNDS.y();
	}

	private String getName() {
		return name;
	}

	/**
	 * Attempts to get a tile within the map at the specified coordinates.
	 * 
	 * @param loc The location of the tile requested.
	 * @return The tile if it can be found, or null if it can't.
	 */
	public Tile getTileAt(Point loc) {
		return tiles.getOccupantAt(loc);
	}

	public void setTileAt(char terrain, Point loc) {
		tiles.placeOccupant(new Tile(terrain), loc);
	}

	public void addCombatant(Combatant c, Point loc) {
		if(tiles.canOccupy(loc) && actors.canOccupy(loc)) {
			actors.placeOccupant(c, loc);
		}
	}

	public boolean doesCombatantExist(Combatant c) {
		return actors.getOccupantById(c.getId()) != null;
	}

	public Combatant getCombatantAt(Point p) {
		return actors.getOccupantAt(p);
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
		Point currentLocation = actors.getLocationById(combatantToMove.getId());
		Point newLocation = new Point(currentLocation.x() + xOffset, currentLocation.y() + yOffset);

		if(tiles.canOccupy(newLocation) && actors.canOccupy(newLocation)) {
			Combatant c = actors.removeOccuapantById(combatantToMove.getId());
			actors.placeOccupant(c, newLocation);
		}
	}

	public boolean isTileOccupiedRelativeTo(int id, int x, int y) {
		Point aLoc = actors.getLocationById(id);
		Point targetLocation = new Point(aLoc.x() + x, aLoc.y() + y);
		Tile t = getTileAt(targetLocation);
		return (!tiles.canOccupy(targetLocation) || actors.getOccupantAt(targetLocation) != null);
	}

	public void killCombatant(Combatant combatantToKill) {
		Point loc = actors.getLocationById(combatantToKill.getId());
		Combatant killedCombatant = actors.removeOccuapantById(combatantToKill.getId());
		catalogs.placeOccupant(killedCombatant.getInventory(), loc);
	}

	/**
	 * Searches for an entity within the grid, given the id.
	 * @param c The combatant to look up.
	 * @return The entity if it can be found, or null if it can't.
	 */
	public Combatant getCombatant(Combatant c) {
		return actors.getOccupantById(c.getId());
	}

	public Combatant getPlayer() {
		return actors.getOccupantById(Combatant.PLAYER_ID);
	}

	/**
	 * Spawns a copy of an item with given id on the given tile.
	 * @param item The item to spawn.
	 * @param placeToAdd The tile to spawn the item in.
	 */
	public void addItem(Item item, Point placeToAdd) {
		if(isValidLocation(placeToAdd)) {
			catalogs.placeOccupant(new Catalog(), placeToAdd);
			catalogs.getOccupantAt(placeToAdd).insertItem(item, 1);
		}
	}


	public void addCatalog(Catalog catalog, Point loc) {
		catalogs.placeOccupant(catalog, loc);
	}

	/**
	 * Gets the catalog located on the tile that the given combatant is occupying.
	 * @param c The combatant.
	 * @return The catalog of the tile that the combatant is occupying.
	 */
	public Catalog getItemsOnTileWithCombatant(Combatant c) {
		Point loc = actors.getLocationById(c.getId());
		return catalogs.getOccupantAt(loc);
	}

	public Catalog getCatalogOnTile(Point loc) {
		return catalogs.getOccupantAt(loc);
	}

	public void removeItem(Item itemToRemove, int numberToRemove, Point loc) {
		Catalog c;
		if((c = catalogs.getOccupantAt(loc)) != null) {
			c.consumeItem(itemToRemove, numberToRemove);
		}
	}

	/**
	 * Checks to see if the given coordinates represent a valid location on the grid.
	 * @param loc The set of coordinates.
	 * @return True if the location exists on the grid, false otherwise.
	 */
	private boolean isValidLocation(Point loc) {
		return loc.x() >= 0 && loc.y() >= 0 && loc.x() < MAX_BOUNDS.x() && loc.y() < MAX_BOUNDS.y();
	}

	public String getGridRepresentation() {
		return getName() + '\n' + MAX_BOUNDS.x() + ',' + MAX_BOUNDS.y() + '\n' +
				tiles + "&CATALOGS\n" +
				catalogs + "&ACTORS\n" +
				actors;
	}
}
