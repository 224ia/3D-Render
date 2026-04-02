package Core;

import Asset.Model;
import Asset.ModelLoader;
import Asset.Texture;
import Asset.TextureLoader;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public final class ObjectBuilder {
    private Vector3f pos = new Vector3f(0f, 0f,0f);
    private Vector3f rot = new Vector3f(0f, 0f,0f);
    private Vector3f size = new Vector3f(1f, 1f, 1f);
    private Vector3f color = new Vector3f(1f, 1f, 1f);
    private Model model = ModelLoader.getModel(null);
    private BufferedImage texture = null;

    public ObjectBuilder position(float x, float y, float z) {
        this.pos.set(x, y, z);
        return this;
    }
    public ObjectBuilder position(Vector3f pos) {
        this.pos = pos;
        return this;
    }

    public ObjectBuilder rotation(float x, float y, float z) {
        this.rot.set(x, y, z);
        return this;
    }
    public ObjectBuilder rotation(Vector3f rot) {
        this.rot = rot;
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
        this.size = size;
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
        this.color = color;
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
        return new Object(pos, rot, size, color, model.getPolygons(), texture);
    }
}
