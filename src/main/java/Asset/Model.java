package Asset;

import Geometry.Polygon;

import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Model {
    private final String name;

    public Model(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


}
