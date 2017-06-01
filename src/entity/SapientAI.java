package entity;

/**
 * Created by Owner on 5/31/2017.
 */
public class SapientAI extends Combatant implements EnemyAI {
    public SapientAI(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
        super(name, title, desc, health, momentum, science, pse, sub, acu, cha, itt);
    }

    public void act(OperationAI opai) {
        opai.useSapient(this);
    }
}
