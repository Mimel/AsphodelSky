package display;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Camera {
    private Matrix4f view;
    private Matrix4f projection;

    private final FloatBuffer modelData;
    private final FloatBuffer viewData;
    private final FloatBuffer projData;

    private int modelLoc;
    private int viewLoc;
    private int projLoc;

    private boolean isAccelerating;
    private float timeSinceSpeedChangePrompt;
    private float accelerant;

    public Camera(Matrix4f view, Matrix4f projection, int shaderProgram) {
        this.view = view;
        this.projection = projection;

        this.isAccelerating = false;
        this.timeSinceSpeedChangePrompt = 0.0f;
        this.accelerant = 0.0f;

        this.modelLoc = glGetUniformLocation(shaderProgram, "model");
        this.viewLoc = glGetUniformLocation(shaderProgram, "view");
        this.projLoc = glGetUniformLocation(shaderProgram, "projection");

        this.modelData = BufferUtils.createFloatBuffer(16);
        this.viewData = BufferUtils.createFloatBuffer(16);
        this.projData = BufferUtils.createFloatBuffer(16);

        glUniformMatrix4fv(viewLoc, false, view.get(viewData));
        glUniformMatrix4fv(projLoc, false, projection.get(projData));
    }

    public void alterModel(Matrix4f newMat) {
        glUniformMatrix4fv(modelLoc, false, newMat.get(modelData));
    }

    public void alterView() {
        if(accelerant != 0.0f || isAccelerating) {
                if(isAccelerating) {
                    accelerant = (float)glfwGetTime() - timeSinceSpeedChangePrompt;
                    if(accelerant > 1.0f) {
                        accelerant = 1.0f;
                    }
                } else {
                    accelerant -= ((float)glfwGetTime() - timeSinceSpeedChangePrompt);
                    timeSinceSpeedChangePrompt = (float)glfwGetTime();
                    if(accelerant < 0.0f) {
                        accelerant = 0.0f;
                    }
            }

            view.translate(new Vector3f(0.09f * accelerant, 0.0f, 0.0f));
            glUniformMatrix4fv(viewLoc, false, view.get(viewData));
        }
    }

    public void accelerate() {
        timeSinceSpeedChangePrompt = (float)glfwGetTime();
        isAccelerating = true;
    }

    public void decelerate() {
        timeSinceSpeedChangePrompt = (float)glfwGetTime();
        isAccelerating = false;
    }
}
