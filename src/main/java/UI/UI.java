package UI;

import java.util.ArrayList;
import java.util.List;

public abstract class UI {
    protected final List<UIElement> elements;

    public UI() {
        elements = new ArrayList<>();
    }

    public void addElement(UIElement element) {
        elements.add(element);
    }

    public List<UIElement> getElements() {
        return elements;
    }

    public abstract void draw();
}
