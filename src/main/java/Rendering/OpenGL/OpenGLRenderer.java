package Rendering.OpenGL;

import Asset.OpenGL.OpenGLModel;
import Core.Camera;
import Core.Object;
import Input.OpenGL.OpenGLInput;
import Input.OpenGL.OpenGLMouse;
import Rendering.Projection;
import Rendering.Renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

public class OpenGLRenderer extends Renderer {
    private final int shaderProgram;

    private OpenGLRenderer(long window, int shaderProgram) {
        super(new OpenGLInput(window),
                new OpenGLMouse(window));
        this.shaderProgram = shaderProgram;
    }

    public OpenGLRenderer(int width, int height) {
        ShaderProcessing shaderProcessing = new ShaderProcessing();
        shaderProcessing.init(width, height);
        this(shaderProcessing.getWindow(), shaderProcessing.getShaderProgram());
    }

    @Override
    public void render(List<Object> objects, Camera camera, Projection projection) {
        glUseProgram(shaderProgram);

        for (Object object : objects) {
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "model"), false, object.modelMatrixBuffer);
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "view"), false, camera.viewMatrixBuffer);
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "projection"), false, projection.projMatrixBuffer);

            if (object.model instanceof OpenGLModel model) {
                glBindVertexArray(model.vao);
                glDrawElements(GL_TRIANGLES, model.vertexCount, GL_UNSIGNED_INT, 0);
            }
        }
    }

    @Override
    protected void drawUI() {

    }
}
