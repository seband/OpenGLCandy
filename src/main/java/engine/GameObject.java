package engine;

import engine.model.Model;

public class GameObject{
    Transform transform = new Transform();
    Model model;

    public void draw(Camera camera) {
        if(model != null)
            model.draw(camera, transform);
    }
}
