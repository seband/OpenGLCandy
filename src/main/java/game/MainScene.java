package game;


import engine.Camera;
import engine.GameObject;
import engine.ObjectScene;
import engine.model.Model;
import engine.model.SquareModel;
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
            //Model m = ModelLoader.loadModel(mainProgram, new File("models/mini_wood_barrel.obj"));
            Model m = new SquareModel(mainProgram);
            m.setTexture(TextureLoader.loadTexture(new File("textures/mini_diffus.tga")));
            //m.setNormalMap(TextureLoader.loadTexture(new File("textures/mini_normal.tga")));
            GameObject gc = new AnimatedGameObject(m);
            gc.transform.position = new Vector3f(0,0,-2);

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
