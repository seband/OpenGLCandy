package engine;

public class ObjectScene extends AbstractScene {


    public ObjectScene(Camera camera) {
        super(camera);
    }

    public void draw(){
        for(GameObject go: this.gameObjectList)
            go.draw(this.camera);
    }

}
