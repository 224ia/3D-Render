package Rendering;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class Projection {
    public final float WIDTH;
    public final float HEIGHT;
    public final float ASPECT;

    public final float NEAR;
    public final float FAR;

    public Matrix4f projMatrix = new Matrix4f();

    public Projection(float width, float height, float aspect, float near, float far, int fov) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ASPECT = aspect;
        this.NEAR = near;
        this.FAR = far;
        createMatrix(fov);
    }

    public void scroll(int fov) {
        createMatrix(fov);
    }

    private void createMatrix(int fov) {
        float ctg = (float) (1f / Math.tan(Math.toRadians(fov / 2f)));
        this.projMatrix.set(ctg / ASPECT, 0, 0, 0,
                0, ctg, 0, 0,
                0, 0, (FAR + NEAR) / (FAR - NEAR), 1,
                0, 0, -2 * FAR * NEAR / (FAR - NEAR), 0);
    }

    public void project(Vector4f v) {
        toClip(v);
        perspectiveDivide(v);
    }

    public void toClip(Vector4f v) {
        v.mul(projMatrix);
    }

    public void perspectiveDivide(Vector4f v) {
        float invW = 1f / v.w;
        v.x = v.x * invW;
        v.y = v.y * invW;
        v.z = v.z * invW;
        v.w = invW;
    }

    public void toScreen(Vector4f v) {
        v.x = (v.x * 0.5f + 0.5f) * WIDTH;
        v.y = (1f - (v.y * 0.5f + 0.5f)) * HEIGHT;
    }
}
