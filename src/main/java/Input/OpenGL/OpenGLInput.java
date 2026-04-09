package Input.OpenGL;

import Input.Input;

import static org.lwjgl.glfw.GLFW.*;

public class OpenGLInput extends Input {

    private final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

    public boolean isKeyPressed(int key) {
        return keys[key];
    }

    public OpenGLInput(long window) {
        glfwSetKeyCallback(window, (win, key, _, action, _) -> {
            if (key >= 0 && key < keys.length) keys[key] = action != GLFW_RELEASE;

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(win, true);
            }
        });
    }
}
