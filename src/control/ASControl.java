package control;

//TODO LIST
/////////// THE NEW SYSTEM WORKS; RESET ALL KEYBINDS TO FIT WITH NEW SYSTEM, CONDENSE INTO ABSTRACT CLASS, AND DO FINAL TESTING.
//AI operations must return array of Events.
//Maybe add different methods for item select/tile select.
//Event queue testing, AI operation testing (NOT implementation)
//General cleanup, resolve TODOs.
//Persistent//Lag on startup - examine.


import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import comm.MessageManager;
import display.Display;
import display.DisplayKeyBindings;
import display.ImageAssets;
import entity.*;
import event.Event;
import event.EventQueue;
import event.Instruction;
import grid.*;
import item.Item;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {
	
	/**
	 * You!
	 */
	private static Player p1;

	/**
	 * The current grid on which the game currently operates.
	 */
	private static Grid grid;
	
	/**
	 * Manager for the message service.
	 */
	private static MessageManager mm;

	/**
	 * The queue of events to take place.
	 */
	private static EventQueue eq;

	/**
	 * An event that is pushed onto the event queue as a result of a key press.
	 */
	private static Event pendingEvent;

	public static void main(String args[]) {
		JFrame gameWindow = new JFrame("Asphodel Sky");
		gameWindow.setMinimumSize(new Dimension(1200, 900));
		gameWindow.setMaximumSize(new Dimension(1920, 1080));
		gameWindow.setUndecorated(true);
		gameWindow.setLocationRelativeTo(null); //Centers window
		
		Display game = new Display(1200, 900);
		
		gameWindow.add(game);

		//Initialize message manager.
		ExecutorService threadList = Executors.newFixedThreadPool(2);
		mm = new MessageManager(game.getFooter());
		threadList.execute(mm);

		//TODO: p1 must be instantiated before enemy map loading in order to ensure that p1 has id 0; fix.
		p1 = new Player("Place Holder", "Apprentice",  16, 22, game.getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		Item.loadItemMapping("map/item_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();

		//Initializing Event Queue.
		eq = new EventQueue();
		
		//PLAYGROUND TEMPORARY
		grid = new Grid(game.getHeader(), game.getFocus());

		grid.addCombatant(p1, 1, 1);

		grid.addItem("Cardiotic Fluid", 4, 4);
		grid.addItem("Solution of Finesse", 3, 4);
		grid.addItem("Solution of Finesse", 5, 5);

		eq.progressTimeBy(5, grid);

		grid.addCombatant("Khweiri Dervish", 6, 6);

		game.repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(game, grid, p1, mm, eq);
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
}
