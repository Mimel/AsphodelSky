package entity;

/**
 * A class that contains combatants that utilize the "Flutter" AI: They randomly go places.
 * @author Matt Imel
 */
public class Flutter extends Combatant {
	
	public static final Combatant[] AI_FLUTTER = new Combatant[]{
		new Flutter("Flutter AI", "Dancer", 15)
	};
	
	Flutter(int max) {
		super(max);
	}
	
	Flutter(String name, String title, int max) {
		super(17, name, title, max);
	} 
}
