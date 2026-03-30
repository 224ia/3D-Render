package Core;

import Util.*;
import Util.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Scene {
    private final ArrayList<Object> objects = new ArrayList<>();
    private final Camera camera;

    public Scene(Renderer renderer, float cameraSpeed) {
        camera = new Camera(new Vector3f(0, 0, 0), renderer.getInput(), renderer.getMouse(), cameraSpeed);
    }

    public Camera getCamera() {
        return camera;
    }

    public void addObject(Vector3f position, Vector3f rotation, Vector3f scale, Color color, String modelName, String textureName) {
        Model model = AssetLoader.getModel(modelName);
        Texture texture = AssetLoader.getTexture(textureName);
        if (model != null) {
            objects.add(new Object(position, rotation, scale, color, model.getPolygons(), texture == null ? null : texture.getImage()));
        } else {
            System.out.println("No loaded model named: " + modelName);
        }
    }

    public List<RenderPolygon> setRenderPolygons(float deltaTime) {
        Matrix4f view = camera.update(deltaTime);
        List<RenderPolygon> renderPolygons = new ArrayList<>();
        for(Object obj : objects) {
            obj.render(view, renderPolygons);
        }

        return renderPolygons;
    }
}
