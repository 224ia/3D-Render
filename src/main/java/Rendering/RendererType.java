package Rendering;

import Rendering.Software.SoftwareRenderer;
import Util.Logger;

public enum RendererType {
    SOFTWARE;

    public Renderer create(int width, int height) {
        Renderer renderer = switch (this) {
            case SOFTWARE -> new SoftwareRenderer(width, height);
        };
        Logger.info("Created " + this.name().toLowerCase() + " renderer");
        return renderer;
    }
}