package engine;

import engine.model.DepthTexture;
import engine.model.Model;
import engine.model.Texture;
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
        if(model != null)
            model.draw(program, camera, transform);
    }
    public void setTexture(Texture tex){
        model.setTexture(tex);
    }
    public void setDepthTexture(DepthTexture tex){
        model.setDepthTexture(tex);
    }
    public boolean getLit(){
        return model.getLit();
    }

    public void setFxMap(Texture tex){
        model.setFxMap(tex);
    }
}
