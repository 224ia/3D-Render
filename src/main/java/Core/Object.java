package Core;

import Geometry.Polygon;
import Geometry.RenderPolygon;
import Geometry.Vertic;
import org.joml.*;

import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.List;

public class Object {
    public Vector3f pos;
    public Vector3f rot;
    public Vector3f size;
    public Vector3f color;

    private Matrix4f fullMatrix;

    public final List<Polygon> polygons;

    private final BufferedImage texture;

    public Object(Vector3f pos, Vector3f rot, Vector3f size, Vector3f color, List<Polygon> polygons, BufferedImage texture) {
        this.pos = pos;
        this.rot = rot;
        this.size = size;
        this.color = color;
        this.polygons = polygons;
        this.texture = texture;

        updateMatrix();
    }

    public void rotate(Vector3f angle) {
        rot.add(angle);
        updateMatrix();
    }

    public void updateMatrix() {
        float angleX = (float) Math.toRadians(rot.x);
        float angleY = (float) Math.toRadians(rot.y);
        float angleZ = (float) Math.toRadians(rot.z);

        fullMatrix = new Matrix4f().translate(pos).rotateX(angleX).rotateY(angleY).rotateZ(angleZ).scale(size);
    }

    public void render(Matrix4f view, List<RenderPolygon> renderPolygons) {
        Matrix4f mvp = new Matrix4f(view).mul(fullMatrix);

        for (Polygon polygon : polygons) {
            Vertic v0 = new Vertic(polygon.vertices[0]);
            Vertic v1 = new Vertic(polygon.vertices[1]);
            Vertic v2 = new Vertic(polygon.vertices[2]);

            v0.pos.mul(mvp);
            v1.pos.mul(mvp);
            v2.pos.mul(mvp);

            v0.invZ = 1f / v0.pos.z;
            v1.invZ = 1f / v1.pos.z;
            v2.invZ = 1f / v2.pos.z;

            Vector4f normal = new Vector4f(polygon.normal, 0);
            normal.mul(mvp);
            normal.normalize();

            Vector4f diff = new Vector4f(v0.pos).add(v1.pos).add(v2.pos).div(3);
            if (diff.dot(normal) <= 0) {
                float depth = (v0.pos.z + v1.pos.z + v2.pos.z) / 3f;
                renderPolygons.add(new RenderPolygon(v0, v1, v2, normal, texture, this.color, depth));
            }
        }
    }
}
