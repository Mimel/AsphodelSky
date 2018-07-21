package item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * An item library containing all possible items in the game. Whenever an item is to be added to the game state,
 * it must be copied from this library.
 */
public class ItemLibrary {

    /**
     * Map that maps the item's name to the item as an object.
     */
    private final Map<String, Item> itemNameToItemMap;

    /**
     * Loads this library's item mapping from a given text file.
     * @param fileName The file name to get the item mapping from.
     */
    public ItemLibrary(String fileName) {
        itemNameToItemMap = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String name = "";
            ItemType type = ItemType.NONE;
            String vDesc = "";
            String uDesc = "";
            StringBuilder effects = new StringBuilder();

            String line;

            while((line = br.readLine()) != null) {
                if(name.equals("")) {
                    int tildeLoc = line.indexOf('~');
                    name = line.substring(0, tildeLoc);
                    type = ItemType.valueOf(line.substring(tildeLoc + 1).trim());
                } else if(vDesc.equals("")) {
                    vDesc = line;
                } else if(uDesc.equals("")) {
                    uDesc = line;
                } else if(line.equals("!END")) {
                    Item newestItem = new Item(name, type, vDesc, uDesc, effects.toString());
                    itemNameToItemMap.put(newestItem.getName(), newestItem);
                    name = "";
                    vDesc = "";
                    uDesc = "";
                    effects = new StringBuilder();
                } else {
                    effects.append(line);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a deep copy of the item in the library based on the name given.
     * @param name The name of the item to retrieve from the library.
     * @return The item with the given name if it exists in the library, false otherwise.
     */
    Item getItemByName(String name) {
        return new Item(itemNameToItemMap.get(name));
    }
}
