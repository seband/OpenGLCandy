package game;


import engine.Camera;
import engine.GameObject;
import engine.ObjectScene;
import engine.model.Model;
import org.joml.Vector3f;
import utils.ModelLoader;
import utils.TextureLoader;

import java.io.File;

/**
 * Main scene for testing
 */
public class MainScene extends ObjectScene {

    public MainScene(Camera camera) {
        super(camera);
    }

    /**
     * Set up the scene (add models etc.)
     */
    @Override
    protected void initScene() {
        try {
            Model m = ModelLoader.loadModel(mainProgram, new File("models/Mailbox.obj"));
            m.setTexture(TextureLoader.loadTexture(new File("textures/DiffuseMap.png")));
            GameObject gc = new AnimatedGameObject(m);
            addGameObject(gc);

            GameObject gc2 = new AnimatedGameObject(ModelLoader.loadModel(mainProgram, new File("models/bunny.obj")));
            gc2.transform.position = new Vector3f(2,0,-3);
            addGameObject(gc2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
