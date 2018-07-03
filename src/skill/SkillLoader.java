package skill;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public final class SkillLoader {
    private static Map<String, Skill> skillNameToSkill;

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
                    skillNameToSkill.put(name, new Skill(name, vDesc, uDesc, effects.toString()));
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

    public static Skill getSkillByName(String name) {
        return skillNameToSkill.get(name);
    }
}
