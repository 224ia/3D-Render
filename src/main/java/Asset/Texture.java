package Asset;

import java.awt.image.BufferedImage;

public class Texture {
    private final String name;
    private final BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Texture(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }
}
