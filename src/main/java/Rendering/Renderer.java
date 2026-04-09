package Rendering;

import Core.Camera;
import Core.Object;
import Input.Input;
import Input.MouseProcessing;
import UI.UI;
import UI.UIElement;

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
        ui.addElement(element);
    }

    public abstract void render(List<Object> objects, Camera camera, Projection projection);

    protected abstract void drawUI();
}
