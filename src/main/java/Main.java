import Core.Engine;
import Core.ObjectBuilder;
import Core.Scene;
import Rendering.Renderer;
import Rendering.RendererType;
import UI.Elements.UITextLabel;
import UI.UIBuilder;
import Util.FpsCounter;

import java.awt.*;

public static final int WIDTH = 1920;
public static final int HEIGHT = 1080;

private final Renderer renderer = RendererType.SOFTWARE.create(WIDTH, HEIGHT);
private final Engine engine = new Engine(renderer, WIDTH, HEIGHT);

private final UITextLabel text = UIBuilder.createTextLabel(760, 980, 400, 100,
        new Color(1f, 1f, 1f, 0.5f), "Some text", new Color(0f, 0f, 0f, 1f), 50);
private final UITextLabel fovText = UIBuilder.createTextLabel(0, 0, 400, 100,
        new Color(1f, 1f, 1f, 0.5f), "FOV: 100", new Color(0f, 0f, 0f, 1f), 50);
private final UITextLabel fpsText = UIBuilder.createTextLabel(0,200, 400, 100,
        new Color(1f, 1f, 1f, 0.5f), "FPS: 60", new Color(0f, 0f, 0f, 1f), 50);

void main() {
    renderer.addUIElement(text);
    renderer.addUIElement(fovText);
    renderer.addUIElement(fpsText);

    engine.setScene(5f);
    Scene scene = engine.getScene();

    // Create objects using ObjectBuilder. All parameters have default values so they are optional
    // See ObjectBuilder for available methods
    // Test Objects
    scene.addObject(new ObjectBuilder().position(-8, 3, 5).rotation(0, 80, 180)
            .color(0.9f).model("TextureTest").texture("Wall").build()); // All parameters used
    scene.addObject(new ObjectBuilder().model("Player").position(5, 0, -5).texture("Background").build()); // Some parameters not used
    scene.addObject(new ObjectBuilder().build()); // No parameters used
    scene.addObject(new ObjectBuilder().model("sdas").texture("qsesa").position(0, -10, 10).build()); // Wrong names used

    engine.start(this::update);
}

void update() {
    fovText.text = "FOV: " + engine.getFov();
    fpsText.text = "FPS: " + FpsCounter.getFps();
}