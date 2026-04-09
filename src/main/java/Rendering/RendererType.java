package Rendering;

import Rendering.OpenGL.OpenGLRenderer;
import Rendering.Software.SoftwareRenderer;
import Util.Logger;

public enum RendererType {
    SOFTWARE,
    OPEN_GL;

    public Renderer create(int width, int height) {
        Renderer renderer = switch (this) {
            case SOFTWARE -> new SoftwareRenderer(width, height);
            case OPEN_GL -> new OpenGLRenderer(width, height);
        };
        Logger.info("Created " + this.name().toLowerCase() + " renderer");
        return renderer;
    }
}