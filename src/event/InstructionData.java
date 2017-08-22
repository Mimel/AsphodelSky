package event;

/**
 * A data object that encapsulates all the data necessary for an event to be used.
 */
public class InstructionData {
    private int casterID;
    private int targetID;
    private int itemID;
    private int skillID;
    private int tileX;
    private int tileY;
    private int secondary;

    public InstructionData(Event ev) {
        this.casterID = ev.getCasterID();
        this.targetID = ev.getTargetID();
        this.itemID = ev.getItemID();
        this.skillID = ev.getSkillID();
        this.tileX = ev.getTileX();
        this.tileY = ev.getTileY();
        this.secondary = ev.getSecondary();
    }

    int getCasterID() {
        return casterID;
    }

    int getTargetID() {
        return targetID;
    }

    int getItemID() {
        return itemID;
    }

    int getSkillID() {
        return skillID;
    }

    int getTileX() {
        return tileX;
    }

    int getTileY() {
        return tileY;
    }

    int getSecondary() {
        return secondary;
    }
}
