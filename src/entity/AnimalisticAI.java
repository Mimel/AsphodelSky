package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class AnimalisticAI extends Combatant {

    public AnimalisticAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    public AnimalisticAI(AnimalisticAI aai) {
        super(aai.getName(), aai.getTitle(), aai.getDesc(),
                aai.getMaxHealth(), aai.getMomentum(), aai.getMaximumScience(),
                aai.getPoise(), aai.getSubtlety(), aai.getAcumen(), aai.getCharisma(), aai.getIntuition());
    }

    public void act(OperationAI opai) {
        opai.useAnimalistic(this);
    }
}
