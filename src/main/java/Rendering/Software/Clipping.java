package Rendering.Software;

import Geometry.RenderPolygon;
import Geometry.Vertic;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Clipping {
    private static Vector4f planePoint;
    private static Vector4f planeNormal;

    public static void setPlane(Vector4f point, Vector4f normal) {
        planePoint = point;
        planeNormal = normal;
    }

    private static float distToPlane(Vector4f v) {
        return v.dot(planeNormal) - planePoint.dot(planeNormal);
    }

    private static Vertic planeIntersect(Vertic v0, Vertic v1) {
        float v0dist = distToPlane(v0.pos);
        float v1dist = distToPlane(v1.pos);
        float t = v0dist / (v0dist - v1dist);

        Vector4f pos = new Vector4f(v0.pos).lerp(v1.pos, t);

        Vector2f uv = null;
        if (v0.uv != null && v1.uv != null) {
            uv = new Vector2f(v0.uv).lerp(v1.uv, t);
        }

        return new Vertic(pos, uv);
    }

    public static List<RenderPolygon> clipTriangle(RenderPolygon originPolygon) {
        List<Vertic> inside = new ArrayList<>();
        List<Vertic> outside = new ArrayList<>();

        if (distToPlane(originPolygon.v0.pos) >= 0) inside.add(originPolygon.v0); else outside.add(originPolygon.v0);
        if (distToPlane(originPolygon.v1.pos) >= 0) inside.add(originPolygon.v1); else outside.add(originPolygon.v1);
        if (distToPlane(originPolygon.v2.pos) >= 0) inside.add(originPolygon.v2); else outside.add(originPolygon.v2);

        List<RenderPolygon> clipped = new ArrayList<>();
        if (outside.isEmpty()) clipped.add(originPolygon);
        else if (inside.size() == 1) {
            Vertic vIntersect0 = planeIntersect(inside.getFirst(), outside.get(0));
            Vertic vIntersect1 = planeIntersect(inside.getFirst(), outside.get(1));

            clipped.add(new RenderPolygon(inside.getFirst(), vIntersect0, vIntersect1, originPolygon.normal, originPolygon.texture, originPolygon.color));
        } else if (inside.size() == 2) {
            Vertic vIntersect0 = planeIntersect(inside.get(0), outside.getFirst());
            Vertic vIntersect1 = planeIntersect(inside.get(1), outside.getFirst());

            clipped.add(new RenderPolygon(inside.get(0), inside.get(1), vIntersect0, originPolygon.normal, originPolygon.texture, originPolygon.color));
            clipped.add(new RenderPolygon(inside.get(1), vIntersect1, vIntersect0, originPolygon.normal, originPolygon.texture, originPolygon.color));
        }
        return clipped;
    }

    public static List<RenderPolygon> projectClip(List<RenderPolygon> polygons) {
        List<RenderPolygon> Q = new ArrayList<>(polygons);
        for (int x = 0; x < 4; x++)
        {
            List<RenderPolygon> temp = new ArrayList<>();
            for (RenderPolygon t : Q) {
                List<RenderPolygon> new_t;
                switch (x) {
                    case 0:
                        Clipping.setPlane(new Vector4f(0, -1, 0, 1), new Vector4f(0, 1, 0, 0));
                        break;
                    case 1:
                        Clipping.setPlane(new Vector4f(0, 1, 0, 1), new Vector4f(0, -1, 0, 0));
                        break;
                    case 2:
                        Clipping.setPlane(new Vector4f(-1, 0, 0, 1), new Vector4f(1, 0, 0, 0));
                        break;
                    case 3:
                        Clipping.setPlane(new Vector4f(1, 0, 0, 1), new Vector4f(-1, 0, 0, 0));
                        break;
                }
                new_t = Clipping.clipTriangle(t);
                temp.addAll(new_t);
            }
            Q = temp;
        }
        return Q;
    }
}