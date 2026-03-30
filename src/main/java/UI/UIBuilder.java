package UI;

import java.awt.*;

public final class UIBuilder {
    private UIBuilder() {}

    public static UITextLabel createTextLabel(int x, int y, int w, int h,
                                              Color bg, String text, Color textCol, int textSize) {
        return new UITextLabel(x, y, w, h, bg, text, textCol, textSize);
    }
}