package grid;

import display.FocusComponent;
import display.HeaderComponent;
import entity.Combatant;
import entity.EnemyGenerator;
import entity.EnemyRoster;
import generator.EmptyShipGenerator;
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
public class Grid {
	
	/**
	 * The dimension of the grid.
	 * The grid is created with the assumption that the 2D array acts like a square
	 * if the constant is used.
	 */
	private final int DEFAULT_DIMENSION = 20;
	
	/**
	 * The current map of the game.
	 */
	private Tile[][] map;

	/**
	 * The name of the map.
	 */
	private String name;
	
	/**
	 * The X-Coordinate of the focused tile, if any exist. If there are no focused tiles, this becomes -1.
	 */
	private int xFocusedTile;
	
	/**
	 * The Y-Coordinate of the focused tile, if any exist. If there are no focused tiles, this becomes -1.
	 */
	private int yFocusedTile;

	/**
	 * The set of enemies on this level.
	 */
	private EnemyRoster roster;
	
	/**
	 * The output of the grid.
	 */
	private FocusComponent gridOutput;

	/**
	 * The output for the map information.
	 */
	private HeaderComponent headerOutput;
	
	/**
	 * Creates an empty grid with default dimensions.
	 */
	public Grid(HeaderComponent hc, FocusComponent fc) {
		this.map = new Tile[DEFAULT_DIMENSION][DEFAULT_DIMENSION];
		this.map = new EmptyShipGenerator().generateGrid(DEFAULT_DIMENSION, DEFAULT_DIMENSION);
		this.xFocusedTile = -1;
		this.yFocusedTile = -1;

		this.name = "<NO TITLE>";

		this.headerOutput = hc;
		this.gridOutput = fc;

		this.roster = new EnemyRoster();
	}
	
	/**
	 * Creates an empty grid with set dimensions.
	 * @param height The height of the grid.
	 * @param width The width of the grid.
	 */
	public Grid(String name, int height, int width, HeaderComponent hc, FocusComponent fc) {
		this.map = new Tile[width][height];
		this.xFocusedTile = -1;
		this.yFocusedTile = -1;

		this.name = name;

		this.headerOutput = hc;
		this.gridOutput = fc;

		this.roster = new EnemyRoster();
	}

	public String getName() {
		return name;
	}

	public int getHeight() {
		return map.length;
	}

	public int getWidth(int row) {
		return map[row].length;
	}

	public int getXFocus() {
		return xFocusedTile;
	}

	public int getYFocus() {
		return yFocusedTile;
	}
	
	/**
	 * Draws the grid, focused on either the player or the manual focus.
	 * This method is preferably called with odd-numbered parameters, though it should handle even-numbered
	 * dimensions well.
	 * @param width The width of the tile array to draw.
	 * @param height The height of the tile array to draw.
	 */
	public void updateGrid(int width, int height) {
		int xFocus = (xFocusedTile == -1 ? roster.getPlayerLocation().getValue0() : xFocusedTile);
		int yFocus = (yFocusedTile == -1 ? roster.getPlayerLocation().getValue1() : yFocusedTile);
		
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
		
		gridOutput.updateGrid(truncatedMap);
	}

	public void updateHeader(int time) {
		headerOutput.setTitle(name);
		headerOutput.setTime(time);
	}

	public Tile getFocusedTile() {
		if(xFocusedTile != -1 && yFocusedTile != -1) {
			return map[yFocusedTile][xFocusedTile];
		} else {
			return null;
		}
	}

	public void setFocusedTile(int newX, int newY) {
		if(xFocusedTile == -1 && yFocusedTile == -1) {
			map[newY][newX].toggleFocused();
			xFocusedTile = newX;
			yFocusedTile = newY;
		}
	}
	
	/**
	 * Clears the focused tile by setting the focused tile to a location out of bounds.
	 */
	public void clearFocusedTile() {
		map[yFocusedTile][xFocusedTile].toggleFocused();
		xFocusedTile = -1;
		yFocusedTile = -1;
	}
	
	/**
	 * Fills the grid with tiles according to the character string inserted,
	 * from the top left, going right, then down, to the bottom right.
	 * 
	 * If the character string is shorter than the number of tiles in the grid,
	 * the remainder are filled with empty (?) tiles. Excess characters are cropped.
	 * 
	 * @param terrain The character string to insert as the map.
	 */
	public void fillGrid(String terrain) {
		int tileCount = 0;
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++, tileCount++) {
				if(terrain != null && tileCount < terrain.length()) {
					map[y][x] = new Tile(terrain.charAt(tileCount));
				} else {
					map[y][x] = new Tile('?');
				}
			}
		}
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
	 * Attempts to set a tile within the map at the specified coordinates to a specific Tile.
	 * @param x The X-coordinate of the tile requested.
	 * @param y The Y-coordinate of the tile requested.
	 * @param t The Tile to replace the current one.
	 * @return If the operation was successful, returns true. Else, returns false.
	 */
	public boolean setTileAt(int x, int y, Tile t) {
		if(y >= 0 && y < map.length && x >= 0 && x < map[y].length) {
			map[y][x] = t;
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * Adds a combatant onto the grid.
	 * @param name The name of the combatant.
	 * @param x The x-coordinate to place the combatant on.
	 * @param y The y-coordinate to place the combatant on.
	 */
	public void addCombatant(String name, int x, int y) {
		if(isValidLocation(x, y) && !map[y][x].isOccupied()) {
			Combatant c = EnemyGenerator.getEnemyByName(name);
			map[y][x].fillOccupant(c);
			roster.addCombatant(x, y, name);
		}
	}

	public void addCombatant(Combatant c, int x, int y) {
		if(isValidLocation(x, y) && !map[y][x].isOccupied()) {
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
			if(map[y][x].isOccupied() && !map[newY][newX].isOccupied()) {
				Combatant c = map[y][x].vacateOccupant();
				map[newY][newX].fillOccupant(c);
				roster.moveCombatant(x, y, newX, newY);
			}
		}
	}

	public void moveCombatant(int id, int newX, int newY) {
		if(isValidLocation(newX, newY) && !map[newY][newX].isOccupied()) {
			Pair<Integer, Integer> coords = roster.getCombatantLocation(id);
			Combatant c = map[coords.getValue1()][coords.getValue0()].vacateOccupant();
			map[newY][newX].fillOccupant(c);
			roster.moveCombatant(id, newX, newY);
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

	public boolean addItem(int itemId, int x, int y) {
		if(isValidLocation(x, y)) {
			map[y][x].getCatalog().insertItem(Item.getItemById(itemId));
			return true;
		}

		return false;
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

	/**
	 * Switches the crosshair location to a tile relative to the one the crosshair is currently on.
	 * If there is no crosshair in the given tile, that tile becomes focused.
	 * Else, the the "isFocused" flag switches between the given tile and the tile plus offsets.
	 * @param xOffset The X-shift where the entity will go.
	 * @param yOffset The Y-shift where the entity will go.
	 */
	public void switchFocus(int xOffset, int yOffset) {
		if(isValidLocation(xFocusedTile, yFocusedTile) && isValidLocation(xFocusedTile + xOffset, yFocusedTile + yOffset)) {
			map[yFocusedTile][xFocusedTile].toggleFocused();
			map[yFocusedTile + yOffset][xFocusedTile + xOffset].toggleFocused();
			xFocusedTile += xOffset;
			yFocusedTile += yOffset;
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
