package entity;

/**
 * A class that contains combatants that utilize the "Rush" AI: They charge the opponent.
 * @author Matt Imel
 */
public class Rush extends Combatant {
	public static final Combatant[] AI_RUSH = new Combatant[]{
		new Flutter("Rush AI", "Charger", 15)
	};
	
	Rush(int max) {
		super(max);
	}
	
	Rush(String name, String title, int max) {
		super(17, name, title, max);
	}
}
