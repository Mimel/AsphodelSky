package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

/**
 * An operation that a given enemy will do, based on it's intelligence.
 */
public interface OperationAI {
    SimpleEvent[] useMindless(MindlessAI mai, int time, CompositeGrid gr);
    SimpleEvent[] useAnimalistic(AnimalisticAI aai, int time, CompositeGrid gr);
    SimpleEvent[] useUnderdeveloped(UnderdevelopedAI uai, int time, CompositeGrid gr);
    SimpleEvent[] useSapient(SapientAI sai, int time, CompositeGrid gr);
    SimpleEvent[] useBrilliant(BrilliantAI bai, int time, CompositeGrid gr);
}
