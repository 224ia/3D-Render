import Core.Engine;
import Core.Object;
import Core.ObjectBuilder;
import Core.Scene;
import Rendering.RendererType;
import UI.Elements.UITextLabel;
import UI.UIBuilder;
import Util.FpsCounter;
import Util.Logger;

import java.awt.*;

public class Main {
    private UITextLabel fovText;
    private UITextLabel fpsText;

    void main() {
        Logger.setDebugMode(true); // The logger has the ability to output some information only in debug mode
        Engine.create(RendererType.OPEN_GL, 1920, 1080, 5f, Main.class);
    }

    // init() - method invoked after Engine is created but before Engine starts
    private void init() {
        Scene scene = Engine.getScene();
        scene.setLight(0, -1, 1);

        // Create objects using ObjectBuilder. All parameters have default values so they are optional
        // See ObjectBuilder class for available methods
        // Test scene
        scene.addObject(new ObjectBuilder().model("Ground").texture("Stone").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("Wall1").texture("Wall").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("Wall2").texture("Wall").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("Wall3").texture("Wall").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("Wall4").texture("Wall").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("WeirdCube1").texture("Wood").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("WeirdCube2").texture("Wood").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("WeirdCube3").texture("Wood").size(0.1f).build());
        scene.addObject(new ObjectBuilder().model("SuperWeirdSphere").texture("Gift").size(0.1f).build());

        UITextLabel text = UIBuilder.createTextLabel(760, 980, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "Some text", new Color(0f, 0f, 0f, 1f), 50);
        fovText = UIBuilder.createTextLabel(0, 0, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "FOV: 100", new Color(0f, 0f, 0f, 1f), 50);
        fpsText = UIBuilder.createTextLabel(0, 200, 400, 100,
                new Color(1f, 1f, 1f, 0.5f), "FPS: 60", new Color(0f, 0f, 0f, 1f), 50);

        Engine.addUIElement(text);
        Engine.addUIElement(fovText);
        Engine.addUIElement(fpsText);
    }

    // update() - method invoked every frame
    private void update() {
        fovText.text = "FOV: " + Engine.getFov();
        fpsText.text = "FPS: " + FpsCounter.getFps();
        Logger.debug(FpsCounter.getFps() + "");

        for (Object object : Engine.getScene().getObjects()) {
//        object.rotate(1f, 0, 1f);
        }
    }
}