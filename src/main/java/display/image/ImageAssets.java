package display.image;

import display.Texture;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that stores image data, as well as performs character-to-image conversions via mutliple hashmaps.
 * @author Matt Imel
 */
public class ImageAssets {
    public static final int SPRITE_DIMENSION_SM_PX = 48;
    public static final int SPRITE_DIMENSION_LG_PX = 96;

    private Map<Character, Integer> terrIDToTextureID;
    private Map<String, Integer> combatantNameToTextureID;

    public ImageAssets() {
        try {
            // Initialize tilesets.
            BufferedImage TILESET_TERR = ImageIO.read(new File("img/terrain/floors.png"));

            // Initialize hashmaps.
            terrIDToTextureID = new HashMap<>();
            combatantNameToTextureID = new HashMap<>();
            fillCharHashmap(terrIDToTextureID, TILESET_TERR, "map/terr_imagemap.dat");
            //fillStringHashmap(combatantNameToTextureID, "img/");

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
    private void fillCharHashmap(Map<Character, Integer> map, BufferedImage tileset, String fileName) {
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

                BufferedImage subImage = tileset.getSubimage(xCoord, yCoord, SPRITE_DIMENSION_LG_PX, SPRITE_DIMENSION_LG_PX);
                map.put(key.charAt(0), Texture.loadTexture(writeImageToBuffer(subImage, SPRITE_DIMENSION_LG_PX)));
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
    private void fillStringHashmap(Map<String, Image> map, BufferedImage tileset, String fileName, int dimension) {
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

    private ByteBuffer writeImageToBuffer(BufferedImage imageToWrite, int squareSize) {
        int[] pixels = imageToWrite.getRGB(0, 0, squareSize, squareSize, null, 0, squareSize);
        ByteBuffer imageRep = BufferUtils.createByteBuffer(pixels.length * Integer.BYTES).clear();

        for(int x = 0; x < imageRep.capacity() / Integer.BYTES; x++) {
            if(x % 500 == 0) {
                System.out.println("Red: " + ((pixels[x] & 0x00FF0000) >> 16));
            }
            imageRep.put((byte)((pixels[x] & 0x00FF0000) >> 16)); // Red for .png
            imageRep.put((byte)((pixels[x] & 0x0000FF00) >> 8)); // Green for .png
            imageRep.put((byte)(pixels[x] & 0x000000FF)); // Blue for .png
            imageRep.put((byte)((pixels[x] & 0xFF000000) >>> 24)); // Alpha for .png
        }

        return imageRep.flip();
    }
}
