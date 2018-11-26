package engine;

import java.util.ArrayList;

public abstract class AbstractScene implements Drawable{
    protected Camera camera;

    protected ArrayList<GameObject> gameObjectList = new ArrayList<>();

    /**
     * Scene used to hold game-objects viewed through main camera
     * @param camera main camera
     */
    public AbstractScene(Camera camera){
        this.camera = camera;
    }

    /**
     * Add game object to the list objects to render
     * @param go
     */
    public void addGameObject(GameObject go){
        gameObjectList.add(go);
    }

    /**
     * Remove game object from list of objects to render
     * @param go
     */
    public void removeGameObject(GameObject go){
        gameObjectList.remove(go);
    }

    /**
     * Call for updating scene and game objects, followed by rendering them
     * @param deltaT
     */
    public void draw(float deltaT){
        this.update();
        this.camera.update(deltaT);
        this.gameObjectList.forEach(gc -> gc.update());
        this.render();
    }

    /**
     * Render the game objects
     */
    public abstract void render();

    /**
     * Handle global scene updates
     */
    protected void update(){}
}
