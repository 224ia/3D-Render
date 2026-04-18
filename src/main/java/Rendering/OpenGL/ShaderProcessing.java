package Rendering.OpenGL;

import Util.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FILL;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProcessing {
    private long window = -1;
    private int shaderProgram;

    /**
     * Reuse an already-created GLFW window + current OpenGL context.
     * Intended for auxiliary pipelines (e.g., UI shaders) that must not create a new window.
     */
    public void useExistingWindow(long window) {
        this.window = window;
    }

    public long getWindow() {
        return window;
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public void init(int width, int height, String shaderPackagePath) {
        if (window == -1) {
            initGLFW(width, height);
            initOpenGL(width, height);
            loadShader(shaderPackagePath);
            return;
        }
        Logger.warn("Shaders have already been initialized");
    }

    private void initGLFW(int width, int height) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArray);
        GLFWErrorCallback.createPrint(ps).set();

        String errorLog = byteArray.toString();
        if (!errorLog.isBlank()) {
            Logger.error("GLFW error: " + errorLog);
        }

        if (!glfwInit()) {
            Logger.error("Unable to initialize GLFW");
            throw new IllegalStateException();
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, "OpenGL Rendering", NULL, NULL);
        if (window == NULL) {
            Logger.error("Failed to create window");
            throw new RuntimeException();
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
                    .getResourceAsStream(filename);
            if (is == null) {
                Logger.error("File not found: " + filename);
                return "";
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.error("Failed to read shader", e);
            return "";
        }
    }

    private int compileShader(String source, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(shader);
            Logger.error("Shader compilation failed: " + log);
            throw new RuntimeException();
        }
        return shader;
    }

    public void loadShader(String shaderPackagePath) {
        String vertexSource = readShader(shaderPackagePath + "Vertex.vert");
        String fragmentSource = readShader(shaderPackagePath + "Fragment.frag");
        if (vertexSource.isEmpty() || fragmentSource.isEmpty()) {
            Logger.error("Failed to load shaders");
            throw new RuntimeException();
        }

        int vertexShader = compileShader(vertexSource, GL_VERTEX_SHADER);
        int fragmentShader = compileShader(fragmentSource, GL_FRAGMENT_SHADER);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            String log = glGetProgramInfoLog(shaderProgram);
            Logger.error("Shader linking failed: " + log);
            throw new RuntimeException();
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
}
