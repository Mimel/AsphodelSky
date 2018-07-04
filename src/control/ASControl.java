package control;

import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import comm.MessageManager;
import display.GameSession;
import display.DisplayKeyBindings;
import display.ImageAssets;
import entity.*;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.*;
import item.ItemLoader;
import item.ItemPromptLoader;
import skill.SkillLoader;

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
		
		GameSession game = new GameSession(1200, 900);
		
		gameWindow.add(game);
		
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
}
