package engine;

import org.joml.Vector4f;

public class Sun {
    public static Sun sun;
    public Vector4f color;
    public GameObject gc;
    public Sun(GameObject gc){
        if(this.sun != this)
            this.sun = this;
        this.gc = gc;
    }
}
