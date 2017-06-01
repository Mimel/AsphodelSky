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

    public void loadEnemyMapping(String fileName) {
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

            while((currLine = br.readLine()) != null) {
                if(name.equals("")) {
                    name = currLine;
                } else if(desc.equals("")) {
                    desc = currLine;
                } else if(currLine.equals("END")) { //Reset
                    nameToCombatant.put(name, new MindlessAI(17, name, desc, health, 0, science, poise, subtlety, acumen, charisma, intuition));
                    name = "";
                    desc = "";
                } else { //Stat detailing
                    String[] stats = currLine.split(" ");
                    if(stats.length >= 7)  { //There are currently 7 stats TODO This is ridiculously assumptive. Revise.
                        health = Integer.parseInt(stats[0]);
                        science = Integer.parseInt(stats[1]);

                        poise = Integer.parseInt(stats[2]);
                        subtlety = Integer.parseInt(stats[3]);
                        acumen = Integer.parseInt(stats[4]);
                        charisma = Integer.parseInt(stats[5]);
                        intuition = Integer.parseInt(stats[6]);
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}