package game;

import java.util.Random;

public final class Toolbox {
	private static Random r = new Random();
	
	public static int rollDice(int numOfDice, int numOfSides) {
		int sum = 0;
		for(int x = 0; x < numOfDice; x++) {
			sum += (r.nextInt(numOfSides) + 1);
		}
		return sum;
	}
	
	public static String pluralize() {
		return "Pending.";
	}
}
