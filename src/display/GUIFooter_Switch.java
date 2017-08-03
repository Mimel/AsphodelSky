package display;

import dialogue.Statement;

import java.awt.*;

/**
 * Created by Owner on 8/3/2017.
 */
public class GUIFooter_Switch {
    String[] messages;

    String title;
    String description;

    Statement dialogue;

    GUIFooter_Messages msgDisplay;
    GUIFooter_Description descDisplay;
    GUIFooter_Dialogue dialogueDisplay;

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

    }

    void progressDialogueTree(String reply) {

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
                dialogueDisplay.drawDialogue(g, "what", "temp", "temp");
                break;
        }
    }
}
