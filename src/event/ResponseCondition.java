package event;

/**
 * Given a set of responses to use, the ResponseTable Condition determines the response of the
 * set to use, based on the outcome of the instruction that generated the response.
 */
enum ResponseCondition {
    SUCCESS,
    POSITIVE,
    NEUTRAL,
    NEGATIVE,
    FAILURE
}
