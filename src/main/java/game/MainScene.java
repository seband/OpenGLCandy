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
            Model m = ModelLoader.loadModel(mainProgram, new File("models/box.obj"));
            m.setTexture(TextureLoader.loadTexture(new File("textures/WeatheredWood2_S.jpg")));
            m.setNormalMap(TextureLoader.loadTexture(new File("textures/NormalMap.png")));
            GameObject gc = new AnimatedGameObject(m);
            gc.transform.position = new Vector3f(0,0,-3);

            addGameObject(gc);

            GameObject gc2 = new AnimatedGameObject(ModelLoader.loadModel(mainProgram, new File("models/box.obj")));
            gc2.transform.position = new Vector3f(2.0f, 2.0f, -3.0f);
            gc2.transform.scale = new Vector3f(0.3f, 0.3f, 0.3f);
            addGameObject(gc2);
            this.camera.setPosition(new Vector3f(3,3,3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
