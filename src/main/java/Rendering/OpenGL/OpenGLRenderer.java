package Rendering.OpenGL;

import Asset.OpenGL.OpenGLModel;
import Core.Camera;
import Core.Object;
import Input.OpenGL.OpenGLInput;
import Input.OpenGL.OpenGLMouse;
import Rendering.Projection;
import Rendering.Renderer;
import UI.OpenGL.OpenGLUI;
import Util.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;

public class OpenGLRenderer extends Renderer {
    private final long window;
    private final int shaderProgram;

    private final int textureUniformLocation;
    private final int useTextureUniformLocation;
    private final IdentityHashMap<BufferedImage, Integer> textureCache = new IdentityHashMap<>();

    private OpenGLRenderer(long window, int shaderProgram, int width, int height) {
        super(new OpenGLInput(window),
                new OpenGLMouse(window));
        this.window = window;
        this.shaderProgram = shaderProgram;

        this.ui = new OpenGLUI(window, width, height);

        glUseProgram(shaderProgram);
        this.textureUniformLocation = glGetUniformLocation(shaderProgram, "texture");
        this.useTextureUniformLocation = glGetUniformLocation(shaderProgram, "useTexture");
    }

    public OpenGLRenderer(int width, int height) {
        this(initShaderProcessing(width, height), width, height);
    }

    private OpenGLRenderer(ShaderProcessing shaderProcessing, int width, int height) {
        this(shaderProcessing.getWindow(), shaderProcessing.getShaderProgram(), width, height);
    }

    private static ShaderProcessing initShaderProcessing(int width, int height) {
        ShaderProcessing shaderProcessing = new ShaderProcessing();
        shaderProcessing.init(width, height, "Shaders/Render/");
        return shaderProcessing;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void render(List<Object> objects, Camera camera, Vector3f lightDir, Projection projection) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(shaderProgram);

        for (Object object : objects) {
            uploadMatrix("model", object.getModelMatrix());
            uploadMatrix("view", camera.getViewMatrix());
            uploadMatrix("projection", projection.projMatrix);

            uploadVector3("color", object.color);
            bindTexture(object.texture);

            uploadVector3("lightDir", lightDir);

            if (object.model instanceof OpenGLModel model) {
                glBindVertexArray(model.vao);
                glDrawArrays(GL_TRIANGLES, 0, model.vertexCount);
            } else {
                Logger.error("Obejct isn't OpenGL type");
            }
        }

        drawUI();

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    private void uploadMatrix(String uniformName, Matrix4f matrix) {
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, uniformName), false, matrix.get(new float[16]));
    }

    private void uploadVector3(String uniformName, Vector3f vector) {
        glUniform3f(glGetUniformLocation(shaderProgram, uniformName), vector.x, vector.y, vector.z);
    }

    private void bindTexture(BufferedImage image) {
        if (useTextureUniformLocation != -1) {
            glUniform1i(useTextureUniformLocation, image != null ? 1 : 0);
        }
        if (image == null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, 0);
            return;
        }

        int texId = textureCache.computeIfAbsent(image, this::createTexture2D);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texId);
        if (textureUniformLocation != -1) {
            glUniform1i(textureUniformLocation, 0);
        }
    }

    private int createTexture2D(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] argb = new int[width * height];
        image.getRGB(0, 0, width, height, argb, 0, width);

        // Flip Y: Java image origin is top-left, OpenGL texture coords usually bottom-left.
        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
        for (int y = height - 1; y >= 0; y--) {
            int row = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = argb[row + x]; // AARRGGBB
                pixels.put((byte) ((pixel >> 16) & 0xFF)); // R
                pixels.put((byte) ((pixel >> 8) & 0xFF));  // G
                pixels.put((byte) (pixel & 0xFF));         // B
                pixels.put((byte) ((pixel >> 24) & 0xFF)); // A
            }
        }
        pixels.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        glBindTexture(GL_TEXTURE_2D, 0);
        return texId;
    }
}
