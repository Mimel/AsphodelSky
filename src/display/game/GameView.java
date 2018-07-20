package display.game;

import comm.MessageManager;
import comm.SourceDescriptionPair;
import display.game.focus.GUIFocus;
import display.game.footer.FooterDialogue;
import display.game.footer.FooterMessageFeed;
import display.game.footer.FooterShortDescriptor;
import display.game.footer.GUIFooter;
import display.game.sidebar.GUISidebar;
import display.image.ImageAssets;
import display.music.AudioPlayer;
import entity.EnemyGenerator;
import entity.Player;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.CompositeGrid;
import grid.Tile;
import item.ItemLoader;
import item.ItemPromptLoader;
import saveload.GridLoader;
import saveload.GridSaver;
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

		//Player p1 = new Player("Place Holder", "Apprentice", 1000, 22);

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		ItemLoader.loadItemEffectMapping("map/item_effectmap.dat");
		ItemPromptLoader.loadItemPromptMapping("map/item_promptmap.dat");
		SkillLibrary.initializeSkillMap("map/skill_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		EventQueue eq = new EventQueue();

		CompositeGrid model = new GridLoader("saves/1.asf").loadGrid();
		Player p1 = (Player)model.getFocusedCombatant();
		SourceDescriptionPair sdp = new SourceDescriptionPair("", "");

		focus = new GUIFocus(0, 0, getWidth(), getHeight(), model);
		sidebar = new GUISidebar(0, 0, 500, 800, p1);
		footer = new GUIFooter(500, 0, 600, 200, new FooterMessageFeed(mm), new FooterShortDescriptor(sdp), new FooterDialogue());

		player.playSong("AttemptNo1.mp3");

		//new GridSaver(model).save();

		repaint();

		DisplayKeyBindings.initKeyBinds(this, model, p1, mm, sdp, eq);
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