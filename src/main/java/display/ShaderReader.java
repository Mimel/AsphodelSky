package display;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class ShaderReader {
    private int shader;

    public ShaderReader(int shaderType, String fileName) {
        shader = glCreateShader(shaderType);

        StringBuilder code = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currLine;
            while((currLine = br.readLine()) != null) {
                code.append(currLine).append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(shader, code.toString());
        glCompileShader(shader);
        System.out.println(glGetShaderInfoLog(shader));
    }

    public int getShader() {
        return shader;
    }
}
