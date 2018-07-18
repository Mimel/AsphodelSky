package display.game;

import comm.MessageManager;
import comm.SourceDescriptionPair;
import display.image.ImageAssets;
import display.music.AudioPlayer;
import entity.Combatant;
import entity.EnemyGenerator;
import entity.Player;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.CompositeGrid;
import grid.Tile;
import item.ItemLoader;
import item.ItemPromptLoader;
import skill.SkillLoader;

import java.awt.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameView extends GameViewObserver {

	private CompositeGrid model;

	private Point trueOrigin;

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

		this.trueOrigin = new Point(0, 0);
		recalculateTrueOrigin();

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

		Player p1 = new Player("Place Holder", "Apprentice", 1000, 22);

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		ItemLoader.loadItemEffectMapping("map/item_effectmap.dat");
		ItemPromptLoader.loadItemPromptMapping("map/item_promptmap.dat");
		SkillLoader.initializeSkillMap("map/skill_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		p1.getInventory().insertItem(ItemLoader.getItemById(0), 1);
		p1.getSkillSet().addSkill(SkillLoader.getSkillByID(0));
		p1.getSkillSet().addSkill(SkillLoader.getSkillByID(1));

		//Initializing SimpleEvent Queue.
		EventQueue eq = new EventQueue();

		//PLAYGROUND TEMPORARY
		model = new CompositeGrid();
		SourceDescriptionPair sdp = new SourceDescriptionPair("", "");

		sidebar = new GUISidebar(0, 0, 500, 800, p1);
		footer = new GUIFooter(500, 0, 600, 200, new FooterMessageFeed(mm), new FooterShortDescriptor(sdp), new FooterDialogue());

		model.addCombatant(p1, 1, 1);
		model.bindTo(0);

		model.addItem(0, 4, 4);
		model.addItem(1, 4, 8);
		for(int x = 0; x < 10; x++) {
			model.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), (3 + 7 * x) % 10, 3 + x);
		}

		model.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), 19, 0);

		model.addCombatant(EnemyGenerator.getEnemyByName("Bilge Rat"), 12, 4);
		model.addCombatant(EnemyGenerator.getEnemyByName("Fireball"), 13, 4);

		player.playSong("AttemptNo1.mp3");


		repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(this, model, p1, mm, sdp, eq);
	}

	@Override
	protected void paintComponent(Graphics g) {
		long timeStart = System.currentTimeMillis();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		recalculateTrueOrigin();
		int testx = trueOrigin.x;
		int testy = trueOrigin.y;
		Point focus = new Point(model.getFocus().x(), model.getFocus().y());
		Point start = new Point(focus.x - widthInTiles() / 2, focus.y - heightInTiles() / 2);

		if(start.x < 0) { start.move(0, start.y); testx = 0; }
		else if(start.x > model.getNumberOfColumns() - visibleColumns()) {
			if(model.getNumberOfColumns() <= widthInTiles()) {
				start.move(0, start.y);
				testx = getWidth() - (widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

				if(getWidth() > model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX) {
					testx = 0;
				}
			} else {
				start.move(model.getNumberOfColumns() - visibleColumns(), start.y);
				testx *= 2;
			}
		}

		if(start.y < 0) { start.move(start.x, 0); testy = 0; }
		else if(start.y > model.getNumberOfRows() - visibleRows()) {
			if(model.getNumberOfRows() <= heightInTiles()) {
				start.move(start.x, 0);
				testy = getHeight() - (heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX);

				if(getHeight() > model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX) {
					testy = 0;
				}
			} else {
				start.move(start.x, model.getNumberOfRows() - visibleRows());
				testy *= 2;
			}
		}

		int xMargin = 0;
		int yMargin = 0;

		if(widthInTiles() > model.getNumberOfColumns()) {
			xMargin = (getWidth() - (model.getNumberOfColumns() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
		}

		if(heightInTiles() > model.getNumberOfRows()) {
			yMargin = (getHeight() - (model.getNumberOfRows() * ImageAssets.SPRITE_DIMENSION_PX)) / 2;
		}

		int startingX = start.x;

		Combatant currentCombatant;
		for(int y = testy + yMargin; y < getHeight() && start.y < model.getNumberOfRows(); y += ImageAssets.SPRITE_DIMENSION_PX) {
			for(int x = testx + xMargin; x < getWidth() && start.x < model.getNumberOfColumns(); x += ImageAssets.SPRITE_DIMENSION_PX) {
				g.drawImage(ImageAssets.getTerrainImage(model.getTileAt(start.x, start.y).getTerrain()), x, y, null);

				if((currentCombatant = model.getCombatantAt(start.x, start.y)) != null) {
					if(currentCombatant.getId() == Player.PLAYER_ID) {
						g.setColor(new Color(200, 100, 30));
						g.fillRect(x, y, ImageAssets.SPRITE_DIMENSION_PX, ImageAssets.SPRITE_DIMENSION_PX);
					} else {
						g.drawImage(ImageAssets.getCharImage(currentCombatant.getName()), x, y, null);
					}
				}

				if(model.getItemsOnTile(start.x, start.y) != null && !model.getItemsOnTile(start.x, start.y).isEmpty()) {
					g.drawImage(ImageAssets.getItemImage(model.getItemsOnTile(start.x, start.y).getFocusedItem().getName()), x, y, null);
				}

				start.translate(1, 0);
			}
			start.move(startingX, start.y + 1);

		}

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

	private int widthInTiles() {
		return (int)Math.ceil((double)getWidth() / ImageAssets.SPRITE_DIMENSION_PX);
	}

	private int heightInTiles() {
		return (int)Math.ceil((double)getHeight() / ImageAssets.SPRITE_DIMENSION_PX);
	}

	private int visibleColumns() {
		if(widthInTiles() % 2 == 0) {
			return widthInTiles() + 1;
		} else {
			return widthInTiles();
		}
	}

	private int visibleRows() {
		if(heightInTiles() % 2 == 0) {
			return heightInTiles() + 1;
		} else {
			return heightInTiles();
		}
	}

	private void recalculateTrueOrigin() {
		int trueXStart = -(((widthInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - getWidth()) / 2);
		int trueYStart = -(((heightInTiles() * ImageAssets.SPRITE_DIMENSION_PX) - getHeight()) / 2);

		if(widthInTiles() % 2 == 0) {
			trueXStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
		}

		if(heightInTiles() % 2 == 0) {
			trueYStart -= (ImageAssets.SPRITE_DIMENSION_PX / 2);
		}
		trueOrigin.setLocation(trueXStart, trueYStart);
	}
}