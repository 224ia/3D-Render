package Util;

import org.joml.Vector4f;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class RenderPolygon {
    public Vertic v0, v1, v2;
    public Vector4f normal;
    public BufferedImage texture;
    public Color color;
    public float depth;

    public RenderPolygon(Vertic v0, Vertic v1, Vertic v2, Vector4f normal, BufferedImage texture, Color color, float depth) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = normal;
        this.texture = texture;
        this.color = color;
        this.depth = depth;
    }
}
