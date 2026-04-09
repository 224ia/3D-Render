package Core;

import Input.Input;
import Input.MouseProcessing;
import Util.FpsCounter;
import Util.Logger;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public final class Camera {
    private final Vector3f pos;

    public final Input input;
    public final MouseProcessing mouse;

    private final Matrix3f dirMat = new Matrix3f();

    private Matrix4f viewMatrix;

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
        viewMatrix = new Matrix4f().rotateX(mouse.getPitch()).rotateY(-mouse.getYaw());

        Vector3f dir = new Vector3f(0, 0, 0);

        if (input.isKeyPressed(GLFW_KEY_W)) dir.z -= 1;
        if (input.isKeyPressed(GLFW_KEY_S)) dir.z += 1;
        if (input.isKeyPressed(GLFW_KEY_A)) dir.x += 1;
        if (input.isKeyPressed(GLFW_KEY_D)) dir.x -= 1;

        if (dir.length() > 0) dir.normalize();

        viewMatrix.get3x3(dirMat).invert();
        dir.mul(dirMat);

        float deltaSpeed = (float) (speed * FpsCounter.getDeltaTime());
        pos.add(dir.mul(deltaSpeed));

        if (input.isKeyPressed(GLFW_KEY_Q)) pos.y += deltaSpeed;
        if (input.isKeyPressed(GLFW_KEY_E)) pos.y -= deltaSpeed;

        viewMatrix.translate(pos.x, pos.y, pos.z);
    }
}
