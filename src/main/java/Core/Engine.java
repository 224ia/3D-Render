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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class Engine {
    private static Renderer renderer;
    private static Scene scene;

    public static Projection projection;

    private static int fov;
    private static final int BASE_FOV = 100;
    private static final int MIN_FOV = 30;
    private static final int MAX_FOV = 150;

    private static long startTime;

    private static java.lang.Object mainClassInstance;
    private static Method initMethod;
    private static Method updateMethod;

    public static void create(RendererType rendererType, int width, int height, float cameraSpeed, Class<?> mainClass) {
        startTime = System.nanoTime();

        renderer = rendererType.create(width, height);

        fov = BASE_FOV;

        projection = new Projection(width, height, (float) width / height, 0.1f, 1000f, fov);

        ModelLoader.setCurPackage("CoolScene"); // Simple multi packaging
        ModelLoader.init(rendererType);
        TextureLoader.init();

        setScene(cameraSpeed);

        FpsCounter.start(System.nanoTime());

        long timeFromStart = (System.nanoTime() - startTime) / 1_000_000;
        Logger.info(String.format("Engine was created in %d milliseconds", timeFromStart));

        try {
            Constructor<?> constructor = mainClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            mainClassInstance = constructor.newInstance();

            initMethod = mainClass.getDeclaredMethod("init");
            updateMethod = mainClass.getDeclaredMethod("update");

            updateMethod.setAccessible(true);

            if (initMethod != null) {
                initMethod.setAccessible(true);
                initMethod.invoke(mainClassInstance);
            }
        } catch (InvocationTargetException | IllegalAccessException
                 | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException("Failed to create engine", e);
        }

        Engine.start();
    }

    public static void addUIElement(UIElement element) {
        renderer.addUIElement(element);
    }

    private static void setScene(float cameraSpeed) {
        System.out.println(scene);
        System.out.println(renderer);
        if (scene == null && renderer != null) {
            scene = new Scene(renderer, cameraSpeed);
        }
    }

    public static Scene getScene() {
        return scene;
    }

    private static void start() {
        long timeFromStart = (System.nanoTime() - startTime) / 1_000_000;
        Logger.info(String.format("Engine was started after %d milliseconds", timeFromStart));

        if (renderer instanceof OpenGLRenderer openGLRenderer) {
            while (!openGLRenderer.shouldClose()) {
                loopIteration();
            }
        } else {
            Timer timer = new Timer(1, _ -> loopIteration());
            timer.start();
        }
    }

    private static void loopIteration() {
        mouseScroll();
        draw();
        FpsCounter.fpsCount();
        if (updateMethod != null) {
            try {
                updateMethod.invoke(mainClassInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Logger.error("Game update crashed: " + e.getMessage());
            }
        }
    }

    private static void mouseScroll() {
        int scroll = scene.getCamera().mouse.getScroll();
        if ((scroll < 0 && fov > MIN_FOV) || (scroll > 0 && fov < MAX_FOV)) {
            fov += scroll * 3;
            fov = Math.clamp(fov, MIN_FOV, MAX_FOV);
            projection.scroll(fov);
        }
    }

    public static int getFov() {
        return fov;
    }

    private static void draw() {
        if (scene != null) {
            List<Object> objects = scene.getObjects();
            Camera camera = scene.getCamera();
            camera.update();
            renderer.render(objects, camera, scene.getLight(), projection);
        }
    }
}
