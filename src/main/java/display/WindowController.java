package display;

import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A controller that manages the JFrame and the JPanels that may be implanted in it.
 */
public class WindowController {
    /**
     * The main display of the program.
     */
    private JFrame view;

    private long windowHandle;

    /**
     * The stack that denotes the order of the JPanels implanted into the JFrame.
     * The topmost JPanel is the one currently being shown.
     */
    private Stack<JComponent> viewStack;

    public WindowController() {
        /*view = new JFrame();
        initializeFrame(new Dimension(400, 400), new Dimension(1920, 1080));

        AudioPlayer ap = new AudioPlayer("audio/music", "audio/sfx");
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(ap);


        Tile.loadTraitMapping("map/terr_infomap.dat");

        this.viewStack = new Stack<>();
        ViewChanger continueGame = new GoToLoadGame("Continue", "saves/1.asf");
        ViewChanger startNewGame = new GoToNewGame("New Game");
        ViewChanger goToOptions = new GoToOptionsMenu("Options", view, this);
        MainMenuLogic mml = new MainMenuLogic(continueGame, startNewGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml, this, ap);

        addViewToTop(mmd);

        this.view.pack();
        this.view.setVisible(true);*/
    }

    public void runApplication() {
        System.out.println("Testing with LWJGL: " + Version.getVersion());

        initializeGLFW();
        runGraphicsLoop();

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Adds a new JPanel to the top of the view stack, as well as shown in the view instead of the previous JPanel.
     * The JFrame is revalidated to show this.
     */
    public void addViewToTop(JComponent newView) {
        if(!viewStack.isEmpty()) {
            view.remove(viewStack.peek());
        }
        viewStack.push(newView);
        view.add(viewStack.peek());
        refitView();
    }

    /**
     * Removes the top JPanel from the view stack. The JFrame will show the newest top JPanel.
     */
    public void removeTopView() {
        view.remove(viewStack.pop());
        if(viewStack.isEmpty()) {
            System.exit(0);
        } else {
            view.add(viewStack.peek());
            refitView();
        }
    }

    /**
     * Performs necessary JFrame revalidation after removing/adding JPanels.
     */
    public void refitView() {
        view.revalidate();
        view.repaint();
    }

    private void initializeFrame(Dimension minSize, Dimension maxSize) {
        view.setTitle("Asphodel Sky");
        view.setMinimumSize(minSize);
        view.setMaximumSize(maxSize);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        windowHandle = glfwCreateWindow(1600, 900, "AsphodelSky", NULL, NULL);

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }

    private void runGraphicsLoop() {
        GL.createCapabilities();
        System.out.println("Testing with OpenGL: " + glGetString(GL_VERSION));

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, new ShaderReader(GL_VERTEX_SHADER, "shaders/sample.vert").getShader());
        glAttachShader(shaderProgram, new ShaderReader(GL_FRAGMENT_SHADER, "shaders/sample.frag").getShader());
        glLinkProgram(shaderProgram);

        System.out.println(glGetProgramInfoLog(shaderProgram));

        glUseProgram(shaderProgram);

        //glDeleteShader(vertShader);
        //glDeleteShader(fragShader);

        FloatBuffer triangle = BufferUtils.createFloatBuffer(32);
        triangle.put(new float[]{
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f,
                0.5f, 0.5f, 0.0f,   0.0f, 0.0f, 1.0f,  1.0f, 0.0f,
                -0.5f, 0.5f, 0.0f,   1.0f, 1.0f, 1.0f,  0.0f, 0.0f
        }).flip();

        IntBuffer indices = BufferUtils.createIntBuffer(6);
        indices.put(new int[] {
                0, 1, 2,
                0, 2, 3
        }).flip();

        int textureId = Texture.loadTexture("img/terrain/floors.png");

        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, triangle, GL_STATIC_DRAW);

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        while(!glfwWindowShouldClose(windowHandle)) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                float time = (float)glfwGetTime();
                float offset = (float)Math.sin(time);
                int xOffsetLoc = glGetUniformLocation(shaderProgram, "xOffset");
                glUniform1f(xOffsetLoc, offset);

                glBindTexture(GL_TEXTURE_2D, textureId);
                glBindVertexArray(vaoID);
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

                glfwSwapBuffers(windowHandle);
                glfwPollEvents();


        }
    }
}
