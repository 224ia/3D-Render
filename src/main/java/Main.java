import Core.Engine;
import Core.ObjectBuilder;
import Core.Scene;
import Rendering.Renderer;
import Rendering.RendererType;
import UI.Elements.UITextLabel;
import UI.UIBuilder;

import java.awt.*;

public static final int WIDTH = 1920;
public static final int HEIGHT = 1080;

void main() {
    Renderer renderer = RendererType.SOFTWARE.create(WIDTH, HEIGHT);

    UITextLabel text = UIBuilder.createTextLabel(760, 980, 400, 100,
            new Color(1f, 1f, 1f, 0.5f), "Юхууу", new Color(0f, 0f, 0f, 1f), 50);
    renderer.addUIElement(text);

    Engine engine = new Engine(renderer, WIDTH, HEIGHT);
    engine.setScene(10f);
    Scene scene = engine.getScene();

    // Create objects using ObjectBuilder. All parameters have default values so they are optional
    // See ObjectBuilder for available methods
    scene.addObject(new ObjectBuilder().model("Player").color(1f).texture("Background")
            .rotation(180, 0, 0).size(0.1f).build());
    scene.addObject(new ObjectBuilder().position(-8, 3, 5).rotation(0, 80, 180)
            .color(0.9f).model("TextureTest").texture("Wall").build());
    scene.addObject(new ObjectBuilder().model("sdas").texture("qsesa").build());

    engine.start();
}