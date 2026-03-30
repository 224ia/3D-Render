package Util;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Vertic {
    public Vector4f pos;
    public float invZ;

    public Vector2f uv;

    public Vertic(Vector4f pos, Vector2f uv) {
        this.pos = pos;
        this.invZ = 1f / pos.z;
        this.uv = uv;
    }

    public Vertic(Vertic v) {
        this.pos = new Vector4f(v.pos);
        this.invZ = v.invZ;
        if (v.uv != null) {
            this.uv = new Vector2f(v.uv);
        }
    }
}
