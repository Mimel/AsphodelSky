package grid;

import display.FocusComponent;
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
	//TODO TESTING
	private Grid<Tile> tiles;

	private IdSearchableGrid<Combatant> actors;

	private Grid<Catalog> catalogs;

	/**
	 * The current map of the game.
	 */
	private Tile[][] map;

	/**
	 * The name of the map.
	 */
	private String name;

	private GridFocus gridCenter;
	
	/**
	 * The output of the grid.
	 */
	private FocusComponent gridOutput;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public CompositeGrid(FocusComponent fc) {
		int DEFAULT_DIMENSION = 20;

		tiles = new TileGrid(DEFAULT_DIMENSION, DEFAULT_DIMENSION);
		actors = new CombatantGrid();
		catalogs = new CatalogGrid();

		this.map = new Tile[DEFAULT_DIMENSION][DEFAULT_DIMENSION];

		for(int x = 0; x < DEFAULT_DIMENSION; x++) {
			for(int y = 0; y < DEFAULT_DIMENSION; y++) {
				map[y][x] = new Tile('.');
			}
		}

		this.gridCenter = new GridFocus(0, 0);

		this.name = "<NO TITLE>";

		this.gridOutput = fc;
	}
	
	/**
	 * Creates an empty grid with set dimensions.
	 * @param height The height of the grid.
	 * @param width The width of the grid.
	 */
	public CompositeGrid(String name, int height, int width, FocusComponent fc) {
		this.map = new Tile[width][height];
		this.gridCenter = new GridFocus(0, 0);

		this.name = name;

		this.gridOutput = fc;
	}

	public String getName() {
		return name;
	}

	public int getXFocus() {
		return gridCenter.getxPosition();
	}

	public int getYFocus() {
		return gridCenter.getyPosition();
	}
	
	/**
	 * Draws the grid, focused on either the player or the manual focus.
	 * This method is preferably called with odd-numbered parameters, though it should handle even-numbered
	 * dimensions well.
	 * @param width The width of the tile array to draw.
	 * @param height The height of the tile array to draw.
	 */
	public void updateGrid(int width, int height) {
		int xFocus = gridCenter.getxPosition();
		int yFocus = gridCenter.getyPosition();
		
		int xStart = xFocus - width/2;
		int yStart = yFocus - height/2;
		
		if(xStart < 0) { xStart = 0; }
		else if(xStart > map[yFocus].length - width) { xStart = map[yFocus].length - width; }
		
		if(yStart < 0) { yStart = 0; }
		else if(yStart > map.length - height) { yStart = map.length - height; }
		
		Tile[][] truncatedMap = new Tile[width][height];

		for(int y = 0; y < height; y++) {
			System.arraycopy(map[yStart + y], xStart, truncatedMap[y], 0, width);
		}
		
		gridOutput.updateGrid(truncatedMap, xFocus - xStart, yFocus - yStart);
	}

	public Tile getFocusedTile() {
		return map[gridCenter.getyPosition()][gridCenter.getxPosition()];
	}

	public void setFocusedTile(int newX, int newY) {
		if(isValidLocation(newX, newY)) {
			gridCenter.setxPosition(newX);
			gridCenter.setyPosition(newY);
		}
	}

	public void bindFocusToPlayer() {
		int playerId = 0;
		if(actors.getOccupantById(playerId) != null) {
			gridCenter.bindToCombatant(playerId);
			gridCenter.setxPosition(actors.getLocationById(playerId).x());
			gridCenter.setyPosition(actors.getLocationById(playerId).y());
		}
	}

	public void unbindFocus() {
		gridCenter.unbind();
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

			if(gridCenter.isBoundToCombatant() && gridCenter.getFocusedCombatantId() == id) {
				gridCenter.setxPosition(newX);
				gridCenter.setyPosition(newY);
			}
		}
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
	 * @return True if the item was successfully placed, false otherwise.
	 */
	public boolean addItem(int itemId, int x, int y) {
		if(isValidLocation(x, y)) {
			map[y][x].getCatalog().insertItem(Item.getItemById(itemId), 1);
			return true;
		}

		return false;
	}

	public Catalog getItemsOnTile(int x, int y) {
		return catalogs.getOccupantAt(x, y);
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
		int newxPosition = gridCenter.getxPosition() + xOffset;
		int newyPosition = gridCenter.getyPosition() + yOffset;
		if(isValidLocation(newxPosition, newyPosition)) {
			gridCenter.setxPosition(newxPosition);
			gridCenter.setyPosition(newyPosition);
		}
	}
	
	/**
	 * Checks to see if the given coordinates represent a valid location on the grid.
	 * @param xCoord The X-coordinate.
	 * @param yCoord The Y-coordinate.
	 * @return True if the location exists on the grid, false otherwise.
	 */
	private boolean isValidLocation(int xCoord, int yCoord) {
		return xCoord >= 0 && yCoord >= 0 && yCoord < map.length && xCoord < map[yCoord].length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Tile[] row : map) {
			for (Tile space : row) {
				sb.append(space.getTerrain());
			}

			sb.append('\n');
		}
		
		return sb.toString();
	}
}
