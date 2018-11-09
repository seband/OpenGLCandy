package engine;

import org.joml.Vector3f;
import utils.OBJLoader;
import utils.Program;

import java.io.File;
import java.io.IOException;

public class ObjectScene extends AbstractScene {
    int mainProgram;

    public ObjectScene(Camera camera) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        initScene();
    }

    private void initScene(){
        try {
            GameObject gc = new GameObject(OBJLoader.loadModel(mainProgram, new File("models/bunny.obj")));
            addGameObject(gc);

            gc = new GameObject(OBJLoader.loadModel(mainProgram, new File("models/bunny.obj")));
            gc.transform.position = new Vector3f(2,0,-3);
            addGameObject(gc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(){
        for(GameObject go: this.gameObjectList)
            go.draw(mainProgram, this.camera);
    }

}
