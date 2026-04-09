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
    public final int vao, vbo, ebo, vertexCount;

    public OpenGLModel(String name, int vertexCount, List<Float> vertexList, List<Float> normals, List<Float> uvs, List<Integer> polygons) {
        super(name);
        this.vertexCount = vertexCount;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        float[] vertices = getVertices(vertexCount, vertexList, normals, uvs);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
        glEnableVertexAttribArray(2);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        int[] indices = getIndices(polygons);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    private float[] getVertices(int vertexCount, List<Float> vertexList, List<Float> normals, List<Float> uvs) {
        float[] vertices = new float[vertexCount * 8];
        for (int i = 0; i < vertexCount; i++) {
            int ind = i * 8;

            vertices[ind] = vertexList.get(i * 3);
            vertices[ind + 1] = vertexList.get(i * 3 + 1);
            vertices[ind + 2] = vertexList.get(i * 3 + 2);

            vertices[ind + 3] = normals.get(i * 3);
            vertices[ind + 4] = normals.get(i * 3 + 1);
            vertices[ind + 5] = normals.get(i * 3 + 2);

            vertices[ind + 6] = uvs.get(i * 2);
            vertices[ind + 7] = uvs.get(i * 2 + 1);
        }
        return vertices;
    }

    private int[] getIndices(List<Integer> polygons) {
        int[] indices = new int[polygons.size()];
        for (int i = 0; i < polygons.size(); i += 7) {
            indices[i] = polygons.get(i);
            indices[i + 1] = polygons.get(i + 2);
            indices[i + 2] = polygons.get(i + 4);
        }
        return indices;
    }
}
