package display;

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
	private static final int SPRITE_DIMENSION_PX;
	
	private static HashMap<Character, Image> terrIdToImage;
	private static HashMap<String, Image> itemIdToImage;
	private static HashMap<String, Image> charIdToImage;
	private static HashMap<Character, Image> miscIdToImage;
	
	static {
		SPRITE_DIMENSION_PX = 48;
		try {
			// Initialize tilesets.
			BufferedImage TILESET_TERR = ImageIO.read(new File("img/terrain/terraintileset.png"));
			BufferedImage TILESET_ITEM = ImageIO.read(new File("img/item/vials.png"));
			BufferedImage TILESET_CHAR = ImageIO.read(new File("img/enemy/enemies.png"));
			BufferedImage TILESET_MISC = ImageIO.read(new File("img/misc/selector_generic.png"));
			
			// Initialize hashmaps.
			terrIdToImage = new HashMap<Character, Image>();
			fillCharHashmap(terrIdToImage, TILESET_TERR, "img/terrain/terrmap.dat");
			itemIdToImage = new HashMap<String, Image>();
			fillStringHashmap(itemIdToImage, TILESET_ITEM, "img/item/itemmap.dat");
			charIdToImage = new HashMap<String, Image>();
			fillStringHashmap(charIdToImage, TILESET_CHAR, "img/enemy/charmap.dat");
			//Incomplete TODO
			miscIdToImage = new HashMap<Character, Image>();
			fillCharHashmap(miscIdToImage, TILESET_MISC, "img/misc/miscmap.dat");
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Private constructor used to prevent instantiation of this utility class.
	 */
	private ImageAssets() {}
	
	/**
	 * Forces class initialization. Must be called anywhere at least once in order to begin initialization
	 * and use the class assets. May be a touch hacky in implementation, and will possibly be temporary.
	 */
	public static void load() {}
	
	/**
	 * Gets the image in the tileset that is bound to the given character.
	 * @param key The character of the image to retrieve.
	 * @return The image bound to the key.
	 */
	static Image getTerrainImage(char key) {
		return terrIdToImage.get(key);
	}
	
	static Image getItemImage(String key) {
		return itemIdToImage.get(key);
	}
	
	static Image getCharImage(String key) {
		return charIdToImage.get(key);
	}
	
	/**
	 * Fills a given hashmap with keys (Specified characters) and values (x, y margins) by parsing a 
	 * text file with the given information.
	 * 
	 * All key-value pairs listed in the .dat file must follow the given parsing specification:
	 * [Character][X-margin],[Y-margin]\n
	 * 
	 * @param map The given hashmap to fill.
	 * @param textFile The text file location to find the pairing data.
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
	 * @param textFile The text file location to find the pairing data.
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
