package engine;

import engine.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class GameObject{
    public Transform transform = new Transform();
    private Model model;
    public GameObject(Model model){
        this.model = model;
    }
    public abstract void update();

    public void draw(int program, Camera camera) {
        update();
        if(model != null)
            model.draw(program, camera, transform);
    }
}
