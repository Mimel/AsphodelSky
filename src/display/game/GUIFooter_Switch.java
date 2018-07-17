package display.game;

import dialogue.Statement;

import java.awt.*;

/**
 * Determinator of the mode the footer is in, and manages the content drawn.
 */
public class GUIFooter_Switch {

    /**
     * A set of messages.
     * Drawn on msgDisplay.
     */
    private String[] messages;

    /**
     * The title of an object.
     * Drawn on descDisplay.
     */
    private String title;

    /**
     * The description of an object.
     * Drawn on descDisplay.
     */
    private String description;

    /**
     * A statement, with text and reply options.
     * Drawn on dialogueDisplay.
     */
    private Statement dialogue;

    /**
     * The reply to choose.
     * Drawn on dialogueDisplay.
     */
    private int dialogueChoice;

    /**
     * The display that draws a set of messages.
     */
    private GUIFooter_Messages msgDisplay;

    /**
     * The display that draws a title and description of an object.
     */
    private GUIFooter_Description descDisplay;

    /**
     * The display that draws a line(s) of dialogue and its' replies.
     */
    private GUIFooter_Dialogue dialogueDisplay;

    GUIFooter_Switch() {
        msgDisplay = new GUIFooter_Messages();
        descDisplay = new GUIFooter_Description();
        dialogueDisplay = new GUIFooter_Dialogue();
    }

    void updateMessages(String[] newMessages) {
        this.messages = newMessages;
    }

    void updateDescription(String newTitle, String newDesc) {
        this.title = newTitle;
        this.description = newDesc;
    }

    void loadDialogueTree(Statement root) {
        this.dialogue = root;
    }

    /**
     * Adjusts the choice by the given addend. Note that the dialogueChoice is not replaced, but
     * altered by the given addend.
     * @param choiceAddend Addend to add to the given choice to get the new choice.
     */
    void shiftChoice(int choiceAddend) {
        if(dialogueChoice + choiceAddend >= 0 && dialogueChoice + choiceAddend < dialogue.getNumOfPaths()) {
            dialogueChoice += choiceAddend;
        }
    }

    /**
     * Continues the dialogue tree.
     */
    void progressDialogueTree() {
        this.dialogue = dialogue.getPath(dialogueChoice);
        dialogueChoice = 0;
    }

    boolean isDialogueEnded() {
        return dialogue.isEndOfDialogue();
    }

    /**
     * Draws the content of the footer based on the path chosen.
     * @param g The graphics object to draw to.
     * @param path The mode to write to.
     */
    void sendTo(Graphics g, FooterMode path) {
        switch(path) {
            case MESSAGES:
                msgDisplay.drawMessages(g, messages);
                break;
            case DESCRIPTION:
                descDisplay.drawDescription(g, title, description);
                break;
            case DIALOGUE:
                dialogueDisplay.drawDialogue(g, dialogue.getDialogue(), dialogueChoice, dialogue.getReplies());
                break;
        }
    }
}
