package Geometry;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;

public class RenderPolygon {
    public Vertic v0, v1, v2;
    public Vector4f normal;
    public BufferedImage texture;
    public Vector3f color;
    public float depth;

    public RenderPolygon(Vertic v0, Vertic v1, Vertic v2, Vector4f normal, BufferedImage texture, Vector3f color, float depth) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = normal;
        this.texture = texture;
        this.color = color;
        this.depth = depth;
    }
}
