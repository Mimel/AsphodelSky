package event;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The entire set of string messages that can be returned on event completion.
 */
public class Response {
    private static Map<Opcode, Map<ResponseCondition, String>> responseTable;

    /**
     * Loads the responseTable from the set of strings in the given .dat file with the given filename.
     * Subsequent calls to this method after the first will immediately return.
     * @param fileName The name of the file to load the table from.
     */
    public static void loadResponseTable(String fileName) {
        if(responseTable == null) {
            responseTable = new HashMap<>();
        } else {
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Opcode op = Opcode.NO_OP;
            Map<ResponseCondition, String> conditionTable = new HashMap<>();
            while((line = br.readLine()) != null) {
                switch (line.charAt(0)) {
                    case '!':
                        op = Opcode.valueOf(line.substring(1));
                        conditionTable = new HashMap<>();
                        break;
                    case '-':
                        responseTable.put(op, conditionTable);
                        break;
                    default:
                        conditionTable.put(
                                ResponseCondition.valueOf(line.substring(0, line.indexOf(':'))),
                                line.substring(line.indexOf('"') + 1, line.length() - 1)
                        );
                        break;
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String getResponse(Opcode op, ResponseCondition outcome, String[] resInserts) {
        String response = responseTable.get(op).get(outcome);

        if(response != null) {
            for(int ch = 0, insertToUse = 0; ch < response.length(); ch++) {
                if(response.charAt(ch) == '~') {
                    response = response.substring(0, response.indexOf('~')) + resInserts[insertToUse] + response.substring(response.indexOf('~') + 1);
                    ch += resInserts[insertToUse++].length();
                }
            }
        }
        return response;
    }

    /**
     * Gets the string response based on the opcode, the condition, and the set of
     * parameters to add to the string.
     * @param op The opcode that determines the set of responses to use.
     * @param outcome A compound object consisting of a Response Condition, which determines the
     *                response out of the set to use, and a variable amount of parameters,
     *                which will be added to the response template.
     * @return The formatted response.
     */
    static String getResponse(Opcode op, ResponseDetails outcome) {
        return getResponse(op, outcome.getCondition(), outcome.getInserts());
    }
}
