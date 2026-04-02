package Core;

import Input.Software.Keys;
import Input.Input;
import Input.MouseProcessing;
import Util.Logger;
import org.joml.*;

public final class Camera {
    private final Vector3f pos;

    public final Input input;
    public final MouseProcessing mouse;

    private final Matrix3f dirMat = new Matrix3f();

    public float speed;

    public Camera(Vector3f pos, Input input, MouseProcessing mouse, float speed) {
        this.pos = pos;
        this.input = input;
        this.mouse = mouse;
        this.speed = speed;

        Logger.info("Camera was created");
    }

    public Matrix4f update(float deltaTime) {
        Matrix4f view = new Matrix4f().rotateX(-mouse.getPitch()).rotateY(-mouse.getYaw());

        Vector3f dir = new Vector3f(0, 0, 0);

        if (input.isKeyPressed(Keys.W)) dir.z += 1;
        if (input.isKeyPressed(Keys.S)) dir.z -= 1;
        if (input.isKeyPressed(Keys.A)) dir.x -= 1;
        if (input.isKeyPressed(Keys.D)) dir.x += 1;

        if (dir.length() > 0) dir.normalize();

        view.get3x3(dirMat);
        // For movement, we need rotate(yaw, pitch), so flip the signs
        dirMat.m02 *= -1;
        dirMat.m20 *= -1;
        dir.mul(dirMat);

        float deltaSpeed = speed * deltaTime;
        pos.add(dir.mul(deltaSpeed));

        if (input.isKeyPressed(Keys.Q)) pos.y += deltaSpeed;
        if (input.isKeyPressed(Keys.E)) pos.y -= deltaSpeed;

        return view.translate(-pos.x, -pos.y, -pos.z);
    }
}
