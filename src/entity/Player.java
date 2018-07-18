package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

/**
 * The player (You). 
 * @author Matt Imel
 */
public class Player extends Combatant {
	
	public Player(String name, String title, int health, int science) {
		super(name, title, "", health, science);
	}

	/**
	 * The player should never act. This method returns, doing nothing.
	 * @param opai The operation to perform on this combatant.
	 */
	@Override
	public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
		return null;
	}
}
