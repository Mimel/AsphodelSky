package entity;

import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

/**
 * An operation that a given enemy will do, based on it's intelligence.
 */
public interface OperationAI {
    List<SimpleEvent> useMindless(MindlessAI mai, int time, CompositeGrid gr);
    List<SimpleEvent> useAnimalistic(AnimalisticAI aai, int time, CompositeGrid gr);
    List<SimpleEvent> useUnderdeveloped(UnderdevelopedAI uai, int time, CompositeGrid gr);
    List<SimpleEvent> useSapient(SapientAI sai, int time, CompositeGrid gr);
    List<SimpleEvent> useBrilliant(BrilliantAI bai, int time, CompositeGrid gr);
}
