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

    InstructionData() {

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

    public InstructionData setCasterIDTo(int CID) {
        this.casterID = CID;
        return this;
    }

    public InstructionData setTargetIDTo(int TID) {
        this.targetID = TID;
        return this;
    }

    public InstructionData setItemIDTo(int IID) {
        this.itemID = IID;
        return this;
    }

    public InstructionData setSkillIDTo(int SID) {
        this.skillID = SID;
        return this;
    }

    public InstructionData setCoordTo(int x, int y) {
        this.tileX = x;
        this.tileY = y;
        return this;
    }

    public InstructionData setSecondaryTo(int sec) {
        this.secondary = sec;
        return this;
    }
}
