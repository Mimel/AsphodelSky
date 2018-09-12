package display;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

class Camera {
    private final Matrix4f view;

    private final FloatBuffer modelData;
    private final FloatBuffer viewData;

    private final int modelLoc;
    private final int viewLoc;

    private final boolean isAccelerating;
    private float timeSinceSpeedChangePrompt;
    private float accelerant;
    private final Direction direction;

    Camera(Matrix4f view, Matrix4f projection, int shaderProgram) {
        this.view = view;

        direction = Direction.C;

        this.isAccelerating = false;
        this.timeSinceSpeedChangePrompt = 0.0f;
        this.accelerant = 0.0f;

        this.modelLoc = glGetUniformLocation(shaderProgram, "model");
        this.viewLoc = glGetUniformLocation(shaderProgram, "view");
        int projLoc = glGetUniformLocation(shaderProgram, "projection");

        this.modelData = BufferUtils.createFloatBuffer(16);
        this.viewData = BufferUtils.createFloatBuffer(16);
        FloatBuffer projData = BufferUtils.createFloatBuffer(16);

        glUniformMatrix4fv(viewLoc, false, view.get(viewData));
        glUniformMatrix4fv(projLoc, false, projection.get(projData));
    }

    void alterModel(Matrix4f newMat) {
        glUniformMatrix4fv(modelLoc, false, newMat.get(modelData));
    }

    void alterView() {
                if(isAccelerating) {
                    accelerant += ((float)glfwGetTime() - timeSinceSpeedChangePrompt);
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
            view.translate(new Vector3f(direction.getVectorDirection()).mul(accelerant * 0.2f));
            //glUniformMatrix4fv(viewLoc, false, view.get(viewData));
    }

    void moveCameraTo(Vector3f newLoc) {
        view.m30(-newLoc.x).m31(-newLoc.y);
        glUniformMatrix4fv(viewLoc, false, view.get(viewData));
    }
}
