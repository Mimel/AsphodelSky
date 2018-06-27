package event;

/**
 * Quasi-wrapper class meant to encapsulate a condition to a response, as well as the strings
 * to insert in the response.
 */
class ResponseDetails {
    private final ResponseCondition cond;

    private final String[] inserts;

    ResponseDetails(ResponseCondition condition, String... inserts) {
        this.cond = condition;

        this.inserts = new String[inserts.length];
        int i = 0;
        for(String param : inserts) {
            this.inserts[i++] = param;
        }
    }

    ResponseCondition getCondition() {
        return cond;
    }

    String[] getInserts() {
        return inserts;
    }
}
