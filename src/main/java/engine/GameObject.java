package engine;

import engine.model.DepthTexture;
import engine.model.Model;
import engine.model.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class GameObject{
    public Transform transform = new Transform();
    private Model model;

    /**
     * Wrapper for model
     * Holds its transform and handles updates
     * @param model
     */
    public GameObject(Model model){
        this.model = model;
    }

    /**
     * Handle updates to game object
     */
    public abstract void update();

    /**
     * Draw the game objects model
     * @param program shader program to use
     * @param camera camera that acts as viewfinder
     */
    public void draw(int program, Camera camera) {
        if(model != null)
            model.draw(program, camera, transform);
    }

    /**
     * Set the model's diffuse texture
     * @param tex diffuse texture
     */
    public void setTexture(Texture tex){
        model.setTexture(tex);
    }

    /**
     * Set the model's depth texture (used for shadow mapping etc.)
     * @param tex
     */
    public void setDepthTexture(DepthTexture tex){
        model.setDepthTexture(tex);
    }

    /**
     * Gets the model's reaction to light
     * @return the model's reaction to light
     */
    public boolean getLit(){
        return model.getLit();
    }

    /**
     * Sets the model's fx-map
     * @param tex map to use for fx in shader
     */
    public void setFxMap(Texture tex){
        model.setFxMap(tex);
    }
}
