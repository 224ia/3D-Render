package Core;

import UI.UIBuilder;
import UI.UITextLabel;
import Util.AssetLoader;
import Util.Projection;
import Util.RenderPolygon;
import Util.Renderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public final class Engine {
    private final Renderer renderer;
    private Scene scene;

    public final int WIDTH;
    public final int HEIGHT;

    private long startTime;
    private float deltaTime;
    private int frames = 0;
    private final float ONE_BY_BILLION = 1 / 1_000_000_000f;
    private float changeTime = 0;
    private final float FPS_UPDATE = 0.4f;

    public final Projection projection;

    private int fov;
    private final int BASE_FOV = 100;
    private final int MIN_FOV = 30;
    private final int MAX_FOV = 150;

    private final UITextLabel fovText;
    private final UITextLabel fpsText;

    public Engine(Renderer renderer, int width, int height) {
        this.renderer = renderer;

        this.WIDTH = width;
        this.HEIGHT = height;

        this.fov = BASE_FOV;

        projection = new Projection(width, height, (float) width / height, fov);

        fovText = UIBuilder.createTextLabel(0, 0, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "FOV: " + fov, new Color(0f, 0f, 0f, 1f), 50);
        fpsText = UIBuilder.createTextLabel(0,200, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "FPS: 60", new Color(0f, 0f, 0f, 1f), 50);
        renderer.addUIElement(fovText);
        renderer.addUIElement(fpsText);

        AssetLoader.init();

        startTime = System.nanoTime();
    }

    public void setScene(float cameraSpeed) {
        if (scene == null && renderer != null) {
            scene = new Scene(renderer, cameraSpeed);
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void start() {
        Timer timer = new Timer(1, _ -> {
            mouseScroll();
            draw();
            fpsCount();
        });
        timer.start();
    }

    private void fpsCount() {
        long now = System.nanoTime();
        deltaTime = (now - startTime) * ONE_BY_BILLION;
        changeTime += deltaTime;
        frames += 1;
        startTime = now;
        if (changeTime >= FPS_UPDATE) {
            fpsText.text = "FPS: " + (int) (frames / changeTime);
            changeTime = 0;
            frames = 0;
        }
    }

    private void mouseScroll() {
        int scroll = scene.getCamera().mouse.getScroll();
        if ((scroll < 0 && fov > MIN_FOV) || (scroll > 0 && fov < MAX_FOV)) {
            fov += scroll * 3;
            fov = Math.clamp(fov, MIN_FOV, MAX_FOV);
            projection.scroll(fov);
            fovText.text = "FOV: " + fov;
        }
    }

    private void draw() {
        if (scene != null) {
            List<RenderPolygon> renderPolygons = scene.setRenderPolygons(Math.max(deltaTime, 1f / 1000));
//            renderPolygons.sort((a, b) -> Float.compare(b.depth, a.depth));
            renderer.render(renderPolygons, projection);
        }
    }
}
