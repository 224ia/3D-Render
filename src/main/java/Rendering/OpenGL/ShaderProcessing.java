package Rendering.OpenGL;

import Util.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FILL;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProcessing {
    private long window = -1;
    private int shaderProgram;

    public long getWindow() {
        return window;
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public void init(int width, int height) {
        if (window == -1) {
            initGLFW(width, height);
            initOpenGL(width, height);
            loadShader();
            return;
        }
        Logger.warn("Shaders have already been initialized");
    }

    private void initGLFW(int width, int height) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, "OpenGL Rendering", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void initOpenGL(int width, int height) {
        GL.createCapabilities();
        glViewport(0, 0, width, height);
        glClearColor(147f / 255f, 147f / 255f, 147f / 255f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    private String readShader(String filename) {
        try {
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("Shaders/" + filename);
            if (is == null) {
                Logger.error("File not found: " + filename);
                return "";
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private int compileShader(String source, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(shader);
            throw new RuntimeException("Shader compilation failed: " + log);
        }
        return shader;
    }

    public void loadShader() {
        if (window != -1) {
            String vertexSource = readShader("Vertex.vert");
            String fragmentSource = readShader("Fragment.frag");

            if (vertexSource.isEmpty() || fragmentSource.isEmpty()) {
                throw new RuntimeException("Failed to load shaders");
            }

            int vertexShader = compileShader(vertexSource, GL_VERTEX_SHADER);
            int fragmentShader = compileShader(fragmentSource, GL_FRAGMENT_SHADER);

            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);

            if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
                String log = glGetProgramInfoLog(shaderProgram);
                throw new RuntimeException("Shader linking failed: " + log);
            }

            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        }
    }
}
