package Asset;

import Core.Cube;
import Geometry.Polygon;
import Geometry.Vertic;
import Util.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Asset.Paths.*;

public class ModelLoader {
    public static final List<Model> models = new ArrayList<>();
    private static Model defaultModel;

    public static void init() {
        if (models.isEmpty()) {
            File folder = new File(MODEL_FOLDER_PATH);
            if (folder.exists() && folder.isDirectory()) {
                File base = new File(MODEL_FOLDER_PATH, DEFAULT_MODEL_PATH);
                if (base.exists()) {
                    String name = base.getName().replace(".obj", "");
                    defaultModel = new Model(name, loadModel(DEFAULT_MODEL_PATH));
                    Logger.info("Base model is loaded");
                }

                File[] files = folder.listFiles((dir, name) -> name.endsWith(".obj"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().replace(".obj", "");
                        models.add(new Model(name, loadModel(file.getName())));
                        Logger.info("Loaded model: " + name);
                    }
                }
            }
            Logger.info("Models are initialized");
        } else {
            Logger.error("Models are already initialized");
        }
        models.add(new Model("Cube", Cube.createCube(new Vector3f(0f, 0, 0), 1f)));
    }

    public static ArrayList<Polygon> loadModel(String path) {
        ArrayList<Vertic> vertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Vector2f> uvs = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(MODEL_FOLDER_PATH + path));
            for (String line : lines) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "v" -> vertices.add(new Vertic(new Vector4f(Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]), 1), null));
                    case "vn" -> normals.add(new Vector3f(Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3])));
                    case "vt" -> uvs.add(new Vector2f(Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2])));
                    case "f" -> {
                        Vertic[] positions = new Vertic[3];
                        int normalIndex = -1;
                        for (int i = 0; i < 3; i++) {
                            String[] indexes = parts[1 + i].split("/");
                            positions[i] = new Vertic(vertices.get(Integer.parseInt(indexes[0]) - 1));
                            if (!indexes[1].isEmpty()) {
                                positions[i].uv = new Vector2f(uvs.get(Integer.parseInt(indexes[1]) - 1));
                            }
                            normalIndex = Integer.parseInt(indexes[2]) - 1;
                        }
                        polygons.add(new Polygon(positions, normals.get(normalIndex)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return polygons;
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
