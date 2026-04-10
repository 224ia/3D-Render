package UI;

import java.awt.*;

public abstract class UIElement {
    public float x;
    public float y;
    public float width;
    public float height;
    public Color bgColor;

    public UIElement(int x, int y, int width, int height, Color bgColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
    }
}
