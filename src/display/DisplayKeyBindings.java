package display;

import comm.MessageManager;
import entity.Combatant;
import entity.Player;
import event.*;
import grid.CompositeGrid;
import grid.Point;
import grid.Tile;
import item.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import static display.DisplayPrompt.DIALOGUE_PROMPT;
import static display.DisplayPrompt.ITEM_PROMPT;
import static display.DisplayPrompt.TILE_PROMPT;

/**
 * The set of keybinds used for the game.
 */
public class DisplayKeyBindings {

    private static PromptManager promptManager;
    private static CompositeGrid grid;
    private static Player p1;
    private static MessageManager messageManager;
    private static CompoundEvent pendingInjection;
    /**
     * Creates keybinds for the game.
     * @param game The GUI.
     */
    public static void initKeyBinds(Display game, CompositeGrid grid, Player p1, MessageManager mm, EventQueue eq) {

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
                List<String> messages = Collections.emptyList();

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

                    //Moves the cursor over the inventory.
                    if(p1.getInventory().setFocus(xOffset * GUISidebar.INVENTORY_ROWS + yOffset)) {
                        p1.updatePlayer();
                        updateSourceDescPair(promptManager.peekPrompt());
                    }

                } else if(game.getConfig() == DisplayConfiguration.DEFAULT) {
                    if(!grid.isTileOccupiedRelativeTo(0, xOffset, yOffset)) {
                        eq.addEvent((SimpleEvent) new SimpleEvent(1, 100, Opcode.COMBATANT_MOVE)
                                .withCasterID(0)
                                .withTargetID(0)
                                .withTile(xOffset, yOffset));
                        messages = eq.progressTimeBy(1, grid);
                    } else {
                        Point playerPos = grid.getLocationOfCombatant(Player.PLAYER_ID);
                        Combatant target = grid.getCombatantAt(playerPos.x() + xOffset, playerPos.y() + yOffset);
                        eq.addEvent((SimpleEvent) new SimpleEvent(1, 100, Opcode.COMBATANT_ADJUSTHP)
                                .withCasterID(Player.PLAYER_ID)
                                .withTargetID(target.getId())
                                .withSecondary(-4));
                        messages = eq.progressTimeBy(1, grid);
                    }
                } else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {
                    grid.shiftFocus(xOffset, yOffset);
                    updateSourceDescPair(promptManager.peekPrompt());
                } else if(game.getConfig() == DisplayConfiguration.DIALOGUE) {
                    game.getFooter().shiftDialogueChoice(yOffset);
                }

                updateOutput(messages);
                game.repaint();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                List<String> messages = Collections.emptyList();
                if(promptManager.isPromptQueueEmpty()) {
                    return;
                }

                switch(promptManager.dequeuePrompt()) {
                    case ITEM_PROMPT:
                        pendingInjection.setItemID(grid.getOccupant(0).getInventory().getFocusedItem().getId());
                        grid.getOccupant(0).getInventory().resetFocusIndex();
                        break;
                    case TILE_PROMPT:
                        pendingInjection.setTile(grid.getFocus().x(), grid.getFocus().y());
                        grid.bindTo(0);
                        break;
                    case DIALOGUE_PROMPT:
                        if(game.getFooter().canDialogueContinue()) {
                            game.getFooter().progressDialogue();
                            addPromptsToDisplayQueue(DIALOGUE_PROMPT);
                        }
                        break;
                }

                if(promptManager.isPromptQueueEmpty()) {
                    if(pendingInjection != null) {
                        eq.createInjection(pendingInjection);
                        pendingInjection = null;
                    }

                    messages = eq.progressTimeBy(0, grid);

                    if(eq.isDialogueTreePending()) {
                        addPromptsToDisplayQueue(DIALOGUE_PROMPT);
                        game.getFooter().insertDialogue(eq.getPendingDialogueTree());
                    }
                } else {
                    updateSourceDescPair(promptManager.peekPrompt());
                }

                updateOutput(messages);
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
                            break;
                    }

                    if(promptManager.isUsedStackEmpty()) {
                        promptManager.clearPromptQueue();
                    } else {
                        switch(promptManager.requeuePrompt()) {
                            case TILE_PROMPT:
                                break;
                        }
                    }
                }

                updateOutput(Collections.emptyList());
                game.repaint();
            }
        };

        Action go_to_default = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!promptManager.isPromptQueueEmpty()) {
                    promptManager.clearPromptQueue();

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action get = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(!(grid.getItemsOnTile(0) == null)) {
                    eq.addEvent((SimpleEvent) new SimpleEvent(0, 100, Opcode.TRANSFER_ITEMALL)
                            .withCasterID(0)
                            .withTargetID(0)
                            .withItemID(grid.getFocusedCatalog().getFocusedItem().getId()));

                    List<String> messages = eq.progressTimeBy(0, grid);


                    updateOutput(messages);
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
                    pendingInjection = new CompoundEvent(0, 20, CompoundOpcode.NO_OP);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action inventory = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    pendingInjection = new CompoundEvent(0, 20, CompoundOpcode.NO_OP);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action use = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    pendingInjection = new CompoundEvent(0, 20, CompoundOpcode.USE_ITEM);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action toss = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT, DisplayPrompt.TILE_PROMPT)) {
                    pendingInjection = new CompoundEvent(0, 20, CompoundOpcode.DROP_ITEM);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action talk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addPromptsToDisplayQueue(ITEM_PROMPT, DIALOGUE_PROMPT)) {
                    // These two instructions load a dialogue tree into the EventQueue.
                    eq.addEvent((SimpleEvent) new SimpleEvent(0, 100, Opcode.START_DIALOGUE)
                            .withCasterID(0)
                            .withTargetID(3)
                            .withSecondary(252));

                    List<String> messages = eq.progressTimeBy(0, grid);

                    game.getFooter().insertDialogue(eq.getPendingDialogueTree());

                    updateOutput(messages);
                    game.repaint();
                }
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

        ///// OPTION COMMANDS /////

        //ENTER = yes.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        game.getActionMap().put("confirm", confirm);

        //n = no.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke('n'), "go_back");
        game.getActionMap().put("go_back", go_back);

        //BACK_SPACE = go to default.
        game.getInputMap(JComponent.WHEN_FOCUSED).getParent().put(KeyStroke.getKeyStroke("BACK_SPACE"), "go_to_default");
        game.getActionMap().put("go_to_default", go_to_default);

        ///// NEUTRAL COMMANDS /////

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

        ///// FRIENDLY COMMANDS /////

        //V = Voice.
        game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('l'), "voice");
        game.getActionMap().put("voice", talk);

        updateOutput(Collections.emptyList());
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
                    grid.unbind();
                    break;
            }
            promptManager.enqueuePrompt(prompt);
        }

        updateSourceDescPair(promptManager.peekPrompt());
        return true;
    }

    /**
     * Performs closing methods before another keypress can be recognized;
     * Adds messages to the MessageManager,
     * Updates the grid and the player.
     * @param messages A list of messages to send to the message manager.
     */
    private static void updateOutput(List<String> messages) {
        messageManager.insertMessage(messages.toArray(new String[messages.size()]));
        grid.updateGrid(GUIFocus.GRID_WIDTH, GUIFocus.GRID_HEIGHT);
        p1.updatePlayer();
    }

    /**
     * Updates the focused source/description pair, depending on the prompt.
     * @param currentPrompt The prompt currently used, to determine the space to locate the
     *                      focused item.
     */
    private static void updateSourceDescPair(DisplayPrompt currentPrompt) {
        switch(currentPrompt) {
            case ITEM_PROMPT:
                messageManager.loadSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
                break;
            case TILE_PROMPT:
                if (grid.getFocusedCombatant() != null) {
                    Combatant o = grid.getFocusedCombatant();
                    messageManager.loadSourceDescPair(o.toString(), o.getDesc());
                } else if (!(grid.getFocusedCatalog() == null) && !grid.getFocusedCatalog().isEmpty()) {
                    Item i = grid.getFocusedCatalog().getFocusedItem();
                    messageManager.loadSourceDescPair(i.getName(), i.getVisualDescription());
                } else {
                    Tile t = grid.getFocusedTile();
                    messageManager.loadSourceDescPair(t.getName(), t.getDesc());
                }
                break;
        }
    }
}
