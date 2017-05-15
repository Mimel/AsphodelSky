package entity;

/**
 * A class that contains combatants that utilize the "Float" AI: They stay a fixed position away from the player.
 * @author Matt Imel
 */
public class Kite extends Combatant {
	public static final Combatant[] AI_KITE = new Combatant[]{
		new Flutter("Kite AI", "Kite", 15)
	};
	
	Kite(int max) {
		super(max);
	}
	
	Kite(String name, String title, int max) {
		super(17, name, title, max);
	}
}
