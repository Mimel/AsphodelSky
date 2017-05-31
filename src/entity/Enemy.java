package entity;

import java.util.HashMap;

/**
 * Container for all enemy information. Distributes instances to other classes
 * via copy constructor and extracts from a hashmap which must be initialized.
 * TODO rename
 */
public class Enemy {

    /**
     * The hashmap that contains all relationships between the name and the stats of the given Combatant.
     * These instances are not meant to be used; they must first be put through the Combatant copy constructor.
     */
    private static HashMap<String, Combatant> nameToCombatant;

    //TODO Consider using visitor pattern for AI.
}

/**
 * This is how the world ends; not with a bang, but with a whisper. TODO delete.
 *
 */