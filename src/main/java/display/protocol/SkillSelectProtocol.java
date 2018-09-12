package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;
import skill.SkillSet;

public class SkillSelectProtocol implements InputProtocol {

    private SkillSet skillSet;
    private CompoundEvent queuedEvent;

    public SkillSelectProtocol(SkillSet ss, CompoundEvent ce) {
        this.skillSet = ss;
        this.queuedEvent = ce;
    }

    @Override
    public void move(EventQueue eq, GUIFocus view, int x, int y) {
        skillSet.setFocusedSkillIndex(x);
    }

    @Override
    public CompoundEvent confirm(EventQueue eq, GUIFocus view) {
        queuedEvent.setSkill(skillSet.getFocusedSkill());
        view.hideSkillSelector();

        return queuedEvent;
    }

    @Override
    public void goBack(EventQueue eq, GUIFocus view) {
        view.hideSkillSelector();
    }
}
