package grid;

import display.FocusComponent;
import entity.Combatant;
import entity.EnemyGenerator;
import entity.EnemyRoster;
import item.Catalog;
import item.Item;
import org.javatuples.Pair;

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
	 * The set of enemies on this level.
	 */
	private EnemyRoster roster;
	
	/**
	 * The output of the grid.
	 */
	private FocusComponent gridOutput;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public CompositeGrid(FocusComponent fc) {
		int DEFAULT_DIMENSION = 20;
		this.map = new Tile[DEFAULT_DIMENSION][DEFAULT_DIMENSION];

		for(int x = 0; x < DEFAULT_DIMENSION; x++) {
			for(int y = 0; y < DEFAULT_DIMENSION; y++) {
				map[y][x] = new Tile('.');
			}
		}

		this.gridCenter = new GridFocus(0, 0);

		this.name = "<NO TITLE>";

		this.gridOutput = fc;

		this.roster = new EnemyRoster();
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

		this.roster = new EnemyRoster();
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
		if(roster.getCombatant(playerId) != null) {
			gridCenter.bindToCombatant(playerId);
			gridCenter.setxPosition(roster.getCombatantLocation(playerId).getValue0());
			gridCenter.setyPosition(roster.getCombatantLocation(playerId).getValue1());
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
		if(y >= 0 && y < map.length && x >= 0 && x < map[y].length) {
			return map[y][x];
		} else {
			return null;
		}	
	}

	/**
	 * Adds a combatant onto the grid.
	 * @param name The name of the combatant.
	 * @param x The x-coordinate to place the combatant on.
	 * @param y The y-coordinate to place the combatant on.
	 */
	public void addCombatant(String name, int x, int y) {
		if(isValidLocation(x, y) && map[y][x].canOccupy() && !map[y][x].isOccupied()) {
			Combatant c = EnemyGenerator.getEnemyByName(name);
			map[y][x].fillOccupant(c);
			roster.addCombatant(x, y, name);
		}
	}

	public void addCombatant(Combatant c, int x, int y) {
		if(isValidLocation(x, y) && map[y][x].canOccupy() && !map[y][x].isOccupied()) {
			map[y][x].fillOccupant(c);
			roster.addCombatant(x, y, c);
		}
	}

	public int getXOfCombatant(int id) {
		return roster.getCombatantLocation(id).getValue0();
	}

	public int getYOfCombatant(int id) {
		return roster.getCombatantLocation(id).getValue1();
	}

	/**
	 * Attempts to move an entity from one tile to another.
	 * @param x The X-coordinate of the tile the entity is occupying.
	 * @param y The Y-coordinate of the tile the entity is occupying.
	 * @param newX The X-shift where the entity will go.
	 * @param newY The Y-shift where the entity will go.
	 */
	public void moveCombatant(int x, int y, int newX, int newY) {
		if(isValidLocation(x, y) && isValidLocation(newX, newY)) {
			if(map[y][x].isOccupied() && map[newY][newX].canOccupy() &&!map[newY][newX].isOccupied()) {
				Combatant c = map[y][x].vacateOccupant();
				map[newY][newX].fillOccupant(c);
				roster.moveCombatant(x, y, newX, newY);

				if(gridCenter.isBoundToCombatant() && gridCenter.getFocusedCombatantId() == c.getId()) {
					gridCenter.setxPosition(newX);
					gridCenter.setyPosition(newY);
				}
			}
		}
	}

	/**
	 * Moves the combatant with given id to a new location on the grid given by the
	 * coordinates given.
	 * @param id The id of the combatant to move.
	 * @param newX The X-coordinate to move the combatant to.
	 * @param newY The Y-coordinate to move the combatant to.
	 */
	public void moveCombatant(int id, int newX, int newY) {
		if(isValidLocation(newX, newY) && map[newY][newX].canOccupy() && !map[newY][newX].isOccupied()) {
			Pair<Integer, Integer> coords = roster.getCombatantLocation(id);
			Combatant c = map[coords.getValue1()][coords.getValue0()].vacateOccupant();
			map[newY][newX].fillOccupant(c);
			roster.moveCombatant(id, newX, newY);

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
	public Combatant searchForOccupant(int id) {
		return roster.getCombatant(id);
	}

	/**
	 * Adds an item to the given tile.
	 * @param itemName The item to add.
	 * @param x The x-coordinate of the tile to add the item.
	 * @param y The y-coordinate of the tile to add the item.
	 * @return True if the item was successfully added, false otherwise.
	 */
	public boolean addItem(String itemName, int x, int y) {
		if(isValidLocation(x, y)) {
			map[y][x].getCatalog().insertItem(Item.getItem(itemName));
			return true;
		}

		return false;
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

	/**
	 * Gets the catalog located on the tile that the given combatant is occupying.
	 * @param combatantId The id of the combatant.
	 * @return The catalog of the tile that the combatant is occupying.
	 */
	public Catalog getItemsOnTile(int combatantId) {
		Pair<Integer, Integer> coords = roster.getCombatantLocation(combatantId);
		return map[coords.getValue1()][coords.getValue0()].getCatalog();
	}

	/**
	 * Removes an item from the given tile.
	 * @param itemId The id of the item to remove.
	 * @param x The x-coordinate of the tile to remove the item from.
	 * @param y The y-coordinate of the tile to remove the item from.
	 */
	public void removeItem(int itemId, int x, int y) {
		if(isValidLocation(x, y)) {
			map[y][x].getCatalog().consumeItem(itemId);
		}
	}

	public void removeItem(int itemId, int numberToRemove, int x, int y) {
		if(isValidLocation(x, y)) {
			map[y][x].getCatalog().consumeItem(itemId, numberToRemove);
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
