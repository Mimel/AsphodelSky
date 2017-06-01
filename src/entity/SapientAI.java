package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class SapientAI extends Combatant {
    public SapientAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    public SapientAI(SapientAI sai) {
        super(sai.getName(), sai.getTitle(), sai.getDesc(),
                sai.getMaxHealth(), sai.getMomentum(), sai.getMaximumScience(),
                sai.getPoise(), sai.getSubtlety(), sai.getAcumen(), sai.getCharisma(), sai.getIntuition());
    }

    public void act(OperationAI opai) {
        opai.useSapient(this);
    }
}
