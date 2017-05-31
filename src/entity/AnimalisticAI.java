package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class AnimalisticAI extends Combatant implements EnemyAI {

    public AnimalisticAI(int id, String name, String title, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(id, name, title, health, momentum, science, pse, sub, acu, cha, itt);
    }

    public void act(OperationAI opai) {
        opai.useAnimalistic(this);
    }
}
