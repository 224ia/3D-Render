package Rendering.Software;

import Geometry.RenderPolygon;
import Geometry.Vertic;
import Input.Software.SoftwareInput;
import Input.Software.SoftwareMouse;
import Rendering.Projection;
import Rendering.Renderer;
import Software.Frame;
import UI.Software.SoftwareUI;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public final class SoftwareRenderer extends Renderer {
    private final Frame frame;
    private final BufferedImage image;
    private final Graphics2D g2;

    private final float[] pixels;

    private SoftwareRenderer(Frame frame) {
        super(new SoftwareInput(frame.getFrame()),
                new SoftwareMouse(frame.getFrame()));
        this.frame = frame;
        this.image = new BufferedImage(frame.WIDTH, frame.HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.g2 = image.createGraphics();
        this.ui = new SoftwareUI(g2);

        this.pixels = new float[frame.WIDTH * frame.HEIGHT];
        Arrays.fill(pixels, 1f);
    }

    public SoftwareRenderer(int width, int height) {
        this(createFrame(width, height));
    }

    private static Frame createFrame(int width, int height) {
        return new Frame(width, height);
    }

    @Override
    public void render(List<RenderPolygon> renderPolygons, Projection projection) {
        g2.setColor(new Color(147, 147, 147, 255));
        g2.fillRect(0, 0, frame.WIDTH, frame.HEIGHT);

        Arrays.fill(pixels, 1f);

        for (RenderPolygon polygon : renderPolygons) {
            projectVertices(g2, polygon, projection);
        }

        if (ui != null) drawUI();
        frame.updateImage(image);
    }

    public void projectVertices(Graphics2D g2, RenderPolygon polygon, Projection projection) {
        if (polygon.v0.pos.z <= 0 || polygon.v1.pos.z <= 0 || polygon.v2.pos.z <= 0) return;

        projection.project(polygon.v0.pos);
        projection.project(polygon.v1.pos);
        projection.project(polygon.v2.pos);

        Vector4f lightDir = new Vector4f(1, 1, 1, 0).normalize();
        float dot = polygon.normal.dot(lightDir.negate()) * 0.5f + 0.5f;

        drawTriangleBarycentric(g2, polygon.v0, polygon.v1, polygon.v2, polygon.texture, polygon.color, dot);
    }

    private void drawTriangleBarycentric(Graphics2D g2, Vertic v0, Vertic v1, Vertic v2, BufferedImage texture, Vector3f color, float dot) {
        int minX = (int) Math.min(v0.pos.x, Math.min(v1.pos.x, v2.pos.x));
        int maxX = (int) Math.max(v0.pos.x, Math.max(v1.pos.x, v2.pos.x));
        int minY = (int) Math.min(v0.pos.y, Math.min(v1.pos.y, v2.pos.y));
        int maxY = (int) Math.max(v0.pos.y, Math.max(v1.pos.y, v2.pos.y));

        minX = Math.max(0, minX);
        maxX = Math.min(frame.WIDTH - 1, maxX);
        minY = Math.max(0, minY);
        maxY = Math.min(frame.HEIGHT - 1, maxY);

        if (v0.uv != null && v1.uv != null && v2.uv != null && texture != null) {
            v0.uv.mul(v0.pos.w);
            v1.uv.mul(v1.pos.w);
            v2.uv.mul(v2.pos.w);
        }

        float area = edgeFunction(v0.pos, v1.pos, v2.pos);
        if (area == 0) return;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                float w0 = edgeFunction(v1.pos, v2.pos, x, y);
                float w1 = edgeFunction(v2.pos, v0.pos, x, y);
                float w2 = area - w0 - w1;
                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    w0 /= area; w1 /= area; w2 = 1 - w0 - w1;

                    // Z-Buffer
                    float zNorm = w0 * v0.pos.z + w1 * v1.pos.z + w2 * v2.pos.z;
                    if (zNorm < pixels[y * frame.WIDTH + x]) {
                        pixels[y * frame.WIDTH + x] = zNorm;

                        // Lighting
                        float r = color.x * dot;
                        float g = color.y * dot;
                        float b = color.z * dot;

                        if (v0.uv != null && v1.uv != null && v2.uv != null && texture != null) {
                            float zInterpolated = 1f / (w0 * v0.pos.w + w1 * v1.pos.w + w2 * v2.pos.w);

                            float u = (w0 * v0.uv.x + w1 * v1.uv.x + w2 * v2.uv.x) * zInterpolated;
                            float v = (w0 * v0.uv.y + w1 * v1.uv.y + w2 * v2.uv.y) * zInterpolated;

                            int texX = (int) (u * texture.getWidth());
                            int texY = (int) (v * texture.getHeight());

                            texX = Math.min(texX, texture.getWidth() - 1);
                            texY = Math.min(texY, texture.getHeight() - 1);

                            Color texColor = new Color(texture.getRGB(texX, texY));

                            int rt = (int) (texColor.getRed() * r);
                            int gt = (int) (texColor.getGreen() * g);
                            int bt = (int) (texColor.getBlue() * b);

                            g2.setColor(new Color(rt, gt, bt));
                        } else {
                            g2.setColor(new Color(r, g, b));
                        }
                        g2.fillRect(x, y, 1, 1);
                    }
                }
            }
        }
    }

    private float edgeFunction(Vector4f v0, Vector4f v1, Vector4f v2) {
        return edgeFunction(v0, v1, v2.x, v2.y);
    }

    private float edgeFunction(Vector4f v0, Vector4f v1, float x, float y) {
        return (x - v0.x) * (v1.y - v0.y) - (y - v0.y) * (v1.x - v0.x);
    }

    @Override
    protected void drawUI() {
        ui.draw();
    }
}
