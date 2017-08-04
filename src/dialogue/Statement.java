package dialogue;

/**
 * Created by Owner on 8/3/2017.
 */
public class Statement {
    private String dialogue;

    private int numOfPaths;

    private Statement[] paths;

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

    boolean isEndOfDialogue() {
        return (numOfPaths == 0);
    }

    boolean isLinear() {
        return (numOfPaths == 1);
    }

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
