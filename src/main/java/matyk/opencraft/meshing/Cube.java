package matyk.opencraft.meshing;

import org.joml.Vector3f;

import java.util.Vector;

public class Cube {
    public static Vector3f[][] vertices = {
            new Vector3f[]{
                    new Vector3f(-0.5f, 0.5f, -0.5f),
                    new Vector3f(-0.5f, -0.5f, -0.5f),
                    new Vector3f(0.5f, -0.5f, -0.5f),
                    new Vector3f(0.5f, 0.5f, -0.5f),
            },
            new Vector3f[]{
                    new Vector3f(-0.5f, 0.5f, 0.5f),
                    new Vector3f(-0.5f, -0.5f, 0.5f),
                    new Vector3f(0.5f, -0.5f, 0.5f),
                    new Vector3f(0.5f, 0.5f, 0.5f),
            },
            new Vector3f[]{
                    new Vector3f(0.5f, 0.5f, -0.5f),
                    new Vector3f(0.5f, -0.5f, -0.5f),
                    new Vector3f(0.5f, -0.5f, 0.5f),
                    new Vector3f(0.5f, 0.5f, 0.5f),
            },
            new Vector3f[]{
                    new Vector3f(-0.5f, 0.5f, -0.5f),
                    new Vector3f(-0.5f, -0.5f, -0.5f),
                    new Vector3f(-0.5f, -0.5f, 0.5f),
                    new Vector3f(-0.5f, 0.5f, 0.5f),
            },
            new Vector3f[]{
                    new Vector3f(-0.5f, 0.5f, 0.5f),
                    new Vector3f(-0.5f, 0.5f, -0.5f),
                    new Vector3f(0.5f, 0.5f, -0.5f),
                    new Vector3f(0.5f, 0.5f, 0.5f),
            },
            new Vector3f[]{
                    new Vector3f(-0.5f, -0.5f, 0.5f),
                    new Vector3f(-0.5f, -0.5f, -0.5f),
                    new Vector3f(0.5f, -0.5f, -0.5f),
                    new Vector3f(0.5f, -0.5f, 0.5f)
            }
    };

    public static Vector3f[] normals = {
            new Vector3f(0, 0, -1),
            new Vector3f(0,0,1),
            new Vector3f(1, 0,0),
            new Vector3f(-1, 0,0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, -1, 0)
    };

    public static int[] indices = {
            0,1,3,
            3,1,2,

//            3,1,0,
//            2,1,3
    };

    /*public static int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
    };*/
}
