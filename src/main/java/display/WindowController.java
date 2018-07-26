package display;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
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

    private Camera c;

    private boolean isHeld = false;

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
        int vertShader = new ShaderReader(GL_VERTEX_SHADER, "shaders/sample.vert").getShader();
        int fragShader = new ShaderReader(GL_FRAGMENT_SHADER, "shaders/sample.frag").getShader();
        glAttachShader(shaderProgram, vertShader);
        glAttachShader(shaderProgram, fragShader);
        glLinkProgram(shaderProgram);

        System.out.println(glGetProgramInfoLog(shaderProgram));

        glUseProgram(shaderProgram);

        Matrix4f view = new Matrix4f();
        view.lookAt(new Vector3f(0.0f, 0.0f, 10.0f), new Vector3f(0.0f, 0.0f,0.0f), new Vector3f(0.0f, 1.0f, 0.0f));
        Matrix4f projection = new Matrix4f();
        projection.perspective((float)Math.toRadians(45.0f), 1600.0f/900.0f, 0.1f, 100.0f);
        Camera c = new Camera(view, projection, shaderProgram);

        Texture.loadTexture("img/terrain/floors.png");

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_UP && action == GLFW_PRESS) {
                c.accelerate(Direction.N);
            } else if(key == GLFW_KEY_UP && action == GLFW_RELEASE) {
                c.decelerate(Direction.N);
            } else if(key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                c.accelerate(Direction.W);
            } else if(key == GLFW_KEY_LEFT && action == GLFW_RELEASE) {
                c.decelerate(Direction.W);
            } else if(key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
                c.accelerate(Direction.S);
            } else if(key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
                c.decelerate(Direction.S);
            } else if(key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                c.accelerate(Direction.E);
            } else if(key == GLFW_KEY_RIGHT && action == GLFW_RELEASE) {
                c.decelerate(Direction.E);
            }
        });

        //glDeleteShader(vertShader);
        //glDeleteShader(fragShader);

        List<DrawnTile> grid = new LinkedList<>();

        for(float x = -25.0f; x < 25.0f; x++) {
            for(float y = -25.0f; y < 25.0f; y++) {
                grid.add(new DrawnTile(new Vector3f(x, y, 0.0f)));
            }
        }


        while(!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            c.alterView();

            for(DrawnTile dt : grid) {
                dt.draw(c);
            }

            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }
}
