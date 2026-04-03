package Core;

import Asset.ModelLoader;
import Asset.TextureLoader;
import Geometry.RenderPolygon;
import Rendering.Projection;
import Rendering.Renderer;
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

    public Engine(Renderer renderer, int width, int height) {
        this.renderer = renderer;

        this.fov = BASE_FOV;

        projection = new Projection(width, height, (float) width / height, 0.1f, 1000f, fov);

        ModelLoader.init();
        TextureLoader.init();

        FpsCounter.start(System.nanoTime());

        Logger.info("Engine was created");
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
        Timer timer = new Timer(1, _ -> {
            mouseScroll();
            draw();
            FpsCounter.fpsCount();
            if (updateFunction != null) {
                updateFunction.run();
            }
        });
        timer.start();
        Logger.info("Engine was started");
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
            List<RenderPolygon> renderPolygons = scene.setRenderPolygons();
            renderer.render(renderPolygons, projection);
        }
    }
}
