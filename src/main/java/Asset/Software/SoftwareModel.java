package Asset.Software;

import Asset.Model;
import Geometry.Polygon;
import Geometry.Vertic;
import Util.Logger;
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
        Logger.debug(name);
        this.polygons = new ArrayList<>();
        for (int i = 0; i < polygons.size(); i += 7) {
            Vertic[] vertices = new Vertic[3];
            for (int j = 0; j < 3; j++) {
                int vertexIndex = polygons.get(i + j * 2);
                int uvIndex = polygons.get(i + j * 2 + 1);
                int vertexOffset = vertexIndex * 3;
                Vector2f uv = null;
                if (uvIndex >= 0) {
                    int uvOffset = uvIndex * 2;
                    uv = new Vector2f(uvs.get(uvOffset), uvs.get(uvOffset + 1));
                }

                vertices[j] = new Vertic(new Vector4f(vertexList.get(vertexOffset),
                        vertexList.get(vertexOffset + 1),
                        vertexList.get(vertexOffset + 2), 1),
                        uv);
            }
            int normalIndex = polygons.get(i + 6);
            int normalOffset = normalIndex * 3;
            Vector3f normal = new Vector3f(normals.get(normalOffset), normals.get(normalOffset + 1), normals.get(normalOffset + 2));
            this.polygons.add(new Polygon(vertices, normal));
        }
    }
}
