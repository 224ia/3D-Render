package Asset.Software;

import Asset.Model;
import Geometry.Polygon;
import Geometry.Vertic;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class SoftwareModel extends Model {
    private final List<Polygon> polygons;

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public SoftwareModel(String name, List<Float> vertexList, List<Float> normals, List<Float> uvs, List<Integer> polygons) {
        super(name);
        this.polygons = new ArrayList<>();
        for (int i = 0; i < polygons.size(); i += 7) {
            Vertic[] vertices = new Vertic[3];
            for (int j = 0; j < 3; j++) {
                int vertexIndex = polygons.get(i + j * 2);
                int uvIndex = polygons.get(i + j * 2 + 1);
                vertices[i] = new Vertic(new Vector4f(vertexList.get(vertexIndex),
                        vertexList.get(vertexIndex + 1),
                        vertexList.get(vertexIndex + 2), 1),
                        new Vector2f(uvs.get(uvIndex), uvs.get(uvIndex + 1)));
            }
            int normalIndex = polygons.get(i + 6);
            Vector3f normal = new Vector3f(normals.get(normalIndex), normals.get(normalIndex + 1), normals.get(normalIndex + 2));
            this.polygons.add(new Polygon(vertices, normal));
        }
    }
}
