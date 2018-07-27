package display.image;

import display.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * A utility class that stores image data, as well as performs character-to-image conversions via mutliple hashmaps.
 * @author Matt Imel
 */
public class ImageAssets {
    public static final int SPRITE_DIMENSION_SM_PX = 48;
    public static final int SPRITE_DIMENSION_LG_PX = 96;

    private HashMap<Character, Integer> terrIDToTextureID;

    public ImageAssets() {
        try {
            // Initialize tilesets.
            BufferedImage TILESET_TERR = ImageIO.read(new File("img/terrain/floors.png"));

            // Initialize hashmaps.
            terrIDToTextureID = new HashMap<>();
            fillCharHashmap(terrIDToTextureID, "img/terrain/", "map/terr_imagemap.dat");

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Gets the image in the tileset that is bound to the given character.
     * @param key The character of the image to retrieve.
     * @return The image bound to the key.
     */
    public Integer getTerrainTextureID(char key) {
        return terrIDToTextureID.get(key);
    }

    /**
     * Fills a given hashmap with keys (Specified characters) and values (x, y margins) by parsing a
     * text file with the given information.
     *
     * All key-value pairs listed in the .dat file must follow the given parsing specification:
     * [Character][X-margin],[Y-margin]\n
     *
     * @param map The given hashmap to fill.
     * @param fileName The name of the text file to read.
     */
    private void fillCharHashmap(HashMap<Character, Integer> map, String pathToImageFiles, String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String pair;
            char key;
            String imageFile;

            while((pair = br.readLine()) != null) {
                key = pair.charAt(0);
                imageFile = pair.substring(1);
                map.put(key, Texture.loadTexture(pathToImageFiles + imageFile));
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
    private void fillStringHashmap(HashMap<String, Image> map, BufferedImage tileset, String fileName, int dimension) {
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

                map.put(key, tileset.getSubimage(xCoord, yCoord, dimension, dimension));
            }

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
