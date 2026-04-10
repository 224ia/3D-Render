package UI.Software;

import UI.UI;
import UI.UIElement;
import UI.Elements.UITextLabel;

import java.awt.*;

public class SoftwareUI extends UI {
    private final Graphics2D g2;

    public SoftwareUI(Graphics2D g2) {
        super();
        this.g2 = g2;
    }

    @Override
    public void draw() {
        for (UIElement element : elements) {
            g2.setColor(element.bgColor);
            g2.fillRect((int) element.x, (int) element.y, (int) element.width, (int) element.height);
            switch (element) {
                case UITextLabel label -> {
                    Font font = new Font("Arial", Font.PLAIN, label.textSize);
                    FontMetrics fm = g2.getFontMetrics(font);

                    int textWidth = fm.stringWidth(label.text);
                    int textHeight = fm.getHeight();
                    int ascent = fm.getAscent();

                    int textX = (int) (element.x + (element.width - textWidth) / 2);
                    int textY = (int) (element.y + (element.height - textHeight) / 2 + ascent);

                    g2.setColor(label.textColor);
                    g2.setFont(font);
                    g2.drawString(label.text, textX, textY);
                }
                default -> {}
            }
        }
    }
}
