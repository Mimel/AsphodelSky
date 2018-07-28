package display;

import display.game.focus.GUIFocus;
import display.image.ImageAssets;
import event.EventQueue;
import event.InstructionSet;
import event.ResponseTable;
import grid.CompositeGrid;
import grid.Tile;
import grid.creation.GridLoaderRectangles;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A controller that manages the JFrame and the JPanels that may be implanted in it.
 */
public class WindowController {

    private long windowHandle;

    private GUIFocus windowDisplay;

    public void runApplication() {
        System.out.println("Testing with LWJGL: " + Version.getVersion());

        initializeGLFW();
        runGraphicsLoop();

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void initializeGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        windowHandle = glfwCreateWindow(1600, 900, "AsphodelSky", NULL, NULL);

        Tile.loadTraitMapping("map/terr_infomap.dat");

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

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
        glEnable(GL_DEPTH_TEST);
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

        ImageAssets ia = new ImageAssets();

        /*glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
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
        });*/

        CompositeGrid model = new GridLoaderRectangles().loadGrid();
        windowDisplay = new GUIFocus(model, c, ia);
        GameKeyBindings kb = new GameKeyBindings(windowHandle, windowDisplay, model, new EventQueue(new InstructionSet(new ResponseTable("map/responsemap.dat"))));
        //glDeleteShader(vertShader);
        //glDeleteShader(fragShader);



        while(!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            c.alterView();

            windowDisplay.draw();

            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }
}
