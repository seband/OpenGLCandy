package engine;

import java.util.ArrayList;

public abstract class AbstractScene implements Drawable{
    protected Camera camera;

    protected ArrayList<GameObject> gameObjectList = new ArrayList<>();

    public AbstractScene(Camera camera){
        this.camera = camera;
    }

    public void addGameObject(GameObject go){
        gameObjectList.add(go);
    }

    public void removeGameObject(GameObject go){
        gameObjectList.remove(go);
    }

    public abstract void draw();
}
