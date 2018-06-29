package event;

import item.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * An event, that when executed, performs multiple events.
 */
public class CompoundEvent extends Event<CompoundOpcode> {

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
        SimpleEvent se = new SimpleEvent(getTriggerDelay(), getPriority(), op);
        se.getData().setCasterIDTo(getCasterID())
                .setTargetIDTo(getTargetID())
                .setItemIDTo(getItemID())
                .setSkillIDTo(getSkillID())
                .setCoordTo(getTileX(), getTileY())
                .setSecondaryTo(getSecondary());
        return se;
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
                SimpleEvent useClause = copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM);
                useClause.getData().setSecondaryTo(1);
                eventList.add(useClause);
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
                SimpleEvent dropClause1 = copyInfoToSimpleEvent(Opcode.COMBATANT_REMOVE_ITEM);
                SimpleEvent dropClause2 = copyInfoToSimpleEvent(Opcode.TILE_SPAWN);

                dropClause1.getData().setSecondaryTo(1);
                dropClause2.getData().setSecondaryTo(1);

                eventList.add(dropClause1);
                eventList.add(dropClause2);
                break;

            case SHELL_TALK:
                eventList.add(copyInfoToSimpleEvent(Opcode.START_DIALOGUE));
                break;
        }

        return eventList;
    }
}
