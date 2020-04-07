package matyk.opencraft.render;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import matyk.opencraft.meshing.jvox.Vector3;
import matyk.opencraft.utils.MatrixUtils;
import matyk.opencraft.utils.StringLoader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.FloatLists;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    static int vaoID;
    static int vbo, ibo, nbo, tbo;

    static int PID;

    static ArrayList<Vector3f> verts;
    static ArrayList<Vector3f> norms;
    static ArrayList<Integer> inds;
    static ArrayList<Vector4f> tint;


    public static void initMesh(ArrayList<Vector3f> verts, ArrayList<Vector3f> norms, ArrayList<Integer> inds, ArrayList<Vector4f> tint) {

        PID = GL20.glCreateProgram();

        int VID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(VID, StringLoader.loadResourceAsString("shaders/vertex.glsl"));
        GL20.glCompileShader(VID);
        if (GL20.glGetShaderi(VID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(VID));
        }

        int FID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(FID, StringLoader.loadResourceAsString("shaders/fragment.glsl"));
        GL20.glCompileShader(FID);
        if (GL20.glGetShaderi(FID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(FID));
        }

        GL20.glAttachShader(PID, VID);
        GL20.glAttachShader(PID, FID);

        GL20.glLinkProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(PID));
        }

        GL20.glValidateProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(PID));
        }

        Renderer.verts = verts;
        Renderer.norms = norms;
        Renderer.inds = inds;
        Renderer.tint = tint;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vbo = glGenBuffers();



        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Floats.toArray(Doubles.asList(verts.stream().flatMap((Function<Vector3f, Stream<Float>>) vector -> Stream.of(vector.x, vector.y, vector.z)).mapToDouble(Float::doubleValue).toArray())), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        nbo = glGenBuffers();

        /*FloatBuffer nboBuffer = MemoryUtil.memAllocFloat(norms.size() * 3).put(Floats.toArray(Doubles.asList(norms.stream().flatMap((Function<Vector3f, Stream<Float>>) vector -> Stream.of(vector.x, vector.y, vector.z)).mapToDouble(Float::doubleValue).toArray()))).flip();

        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, nboBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);*/

        tbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, Floats.toArray(Doubles.asList(tint.stream().flatMap((Function<Vector4f, Stream<Float>>) vector -> Stream.of(vector.x, vector.y, vector.z, vector.w)).mapToDouble(Float::doubleValue).toArray())), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ibo = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Ints.toArray(inds), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public static void render() {
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);


        glUseProgram(PID);

        setUniform("project", MatrixUtils.projectionMatrix(70, 1, 0.1f, 100f));
        setUniform("view", MatrixUtils.viewMatrix(new Vector3f(8,8, 32), new Vector3f(0,0,0)));
        setUniform("model", MatrixUtils.transformationMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1)));


        glDrawElements(GL_TRIANGLES, inds.size(), GL_UNSIGNED_INT, 0);

        glUseProgram(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public static void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(PID, name), false, matrixB);
        MemoryUtil.memFree(matrixB);
    }
}
