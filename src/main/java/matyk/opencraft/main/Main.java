package matyk.opencraft.main;

import matyk.opencraft.io.Window;
import matyk.opencraft.meshing.Mesher;
import matyk.opencraft.render.Renderer;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        window.init();

        Mesher.mesh();

        Renderer.initMesh(Mesher.getVerts(), Mesher.getNorms(), Mesher.getInds(), Mesher.getTint());

        while(!glfwWindowShouldClose(window.winID)) {
            window.update();
            Renderer.render();
            window.swapBuffers();
        }
    }
}
