package Core;

import Asset.Model;
import Asset.ModelLoader;
import Asset.Texture;
import Asset.TextureLoader;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public final class ObjectBuilder {
    private String name = "No name";
    private final Vector3f pos = new Vector3f(0f, 0f,0f);
    private final Vector3f rot = new Vector3f(0f, 0f,0f);
    private final Vector3f size = new Vector3f(1f, 1f, 1f);
    private final Vector3f color = new Vector3f(1f, 1f, 1f);
    private Model model = ModelLoader.getModel(null);
    private BufferedImage texture = null;

    public ObjectBuilder name(String name) {
        this.name = name != null ? name : "No name";
        return this;
    }

    public ObjectBuilder position(float x, float y, float z) {
        this.pos.set(x, -y, z); // Inverted y for right translation
        return this;
    }
    public ObjectBuilder position(Vector3f pos) {
        this.pos.set(pos.x, -pos.y, pos.z); // Inverted y for right translation
        return this;
    }

    public ObjectBuilder rotation(float x, float y, float z) {
        this.rot.set(x, y, z);
        return this;
    }
    public ObjectBuilder rotation(Vector3f rot) {
        this.rot.set(rot.x, rot.y, rot.z);
        return this;
    }

    public ObjectBuilder size(float x, float y, float z) {
        this.size.set(x, y, z);
        return this;
    }
    public ObjectBuilder size(float scale) {
        this.size.set(scale, scale, scale);
        return this;
    }
    public ObjectBuilder size(Vector3f size) {
        this.size.set(size.x, size.y, size.z);
        return this;
    }

    public ObjectBuilder color(float r, float g, float b) {
        this.color.set(r, g, b);
        return this;
    }
    public ObjectBuilder color(float color) {
        this.color.set(color, color, color);
        return this;
    }
    public ObjectBuilder color(Vector3f color) {
        this.color.set(color.x, color.y, color.z);
        return this;
    }

    public ObjectBuilder model(String modelName) {
        this.model = ModelLoader.getModel(modelName);
        return this;
    }
    public ObjectBuilder texture(String textureName) {
        Texture tex = TextureLoader.getTexture(textureName);
        this.texture = tex == null ? null : tex.getImage();
        return this;
    }

    public Object build() {
        return new Object(name, pos, rot, size, color, model.getPolygons(), texture);
    }
}
