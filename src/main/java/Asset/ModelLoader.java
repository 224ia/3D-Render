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
        int verticCount = 0;
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
                        verticCount++;
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
                        int normalIndex = -1;
                        for (int i = 0; i < 3; i++) {
                            String[] indexes = parts[1 + i].split("/");
                            polygons.add(Integer.parseInt(indexes[0]) - 1);
                            polygons.add(indexes[1].isEmpty() ? -1 : (Integer.parseInt(indexes[1]) - 1));
                            normalIndex = Integer.parseInt(indexes[2]) - 1;
                        }
                        polygons.add(normalIndex);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return switch (rendererType) {
            case SOFTWARE -> new SoftwareModel(name, vertices, normals, uvs, polygons);
            case OPEN_GL -> new OpenGLModel(name, verticCount, vertices, normals, uvs, polygons);
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
        return defaultModel;
    }
}
