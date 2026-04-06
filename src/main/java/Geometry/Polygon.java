package Geometry;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Polygon {
    public final Vertic[] vertices;
    public final Vector3f normal;

    public Polygon(Vertic[] vertices, Vector3f normal) {
        this.vertices = vertices;
        this.normal = normal;
    }

    public Polygon(Vector3f[] pos, Vector3f normal) {
        Vertic[] vertices = new Vertic[3];
        for (int i = 0; i < 3; i++) {
            vertices[i] = new Vertic(new Vector4f(pos[i], 1), null);
        }
        this.vertices = vertices;
        this.normal = normal;
    }
}
