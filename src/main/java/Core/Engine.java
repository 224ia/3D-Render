package Core;

import Asset.ModelLoader;
import Asset.TextureLoader;
import Rendering.OpenGL.OpenGLRenderer;
import Rendering.Projection;
import Rendering.Renderer;
import Rendering.RendererType;
import UI.UIElement;
import Util.FpsCounter;
import Util.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public final class Engine {
    private final Renderer renderer;
    private Scene scene;

    public final Projection projection;

    private int fov;
    private final int BASE_FOV = 100;
    private final int MIN_FOV = 30;
    private final int MAX_FOV = 150;

    private final long startTime;

    public Engine(RendererType rendererType, int width, int height) {
        startTime = System.nanoTime();

        this.renderer = rendererType.create(width, height);

        this.fov = BASE_FOV;

        projection = new Projection(width, height, (float) width / height, 0.1f, 1000f, fov);

        ModelLoader.init(rendererType);
        TextureLoader.init();

        FpsCounter.start(System.nanoTime());

        long timeFromStart = (System.nanoTime() - startTime) / 1_000_000;
        Logger.info(String.format("Engine was created in %d milliseconds", timeFromStart));
    }

    public void addUIElement(UIElement element) {
        renderer.addUIElement(element);
    }

    public void setScene(float cameraSpeed) {
        if (scene == null && renderer != null) {
            scene = new Scene(renderer, cameraSpeed);
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void start(Runnable updateFunction) {
        long timeFromStart = (System.nanoTime() - startTime) / 1_000_000;
        Logger.info(String.format("Engine was started after %d milliseconds", timeFromStart));

        if (renderer instanceof OpenGLRenderer openGLRenderer) {
            while (!openGLRenderer.shouldClose()) {
                mouseScroll();
                draw();
                FpsCounter.fpsCount();
                if (updateFunction != null) {
                    updateFunction.run();
                }
            }
            return;
        }

        Timer timer = new Timer(1, _ -> {
            mouseScroll();
            draw();
            FpsCounter.fpsCount();
            if (updateFunction != null) {
                updateFunction.run();
            }
        });
        timer.start();
    }

    private void mouseScroll() {
        int scroll = scene.getCamera().mouse.getScroll();
        if ((scroll < 0 && fov > MIN_FOV) || (scroll > 0 && fov < MAX_FOV)) {
            fov += scroll * 3;
            fov = Math.clamp(fov, MIN_FOV, MAX_FOV);
            projection.scroll(fov);
        }
    }

    public int getFov() {
        return fov;
    }

    private void draw() {
        if (scene != null) {
            List<Object> objects = scene.getObjects();
            Camera camera = scene.getCamera();
            camera.update();
            renderer.render(objects, camera, scene.getLight(), projection);
        }
    }
}
