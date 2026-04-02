package Geometry;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Vertic {
    public Vector4f pos;
    public Vector2f uv;

    public Vertic(Vector4f pos, Vector2f uv) {
        this.pos = pos;
        this.uv = uv;
    }

    public Vertic(Vertic v) {
        this.pos = new Vector4f(v.pos);
        if (v.uv != null) {
            this.uv = new Vector2f(v.uv);
        }
    }
}
