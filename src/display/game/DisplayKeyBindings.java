package display.game;

import comm.MessageManager;
import comm.SourceDescriptionPair;
import display.game.sidebar.GUISidebar;
import entity.Combatant;
import entity.Player;
import event.*;
import event.compound_event.*;
import grid.CompositeGrid;
import grid.Point;
import grid.Tile;
import item.Item;
import item.ItemPromptLoader;
import skill.Skill;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static display.game.DisplayPrompt.ACTOR_PROMPT;
import static display.game.DisplayPrompt.DIALOGUE_PROMPT;
import static display.game.DisplayPrompt.TILE_PROMPT;

/**
 * The set of keybinds used for the game.
 */
class DisplayKeyBindings {

    private static PromptManager promptManager;
    private static CompositeGrid grid;
    private static Player p1;
    private static MessageManager messageManager;
    private static SourceDescriptionPair sdp;
    private static CompoundEvent pendingInjection;

    private static boolean lookForItemPrompts = false;

    /**
     * Creates keybinds for the game.
     * @param game The GUI.
     */
    static void initKeyBinds(GameView game, CompositeGrid grid, Player p1, MessageManager mm, SourceDescriptionPair sdp, EventQueue eq) {

        DisplayKeyBindings.promptManager = new PromptManager(game);
        DisplayKeyBindings.grid = grid;
        DisplayKeyBindings.p1 = p1;
        DisplayKeyBindings.messageManager = mm;
        DisplayKeyBindings.sdp = sdp;

        Action exitProgram = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.pause();
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
                        //p1.updatePlayer(); //TODO UPDATE PLAYER
                        updateSourceDescPair(promptManager.peekPrompt());
                    }

                } else if(game.getConfig() == DisplayConfiguration.SKILL_SELECT) {

                    //Moves the cursor over the skill set.
                    if(p1.getSkillSet().setFocusedSkillIndex(xOffset)) {
                        //p1.updatePlayer(); //TODO UPDATE PLAYER
                        updateSourceDescPair(promptManager.peekPrompt());
                    }

                } else if(game.getConfig() == DisplayConfiguration.DEFAULT) {
                    if(!grid.isTileOccupiedRelativeTo(0, xOffset, yOffset)) {
                        SimpleEvent moveSelfEvent = new SimpleEvent(1, 100, Opcode.COMBATANT_MOVE);
                        moveSelfEvent.getData().setCasterIDTo(Player.PLAYER_ID).setTargetIDTo(Player.PLAYER_ID).setCoordTo(xOffset, yOffset);
                        eq.addEvent(moveSelfEvent);
                        messages = eq.progressTimeBy(1, grid);
                    } else {
                        Point playerPos = grid.getLocationOfCombatant(Player.PLAYER_ID);
                        Combatant target = grid.getCombatantAt(playerPos.x() + xOffset, playerPos.y() + yOffset);

                        SimpleEvent attackAdjacentEvent = new SimpleEvent(1, 100, Opcode.COMBATANT_ADJUSTHP);
                        attackAdjacentEvent.getData().setCasterIDTo(Player.PLAYER_ID).setTargetIDTo(target.getId()).setSecondaryTo(-4);
                        eq.addEvent(attackAdjacentEvent);

                        messages = eq.progressTimeBy(1, grid);
                    }
                } else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {
                    if(promptManager.peekPrompt() == ACTOR_PROMPT) {
                        grid.shiftFocusToClosestCombatant(xOffset, yOffset);
                    } else if(promptManager.peekPrompt() == TILE_PROMPT) {
                        grid.shiftFocus(xOffset, yOffset);
                    }

                    updateSourceDescPair(promptManager.peekPrompt());
                } else if(game.getConfig() == DisplayConfiguration.DIALOGUE) {
                    //game.getFooter().shiftDialogueChoice(yOffset);
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
                    case ACTOR_PROMPT:
                        pendingInjection.setTargetID(grid.getFocusedCombatant().getId());
                        grid.bindTo(Player.PLAYER_ID);
                        break;
                    case ITEM_PROMPT:
                        Item focusedItem = grid.getOccupant(Player.PLAYER_ID).getInventory().getFocusedItem();
                        pendingInjection.setItemID(focusedItem.getId());
                        grid.getOccupant(Player.PLAYER_ID).getInventory().resetFocusIndex();

                        if(lookForItemPrompts) {
                            ArrayList<DisplayPrompt> prompts = ItemPromptLoader.getItemPrompts(focusedItem.getName());
                            addPromptsToDisplayQueue(prompts.toArray(new DisplayPrompt[0]));
                            lookForItemPrompts = false;
                        }
                        break;
                    case SKILL_PROMPT:
                        Skill focusedSkill = grid.getOccupant(Player.PLAYER_ID).getSkillSet().getFocusedSkill();
                        pendingInjection.setSkillID(focusedSkill.getId());
                        grid.getOccupant(Player.PLAYER_ID).getSkillSet().resetFocusedSkillIndex();
                        break;
                    case TILE_PROMPT:
                        pendingInjection.setTile(grid.getFocus().x(), grid.getFocus().y());
                        grid.bindTo(Player.PLAYER_ID);
                        break;
                    case DIALOGUE_PROMPT:
                        /*if(game.getFooter().canDialogueContinue()) {
                            game.getFooter().progressDialogue();
                            addPromptsToDisplayQueue(DIALOGUE_PROMPT);
                        }*/
                        break;
                }

                if(promptManager.isPromptQueueEmpty() || promptManager.peekPrompt() == DIALOGUE_PROMPT) {
                    if(pendingInjection != null) {
                        eq.createInjection(pendingInjection);
                        pendingInjection = null;
                    }

                    messages = eq.progressTimeBy(0, grid);

                    /*if(eq.isDialogueTreePending()) {
                        game.getFooter().insertDialogue(eq.getPendingDialogueTree());
                    }*/
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
                        case ACTOR_PROMPT:
                        case TILE_PROMPT:
                            grid.bindTo(Player.PLAYER_ID);
                            break;
                    }

                    if(promptManager.isUsedStackEmpty()) {
                        promptManager.clearPromptQueue();
                    } else {
                        switch(promptManager.requeuePrompt()) {
                            case ACTOR_PROMPT:
                            case TILE_PROMPT:
                                grid.unbind();
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
                    grid.bindTo(Player.PLAYER_ID);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action get = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(keybindsAreRestricted() && !(grid.getItemsOnTile(0) == null)) {
                    SimpleEvent getEvent = new SimpleEvent(0, 100, Opcode.TRANSFER_ITEMALL);
                    getEvent.getData().setCasterIDTo(Player.PLAYER_ID).setTargetIDTo(Player.PLAYER_ID)
                            .setItemIDTo(grid.getFocusedCatalog().getFocusedItem().getId());
                    eq.addEvent(getEvent);

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
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(DisplayPrompt.TILE_PROMPT)) {
                    pendingInjection = new NoOpEvent(0, 20);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action inventory = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    pendingInjection = new NoOpEvent(0, 20);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action viewSkills = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(DisplayPrompt.SKILL_PROMPT)) {
                    pendingInjection = new UseSkillEvent(0, 20);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action use = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT)) {
                    pendingInjection = new UseItemEvent(0, 20);
                    lookForItemPrompts = true;

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action toss = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(DisplayPrompt.ITEM_PROMPT, DisplayPrompt.TILE_PROMPT)) {
                    pendingInjection = new DropItemEvent(0, 20);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        Action talk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keybindsAreRestricted() && addPromptsToDisplayQueue(ACTOR_PROMPT, DIALOGUE_PROMPT)) {
                    // These two instructions load a dialogue tree into the EventQueue.
                    pendingInjection = new ShellTalkEvent(0, 20);
                    pendingInjection.getData().setCasterIDTo(Player.PLAYER_ID).setSecondaryTo(252);

                    updateOutput(Collections.emptyList());
                    game.repaint();
                }
            }
        };

        game.getInputMap().setParent(new InputMap());

        //R = Recon.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('r'), "recon");
        game.getActionMap().put("recon", recon);

        //ESC = Exits the program.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exitProgram");
        game.getActionMap().put("exitProgram", exitProgram);

        //Q,W,E,A,D,Z,X,C = Move. The following eight binds reflect moving.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'), "moveLeft");
        game.getActionMap().put("moveLeft", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'), "moveUp");
        game.getActionMap().put("moveUp", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'), "moveRight");
        game.getActionMap().put("moveRight", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('x'), "moveDown");
        game.getActionMap().put("moveDown", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('q'), "moveNW");
        game.getActionMap().put("moveNW", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('e'), "moveNE");
        game.getActionMap().put("moveNE", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('z'), "moveSW");
        game.getActionMap().put("moveSW", move);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('c'), "moveSE");
        game.getActionMap().put("moveSE", move);

        ///// OPTION COMMANDS /////

        //ENTER = yes.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        game.getActionMap().put("confirm", confirm);

        //n = no.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('n'), "go_back");
        game.getActionMap().put("go_back", go_back);

        //BACK_SPACE = go to default.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("BACK_SPACE"), "go_to_default");
        game.getActionMap().put("go_to_default", go_to_default);

        ///// NEUTRAL COMMANDS /////

        //G = Get.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('g'), "get");
        game.getActionMap().put("get", get);

        //I = Inventory.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('i'), "inventory");
        game.getActionMap().put("inventory", inventory);

        //V = View Skills.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('v'), "view_skills");
        game.getActionMap().put("view_skills", viewSkills);

        //U = Use.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('u'), "use");
        game.getActionMap().put("use", use);

        //T = Toss.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "toss");
        game.getActionMap().put("toss", toss);

        ///// FRIENDLY COMMANDS /////

        //L = Voice.
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('l'), "voice");
        game.getActionMap().put("voice", talk);

        updateOutput(Collections.emptyList());
    }

    private static boolean keybindsAreRestricted() {
        return promptManager.isPromptQueueEmpty();
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
                case ACTOR_PROMPT:
                    if(grid.getAllCombatants().length == 0) {
                        promptManager.clearPromptQueue();
                        return false;
                    }
                    grid.unbind();
                    break;
                case ITEM_PROMPT:
                    if(p1.getInventory().isEmpty()) {
                        promptManager.clearPromptQueue();
                        return false;
                    }
                    break;
                case SKILL_PROMPT:
                    if(p1.getSkillSet().isSkillSetEmpty()) {
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

        if(prompts.length > 0) {
            updateSourceDescPair(promptManager.peekPrompt());
        }
        return true;
    }

    /**
     * Performs closing methods before another keypress can be recognized;
     * Adds messages to the MessageManager,
     * Updates the grid and the player.
     * @param messages A list of messages to send to the message manager.
     */
    private static void updateOutput(List<String> messages) {
        messageManager.insertMessage(messages.toArray(new String[0]));
    }

    /**
     * Updates the focused source/description pair, depending on the prompt.
     * @param currentPrompt The prompt currently used, to determine the space to locate the
     *                      focused item.
     */
    private static void updateSourceDescPair(DisplayPrompt currentPrompt) {
        switch(currentPrompt) {
            case ITEM_PROMPT:
                sdp.setSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
                break;

            case SKILL_PROMPT:
                sdp.setSourceDescPair(p1.getSkillSet().getFocusedSkill().getName(), p1.getSkillSet().getFocusedSkill().getDesc_flavor());
                break;
            case ACTOR_PROMPT:
            case TILE_PROMPT:
                if (grid.getFocusedCombatant() != null) {
                    Combatant o = grid.getFocusedCombatant();
                    sdp.setSourceDescPair(o.toString(), o.getDesc());
                } else if (!(grid.getFocusedCatalog() == null) && !grid.getFocusedCatalog().isEmpty()) {
                    Item i = grid.getFocusedCatalog().getFocusedItem();
                    sdp.setSourceDescPair(i.getName(), i.getVisualDescription());
                } else {
                    Tile t = grid.getFocusedTile();
                    sdp.setSourceDescPair(t.getName(), t.getDesc());
                }
                break;
        }
    }
}
