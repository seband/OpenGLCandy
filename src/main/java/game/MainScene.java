package game;


import engine.*;
import engine.Renderers.FBORenderer;
import engine.Renderers.FBOSceneRenderer;
import engine.Renderers.TextureRenderer;
import engine.model.Model;
import engine.model.SquareModel;
import engine.model.Texture;
import org.joml.Vector3f;
import utils.ModelLoader;
import utils.TextureLoader;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;


/**
 * Main scene for testing
 */
public class MainScene extends AbstractScene {
    int mainProgram, textureProgram;
    FBORenderer sceneRenderer;
    TextureRenderer textureRenderer;
    GameObject square;
    public MainScene(Camera camera) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
            textureProgram = utils.Program.createProgram("shaders/textureShader.vert", "shaders/textureShader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        sceneRenderer = new FBOSceneRenderer(mainProgram, 600, 600);
        textureRenderer = new TextureRenderer(textureProgram);
        square  = new StaticGameObject(new SquareModel(textureProgram));
        square.transform.position = new Vector3f(0,0,-2);
        initScene();
    }

    /**
     * Set up the scene (add models etc.)
     */
    protected void initScene() {
        try {
            Model m = ModelLoader.loadModel(mainProgram, new File("models/mini_wood_barrel.obj"));
            //Model m = new SquareModel(mainProgram);
            m.setTexture(TextureLoader.loadTexture(new File("textures/mini_diffus.tga")));
            m.setNormalMap(TextureLoader.loadTexture(new File("textures/mini_normal.tga")));
            GameObject gc = new AnimatedGameObject(m);
            gc.transform.position = new Vector3f(0,-1,-3);
            gc.transform.scale= new Vector3f(0.03f,0.03f,0.03f);

            addGameObject(gc);
            this.camera.setPosition(new Vector3f(3,3,3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw() {
        //Render GameObjects
        gameObjectList.forEach(x -> sceneRenderer.draw(x, camera));

        //Render FBO result
        square.setTexture(sceneRenderer.getTexture());
        textureRenderer.draw(square, camera);
    }
}
