package matyk.opencraft.meshing;

import com.google.common.primitives.Ints;
import io.kaitai.struct.ByteBufferKaitaiStream;
import matyk.opencraft.meshing.jvox.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mesher {

    static ArrayList<Vector3f> verts = new ArrayList<>();
    static ArrayList<Vector3f> norms = new ArrayList<>();
    static ArrayList<Integer> inds = new ArrayList<>();
    static ArrayList<Vector4f> tint = new ArrayList<>();

    static int index = 0;

    private static void meshCube(Voxel vox, Color[] pallet) {
        List<Vector3f> vertPosNZ = new ArrayList<>(Arrays.asList(Cube.vertices[0]));
        List<Vector3f> vertPosPZ = new ArrayList<>(Arrays.asList(Cube.vertices[1]));
        List<Vector3f> vertPosPX = new ArrayList<>(Arrays.asList(Cube.vertices[2]));
        List<Vector3f> vertPosNX = new ArrayList<>(Arrays.asList(Cube.vertices[3]));
        List<Vector3f> vertPosPY = new ArrayList<>(Arrays.asList(Cube.vertices[4]));
        List<Vector3f> vertPosNY = new ArrayList<>(Arrays.asList(Cube.vertices[5]));
        List<Integer> indsList = new ArrayList<>(Ints.asList(Cube.indices));
        List<Vector3f> normals = new ArrayList<>(Arrays.asList(Cube.normals));

        vertPosNZ = vertPosNZ.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());
        vertPosPZ = vertPosPZ.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());
        vertPosPX = vertPosPX.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());
        vertPosNX = vertPosNX.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());
        vertPosPY = vertPosPY.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());
        vertPosNY = vertPosNY.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());

        /*Collections.reverse(vertPosNZ);
        Collections.reverse(vertPosPZ);
        Collections.reverse(vertPosPX);
        Collections.reverse(vertPosNX);
        Collections.reverse(vertPosPY);
        Collections.reverse(vertPosNY);*/

        //Collections.reverse(indsList);

        for(int i = 0; i < 6; i++) {
            inds.addAll(indsList.stream().map(integer -> integer+4*index).collect(Collectors.toList()));
            index++;
        }

        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 4; j++)
                norms.add(normals.get(i));
        }

        ArrayList<Vector3f> vertPoses = new ArrayList<>();
        vertPoses.addAll(vertPosNZ);
        vertPoses.addAll(vertPosPZ);
        vertPoses.addAll(vertPosPX);
        vertPoses.addAll(vertPosNX);
        vertPoses.addAll(vertPosPY);
        vertPoses.addAll(vertPosNY);

        verts.addAll(vertPoses);

        vertPoses.forEach(v -> {
            Color color = pallet[vox.getColourIndex()+7];

            tint.add(new Vector4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        });
    }

    public static void mesh() {
        InputStream voxStream = Mesher.class.getResourceAsStream("/models/" + "Leaves.vox");

        VoxFile voxFile = null;

        try (VoxReader reader = new VoxReader(voxStream)) {
            // VoxReader::read will never return null,
            // but it can throw an InvalidVoxException.
            voxFile = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // VoxFile::getMaterials returns a map from material ID to material.
        // If your vox file contains the deprecated materials
        // stored in MATT chunks, use VoxFile::getOldMaterials instead.
        HashMap<Integer, VoxMaterial> materials = voxFile.getMaterials();

        // VoxFile::getPalette returns the palette used for the model.
        // The palette is an array of ints formatted as R8G8B8A8.


        Color[] palett = new Color[] {
                new Color(39, 133, 0, 255),
                new Color(20, 74, 0, 255),
                new Color(46, 163, 0, 255)
        };

        // VoxFile::getModels returns all the models used in the file.
        // Any valid .vox file must contain at least one model,
        // therefore models[0] will never be null.
        VoxModel[] models = voxFile.getModels();
        VoxModel model = models[0];

        // And finally, actually retrieving the voxels.
        Vector3<Integer> size = model.getSize();
        Voxel[] voxels = model.getVoxels();

        // These voxels can then be used as such.
        for(Voxel vox:voxels) {
            meshCube(vox, palett);
        }

        //meshCube(voxels[0], palett);
    }

    public static ArrayList<Vector3f> getVerts() {
        return verts;
    }

    public static ArrayList<Vector3f> getNorms() {
        return norms;
    }

    public static ArrayList<Integer> getInds() {
        return inds;
    }

    public static ArrayList<Vector4f> getTint() {
        return tint;
    }
}
