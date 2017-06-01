package entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Container for all enemy information. Distributes instances to other classes
 * via copy constructor and extracts from a hashmap which must be initialized.
 */
public final class EnemyGenerator {

    /**
     * The hashmap that contains all relationships between the name and the stats of the given Combatant.
     * These instances are not meant to be used; they must first be put through the Combatant copy constructor.
     */
    private static HashMap<String, Combatant> nameToCombatant;

    /**
     * Private constructor ensures no instantiation of this class.
     */
    private EnemyGenerator(){}

    public static Combatant getEnemyByName(String name) {
        Combatant c = nameToCombatant.get(name);

        if(c != null) {
            if(c instanceof MindlessAI) {
                return new MindlessAI((MindlessAI)c);
            } else if(c instanceof AnimalisticAI) {
                return new AnimalisticAI((AnimalisticAI)c);
            } else if(c instanceof UnderdevelopedAI) {
                return new UnderdevelopedAI((UnderdevelopedAI)c);
            } else if(c instanceof SapientAI) {
                return new SapientAI((SapientAI)c);
            } else if(c instanceof BrilliantAI) {
                return new BrilliantAI((BrilliantAI)c);
            }
        }

        return null;
    }

    public static void loadEnemyMapping(String fileName) {
        if(nameToCombatant == null) {
            nameToCombatant = new HashMap<String, Combatant>();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currLine;
            String name = "";
            String desc = "";

            int health = 0;
            int science = 0;

            int poise = 0;
            int subtlety = 0;
            int acumen = 0;
            int charisma = 0;
            int intuition = 0;

            int arrowPos = 0; //The position of the '>' key in each line.

            while((currLine = br.readLine()) != null) {
                arrowPos = currLine.indexOf('>');

                if(name.equals("")) { //Line after the intelligence descriptor, where name and desc are found.
                    name = currLine.substring(0, arrowPos);
                    desc = currLine.substring(arrowPos + 1);

                } else if(arrowPos == -1) { //The intelligence descriptor; Create new object based on character, add to map, and reset.
                    //Acts as a compact factory of sorts.
                    switch(currLine.charAt(0)) {
                        case 'M':
                            nameToCombatant.put(name, new MindlessAI(name, "", desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                            break;
                        case 'A':
                            nameToCombatant.put(name, new AnimalisticAI(name, "", desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                            break;
                        case 'U':
                            nameToCombatant.put(name, new UnderdevelopedAI(name, "", desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                            break;
                        case 'S':
                            nameToCombatant.put(name, new SapientAI(name, "", desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                            break;
                        case 'B':
                            nameToCombatant.put(name, new BrilliantAI(name, "", desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                            break;
                    }

                    name = "";
                    desc = "";

                } else {

                    int value = Integer.parseInt(currLine.substring(arrowPos + 1));

                    switch(currLine.substring(0, arrowPos)) {
                        case "HPP":
                            health = value;
                            break;
                        case "SPP":
                            science = value;
                            break;
                        case "PSE":
                            poise = value;
                            break;
                        case "SUB":
                            subtlety = value;
                            break;
                        case "ACU":
                            acumen = value;
                            break;
                        case "CHA":
                            charisma = value;
                            break;
                        case "ITT":
                            intuition = value;
                            break;
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}