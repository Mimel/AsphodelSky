package game;

import java.util.Random;

public final class Toolbox {
	private static Random r = new Random();
	
	/**
	 * 
	 * @param numOfDice
	 * @param numOfSides
	 * @return
	 */
	public static int rollDice(int numOfDice, int numOfSides) {
		int sum = 0;
		for(int x = 0; x < numOfDice; x++) {
			sum += (r.nextInt(numOfSides) + 1);
		}
		return sum;
	}
	
	public static String pluralize(int number) {
		if(number != 1) {
			return "s";
		} else {
			return "";
		}
	}
	
	public static String pluralize(String base, int number) {
		if(number != 1) {
			return base + "s";
		} else {
			return base;
		}
	}
}
