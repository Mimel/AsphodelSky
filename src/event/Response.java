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
    static Map<Opcode, Map<ResponseCondition, String>> responseTable;

    /**
     * Loads the responseTable from the set of strings in the given .dat file with the given filename.
     * Subsequent calls to this method after the first will immediately return.
     * @param fileName
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
                if(line.charAt(0) == '!') {
                    op = Opcode.valueOf(line.substring(1));
                    conditionTable = new HashMap<>();
                } else if(line.charAt(0) == '-') {
                    responseTable.put(op, conditionTable);
                } else {
                    conditionTable.put(
                            ResponseCondition.valueOf(line.substring(0, line.indexOf(':'))),
                            line.substring(line.indexOf('"') + 1, line.length() - 1)
                    );
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static String getResponse(Opcode op, ResponseCondition outcome, String[] resInserts) {
        String response = responseTable.get(op).get(outcome);
        if(response != null) {
            response = String.format(response, resInserts);
        }
        return response;
    }

    static String getResponse(Opcode op, ResponseDetails outcome) {
        return getResponse(op, outcome.getCondition(), outcome.getInserts());
    }
}
