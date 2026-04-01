package Util;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class AssetLoader {
    public static final String RESOURCES_FOLDER_PATH = "src/main/resources/";
    public static final String ASSETS_FOLDER_PATH = RESOURCES_FOLDER_PATH + "Assets/";
    public static final String MODEL_FOLDER_PATH = ASSETS_FOLDER_PATH + "Models/";
    public static final String TEXTURES_FOLDER_PATH = ASSETS_FOLDER_PATH + "Textures/";

    public static final List<Model> models = new ArrayList<>();
    public static final List<Texture> textures = new ArrayList<>();

    public static void init() {
        if (textures.isEmpty()) {
            File folder = new File(TEXTURES_FOLDER_PATH);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().replace(".png", "");
                        try {
                            BufferedImage image = ImageIO.read(file);
                            textures.add(new Texture(name, image));
                            System.out.println("Loaded texture: " + name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (models.isEmpty()) {
            File folder = new File(MODEL_FOLDER_PATH);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".obj"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().replace(".obj", "");
                        models.add(new Model(name, loadModel(file.getName())));
                        System.out.println("Loaded model: " + name);
                    }
                }
            }
        }
    }

    public static ArrayList<Polygon> loadModel(String path) {
        ArrayList<Vertic> vertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Vector2f> uvs = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();
        try {
            int it = 0;
            List<String> lines = Files.readAllLines(Paths.get(MODEL_FOLDER_PATH + path));
            for (String line : lines) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "v" -> {
                        vertices.add(new Vertic(new Vector4f(Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3]), 1), null));
                        it = (it + 1) % 3;
                    }
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
        return models.stream()
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static Texture getTexture(String name) {
        return textures.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
