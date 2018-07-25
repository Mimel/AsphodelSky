package item;

import display.game.DisplayPrompt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A library that binds item names to the prompts that must be added to the prompt queue
 * when they are used.
 *
 * @see DisplayPrompt
 * @see display.game.PromptManager
 */
public class ItemPromptLibrary {

    /**
     * A library that binds item names to the prompts that must be added to the prompt queue
     * when they are used.
     */
    private final HashMap<String, ArrayList<DisplayPrompt>> itemToPromptList;

    /**
     * Loads the library mapping from a given text file.
     * @param fileName The text file to load the library mapping from.
     */
    public ItemPromptLibrary(String fileName) {
        itemToPromptList = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currLine;
            char firstCharacter;
            String itemName = "";
            ArrayList<DisplayPrompt> prompts = new ArrayList<>();

            while((currLine = br.readLine()) != null) {
                firstCharacter = currLine.charAt(0);
                if(firstCharacter == '-') {
                    prompts.add(DisplayPrompt.valueOf(currLine.substring(1)));
                } else if(firstCharacter == '!') {
                    itemToPromptList.put(itemName, prompts);

                    itemName = "";
                    prompts = new ArrayList<>();
                } else {
                    itemName = currLine;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the item prompts from a given item name.
     * @param itemName The name of the item.
     * @return The prompts that that item usage requires, or null if either none exist or the item name doesn't exist.
     */
    public ArrayList<DisplayPrompt> getItemPrompts(String itemName) {
        return itemToPromptList.get(itemName);
    }
}
