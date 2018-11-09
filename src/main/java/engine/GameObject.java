package engine;

import engine.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject{
    public Transform transform = new Transform();
    Model model;
    public GameObject(Model model){
        this.model = model;
        transform.position = new Vector3f(0,0,-3);
    }
    public void update(){
        transform.rot = new Matrix4f().rotateX(0.01f).mul(transform.rot);
    }

    public void draw(int program, Camera camera) {
        update();
        if(model != null)
            model.draw(program, camera, transform);
    }
}
