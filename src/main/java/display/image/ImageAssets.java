package display.image;

import display.Texture;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

/**
 * A utility class that stores image data, as well as performs character-to-image conversions via mutliple hashmaps.
 * @author Matt Imel
 */
public class ImageAssets {
    public static final int SPRITE_DIMENSION_SM_PX = 48;
    public static final int SPRITE_DIMENSION_LG_PX = 96;

    private Map<Character, Integer> terrIDToTextureID;
    private Map<String, Integer> combatantNameToTextureID;
    private Map<String, Integer> sm_itemNameToTextureID;
    private Map<String, Integer> lg_itemNameToTextureID;
    private Map<Character, Integer> sm_miscRepToTextureID;
    private Map<Character, Integer> lg_miscRepToTextureID;

    private List<Integer> allTextureIDs;

    private int playerImage;
    private int defaultImage;

    public ImageAssets() {
        try {
            allTextureIDs = new LinkedList<>();

            // Initialize tilesets.
            BufferedImage TILESET_TERR = ImageIO.read(new File("img/terrain/floors.png"));
            BufferedImage TILESET_COMBATANT = ImageIO.read(new File("img/enemy/entities.png"));
            BufferedImage TILESET_SM_ITEM = ImageIO.read(new File("img/item/vials.png"));
            BufferedImage TILESET_LG_ITEM = ImageIO.read(new File("img/item/items.png"));
            BufferedImage TILESET_SM_MISC = ImageIO.read(new File("img/misc/misc.png"));

            // Initialize hashmaps.
            terrIDToTextureID = new HashMap<>();
            combatantNameToTextureID = new HashMap<>();
            sm_itemNameToTextureID = new HashMap<>();
            lg_itemNameToTextureID = new HashMap<>();
            sm_miscRepToTextureID = new HashMap<>();
            lg_miscRepToTextureID = new HashMap<>();

            // Fill hashmaps with tileset data.
            fillCharHashmap(terrIDToTextureID, TILESET_TERR, "map/terr_imagemap.dat", SPRITE_DIMENSION_LG_PX);
            fillStringHashmap(combatantNameToTextureID, TILESET_COMBATANT, "map/char_imagemap.dat", SPRITE_DIMENSION_LG_PX);
            fillStringHashmap(sm_itemNameToTextureID, TILESET_SM_ITEM, "map/item_sm_imagemap.dat", SPRITE_DIMENSION_SM_PX);
            fillStringHashmap(lg_itemNameToTextureID, TILESET_LG_ITEM, "map/item_lg_imagemap.dat", SPRITE_DIMENSION_LG_PX);
            fillCharHashmap(sm_miscRepToTextureID, TILESET_SM_MISC, "map/misc_imagemap.dat", SPRITE_DIMENSION_SM_PX);

            // Load single images.
            playerImage = Texture.loadTexture(writeImageToBuffer(ImageIO.read(new File("img/enemy/characters.png")), SPRITE_DIMENSION_LG_PX), SPRITE_DIMENSION_LG_PX);
            defaultImage = Texture.loadTexture(writeImageToBuffer(ImageIO.read(new File("img/default/default.png")), SPRITE_DIMENSION_LG_PX), SPRITE_DIMENSION_LG_PX);
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
        if(terrIDToTextureID.containsKey(key)) {
            return terrIDToTextureID.get(key);
        } else {
            return defaultImage;
        }
    }

    public Integer getCombatantTextureID(String key) {
        if(combatantNameToTextureID.containsKey(key)) {
            return combatantNameToTextureID.get(key);
        } else {
            return defaultImage;
        }
    }

    public Integer getSmallItemTextureID(String key) {
        return sm_itemNameToTextureID.get(key);
    }

    public Integer getLargeItemTextureID(String key) {
        if(lg_itemNameToTextureID.containsKey(key)) {
            return lg_itemNameToTextureID.get(key);
        } else {
            return defaultImage;
        }
    }

    public Integer getSmallMiscTextureID(char key) {
        return sm_miscRepToTextureID.get(key);
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
    private void fillCharHashmap(Map<Character, Integer> map, BufferedImage tileset, String fileName, int squareSize) {
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

                BufferedImage subImage = tileset.getSubimage(xCoord, yCoord, squareSize, squareSize);
                int texture = Texture.loadTexture(writeImageToBuffer(subImage, squareSize), squareSize);

                map.put(key.charAt(0), texture);
                allTextureIDs.add(texture);
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
    private void fillStringHashmap(Map<String, Integer> map, BufferedImage tileset, String fileName, int squareSize) {
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

                BufferedImage subImage = tileset.getSubimage(xCoord, yCoord, squareSize, squareSize);
                int texture = Texture.loadTexture(writeImageToBuffer(subImage, squareSize), squareSize);

                map.put(key, texture);
                allTextureIDs.add(texture);
            }

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private ByteBuffer writeImageToBuffer(BufferedImage imageToWrite, int squareSize) {
        int[] pixels = imageToWrite.getRGB(0, 0, squareSize, squareSize, null, 0, squareSize);
        ByteBuffer imageRep = BufferUtils.createByteBuffer(pixels.length * Integer.BYTES).clear();

        for(int x = 0; x < imageRep.capacity() / Integer.BYTES; x++) {
            imageRep.put((byte)((pixels[x] & 0x00FF0000) >> 16)); // Red for .png
            imageRep.put((byte)((pixels[x] & 0x0000FF00) >> 8)); // Green for .png
            imageRep.put((byte)(pixels[x] & 0x000000FF)); // Blue for .png
            imageRep.put((byte)((pixels[x] & 0xFF000000) >>> 24)); // Alpha for .png
        }

        return imageRep.flip();
    }

    public void deleteAllTextures() {
        for(int x : allTextureIDs) {
            glDeleteTextures(x);
        }
    }
}
