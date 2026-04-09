package Rendering.Software;

import Geometry.RenderPolygon;
import Geometry.Vertic;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

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
        List<RenderPolygon> result = new ArrayList<>();
        for (RenderPolygon polygon : polygons) {
            List<Vertic> clipped = new ArrayList<>(List.of(polygon.v0, polygon.v1, polygon.v2));
            clipped = clipPolygon(clipped, v -> v.pos.x + v.pos.w);
            clipped = clipPolygon(clipped, v -> v.pos.w - v.pos.x);
            clipped = clipPolygon(clipped, v -> v.pos.y + v.pos.w);
            clipped = clipPolygon(clipped, v -> v.pos.w - v.pos.y);

            if (clipped.size() < 3) {
                continue;
            }

            Vertic first = clipped.getFirst();
            for (int i = 1; i < clipped.size() - 1; i++) {
                result.add(new RenderPolygon(first, clipped.get(i), clipped.get(i + 1),
                        polygon.normal, polygon.texture, polygon.color));
            }
        }
        return result;
    }

    private static List<Vertic> clipPolygon(List<Vertic> polygon, ToDoubleFunction<Vertic> distanceFunction) {
        if (polygon.isEmpty()) {
            return polygon;
        }

        List<Vertic> clipped = new ArrayList<>();
        Vertic previous = polygon.getLast();
        double previousDistance = distanceFunction.applyAsDouble(previous);

        for (Vertic current : polygon) {
            double currentDistance = distanceFunction.applyAsDouble(current);
            boolean previousInside = previousDistance >= 0.0;
            boolean currentInside = currentDistance >= 0.0;

            if (previousInside && currentInside) {
                clipped.add(current);
            } else if (previousInside) {
                clipped.add(interpolate(previous, current, previousDistance, currentDistance));
            } else if (currentInside) {
                clipped.add(interpolate(previous, current, previousDistance, currentDistance));
                clipped.add(current);
            }

            previous = current;
            previousDistance = currentDistance;
        }

        return clipped;
    }

    private static Vertic interpolate(Vertic from, Vertic to, double fromDistance, double toDistance) {
        float t = (float) (fromDistance / (fromDistance - toDistance));
        Vector4f pos = new Vector4f(from.pos).lerp(to.pos, t);

        Vector2f uv = null;
        if (from.uv != null && to.uv != null) {
            uv = new Vector2f(from.uv).lerp(to.uv, t);
        }

        return new Vertic(pos, uv);
    }
}
