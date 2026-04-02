package UI.Elements;

import UI.UIElement;

import java.awt.*;

public class UITextLabel extends UIElement {
    public String text;
    public Color textColor;
    public int textSize;

    public UITextLabel(int x, int y, int width, int height, Color bgColor, String text, Color textColor, int textSize) {
        super(x, y, width, height, bgColor);
        this.text = text;
        this.textColor = textColor;
        this.textSize = textSize;
    }
}
