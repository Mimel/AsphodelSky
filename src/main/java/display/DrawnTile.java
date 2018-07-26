package display;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DrawnTile {
    private int vaoID;

    private float[] shape;
    private int[] indices;
    private Vector3f position;

    public DrawnTile(Vector3f position) {
        shape = new float[]{
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f,
                0.5f, 0.5f, 0.0f,   0.0f, 0.0f, 1.0f,  1.0f, 0.0f,
                -0.5f, 0.5f, 0.0f,   1.0f, 1.0f, 1.0f,  0.0f, 0.0f
        };

        indices = new int[] {
                0, 1, 2,
                0, 2, 3
        };

        this.position = position;

        init();
    }

    public DrawnTile(float[] vertices, int[] indices, Vector3f position) {
        this.shape = vertices;
        this.indices = indices;
        this.position = position;
        init();
    }

    private void init() {
        FloatBuffer fb = BufferUtils.createFloatBuffer(shape.length);
        fb.put(shape).flip();

        IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
        ib.put(indices).flip();

        this.vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw(Camera c) {
        glBindVertexArray(vaoID);

        c.alterModel(new Matrix4f().translate(position));

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
