package Core;

import Rendering.Renderer;
import Util.Logger;
import org.joml.Vector3f;

import java.util.ArrayList;

public final class Scene {
    private final ArrayList<Object> objects = new ArrayList<>();
    private final Vector3f lightDir = new Vector3f(0, 0, 1);
    private final Camera camera;

    public Scene(Renderer renderer, float cameraSpeed) {
        camera = new Camera(new Vector3f(0, 0, 0), renderer.getInput(), renderer.getMouse(), cameraSpeed);
        Logger.info("Scene was created");
    }

    public void setLight(float x, float y, float z) {
        lightDir.set(x, y, z);
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public Vector3f getLight() {
        return lightDir;
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
}
