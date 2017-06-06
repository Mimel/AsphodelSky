package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class UnderdevelopedAI extends Combatant {
    UnderdevelopedAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    UnderdevelopedAI(UnderdevelopedAI uai) {
        super(uai.getName(), uai.getTitle(), uai.getDesc(),
                uai.getMaxHealth(), uai.getMomentum(), uai.getMaximumScience(),
                uai.getPoise(), uai.getSubtlety(), uai.getAcumen(), uai.getCharisma(), uai.getIntuition());
    }

    public void act(OperationAI opai) {
        opai.useUnderdeveloped(this);
    }
}
