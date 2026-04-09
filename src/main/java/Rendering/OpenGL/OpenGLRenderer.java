package Rendering.OpenGL;

import Asset.OpenGL.OpenGLModel;
import Core.Camera;
import Core.Object;
import Input.OpenGL.OpenGLInput;
import Input.OpenGL.OpenGLMouse;
import Rendering.Projection;
import Rendering.Renderer;
import Util.Logger;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

public class OpenGLRenderer extends Renderer {
    private final long window;
    private final int shaderProgram;

    private OpenGLRenderer(long window, int shaderProgram) {
        super(new OpenGLInput(window),
                new OpenGLMouse(window));
        this.window = window;
        this.shaderProgram = shaderProgram;
    }

    public OpenGLRenderer(int width, int height) {
        this(initShaderProcessing(width, height));
    }

    private OpenGLRenderer(ShaderProcessing shaderProcessing) {
        this(shaderProcessing.getWindow(), shaderProcessing.getShaderProgram());
    }

    private static ShaderProcessing initShaderProcessing(int width, int height) {
        ShaderProcessing shaderProcessing = new ShaderProcessing();
        shaderProcessing.init(width, height);
        return shaderProcessing;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void render(List<Object> objects, Camera camera, Projection projection) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(shaderProgram);

        for (Object object : objects) {
            uploadMatrix("model", object.getModelMatrix());
            uploadMatrix("view", camera.getViewMatrix());
            uploadMatrix("projection", projection.projMatrix);

            if (object.model instanceof OpenGLModel model) {
                glBindVertexArray(model.vao);
                glDrawArrays(GL_TRIANGLES, 0, model.vertexCount);
            } else {
                Logger.error("Obejct isn't OpenGL type");
            }
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    private void uploadMatrix(String uniformName, Matrix4f matrix) {
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, uniformName), false, matrix.get(new float[16]));
    }

    @Override
    protected void drawUI() {

    }
}
