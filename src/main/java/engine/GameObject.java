package engine;

import engine.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject{
    Transform transform = new Transform();
    Model model;
    public GameObject(Model model){
        this.model = model;
    }
    public void update(){
        transform.rot = new Matrix4f().rotateX(0.01f).mul(transform.rot);
    }

    public void draw(Camera camera) {
        update();
        if(model != null)
            model.draw(camera, transform);
    }
}
