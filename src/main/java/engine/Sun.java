package engine;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Sun {
    public static Sun sun;
    public GameObject gc;
    public Matrix4f shadowMatrix = new Matrix4f();
    public Sun(GameObject gc){
        if(this.sun != this)
            this.sun = this;
        this.gc = gc;
    }
}
