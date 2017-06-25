package grid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import entity.Combatant;
import org.javatuples.Triplet;

import item.Catalog;

/**
 * The makeup of a grid, that may contain items, the player, and enemy, or otherwise;
 * All data is stored in each tile.
 * @author Matt Imel
 */
public class Tile {
	
	/**
	 * Maps character representations of terrain to their descriptions.
	 * The triplet consists of (in order) the NAME, the DESCRIPTION, and 
	 * the FLAGS.
	 */
	private static Map<Character, Triplet<String, String, Byte>> TerrainCharacterToTraits;
	
	/**
	 * The terrain of the tile, denoted by a character.
	 * 
	 */
	private char terrain;
	
	/**
	 * The name of the tile.
	 */
	private String name;
	
	/**
	 * The description of the tile.
	 */
	private String description;
	
	/**
	 * The flags on a tile are represented by 8 binary digits in a byte.
	 * The bits can be represented as the string T???????, where T is the most significant bit.
	 * 
	 * T = Traversable: When on, the tile is able to be traversed. This implies
	 * that there can be an occupant on this tile, and that occupants can move into
	 * and out of this tile. Only one occupant can be in a tile.
	 * 
	 * TODO Add more.
	 */
	private byte flags;
	
	/**
	 * If this tile is being focused for either search or selection, this is true.
	 */
	private boolean isFocused;
	
	/**
	 * The occupant of this tile. Null indicates that it either does not have an occupant
	 * or it cannot occupy an entity.
	 */
	private Combatant occupant;
	
	/**
	 * The set of items on this tile.
	 */
	private Catalog catalog;
	
	public Tile(char terr) {
		this.terrain = terr;
		
		//By default, a newly-created tile cannot be focused.
		this.isFocused = false;
		
		if(TerrainCharacterToTraits != null) {
			Triplet<String, String, Byte> traits = TerrainCharacterToTraits.get(terr);
			this.name = traits.getValue0();
			this.description = traits.getValue1();
			this.flags = traits.getValue2();
		}
		
		//No initial occupant.
		this.occupant = null;
		
		//Tiles start with no items, with the ability to gain more.
		this.catalog = new Catalog();
	}
	
	/**
	 * Loads the hashmap TerrainCharacterToTraits by scouring a file with the given
	 * file name and adding the key-value pairs to the hashmap.
	 * @param fileName The name of the file to find the key-value pairs in.
	 * @see #TerrainCharacterToTraits
	 */
	public static void loadTraitMapping(String fileName) {
		
		//Initializes the map if this is the first time loading.
		if(TerrainCharacterToTraits == null) {
			TerrainCharacterToTraits = new HashMap<Character, Triplet<String, String, Byte>>();
		}
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			char terr = ' ';
			String name = "";
			String desc = "";
			byte flags = 0b00000000;
			String currLine;
			boolean newFlag = true;
			
			while((currLine = br.readLine()) != null) {
				//Adds the association to the map. Clears flags and primes another entry.
				if(currLine.equals("!END")) {
					TerrainCharacterToTraits.put(terr, new Triplet<String, String, Byte>(name, desc, flags));
					flags = 0b00000000;
					newFlag = true;
				} else {
					if(newFlag) {
						int equalPos = currLine.indexOf('=');
						terr = currLine.charAt(0);
						name = currLine.substring(1, equalPos);
						desc = currLine.substring(equalPos + 1);
						newFlag = false;
					} else {
						switch(currLine.charAt(0)) {
							case 'T': //Traversable
								flags = (byte)(flags | 0b10000000);
						}
					}
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	//Accessors, Mutators
	public char getTerrain() { return terrain; }
	public String getName() { return name; }
	public String getDesc() { return description; }
	public Combatant getOccupant() { return occupant; }
	public boolean isFocused() { return isFocused; }
	public Catalog getCatalog() { return catalog; }
	
	/**
	 * Checks if the Traversible bit in the flags byte is on; if so, then the tile can be occupied.
	 * @return True if the tile can be occupied, false if not.
	 */
	public boolean canOccupy() {
		return (flags & 0b10000000) != 0;
	}
	
	/**
	 * Makes a tile focused when it was originally unfocused, or
	 * unfocused when it was originally focused.
	 */
	void toggleFocused() {
		isFocused = !isFocused;
	}
	
	/**
	 * Removes the occupant from a tile.
	 * @return The occupant.
	 */
	public Combatant vacateOccupant() {
		Combatant temp = occupant;
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
	public boolean fillOccupant(Combatant newOccupant) {
		//If the tile is can be traversed, and there is no occupant...
		if(canOccupy() && occupant == null) {
			occupant = newOccupant;
			return true;
		} else {
			return false;
		}
	}
}