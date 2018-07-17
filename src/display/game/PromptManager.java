package display.game;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Manager of the prompt queue. Works in conjunction with the controller and the display to adjusts the display output
 * based on the inputs given.
 */
public class PromptManager {

    /**
     * The display to adjust.
     */
    GameSession screen;

    /**
     * A queue of required requests that must be fulfilled before the configuration resets to default.
     */
    private Deque<DisplayPrompt> promptQueue;

    /**
     * A stack of requests that have been fulfilled.
     */
    private Stack<DisplayPrompt> usedPromptStack;

    PromptManager(GameSession d) {
        this.screen = d;
        this.promptQueue = new LinkedList<>();
        this.usedPromptStack = new Stack<>();
    }

    boolean isPromptQueueEmpty() {
        return promptQueue.isEmpty();
    }

    boolean isUsedStackEmpty() {
        return usedPromptStack.isEmpty();
    }

    /**
     * Enqueues a prompt onto this display's prompt queue. If a prompt is added when there are no prompts on the
     * queue, the key binds are restricted, allowing only arrow key movements, yes, no, and game exit commands.
     * @param dp The display prompt to add to the queue.
     */
    void enqueuePrompt(DisplayPrompt dp) {
        promptQueue.addLast(dp);
        adjustDisplayByCurrentPrompt();
    }

    DisplayPrompt peekPrompt() {
        return promptQueue.peekFirst();
    }

    /**
     * Adds the prompt on the top of the used prompt stack back to the head of the queue.
     * @return The prompt that was added to the head of the queue.
     */
    DisplayPrompt requeuePrompt() {
        if(!usedPromptStack.isEmpty()) {
            promptQueue.addFirst(usedPromptStack.pop());
            adjustDisplayByCurrentPrompt();
        }

        return promptQueue.peekFirst();
    }

    /**
     * Dequeues a prompt from this display's prompt queue. If there are no prompts left after removal, then
     * the key binds are unrestricted, allowing for complete control of the game.
     * @return The prompt that was removed.
     */
    DisplayPrompt dequeuePrompt() {
        DisplayPrompt dp = promptQueue.pollFirst();
        usedPromptStack.push(dp);

        if(promptQueue.isEmpty()) {
            usedPromptStack.clear();
        }

        adjustDisplayByCurrentPrompt();
        return dp;
    }

    /**
     * Clears the prompt queue, and unrestricts keybinds if there are any removed prompts.
     */
    void clearPromptQueue() {
        if(!isPromptQueueEmpty()) {
            promptQueue.clear();
            usedPromptStack.clear();
            adjustDisplayByCurrentPrompt();
        }
    }

    /**
     * Adjusts the display according to the prompt in the front of the queue.
     */
    private void adjustDisplayByCurrentPrompt() {
        if(promptQueue.isEmpty()) {
            screen.switchState(DisplayConfiguration.DEFAULT);
        } else {
            switch(promptQueue.peekFirst()) {
                case ITEM_PROMPT:
                    screen.switchState(DisplayConfiguration.INVENTORY_SELECT);
                    break;
                case SKILL_PROMPT:
                    screen.switchState(DisplayConfiguration.SKILL_SELECT);
                    break;
                case ACTOR_PROMPT:
                case TILE_PROMPT:
                    screen.switchState(DisplayConfiguration.TILE_SELECT);
                    break;
                case DIALOGUE_PROMPT:
                    screen.switchState(DisplayConfiguration.DIALOGUE);
                    break;
            }
        }
    }
}
