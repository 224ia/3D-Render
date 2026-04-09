package Core;

import Input.Software.Keys;
import Input.Input;
import Input.MouseProcessing;
import Util.FpsCounter;
import Util.Logger;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public final class Camera {
    private final Vector3f pos;

    public final Input input;
    public final MouseProcessing mouse;

    private final Matrix3f dirMat = new Matrix3f();

    private Matrix4f viewMatrix;
    public final FloatBuffer viewMatrixBuffer = BufferUtils.createFloatBuffer(16);

    public float speed;

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Camera(Vector3f pos, Input input, MouseProcessing mouse, float speed) {
        this.pos = pos;
        this.input = input;
        this.mouse = mouse;
        this.speed = speed;

        Logger.info("Camera was created");
    }

    public void update() {
        viewMatrix = new Matrix4f().rotateX(-mouse.getPitch()).rotateY(-mouse.getYaw());

        Vector3f dir = new Vector3f(0, 0, 0);

        if (input.isKeyPressed(Keys.W)) dir.z += 1;
        if (input.isKeyPressed(Keys.S)) dir.z -= 1;
        if (input.isKeyPressed(Keys.A)) dir.x -= 1;
        if (input.isKeyPressed(Keys.D)) dir.x += 1;

        if (dir.length() > 0) dir.normalize();

        viewMatrix.get3x3(dirMat).invert();
        dir.mul(dirMat);

        float deltaSpeed = (float) (speed * FpsCounter.getDeltaTime());
        pos.add(dir.mul(deltaSpeed));

        if (input.isKeyPressed(Keys.Q)) pos.y += deltaSpeed;
        if (input.isKeyPressed(Keys.E)) pos.y -= deltaSpeed;

        viewMatrix.translate(-pos.x, -pos.y, -pos.z);
        viewMatrix.get(viewMatrixBuffer);
    }
}
