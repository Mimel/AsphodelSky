package display;

import display.game.focus.GUIFocus;
import display.image.ImageAssets;
import event.EventQueue;
import event.InstructionSet;
import event.ResponseTable;
import grid.CompositeGrid;
import grid.Tile;
import grid.creation.GridLoaderFromFile;
import item.ItemLibrary;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import skill.SkillLibrary;

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

    private int windowWidth;
    private int windowHeight;

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

            windowWidth = pWidth.get(0);
            windowHeight = pHeight.get(0);

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
        glEnable(GL_STENCIL_TEST);
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

        CompositeGrid model = new GridLoaderFromFile("saves/1.asf", new ItemLibrary("map/item_effectmap.dat"), new SkillLibrary("map/skill_effectmap.dat")).loadGrid();
        windowDisplay = new GUIFocus(model.getPlayer(), new Stage(model, ia, c), new GraphicInstructionSet(), windowWidth, windowHeight);
        GameKeyBindings kb = new GameKeyBindings(windowHandle, windowDisplay, model, new EventQueue(new InstructionSet(new ResponseTable("map/responsemap.dat"))));


        while(!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            glEnable(GL_DEPTH_TEST);
            glEnable(GL_STENCIL_TEST);
            glUseProgram(shaderProgram);
            glfwPollEvents();

            c.alterView();

            windowDisplay.draw();

            glfwSwapBuffers(windowHandle);
        }

        ia.deleteAllTextures();

    }
}
