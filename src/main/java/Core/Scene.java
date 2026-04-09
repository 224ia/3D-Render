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

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

    public void addObject(Object object) {
        if (object != null) {
            objects.add(object);
            Logger.info(String.format("Object \"%s\" added to the scene", object.name));
        } else {
            Logger.warn("Object can't be added to scene since it is null");
        }
    }

    public List<RenderPolygon> setRenderPolygons() {
        Matrix4f view = camera.update();
        List<RenderPolygon> renderPolygons = new ArrayList<>();
        for(Object obj : objects) {
        }

        return renderPolygons;
    }
}
