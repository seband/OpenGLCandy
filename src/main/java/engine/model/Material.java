package engine.model;

import org.joml.Vector3f;

public class Material {
    Vector3f ka;
    Vector3f kd;
    Vector3f ks;
    public float shininess;
    public Material(float ka, float kd, float ks, float shininess){
        this.ka = new Vector3f(ka);
        this.kd = new Vector3f(kd);
        this.ks = new Vector3f(ks);
        this.shininess = shininess;
    }
}
