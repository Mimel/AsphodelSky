package event;

/**
 * A data object that encapsulates all the data necessary for an event to be used.
 */
class InstructionData {
    private final int casterID;
    private final int targetID;
    private final int itemID;
    private final int skillID;
    private final int tileX;
    private final int tileY;
    private final int secondary;

    public InstructionData(Event ev) {
        this.casterID = ev.getCasterID();
        this.targetID = ev.getTargetID();
        this.itemID = ev.getItemID();
        this.skillID = ev.getSkillID();
        this.tileX = ev.getTileX();
        this.tileY = ev.getTileY();
        this.secondary = ev.getSecondary();
    }

// --Commented out by Inspection START (6/27/2018 5:27 PM):
//    int getCasterID() {
//        return casterID;
//    }
// --Commented out by Inspection STOP (6/27/2018 5:27 PM)

    int getTargetID() {
        return targetID;
    }

    int getItemID() {
        return itemID;
    }

// --Commented out by Inspection START (6/27/2018 5:27 PM):
//    int getSkillID() {
//        return skillID;
//    }
// --Commented out by Inspection STOP (6/27/2018 5:27 PM)

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
