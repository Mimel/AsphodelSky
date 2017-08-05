package display;

import dialogue.Statement;

import java.awt.*;

/**
 * Created by Owner on 8/3/2017.
 */
public class GUIFooter_Switch {
    private String[] messages;

    private String title;
    private String description;

    private Statement dialogue;
    private int dialogueChoice;

    private GUIFooter_Messages msgDisplay;
    private GUIFooter_Description descDisplay;
    private GUIFooter_Dialogue dialogueDisplay;

    GUIFooter_Switch() {
        msgDisplay = new GUIFooter_Messages();
        descDisplay = new GUIFooter_Description();
        dialogueDisplay = new GUIFooter_Dialogue();
    }

    void updateMessages(String[] newMessages) {
        this.messages = newMessages;
        //System.arraycopy(newMessages, 0, this.messages, 0, this.messages.length);
    }

    void updateDescription(String newTitle, String newDesc) {
        this.title = newTitle;
        this.description = newDesc;
    }

    void loadDialogueTree(Statement root) {
        this.dialogue = root;
    }

    void shiftChoice(int choiceAddend) {
        if(dialogueChoice + choiceAddend >= 0 && dialogueChoice + choiceAddend < dialogue.getNumOfPaths()) {
            dialogueChoice += choiceAddend;
        }
    }

    void progressDialogueTree() {
        this.dialogue = dialogue.getPath(dialogueChoice);
        dialogueChoice = 0;
    }

    boolean isDialogueEnded() {
        return dialogue.isEndOfDialogue();
    }

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
