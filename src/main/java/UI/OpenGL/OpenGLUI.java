package UI.OpenGL;

import Rendering.OpenGL.ShaderProcessing;
import UI.UI;
import UI.UIElement;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class OpenGLUI extends UI {
    private static final int VERTEX_COUNT = 6;

    private final int screenWidth;
    private final int screenHeight;
    private final int shaderProgram;
    private final int vao;
    private final int vbo;

    public OpenGLUI(long window, int width, int height) {
        super();
        this.screenWidth = width;
        this.screenHeight = height;

        ShaderProcessing shaderProcessing = new ShaderProcessing();
        shaderProcessing.useExistingWindow(window);
        shaderProcessing.loadShader("Shaders/UI/");
        this.shaderProgram = shaderProcessing.getShaderProgram();

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) VERTEX_COUNT * 2 * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void addElement(UIElement element) {
        super.addElement(element);
    }

    @Override
    public void draw() {
        glUseProgram(shaderProgram);
        glBindVertexArray(vao);
        glDisable(GL_DEPTH_TEST);

        for (UIElement element : elements) {
            uploadQuad(element);
            uploadColor(element.bgColor);
            glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
        }

        glEnable(GL_DEPTH_TEST);
        glBindVertexArray(0);
    }

    private void uploadQuad(UIElement element) {
        float left = toClipX(element.x);
        float right = toClipX(element.x + element.width);
        float top = toClipY(element.y);
        float bottom = toClipY(element.y + element.height);

        float[] vertices = {
                left, top,
                right, top,
                right, bottom,
                left, top,
                right, bottom,
                left, bottom
        };

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    private void uploadColor(Color color) {
        int colorLocation = glGetUniformLocation(shaderProgram, "bgColor");
        glUniform4f(colorLocation,
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f);
    }

    private float toClipX(float x) {
        return x / screenWidth * 2f - 1f;
    }

    private float toClipY(float y) {
        return 1f - y / screenHeight * 2f;
    }
}
