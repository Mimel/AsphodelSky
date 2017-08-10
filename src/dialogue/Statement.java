package dialogue;

/**
 * A tree-node object that represents a line(s) of dialogue, and a set of replies.
 */
public class Statement {
    /**
     * The line(s) of dialogue that are associated with the statement.
     */
    private String dialogue;

    /**
     * The number of possible reply choices.
     * 0 indicates an end node.
     * 1 indicates a mandatory next, and acts like a linked list.
     * 2+ indicates that a reply must be selected from a list.
     */
    private int numOfPaths;

    /**
     * A set of paths where the node branches to.
     * This is the exact same size as the replies variable.
     */
    private Statement[] paths;

    /**
     * The replies associated with each path.
     * This is the exact same size as the paths variable.
     */
    private String[] replies;

    Statement(String dialogue, int numOfPaths) {
        this.dialogue = dialogue;
        if(numOfPaths >= 0) {
            this.numOfPaths = numOfPaths;
            this.paths = new Statement[numOfPaths];
            this.replies = new String[numOfPaths];
        }
    }

    public String getDialogue() {
        return dialogue;
    }

    public int getNumOfPaths() {
        return numOfPaths;
    }

    public String[] getReplies() {
        return replies;
    }

    public boolean isEndOfDialogue() {
        return (numOfPaths == 0);
    }

    /**
     * Adds a reply to this statement.
     * @param reply The reply text to show that will be bound to the node.
     * @param nextDialogue The dialogue of the next node.
     * @param nextNumOfPaths The number of paths the next node will have.
     * @return The node that was created.
     */
    Statement addPath(String reply, String nextDialogue, int nextNumOfPaths) {
        for(int route = 0; route < numOfPaths; route++) {
            if(replies[route] == null) {
                paths[route] = new Statement(nextDialogue, nextNumOfPaths);
                replies[route] = reply;

                return paths[route];
            }
        }

        return null;
    }

    public Statement getPath(int pathToTake) {
        return paths[pathToTake];
    }

    public String toString() {
        return dialogue;
    }
}
