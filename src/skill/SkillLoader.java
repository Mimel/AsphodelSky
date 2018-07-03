package skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SkillLoader {
    private static Map<Integer, Skill> skillNameToSkill;

    private SkillLoader(){}

    public static void initializeSkillMap(String fileName) {
        if(skillNameToSkill == null) {
            skillNameToSkill = new HashMap<>();
        } else {
            return;
        }

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
                    skillNameToSkill.put(newestSkill.getId(), newestSkill);
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

    public static Skill getSkillByID(int id) {
        return skillNameToSkill.get(id);
    }
}
