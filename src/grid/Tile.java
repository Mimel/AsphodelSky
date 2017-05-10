package grid;

import entity.Occupant;
import item.Catalog;

/**
 * The makeup of a grid, that may contain items, the player, and enemy, or otherwise;
 * All data is stored in each tile.
 * @author Matt Imel
 *
 * TODO: Colors are a measure to replace images if they cannot be loaded.
 */
public class Tile {
	
	/**
	 * The terrain of the tile, denoted by a character.
	 * 
	 */
	private char terrain;
	
	/**
	 * If this tile is being focused for either search or selection, this is true.
	 */
	private boolean isFocused;
	
	/**
	 * Indicator on whether or not a tile can occupy an entity.
	 * If true, the tile can occupy an entity.
	 * If false, the tile cannot; it is always null.
	 */
	private boolean canOccupy;
	
	/**
	 * The occupant of this tile. Null indicates that it either does not have an occupant
	 * or it cannot occupy an entity.
	 */
	private Occupant occupant;
	
	/**
	 * The set of items on this tile.
	 */
	private Catalog catalog;
	
	public Tile(char terr) {
		this.terrain = terr;
		this.isFocused = false;
		
		//Most tiles cannot be occupied.
		this.canOccupy = false;
		
		switch(terr) {
			//Generic floor
			case '.':
				canOccupy = true;
				break;
		}
		
		this.occupant = null;
		
		//Allow three items maximum per tile.
		this.catalog = new Catalog();
	}
	
	//Accessors, Mutators
	public char getTerrain() { return terrain; }
	public Occupant getOccupant() { return occupant; }
	public boolean isFocused() { return isFocused; }
	public boolean canOccupy() { return canOccupy; }
	public Catalog getCatalog() { return catalog; }
	
	void toggleFocused() {
		isFocused = !isFocused;
	}
	
	/**
	 * Removes the occupant from a tile.
	 * @return The occupant.
	 */
	public Occupant vacateOccupant() {
		Occupant temp = occupant;
		occupant = null;
		return temp;
	}
	
	/**
	 * Attempts to place an occupant in an empty tile. If there is already an occupant in the tile,
	 * The operation fails.
	 * 
	 * @param newOccupant The new occupant.
	 * @return True if the occupant is successfully inserted, false if not.
	 */
	public boolean fillOccupant(Occupant newOccupant) {
		if(canOccupy && occupant == null) {
			occupant = newOccupant;
			return true;
		} else {
			return false;
		}
	}
}