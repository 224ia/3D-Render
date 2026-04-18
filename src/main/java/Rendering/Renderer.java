package Rendering;

import Core.Camera;
import Core.Object;
import Input.Input;
import Input.MouseProcessing;
import UI.UI;
import UI.UIElement;
import Util.Logger;
import org.joml.Vector3f;

import java.util.List;

public abstract class Renderer {
    private final Input input;
    private final MouseProcessing mouse;

    protected UI ui;

    public Renderer(Input input, MouseProcessing mouse) {
        this.input = input;
        this.mouse = mouse;
    }

    public Input getInput() {
        return input;
    }

    public MouseProcessing getMouse() {
        return mouse;
    }

    public void addUIElement(UIElement element) {
        if (ui != null) {
            ui.addElement(element);
        } else {
            Logger.error("UI wasn't created");
        }
    }

    public abstract void changeWindowTitle(String title);

    public abstract void render(List<Object> objects, Camera camera, Vector3f lightDir, Projection projection);

    protected void drawUI() {
        if (ui != null) {
            ui.draw();
        }
    }
}
