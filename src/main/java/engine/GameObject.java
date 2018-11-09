package engine;

import engine.model.Model;

public class GameObject{
    Transform transform = new Transform();
    Model model;
    public GameObject(Model model){
        this.model = model;
    }
    public void draw(Camera camera) {
        if(model != null)
            model.draw(camera, transform);
    }
}
