package control;

import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import comm.MessageManager;
import display.Display;
import display.DisplayKeyBindings;
import display.ImageAssets;
import entity.*;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.*;
import item.Item;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {

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
		MessageManager mm = new MessageManager(game.getFooter());
		threadList.execute(mm);

		//TODO: p1 must be instantiated before enemy map loading in order to ensure that p1 has id 0; fix.
		Player p1 = new Player("Place Holder", "Apprentice", 16, 22, game.getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		Item.loadItemMapping("map/item_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		//Initializing SimpleEvent Queue.
		EventQueue eq = new EventQueue();
		
		//PLAYGROUND TEMPORARY
		CompositeGrid compositeGrid = new CompositeGrid(game.getFocus());

		compositeGrid.addCombatant(p1, 1, 1);
		compositeGrid.bindFocusToPlayer();

		eq.progressTimeBy(5, compositeGrid);

		game.repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(game, compositeGrid, p1, mm, eq);
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
}
