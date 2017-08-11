package event;

import item.Item;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public class CompoundEvent extends Event<CompoundOpcode> {

    public CompoundEvent(int time, int priority, CompoundOpcode mo) {
        super(time, priority, mo);
    }

    private SimpleEvent copyInfoToSimpleEvent(Opcode op) {
        return (SimpleEvent) new SimpleEvent(getTriggerDelay(), getPriority(), op)
                .withCasterID(getCasterID())
                .withTargetID(getTargetID())
                .withItemID(getItemID())
                .withSkillID(getSkillID())
                .withTile(getTileX(), getTileY())
                .withSecondary(getSecondary());
    }

    /**
     * Reduces this CompoundEvent into a set of smaller events based on the Macro opcode and returns those
     * events.
     * @return The list of events that this CompoundEvent decomposes to.
     */
    List<SimpleEvent> decomposeMacroEvent() {
        List<SimpleEvent> eventList = new LinkedList<>();
        switch(getOperation()) {
            case USE_ITEM:

                //TEST
                Item i = Item.getItemById(0);
                eventList.add((SimpleEvent) copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM).withSecondary(1));
                eventList.addAll(Item.getItemById(getItemID()).use(getTargetID()));
                break;
            case DROP_ITEM:
                eventList.add((SimpleEvent) copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM).withSecondary(1));
                eventList.add((SimpleEvent) copyInfoToSimpleEvent(Opcode.TILE_SPAWN).withSecondary(1));
                break;
        }

        return eventList;
    }
}
