package entity;

import event.*;
import event.flag.Flag;
import event.flag.FlagRedirectLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    /**
     * Returns a copy of an enemy in the mapping by name.
     * @param name The name of the combatant to search for.
     * @return A copy of the combatant if the name is present, or null if not.
     */
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

    /**
     * Loads the entire map of names to enemy stats, given a specific file name.
     * @param fileName The .dat file to look up, containing all mapping information.
     */
    public static void loadEnemyMapping(String fileName) {
        if(nameToCombatant == null) {
            nameToCombatant = new HashMap<>();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currLine;
            char firstCharacter;
            Combatant newCombatant = null;
            Flag currentFlag = null;

            while((currLine = br.readLine()) != null) {
                firstCharacter = currLine.charAt(0);
                if(firstCharacter == '$') {
                    switch(currLine.charAt(1)) {
                        case 'M':
                            newCombatant = new MindlessAI();
                            break;
                        case 'A':
                            newCombatant = new AnimalisticAI();
                            break;
                        case 'U':
                            newCombatant = new UnderdevelopedAI();
                            break;
                        case 'S':
                            newCombatant = new SapientAI();
                            break;
                        case 'B':
                            newCombatant = new BrilliantAI();
                            break;
                    }
                } else if(firstCharacter == '!') {
                    int slashLoc = currLine.indexOf('/');
                    String opTrigger = currLine.substring(1, slashLoc);
                    String responseState = currLine.substring(slashLoc + 1);
                    currentFlag = Flag.determineFlag(Opcode.valueOf(opTrigger), FlagType.valueOf(responseState));
                } else if(firstCharacter == '-') {
                    if(currentFlag != null) {
                        newCombatant.addToFlagList(currentFlag);
                        currentFlag = null;
                    }
                    nameToCombatant.put(newCombatant.getName(), newCombatant);
                } else if(firstCharacter == ' ') {
                    if(currentFlag != null) {
                        SimpleEvent trigger = SimpleEvent.interpretEvent(currLine.substring(1, currLine.indexOf('@')).trim());
                        trigger.getData().setCasterIDTo(newCombatant.getId());
                        FlagRedirectLocation loc = FlagRedirectLocation.valueOf(currLine.substring(currLine.indexOf('@') + 1));
                        currentFlag.addEventToFlag(trigger, loc);
                    }
                } else {
                    int equalSignLoc = currLine.indexOf('=');
                    String fieldToSet = currLine.substring(0, equalSignLoc);
                    String valueOfField = currLine.substring(equalSignLoc + 1);

                    assignToField(newCombatant, fieldToSet, valueOfField);
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void assignToField(Combatant newCombatant, String setterName, String valueOfField) {
        assert (newCombatant != null);

        Method setter;
        try {
            if(isStringNumeric(valueOfField)) {
                setter = newCombatant.getClass().getMethod(setterName, int.class);
                setter.invoke(newCombatant, Integer.parseInt(valueOfField));
            } else {
                setter = newCombatant.getClass().getMethod(setterName, String.class);
                setter.invoke(newCombatant, valueOfField);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static boolean isStringNumeric(String input) {
        for(char character : input.toCharArray()) {
            if(!Character.isDigit(character)) {
                return false;
            }
        }

        return true;
    }
}