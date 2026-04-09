package Asset.OpenGL;

import Asset.Model;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class OpenGLModel extends Model {
    public final int vao, vbo, vertexCount;

    public OpenGLModel(String name, List<Float> vertexList, List<Float> normals, List<Float> uvs, List<Integer> polygons) {
        super(name);
        this.vertexCount = polygons.size() / 7 * 3;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        float[] vertices = getVertices(vertexList, normals, uvs, polygons);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private float[] getVertices(List<Float> vertexList, List<Float> normals, List<Float> uvs, List<Integer> polygons) {
        float[] vertices = new float[vertexCount * 8];
        int outputVertex = 0;

        for (int i = 0; i < polygons.size(); i += 7) {
            int normalIndex = polygons.get(i + 6);

            for (int j = 0; j < 3; j++) {
                int vertexIndex = polygons.get(i + j * 2);
                int uvIndex = polygons.get(i + j * 2 + 1);
                int out = outputVertex * 8;

                writeVec3(vertices, out, vertexList, vertexIndex);
                writeVec3(vertices, out + 3, normals, normalIndex);
                writeVec2(vertices, out + 6, uvs, uvIndex);

                outputVertex++;
            }
        }

        return vertices;
    }

    private void writeVec3(float[] target, int offset, List<Float> source, int index) {
        if (index < 0) {
            target[offset] = 0f;
            target[offset + 1] = 0f;
            target[offset + 2] = 0f;
            return;
        }

        int sourceOffset = index * 3;
        target[offset] = source.get(sourceOffset);
        target[offset + 1] = source.get(sourceOffset + 1);
        target[offset + 2] = source.get(sourceOffset + 2);
    }

    private void writeVec2(float[] target, int offset, List<Float> source, int index) {
        if (index < 0) {
            target[offset] = 0f;
            target[offset + 1] = 0f;
            return;
        }

        int sourceOffset = index * 2;
        target[offset] = source.get(sourceOffset);
        target[offset + 1] = source.get(sourceOffset + 1);
    }
}
