package Rendering.OpenGL;

import Util.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
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
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
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
            String fragmentSource = readShader("Fragment.fraq");

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

    private void gameLoop() {
        float[] vertices = {-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f};

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        double startTime = glfwGetTime();

//        while (!glfwWindowShouldClose(window)) {
//            if (shaderProgram != -1) {
//                double currentTime = glfwGetTime() - startTime;
//
//                glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // чорний фон
//                glClear(GL_COLOR_BUFFER_BIT);
//
//                if (input.isKeyPressed(GLFW_KEY_F1)) {
//                    loadShader(Render.RAY_CASTING);
//                } else if (input.isKeyPressed(GLFW_KEY_F2)) {
//                    loadShader(Render.RAY_TRACING);
//                } else if (input.isKeyPressed(GLFW_KEY_F3)) {
//                    loadShader(Render.RAY_MARCHING);
//                }
//
//                glUseProgram(shaderProgram);
//
//                glBindVertexArray(vao);
//
//                int resLoc = glGetUniformLocation(shaderProgram, "iResolution");
//                glUniform2f(resLoc, width, height);
//
//                int timeLoc = glGetUniformLocation(shaderProgram, "iTime");
//                glUniform1f(timeLoc, (float) currentTime);
//
//                float[] sceneData = scene.getSceneData();
//                int objectCount = sceneData.length / 8;
//
//                int dataLoc = glGetUniformLocation(shaderProgram, "sceneData");
//                glUniform1fv(dataLoc, sceneData);
//
//                int countLoc = glGetUniformLocation(shaderProgram, "objectCount");
//                glUniform1i(countLoc, objectCount);
//
//                scene.light.dir = new Vector3f(0.45f, (float) -(Math.sin(currentTime / 5)), (float) -Math.cos(currentTime / 5)).normalize();
//                Vector3f lightDir = scene.light.dir;
//                int lightDirLoc = glGetUniformLocation(shaderProgram, "lightDir");
//                glUniform3f(lightDirLoc, lightDir.x, lightDir.y, lightDir.z);
//
//                camera.update();
//                Vector3f cameraPos = camera.getPos();
//                int posLoc = glGetUniformLocation(shaderProgram, "pos");
//                glUniform3f(posLoc, cameraPos.x, cameraPos.y, cameraPos.z);
//
//                int yawLoc = glGetUniformLocation(shaderProgram, "uYaw");
//                glUniform1f(yawLoc, input.getYaw());
//
//                int pitchLoc = glGetUniformLocation(shaderProgram, "uPitch");
//                glUniform1f(pitchLoc, input.getPitch());
//
//                int scrollLoc = glGetUniformLocation(shaderProgram, "uScroll");
//                glUniform1f(scrollLoc, input.getScroll());
//
//                glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//
//                glfwSwapBuffers(window);
//                glfwPollEvents();
//            }
//        }
    }

    private void cleanup() {
        if (window != -1) {
            glfwDestroyWindow(window);
            window = -1;
        }
        glfwTerminate();
    }
}
