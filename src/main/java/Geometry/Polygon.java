package Geometry;

import org.joml.Vector3f;

public class Polygon {
    public final Vertic[] vertices;
    public final Vector3f normal;

    public Polygon(Vertic[] vertices, Vector3f normal) {
        this.vertices = vertices;
        this.normal = normal;
    }
}
