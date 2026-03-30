package UI;

import java.awt.*;

public abstract class UIElement {
    public int x;
    public int y;
    public int width;
    public int height;
    public Color bgColor;

    public UIElement(int x, int y, int width, int height, Color bgColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
    }
}
