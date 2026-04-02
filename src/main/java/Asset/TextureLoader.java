package Asset;

import Util.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Asset.Paths.*;

public class TextureLoader {
    public static final List<Texture> textures = new ArrayList<>();

    public static void init() {
        if (textures.isEmpty()) {
            File folder = new File(TEXTURES_FOLDER_PATH);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().replace(".png", "");
                        try {
                            BufferedImage image = ImageIO.read(file);
                            textures.add(new Texture(name, image));
                            Logger.info("Loaded texture: " + name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Logger.info("Textures are initialized");
        } else {
            Logger.error("Textures are already initialized");
        }
    }

    public static Texture getTexture(String name) {
        Texture got = textures.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (got == null && name != null) {
            Logger.error("No texture named: " + name);
        }
        return got;
    }
}
