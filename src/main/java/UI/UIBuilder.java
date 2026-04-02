package UI;

import UI.Elements.UITextLabel;
import Util.Logger;

import java.awt.*;

public final class UIBuilder {
    private UIBuilder() {}

    public static UITextLabel createTextLabel(int x, int y, int w, int h,
                                              Color bg, String text, Color textCol, int textSize) {
        Logger.info("Created Text Label");
        return new UITextLabel(x, y, w, h, bg, text, textCol, textSize);
    }
}