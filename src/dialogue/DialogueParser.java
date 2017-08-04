package dialogue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by Owner on 8/3/2017.
 */
public class DialogueParser {
    private DialogueParser(){}

    public static Statement loadDialogueTree(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Statement root = null;
            Statement pointer = null;
            String line;
            String[] split_line;
            String pending_reply = "";
            Stack<Statement> previousJunctions = new Stack<>();
            previousJunctions.add(root);

            while((line = br.readLine()) != null) {
                line = line.trim();
                switch(line.charAt(0)) {
                    case '|':
                        split_line = line.substring(1).split(">");
                        if(root == null) {
                            root = new Statement(split_line[1], Byte.parseByte(split_line[0]));
                            pointer = root;
                        } else {
                            pointer = pointer.addPath(pending_reply, split_line[1], Byte.parseByte(split_line[0]));
                            if(Byte.parseByte(split_line[0]) == 0) {
                                pointer = previousJunctions.peek();
                            }
                        }
                        break;
                    case '?':
                        split_line = line.substring(1).split("_");
                        pending_reply = split_line[1];
                        break;
                    case '{':
                        previousJunctions.push(pointer);
                        break;
                    case '}':
                        previousJunctions.pop();
                        pointer = previousJunctions.peek();
                        break;
                    default:
                        throw new IOException("What?");
                }
            }
            return root;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
