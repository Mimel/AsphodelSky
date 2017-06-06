package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class MindlessAI extends Combatant {

    MindlessAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    MindlessAI(MindlessAI mai) {
        super(mai.getName(), mai.getTitle(), mai.getDesc(),
                mai.getMaxHealth(), mai.getMomentum(), mai.getMaximumScience(),
                mai.getPoise(), mai.getSubtlety(), mai.getAcumen(), mai.getCharisma(), mai.getIntuition());
    }

    public void act(OperationAI opai) {
        opai.useMindless(this);
    }
}
