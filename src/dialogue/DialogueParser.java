package dialogue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Static utility class used to create dialogue trees from .dat files.
 */
public class DialogueParser {
    private DialogueParser(){}

    /**
     * Loads a dialogue tree from a given file.
     * @param fileName The .dat file to extract the dialogue tree from.
     * @return The root of the dialogue tree.
     */
    public static Statement loadDialogueTree(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Statement root = null;
            String line;
            String pending_reply = "";
            Stack<Statement> previousJunctions = new Stack<>();

            while((line = br.readLine()) != null) {
                line = line.trim();
                switch(line.charAt(0)) {
                    case '|':
                        int numOfPaths = Integer.parseInt(line.substring(1, line.indexOf('>')));
                        String dialogue = line.substring(line.indexOf('>') + 1);

                        if(root == null) {
                            root = new Statement(dialogue, numOfPaths);
                            previousJunctions.add(root);
                        } else {
                            root = root.addPath(pending_reply, dialogue, numOfPaths);
                            if(numOfPaths == 0) {
                                root = previousJunctions.peek();
                            }
                        }
                        break;
                    case '?':
                        pending_reply = line.substring(1);
                        break;
                    case '{':
                        previousJunctions.push(root);
                        break;
                    case '}':
                        previousJunctions.pop();
                        root = previousJunctions.peek();
                        break;
                    default:
                        throw new IOException("Unknown starting character found.");
                }
            }

            if(!previousJunctions.isEmpty()) {
                root = previousJunctions.pop();
            }
            return root;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
