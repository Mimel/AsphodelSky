package entity;

import grid.Grid;

/**
 * An operation that a given enemy will do, based on it's intelligence.
 */
public interface OperationAI {
    void useMindless(MindlessAI mai, int time, Grid gr);
    void useAnimalistic(AnimalisticAI aai, int time, Grid gr);
    void useUnderdeveloped(UnderdevelopedAI uai, int time, Grid gr);
    void useSapient(SapientAI sai, int time, Grid gr);
    void useBrilliant(BrilliantAI bai, int time, Grid gr);
}
