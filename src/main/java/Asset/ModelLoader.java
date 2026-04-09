package Asset;

import Asset.OpenGL.OpenGLModel;
import Asset.Software.SoftwareModel;
import Rendering.RendererType;
import Util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Asset.Paths.*;

public class ModelLoader {
    private static RendererType rendererType;

    public static final List<Model> models = new ArrayList<>();
    private static Model defaultModel;

    public static void init(RendererType rendererType) {
        ModelLoader.rendererType = rendererType;
        if (models.isEmpty()) {
            File folder = new File(MODEL_FOLDER_PATH);
            if (folder.exists() && folder.isDirectory()) {
                File base = new File(MODEL_FOLDER_PATH, DEFAULT_MODEL_PATH);
                if (base.exists()) {
                    String name = base.getName().replace(".obj", "");
                    defaultModel = loadModel(DEFAULT_MODEL_PATH, name);
                    Logger.info("Base model is loaded");
                }

                File[] files = folder.listFiles((dir, name) -> name.endsWith(".obj"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().replace(".obj", "");
                        models.add(loadModel(file.getName(), name));
                        Logger.info("Loaded model: " + name);
                    }
                }
            }
            Logger.info("Models are initialized");
        } else {
            Logger.error("Models are already initialized");
        }
//        models.add(new Model("Cube", Cube.createCube(new Vector3f(0f, 0, 0), 1f)));
    }

    public static Model loadModel(String path, String name) {
        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        ArrayList<Float> uvs = new ArrayList<>();
        ArrayList<Integer> polygons = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(MODEL_FOLDER_PATH + path));
            for (String line : lines) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "v" -> {
                        vertices.add(Float.parseFloat(parts[1]));
                        vertices.add(Float.parseFloat(parts[2]));
                        vertices.add(Float.parseFloat(parts[3]));
                    }
                    case "vn" -> {
                        normals.add(Float.parseFloat(parts[1]));
                        normals.add(Float.parseFloat(parts[2]));
                        normals.add(Float.parseFloat(parts[3]));
                    }
                    case "vt" -> {
                        uvs.add(Float.parseFloat(parts[1]));
                        uvs.add(Float.parseFloat(parts[2]));
                    }
                    case "f" -> {
                        int[][] faceVertices = new int[parts.length - 1][3];
                        for (int i = 1; i < parts.length; i++) {
                            faceVertices[i - 1] = parseFaceVertex(parts[i]);
                        }

                        for (int i = 1; i < faceVertices.length - 1; i++) {
                            addTriangle(polygons, faceVertices[0], faceVertices[i], faceVertices[i + 1]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return switch (rendererType) {
            case SOFTWARE -> new SoftwareModel(name, vertices, normals, uvs, polygons);
            case OPEN_GL -> new OpenGLModel(name, vertices, normals, uvs, polygons);
        };
    }

    public static Model getModel(String name) {
        Model got = models.stream()
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (got != null) return got;
        if (name != null) {
            Logger.warn("No model named: " + name);
        }
        if (defaultModel != null) {
            return defaultModel;
        }
        if (!models.isEmpty()) {
            Logger.warn("Default model is missing, using the first loaded model");
            return models.getFirst();
        }
        Logger.error("No models were loaded");
        return null;
    }

    private static int[] parseFaceVertex(String facePart) {
        String[] indexes = facePart.split("/");
        int vertexIndex = Integer.parseInt(indexes[0]) - 1;
        int uvIndex = indexes.length > 1 && !indexes[1].isEmpty() ? Integer.parseInt(indexes[1]) - 1 : -1;
        int normalIndex = indexes.length > 2 && !indexes[2].isEmpty() ? Integer.parseInt(indexes[2]) - 1 : -1;
        return new int[] { vertexIndex, uvIndex, normalIndex };
    }

    private static void addTriangle(List<Integer> polygons, int[] v0, int[] v1, int[] v2) {
        polygons.add(v0[0]);
        polygons.add(v0[1]);
        polygons.add(v1[0]);
        polygons.add(v1[1]);
        polygons.add(v2[0]);
        polygons.add(v2[1]);
        polygons.add(v0[2] >= 0 ? v0[2] : v1[2] >= 0 ? v1[2] : v2[2]);
    }
}
