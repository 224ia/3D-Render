package Core;

import Geometry.Polygon;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    public static List<Polygon> createCube(Vector3f center, float size) {
        List<Polygon> polygons = new ArrayList<>();

        float half = size / 2;
        float x0 = center.x - half;
        float x1 = center.x + half;
        float y0 = center.y - half;
        float y1 = center.y + half;
        float z0 = center.z - half;
        float z1 = center.z + half;

        Vector3f v0 = new Vector3f(x0, y0, z0);
        Vector3f v1 = new Vector3f(x1, y0, z0);
        Vector3f v2 = new Vector3f(x1, y0, z1);
        Vector3f v3 = new Vector3f(x0, y0, z1);
        Vector3f v4 = new Vector3f(x0, y1, z0);
        Vector3f v5 = new Vector3f(x1, y1, z0);
        Vector3f v6 = new Vector3f(x1, y1, z1);
        Vector3f v7 = new Vector3f(x0, y1, z1);

        Vector3f front = new Vector3f(0, 0, 1);
        Vector3f back = new Vector3f(0, 0, -1);
        Vector3f right = new Vector3f(1, 0, 0);
        Vector3f left = new Vector3f(-1, 0, 0);
        Vector3f top = new Vector3f(0, 1, 0);
        Vector3f bottom = new Vector3f(0, -1, 0);

        polygons.add(new Polygon(new Vector3f[]{v3, v2, v6}, front));
        polygons.add(new Polygon(new Vector3f[]{v3, v6, v7}, front));

        polygons.add(new Polygon(new Vector3f[]{v0, v5, v1}, back));
        polygons.add(new Polygon(new Vector3f[]{v0, v4, v5}, back));

        polygons.add(new Polygon(new Vector3f[]{v1, v6, v2}, right));
        polygons.add(new Polygon(new Vector3f[]{v1, v5, v6}, right));

        polygons.add(new Polygon(new Vector3f[]{v0, v3, v7}, left));
        polygons.add(new Polygon(new Vector3f[]{v0, v7, v4}, left));

        polygons.add(new Polygon(new Vector3f[]{v4, v6, v5}, top));
        polygons.add(new Polygon(new Vector3f[]{v4, v7, v6}, top));

        polygons.add(new Polygon(new Vector3f[]{v0, v1, v2}, bottom));
        polygons.add(new Polygon(new Vector3f[]{v0, v2, v3}, bottom));

        return polygons;
    }
}
