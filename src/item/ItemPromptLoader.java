package item;

import display.DisplayPrompt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class ItemPromptLoader {
    private static HashMap<String, ArrayList<DisplayPrompt>> itemToPromptList;

    private ItemPromptLoader() {}

    public static ArrayList<DisplayPrompt> getItemPrompts(String itemName) {
        return itemToPromptList.get(itemName);
    }

    public static void loadItemPromptMapping(String fileName) {
        if(itemToPromptList == null) {
            itemToPromptList = new HashMap<>();
        }

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
}
