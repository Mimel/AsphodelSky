package entity;

import event.Event;
import grid.Grid;

/**
 * An operation that a given enemy will do, based on it's intelligence.
 */
public interface OperationAI {
    Event[] useMindless(MindlessAI mai, int time, Grid gr);
    Event[] useAnimalistic(AnimalisticAI aai, int time, Grid gr);
    Event[] useUnderdeveloped(UnderdevelopedAI uai, int time, Grid gr);
    Event[] useSapient(SapientAI sai, int time, Grid gr);
    Event[] useBrilliant(BrilliantAI bai, int time, Grid gr);
}
