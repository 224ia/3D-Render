package Core;

import UI.UIBuilder;
import UI.UITextLabel;
import Util.*;
import org.joml.*;

import java.awt.*;

public class Main {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public static void main(String[] args) {
        Renderer renderer = RendererType.SOFTWARE.create(WIDTH, HEIGHT);

        UITextLabel text = UIBuilder.createTextLabel(760, 980, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "Юхууу", new Color(0f, 0f, 0f, 1f), 50);
        renderer.addUIElement(text);

        Engine engine = new Engine(renderer, WIDTH, HEIGHT, 110);
        engine.setScene(10f);
        Scene scene = engine.getScene();
        // Test Objects
        // Params: position, rotation, size, color, modelName, textureName (can be null)
        scene.addObject(new Vector3f(-8, 3, 5), new Vector3f(0, 80, 180), new Vector3f(1, 1, 1), new Color(0.9f, 0.9f, 0.9f),
                "TextureTest", "Wall");

        engine.start();
    }
}
