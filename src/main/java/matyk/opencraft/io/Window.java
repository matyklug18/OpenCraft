package matyk.opencraft.io;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    public long winID;

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        
        glfwDefaultWindowHints(); 
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); 
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        winID = glfwCreateWindow(1000, 1000, "Hello World!", NULL, NULL);
        if (winID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(winID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); 
        });
        
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); 
            IntBuffer pHeight = stack.mallocInt(1); 

            glfwGetWindowSize(winID, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    winID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } 

        glfwMakeContextCurrent(winID);
        
        glfwSwapInterval(1);
        
        glfwShowWindow(winID);

        GL.createCapabilities();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(winID); 
    }
}
