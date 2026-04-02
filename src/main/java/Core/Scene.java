package Core;

import Geometry.RenderPolygon;
import Rendering.Renderer;
import Util.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public final class Scene {
    private final ArrayList<Object> objects = new ArrayList<>();
    private final Camera camera;

    public Scene(Renderer renderer, float cameraSpeed) {
        camera = new Camera(new Vector3f(0, 0, 0), renderer.getInput(), renderer.getMouse(), cameraSpeed);
        Logger.info("Scene was created");
    }

    public Camera getCamera() {
        return camera;
    }

    public void addObject(Object object) {
        objects.add(object);
        Logger.info("Added Object to the scene");
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
