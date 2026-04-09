package Core;

import Asset.Model;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.lang.Math;
import java.nio.FloatBuffer;

public final class Object {
    public String name;
    public Vector3f pos;
    public Vector3f rot;
    public Vector3f size;
    public Vector3f color;

    private Matrix4f modelMatrix;
    public final FloatBuffer modelMatrixBuffer = BufferUtils.createFloatBuffer(16);

    public final Model model;

    public final BufferedImage texture;

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Object(String name, Vector3f pos, Vector3f rot, Vector3f size, Vector3f color, Model model, BufferedImage texture) {
        this.name = name;
        this.pos = pos;
        this.rot = rot;
        this.size = size;
        this.color = color;
        this.model = model;
        this.texture = texture;

        updateMatrix();
    }

    public void rotate(Vector3f angle) {
        rot.add(angle);
        updateMatrix();
    }
    public void rotate(float x, float y, float z) {
        rot.add(new Vector3f(x, y, z));
        updateMatrix();
    }

    public void updateMatrix() {
        float angleX = (float) Math.toRadians(rot.x);
        float angleY = (float) Math.toRadians(rot.y);
        float angleZ = (float) Math.toRadians(rot.z);

        modelMatrix = new Matrix4f().translate(pos).rotateX(angleX).rotateY(angleY).rotateZ(angleZ).scale(size);
        modelMatrix.get(modelMatrixBuffer);
    }


}
