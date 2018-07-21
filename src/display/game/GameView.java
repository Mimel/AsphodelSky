package display.game;

import comm.MessageManager;
import comm.SourceDescriptionTriplet;
import display.game.focus.GUIFocus;
import display.game.footer.FooterDialogue;
import display.game.footer.FooterMessageFeed;
import display.game.footer.FooterShortDescriptor;
import display.game.footer.GUIFooter;
import display.game.sidebar.GUISidebar;
import display.image.ImageAssets;
import display.music.AudioPlayer;
import entity.Player;
import event.EventQueue;
import event.InstructionSet;
import event.ResponseTable;
import grid.CompositeGrid;
import grid.Tile;
import item.ItemLibrary;
import item.ItemPromptLibrary;
import saveload.GridLoader;
import skill.SkillLibrary;

import java.awt.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameView extends GameViewObserver {

	private GUIFocus focus;

	private GUISidebar sidebar;

	private GUIFooter footer;

	private DisplayConfiguration currentConfig;

	private AudioPlayer player;

	GameView(int winWidth, int winHeight, AudioPlayer ap, GameManager gm) {
		this.setLayout(new BorderLayout());

		this.setBounds(0, 0, winWidth, winHeight);

		this.currentConfig = DisplayConfiguration.DEFAULT;

		this.player = ap;
		this.viewManager = gm;
		this.viewManager.addObserver(this);

		initializeGameSession();
	}

	DisplayConfiguration getConfig() {
		return currentConfig;
	}

	/**
	 * Switches the configuration of the display. The configuration determines the information that is
	 * displayed on the screen. When switching the state, the previous state is overridden.
	 * @param newConfig The new configuration to use.
	 */
	void switchState(DisplayConfiguration newConfig) {
		switch(newConfig) {
			case DIALOGUE:
				sidebar.switchToNoSelect();
				footer.switchToDialogue();
				break;
			case TILE_SELECT:
				sidebar.switchToNoSelect();
				footer.switchToSrcDesc();
				break;
			case SKILL_SELECT:
				sidebar.switchToSkillSelect();
				footer.switchToSrcDesc();
				break;
			case INVENTORY_SELECT:
				sidebar.switchToItemSelect();
				footer.switchToSrcDesc();
				break;
			case DEFAULT:
				sidebar.switchToNoSelect();
				footer.switchToMessages();
				break;
		}
		currentConfig = newConfig;
	}

	private void initializeGameSession() {
		MessageManager mm = new MessageManager();

		//Mapping/Images/Assets loading.
		ImageAssets iAssets = new ImageAssets();
		ItemPromptLibrary ipl = new ItemPromptLibrary("map/item_promptmap.dat");

		Tile.loadTraitMapping("map/terr_infomap.dat");

		ResponseTable rt = new ResponseTable("map/responsemap.dat");
		InstructionSet operations = new InstructionSet(rt);
		EventQueue eq = new EventQueue(operations);

		ItemLibrary il = new ItemLibrary("map/item_effectmap.dat");
		SkillLibrary sl = new SkillLibrary("map/skill_effectmap.dat");
		CompositeGrid model = new GridLoader("saves/1.asf", il, sl).loadGrid();

		Player p1 = (Player)model.getFocusedCombatant();
		SourceDescriptionTriplet sdp = new SourceDescriptionTriplet("", "", "");

		focus = new GUIFocus(0, 0, getWidth(), getHeight(), model, iAssets);
		sidebar = new GUISidebar(0, 0, 500, 800, p1, iAssets);
		footer = new GUIFooter(500, 0, 600, 200, new FooterMessageFeed(mm), new FooterShortDescriptor(sdp), new FooterDialogue());

		player.playSong("AttemptNo1.mp3");

		//new GridSaver(model).save();

		repaint();

		DisplayKeyBindings.initKeyBinds(this, model, mm, sdp, eq, ipl);
	}

	@Override
	protected void paintComponent(Graphics g) {
		long timeStart = System.currentTimeMillis();

		focus.paint(g);
		sidebar.paint(g);
		footer.paint(g);

		long timeEnd = System.currentTimeMillis();
		System.out.println("Paint time: " + (timeEnd - timeStart) + "ms.");
	}

	void pause() {
		viewManager.setFocusedPanel(GameSessionViewState.PAUSE_MENU_MAIN);
	}

	@Override
	public void update() {
		if(viewManager.isFocus(GameSessionViewState.GAME)) {
			enableInputs();
		} else {
			disableInputs();
		}
	}
}