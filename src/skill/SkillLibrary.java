package skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SkillLibrary {
    private Map<Integer, Skill> skillIdToSkill;
    private Map<String, Skill> skillNameToSkill;

    public SkillLibrary(String fileName) {
        skillIdToSkill = new HashMap<>();
        skillNameToSkill = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            String name = "";
            String vDesc = "";
            String uDesc = "";
            StringBuilder effects = new StringBuilder();

            while((line = br.readLine()) != null) {
                if(name.equals("")) {
                    name = line;
                } else if(vDesc.equals("")) {
                    vDesc = line;
                } else if(uDesc.equals("")) {
                    uDesc = line;
                } else if(line.equals("!END")) {
                    Skill newestSkill = new Skill(name, vDesc, uDesc, effects.toString());
                    skillIdToSkill.put(newestSkill.getId(), newestSkill);
                    skillNameToSkill.put(name, newestSkill);
                    name = "";
                    vDesc = "";
                    uDesc = "";
                    effects = new StringBuilder();
                } else {
                    effects.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Skill getSkillByID(int id) {
        return new Skill(skillIdToSkill.get(id));
    }

    Skill getSkillByName(String name) {
        return new Skill(skillNameToSkill.get(name));
    }
}
