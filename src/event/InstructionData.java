package event;

import javax.xml.crypto.Data;

/**
 * Created by Owner on 8/9/2017.
 */
public class InstructionData {
    private int casterID;
    private int targetID;
    private int itemID;
    private int skillID;
    private int tileX;
    private int tileY;
    private int secondary;

    public static class DataBuilder {
        private int casterID;
        private int targetID;
        private int itemID;
        private int skillID;
        private int tileX;
        private int tileY;
        private int secondary;

        public DataBuilder(int casterID) {
            this.casterID = casterID;
        }

        public DataBuilder(InstructionData oldData) {
            this.casterID = oldData.casterID;
            this.targetID = oldData.targetID;
            this.itemID = oldData.itemID;
            this.skillID = oldData.skillID;
            this.tileX = oldData.tileX;
            this.tileY = oldData.tileY;
            this.secondary = oldData.secondary;
        }

        public DataBuilder casterID(int casterID) {
            this.casterID = casterID;
            return this;
        }

        public DataBuilder targetID(int targetID) {
            this.targetID = targetID;
            return this;
        }

        public DataBuilder itemID(int itemID) {
            this.itemID = itemID;
            return this;
        }

        public DataBuilder tile(int x, int y) {
            this.tileX = x;
            this.tileY = y;
            return this;
        }

        public DataBuilder secondary(int secondary) {
            this.secondary = secondary;
            return this;
        }

        public InstructionData build() {
            return new InstructionData(this);
        }
    }

    public InstructionData(int casterID) {
        this.casterID = casterID;
    }

    public InstructionData(DataBuilder db) {
        this.casterID = db.casterID;
        this.targetID = db.targetID;
        this.itemID = db.itemID;
        this.skillID = db.skillID;
        this.tileX = db.tileX;
        this.tileY = db.tileY;
        this.secondary = db.secondary;
    }

    public InstructionData(int casterID, int targetID, int itemID, int skillID, int tileX, int tileY, int secondary) {
        this.casterID = casterID;
        this.targetID = targetID;
        this.itemID = itemID;
        this.skillID = skillID;
        this.tileX = tileX;
        this.tileY = tileY;
        this.secondary = secondary;
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
