package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class BrilliantAI extends Combatant {
    BrilliantAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    BrilliantAI(BrilliantAI bai) {
        super(bai.getName(), bai.getTitle(), bai.getDesc(),
                bai.getMaxHealth(), bai.getMomentum(), bai.getMaximumScience(),
                bai.getPoise(), bai.getSubtlety(), bai.getAcumen(), bai.getCharisma(), bai.getIntuition());
    }

    public void act(OperationAI opai) {
        opai.useBrilliant(this);
    }
}
