package display;

import comm.MessageManager;
import entity.Combatant;
import entity.Player;
import event.EventQueue;
import event.MacroOperation;
import grid.Grid;
import grid.Tile;
import item.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Owner on 7/11/2017.
 */
public class DisplayKeyBindings {
    /**
     * Creates keybinds for the game.
     * @param game The GUI.
     */
    public static void initKeyBinds(Display game, Grid grid, Player p1, MessageManager mm, EventQueue eq) {
        Action exitProgram = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        Action move = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                int xOffset = 0;
                int yOffset = 0;

                switch(e.getActionCommand().charAt(0)) {
                    case 'a': //W
                        xOffset = -1;
                        break;
                    case 'w': //N
                        yOffset = -1;
                        break;
                    case 'd': //E
                        xOffset = 1;
                        break;
                    case 'x': //S
                        yOffset = 1;
                        break;
                    case 'q': //NW
                        xOffset = -1;
                        yOffset = -1;
                        break;
                    case 'e': //NE
                        xOffset = 1;
                        yOffset = -1;
                        break;
                    case 'z': //SW
                        xOffset = -1;
                        yOffset = 1;
                        break;
                    case 'c': //SE
                        xOffset = 1;
                        yOffset = 1;
                        break;
                }

                if(game.getConfig() == DisplayConfiguration.INVENTORY_SELECT) {

                    //Moves the cursor over the inventory. TODO: Magic.
                    if(p1.getInventory().setFocus(xOffset * 3 + yOffset)) {
                        p1.updatePlayer();
                        mm.loadSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
                    }

                } else if(game.getConfig() == DisplayConfiguration.DEFAULT) {

                    //Moves the player.
                    grid.moveCombatant(0, grid.getXOfCombatant(0) + xOffset, grid.getYOfCombatant(0) + yOffset);
                    eq.progressTimeBy(1, grid);

                } else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {

                    //Moves the crosshair.
                    grid.switchFocus(xOffset, yOffset);

                    //The following if-else chain searches a tile by order of priority; First for occupants, then items, then floor features, then floor types.
                    if (grid.getFocusedTile().getOccupant() != null) {
                        //If the crosshair overlaps an occupant, prints their name, title, and description to the Message manager.
                        Combatant o = grid.getFocusedTile().getOccupant();

                        mm.loadSourceDescPair(o.toString(), o.getDesc());
                    } else if (!grid.getFocusedTile().getCatalog().isEmpty()) {
                        //If the crosshair overlaps an item, prints the items name and description to the Message manager.
                        Item i = grid.getFocusedTile().getCatalog().getFocusedItem();
                        mm.loadSourceDescPair(i.getName(), i.getVisualDescription());
                    } else {
                        //If the crosshair overlaps nothing, prints the tile name and description.
                        Tile t = grid.getFocusedTile();
                        mm.loadSourceDescPair(t.getName(), t.getDesc());
                    }
                }

                updateOutput(grid, p1, eq);
                game.repaint();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                switch(game.dequeuePrompt()) {
                    case ITEM_PROMPT:
                        eq.getPendingEvent().setAffectedId(grid.searchForOccupant(0).getInventory().getFocusIndex());
                        grid.searchForOccupant(0).getInventory().resetFocusIndex();
                        break;
                    case TILE_PROMPT:
                        eq.getPendingEvent().setxTile(grid.getXFocus());
                        eq.getPendingEvent().setyTile(grid.getYFocus());
                        grid.clearFocusedTile();
                        break;
                }

                if(game.isPromptQueueEmpty()) {
                    eq.executePendingEvent();
                }

                updateOutput(grid, p1, eq);
                game.repaint();
            }
        };

        Action get = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                //If the tile the player is on contains at least one item,
                //then the item is transfered into the player's inventory.
                if(!grid.getTileAt(grid.getXOfCombatant(0), grid.getYOfCombatant(0)).getCatalog().isEmpty()) {
                    p1.getInventory().transferFrom(grid.getTileAt(grid.getXOfCombatant(0), grid.getYOfCombatant(0)).getCatalog());
                    mm.insertMessage("Picked up items.");

                    eq.progressTimeBy(10, grid);
                    updateOutput(grid, p1, eq);
                    game.repaint();
                } else {
                    mm.insertMessage("There are no items to pick up.");
                }
            }
        };

        Action recon = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if(game.getConfig() == DisplayConfiguration.DEFAULT) {

                    game.switchState(DisplayConfiguration.TILE_SELECT);
                    grid.setFocusedTile(grid.getXOfCombatant(0), grid.getYOfCombatant(0));
                    mm.loadSourceDescPair(p1.toString(), p1.getDesc());

                } else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {

                    game.switchState(DisplayConfiguration.DEFAULT);
                    grid.clearFocusedTile();
                    mm.insertMessage("Exiting Recon mode.");

                }

                updateOutput(grid, p1, eq);
                game.repaint();
            }
        };

        Action inventory = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enqueuePrompt(DisplayPrompt.ITEM_PROMPT);

                updateOutput(grid, p1, eq);
                game.repaint();
            }
        };

        Action use = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(game.getConfig() == DisplayConfiguration.INVENTORY_SELECT) {
                    eq.addEvents(p1.getInventory().consumeItem(p1.getInventory().getFocusedItem().getId()).use(p1, grid));
                    p1.getInventory().resetFocusIndex();

                    mm.insertMessage("Consumed.");

                    eq.progressTimeInstantaneous(grid);
                    game.switchState(DisplayConfiguration.DEFAULT);
                    updateOutput(grid, p1, eq);
                    game.repaint();
                } else {
                    inventory.actionPerformed(null);
                }
            }
        };

        Action toss = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enqueuePrompt(DisplayPrompt.ITEM_PROMPT);
                game.enqueuePrompt(DisplayPrompt.TILE_PROMPT);
                eq.createPendingEvent(0, MacroOperation.DROP_ITEM);
                eq.getPendingEvent().setActorId(0);

                //TODO 7/16, 4:11am; This works, but rework focusedTile on grid.
                grid.setFocusedTile(2, 2);
                updateOutput(grid, p1, eq);
                game.repaint();
            }
        };

        //ESC = Exits the program.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "exitProgram");
        game.getActionMap().put("exitProgram", exitProgram);

        //Q,W,E,A,D,Z,X,C = Move. The following eight binds reflect moving.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('a'), "moveLeft");
        game.getActionMap().put("moveLeft", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('w'), "moveUp");
        game.getActionMap().put("moveUp", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('d'), "moveRight");
        game.getActionMap().put("moveRight", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('x'), "moveDown");
        game.getActionMap().put("moveDown", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('q'), "moveNW");
        game.getActionMap().put("moveNW", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('e'), "moveNE");
        game.getActionMap().put("moveNE", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('z'), "moveSW");
        game.getActionMap().put("moveSW", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('c'), "moveSE");
        game.getActionMap().put("moveSE", move);

        //ENTER = yes.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        game.getActionMap().put("confirm", confirm);

		//All keybinds needed for restricted set are above.
		game.initializeRestrictedCharacterSet(game.getInputMap(), game.getActionMap());

        //G = Get.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('g'), "get");
        game.getActionMap().put("get", get);

        //R = Recon.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('r'), "recon");
        game.getActionMap().put("recon", recon);

        //I = Inventory.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('i'), "inventory");
        game.getActionMap().put("inventory", inventory);

        //U = Use.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('u'), "use");
        game.getActionMap().put("use", use);

        //T = Toss.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('t'), "toss");
        game.getActionMap().put("toss", toss);

        updateOutput(grid, p1, eq);
    }

    private static void updateOutput(Grid grid, Player p1, EventQueue eq) {
        grid.updateHeader(eq.getTime());
        //TODO magic
        grid.updateGrid(13, 13);
        p1.updatePlayer();
    }
}
