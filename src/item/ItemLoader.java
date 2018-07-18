package item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public final class ItemLoader {

    /**
     * Map that maps the item's name to the item as an object.
     */
    private static Map<Integer, Item> itemIDToItemMap;

    private static Map<String, Integer> itemNameToItemIDMap;

    public static void loadItemEffectMapping(String fileName) {
        if(itemIDToItemMap == null) {
            itemIDToItemMap = new HashMap<>();
            itemNameToItemIDMap = new HashMap<>();
        } else {
            return;
        }

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
                    itemIDToItemMap.put(newestItem.getId(), newestItem);
                    itemNameToItemIDMap.put(name, newestItem.getId());
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

    public static Item getItemById(int id) {
        return itemIDToItemMap.get(id);
    }

    public static Item getItemByName(String name) {
        return itemIDToItemMap.get(itemNameToItemIDMap.get(name));
    }
}
