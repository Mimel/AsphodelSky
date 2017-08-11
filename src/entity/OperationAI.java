package entity;

import event.SimpleEvent;
import grid.Grid;

/**
 * An operation that a given enemy will do, based on it's intelligence.
 */
public interface OperationAI {
    SimpleEvent[] useMindless(MindlessAI mai, int time, Grid gr);
    SimpleEvent[] useAnimalistic(AnimalisticAI aai, int time, Grid gr);
    SimpleEvent[] useUnderdeveloped(UnderdevelopedAI uai, int time, Grid gr);
    SimpleEvent[] useSapient(SapientAI sai, int time, Grid gr);
    SimpleEvent[] useBrilliant(BrilliantAI bai, int time, Grid gr);
}
