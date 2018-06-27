package event;

import item.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public class CompoundEvent extends Event<CompoundOpcode, CompoundEvent> {

    public CompoundEvent(int time, int priority, CompoundOpcode mo) {
        super(time, priority, mo);
    }

    public CompoundEvent(CompoundEvent ce) {
        super(ce.getTriggerDelay(), ce.getPriority(), ce.getOperation());
        this.setCasterID(ce.getCasterID());
        this.setTargetID(ce.getTargetID());
        this.setItemID(ce.getItemID());
        this.setSkillID(ce.getSkillID());
        this.setTile(ce.getTileX(), ce.getTileY());
        this.setSecondary(ce.getSecondary());
    }

    private SimpleEvent copyInfoToSimpleEvent(Opcode op) {
        return new SimpleEvent(getTriggerDelay(), getPriority(), op)
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
     * Note that the trigger delay of the Compound Event is equal to that of the current time; all
     * @return The list of events that this CompoundEvent decomposes to.
     */
    List<SimpleEvent> decomposeMacroEvent() {
        List<SimpleEvent> eventList = new LinkedList<>();
        this.setTriggerDelay(0);
        switch(getOperation()) {
            case USE_ITEM:
                eventList.add(copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM).withSecondary(1));
                Item target;
                if((target = Item.getItemById(getItemID())) != null) {
                    List<SimpleEvent> l = target.use(getTargetID());
                    for(SimpleEvent se : l) {
                        se.setTriggerDelay(se.getTriggerDelay() + getTriggerDelay());
                        eventList.add(se);
                    }
                }
                break;

            case DROP_ITEM:
                eventList.add(copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM).withSecondary(1));
                eventList.add(copyInfoToSimpleEvent(Opcode.TILE_SPAWN).withSecondary(1));
                break;

            case SHELL_TALK:
                eventList.add(copyInfoToSimpleEvent(Opcode.START_DIALOGUE));
                break;
        }

        return eventList;
    }
}
