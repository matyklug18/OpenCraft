package matyk.opencraft.meshing;

import com.google.common.primitives.Ints;
import matyk.opencraft.meshing.jvox.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
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

    private static void meshCube(Voxel vox, int[] palette) {
        List<Vector3f> vertPos = new ArrayList<>(Arrays.asList(Cube.vertices));
        List<Integer> indsList = new ArrayList<>(Ints.asList(Cube.indices));

        vertPos = vertPos.stream().map(vector -> vector.add(vox.getPosition().getX(), vox.getPosition().getY(), vox.getPosition().getZ(), new Vector3f())).collect(Collectors.toList());

        indsList = indsList.stream().map(integer -> integer+23*index).collect(Collectors.toList());

        verts.addAll(vertPos);

        inds.addAll(indsList);

        vertPos.forEach(v -> {
            int color = palette[vox.getColourIndex()+127];

            Color colorC = new Color(color, true);

            tint.add(new Vector4f(colorC.getRed(), colorC.getGreen(), colorC.getBlue(), colorC.getAlpha()));
        });

        index++;
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
        int[] palette = voxFile.getPalette();

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
            meshCube(vox, palette);
        }
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
