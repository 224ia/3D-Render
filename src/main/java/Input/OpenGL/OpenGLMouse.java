package Input.OpenGL;

import Input.MouseProcessing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

public class OpenGLMouse extends MouseProcessing {
    private double lastMouseX = 400;
    private double lastMouseY = 400;
    private boolean firstMouseInput = true;

    public OpenGLMouse(long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetScrollCallback(window, (_, _, yOffset) ->
                scroll = (int) Math.clamp(scroll + yOffset / 3.5f, 1f, 4f));

        glfwSetCursorPosCallback(window, (_, xPos, yPos) -> {
            if (firstMouseInput) {
                lastMouseX = xPos;
                lastMouseY = yPos;
                firstMouseInput = false;
                return;
            }

            double deltaX = xPos - lastMouseX;
            double deltaY = yPos - lastMouseY;

            yaw += (float) (deltaX * 0.005);
            pitch -= (float) (deltaY * 0.005);

            if (pitch > 1.5f) pitch = 1.5f;
            if (pitch < -1.5f) pitch = -1.5f;

            lastMouseX = xPos;
            lastMouseY = yPos;
        });
    }
}
