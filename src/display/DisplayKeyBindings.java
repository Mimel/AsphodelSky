package display;

import comm.MessageManager;
import entity.Combatant;
import entity.Player;
import event.EventQueue;
import event.InstructionData;
import event.CompoundOpcode;
import event.Opcode;
import grid.Grid;
import grid.Tile;
import item.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * The set of keybinds used for the game.
 */
public class DisplayKeyBindings {

    private static PromptManager promptManager;
    private static Grid grid;
    private static Player p1;
    private static MessageManager messageManager;
    /**
     * Creates keybinds for the game.
     * @param game The GUI.
     */
    public static void initKeyBinds(Display game, Grid grid, Player p1, MessageManager mm, EventQueue eq) {

        DisplayKeyBindings.promptManager = new PromptManager(game);
        DisplayKeyBindings.grid = grid;
        DisplayKeyBindings.p1 = p1;
        DisplayKeyBindings.messageManager = mm;

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
                        updateSourceDescPair(promptManager.peekPrompt());
                    }

                } else if(game.getConfig() == DisplayConfiguration.DEFAULT) {
                    grid.moveCombatant(0, grid.getXOfCombatant(0) + xOffset, grid.getYOfCombatant(0) + yOffset);
                    eq.progressTimeBy(1, grid);

                } else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {
                    grid.shiftFocus(xOffset, yOffset);
                    updateSourceDescPair(promptManager.peekPrompt());
                } else if(game.getConfig() == DisplayConfiguration.DIALOGUE) {
                    game.getFooter().shiftDialogueChoice(yOffset);
                }

                updateOutput();
                game.repaint();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                switch(promptManager.dequeuePrompt()) {
                    case ITEM_PROMPT:
                        eq.getPendingEvent().reviseData(new InstructionData.DataBuilder(eq.getPendingEvent().getData()).itemID(grid.searchForOccupant(0).getInventory().getFocusedItem().getId()));
                        grid.searchForOccupant(0).getInventory().resetFocusIndex();
                        break;
                    case TILE_PROMPT:
                        eq.getPendingEvent().reviseData(new InstructionData.DataBuilder(eq.getPendingEvent().getData()).tile(grid.getXFocus(), grid.getYFocus()));
                        grid.bindFocusToPlayer();
                        break;
                    case DIALOGUE_PROMPT:
                        if(!game.getFooter().isDialogueEnded()) {
                            game.getFooter().progressDialogue();
                            addPromptsToDisplayQueue(DisplayPrompt.DIALOGUE_PROMPT);
                        }
                        break;
                }

                if(promptManager.isPromptQueueEmpty()) {
                    eq.executePendingEvent();
                    eq.progressTimeInstantaneous(grid);

                    if(eq.isDialogueTreePending()) {
                        addPromptsToDisplayQueue(DisplayPrompt.DIALOGUE_PROMPT);
                        game.getFooter().insertDialogue(eq.getPendingDialogueTree());
                    }
                } else {
                    updateSourceDescPair(promptManager.peekPrompt());
                }

                updateOutput();
                game.repaint();
            }
        };

        Action go_back = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!promptManager.isPromptQueueEmpty()) {
                    //Undo initialization.
                    switch(promptManager.peekPrompt()) {
                        case TILE_PROMPT:
                            grid.bindFocusToPlayer();
                            break;
                    }

                    if(promptManager.isUsedStackEmpty()) {
                        promptManager.clearPromptQueue();
                    } else {
                        switch(promptManager.requeuePrompt()) {
                            case TILE_PROMPT:
                                grid.bindFocusToPlayer();
                                grid.unbindFocus();
                                break;
                        }
                    }
                }

                updateOutput();
                game.repaint();
            }
        };

        Action go_to_default = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!promptManager.isPromptQueueEmpty()) {
                    promptManager.clearPromptQueue();
                    grid.bindFocusToPlayer();

                    updateOutput();
                    game.repaint();
                }
            }
        };

        Action get = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(!grid.getItemsOnTile(0).isEmpty()) {
                    //TODO add.
                    eq.addEvent(0, 100, Opcode.TRANSFER_ITEMALL,
                            new InstructionData.DataBuilder(0).targetID(0).itemID(grid.getFocusedTile().getCatalog().getFocusedItem().getId()).build());
                    mm.insertMessage(eq.progressTimeInstantaneous(grid).get(0));

                    updateOutput();
                } else {
                    mm.insertMessage("There are no items on this tile.");
                }

                game.repaint();
            }
        };

        Action recon = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.TILE_PROMPT)) {
                    eq.createPendingEvent(0, CompoundOpcode.NO_OP, new InstructionData.DataBuilder(0).build());
                    grid.setFocusedTile(grid.getXOfCombatant(0), grid.getYOfCombatant(0));

                    updateOutput();
                    game.repaint();
                }
            }
        };

        Action inventory = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    eq.createPendingEvent(0, CompoundOpcode.NO_OP, new InstructionData.DataBuilder(0).build());

                    updateOutput();
                    game.repaint();
                }
            }
        };

        Action use = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    eq.createPendingEvent(0, CompoundOpcode.USE_ITEM, new InstructionData.DataBuilder(0).build());

                    updateOutput();
                    game.repaint();
                }
            }
        };

        Action toss = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT, DisplayPrompt.TILE_PROMPT)) {
                    eq.createPendingEvent(0, CompoundOpcode.DROP_ITEM, new InstructionData.DataBuilder(0).build());

                    updateOutput();
                    game.repaint();
                }
            }
        };

        Action test = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        game.getInputMap().setParent(new InputMap());

        //ESC = Exits the program.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke("ESCAPE"), "exitProgram");
        game.getActionMap().put("exitProgram", exitProgram);

        //Q,W,E,A,D,Z,X,C = Move. The following eight binds reflect moving.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('a'), "moveLeft");
        game.getActionMap().put("moveLeft", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('w'), "moveUp");
        game.getActionMap().put("moveUp", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('d'), "moveRight");
        game.getActionMap().put("moveRight", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('x'), "moveDown");
        game.getActionMap().put("moveDown", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('q'), "moveNW");
        game.getActionMap().put("moveNW", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('e'), "moveNE");
        game.getActionMap().put("moveNE", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('z'), "moveSW");
        game.getActionMap().put("moveSW", move);

        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('c'), "moveSE");
        game.getActionMap().put("moveSE", move);

        //ENTER = yes.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        game.getActionMap().put("confirm", confirm);

        //n = no.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('n'), "go_back");
        game.getActionMap().put("go_back", go_back);

        //BACK_SPACE = go to default.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke("BACK_SPACE"), "go_to_default");
        game.getActionMap().put("go_to_default", go_to_default);

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

        //J = TESTING FUNCTION.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('j'), "test");
        game.getActionMap().put("test", test);


        updateOutput();
    }

    /**
     * Adds a given prompt(s) to the queue in the display. If one of the prompts fails to be added, then all prompts are
     * removed, and this method returns false. For prompt safety reasons, all additions to the queue must go through this
     * method first, rather than directly through the key binding methods.
     * @param prompts 0 or more prompts to add to the queue.
     * @return True if all prompts were successfully added, false otherwise.
     */
    private static boolean addPromptsToDisplayQueue(DisplayPrompt... prompts) {
        for(DisplayPrompt prompt : prompts) {
            switch(prompt) {
                case ITEM_PROMPT:
                    if(p1.getInventory().isEmpty()) {
                        promptManager.clearPromptQueue();
                        return false;
                    }
                    break;
                case TILE_PROMPT:
                    grid.unbindFocus();
                    break;
            }
            promptManager.enqueuePrompt(prompt);
        }

        updateSourceDescPair(promptManager.peekPrompt());
        return true;
    }

    private static void updateOutput() {
        //TODO magic
        grid.updateGrid(13, 13);
        p1.updatePlayer();
    }

    private static void updateSourceDescPair(DisplayPrompt currentPrompt) {
        switch(currentPrompt) {
            case ITEM_PROMPT:
                messageManager.loadSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
                break;
            case TILE_PROMPT:
                if (grid.getFocusedTile().getOccupant() != null) {
                    Combatant o = grid.getFocusedTile().getOccupant();
                    messageManager.loadSourceDescPair(o.toString(), o.getDesc());
                } else if (!grid.getFocusedTile().getCatalog().isEmpty()) {
                    Item i = grid.getFocusedTile().getCatalog().getFocusedItem();
                    messageManager.loadSourceDescPair(i.getName(), i.getVisualDescription());
                } else {
                    Tile t = grid.getFocusedTile();
                    messageManager.loadSourceDescPair(t.getName(), t.getDesc());
                }
                break;
        }
    }
}
