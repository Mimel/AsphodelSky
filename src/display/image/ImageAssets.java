package display.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * A utility class that stores image data, as well as performs character-to-image conversions via mutliple hashmaps.
 * @author Matt Imel
 */
public final class ImageAssets {
	public static final int SPRITE_DIMENSION_PX = 48;
	
	private static HashMap<Character, Image> terrIdToImage;
	private static HashMap<String, Image> itemIdToImage;
	private static HashMap<String, Image> skillIdToImage;
	private static HashMap<String, Image> charIdToImage;
	private static HashMap<Character, Image> miscIdToImage;

	/**
	 * Private constructor used to prevent instantiation of this utility class.
	 */
	private ImageAssets() {}
	
	/**
	 * Forces class initialization. Must be called anywhere at least once in order to begin initialization
	 * and use the class assets. May be a touch hacky in implementation, and will possibly be temporary.
	 */
	public static void loadImageMapping() {
		try {
			// Initialize tilesets.
			BufferedImage TILESET_TERR = ImageIO.read(new File("img/terrain/terraintileset.png"));
			BufferedImage TILESET_ITEM = ImageIO.read(new File("img/item/vials.png"));
			BufferedImage TILESET_SKLL = ImageIO.read(new File("img/skill/tests.png"));
			BufferedImage TILESET_CHAR = ImageIO.read(new File("img/enemy/enemies.png"));
			BufferedImage TILESET_MISC = ImageIO.read(new File("img/misc/misc.png"));

			// Initialize hashmaps.
			terrIdToImage = new HashMap<>();
			fillCharHashmap(terrIdToImage, TILESET_TERR, "map/terr_imagemap.dat");
			itemIdToImage = new HashMap<>();
			fillStringHashmap(itemIdToImage, TILESET_ITEM, "map/item_imagemap.dat");
			skillIdToImage = new HashMap<>();
			fillStringHashmap(skillIdToImage, TILESET_SKLL, "map/skill_imagemap.dat");
			charIdToImage = new HashMap<>();
			fillStringHashmap(charIdToImage, TILESET_CHAR, "map/char_imagemap.dat");
			miscIdToImage = new HashMap<>();
			fillCharHashmap(miscIdToImage, TILESET_MISC, "map/misc_imagemap.dat");

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Gets the image in the tileset that is bound to the given character.
	 * @param key The character of the image to retrieve.
	 * @return The image bound to the key.
	 */
	public static Image getTerrainImage(char key) {
		return terrIdToImage.get(key);
	}
	
	public static Image getItemImage(String key) {
		return itemIdToImage.get(key);
	}

	public static Image getSkillImage(String key) {
	    return skillIdToImage.get(key);
    }
	
	public static Image getCharImage(String key) {
		return charIdToImage.get(key);
	}

	public static Image getMiscImage(char key) { return miscIdToImage.get(key); }
	
	/**
	 * Fills a given hashmap with keys (Specified characters) and values (x, y margins) by parsing a 
	 * text file with the given information.
	 * 
	 * All key-value pairs listed in the .dat file must follow the given parsing specification:
	 * [Character][X-margin],[Y-margin]\n
	 * 
	 * @param map The given hashmap to fill.
	 * @param tileset The image to use to represent the tileset. All coordinates read in from the text file will be
	 *                based off of this file.
	 * @param fileName The name of the text file to read.
	 */
	private static void fillCharHashmap(HashMap<Character, Image> map, BufferedImage tileset, String fileName) {
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
			String pair;
			char key;
			int xCoord;                                          
			int yCoord;                                          
			int commaLoc;                                        
			
			while((pair = br.readLine()) != null) {
				key = pair.charAt(0);
				if((commaLoc = pair.indexOf(',')) != -1) {
					xCoord = Integer.parseInt(pair.substring(1, commaLoc));
					yCoord = Integer.parseInt(pair.substring(commaLoc + 1));
				} else {
					xCoord = -1;
					yCoord = -1;
				}
				map.put(key, tileset.getSubimage(xCoord, yCoord, SPRITE_DIMENSION_PX, SPRITE_DIMENSION_PX));
			}
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	/**
	 * Fills a given hashmap with keys (Specified string) and values (x, y margins) by parsing a 
	 * text file with the given information.
	 * 
	 * All key-value pairs listed in the .dat file must follow the given parsing specification:
	 * [String]@[X-margin],[Y-margin]\n
	 * 
	 * @param map The given hashmap to fill.
	 * @param tileset The image to use to represent the tileset. All coordinates read in from the text file will be
	 *                based off of this file.
	 * @param fileName The name of the text file to read.
	 */
	private static void fillStringHashmap(HashMap<String, Image> map, BufferedImage tileset, String fileName) {
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
			String pair;
			String key;
			int xCoord;                                          
			int yCoord;
			int atLoc;
			int commaLoc;                                        
			
			while((pair = br.readLine()) != null) {
				
				if((atLoc = pair.indexOf('@')) != -1) {
					key = pair.substring(0, atLoc);
				} else {
					continue;
				}
				
				if((commaLoc = pair.indexOf(',')) != -1) {
					xCoord = Integer.parseInt(pair.substring(atLoc + 1, commaLoc));
					yCoord = Integer.parseInt(pair.substring(commaLoc + 1));
				} else {
					continue;
				}
				
				map.put(key, tileset.getSubimage(xCoord, yCoord, SPRITE_DIMENSION_PX, SPRITE_DIMENSION_PX));
			}
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
}
