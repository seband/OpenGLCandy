package game;


import engine.*;
import engine.Renderers.*;
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
    int mainProgram, textureProgram, radialBlurProgram, depthTextureProgram, noLightProgram;
    FBORenderer sceneRenderer, noLightRenderer,godRayRenderer;
    TextureRenderer textureRenderer;
    FBODepthRenderer depthTextureRenderer;
    GameObject square;
    public MainScene(Camera camera) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
            noLightProgram = utils.Program.createProgram("shaders/noLightShader.vert", "shaders/noLightShader.frag");
            textureProgram = utils.Program.createProgram("shaders/textureShader.vert", "shaders/textureShader.frag");
            radialBlurProgram = utils.Program.createProgram("shaders/radialBlurShader.vert", "shaders/radialBlurShader.frag");
            depthTextureProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        sceneRenderer = new FBOSceneRenderer(mainProgram, 600, 600);
        noLightRenderer = new FBOSceneRenderer(noLightProgram, 600, 600);
        godRayRenderer = new FBOGodRayRenderer(radialBlurProgram, 600, 600);
       // depthTextureRenderer = new FBODepthRenderer(depthTextureProgram, 600, 600);
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
            m.setTexture(TextureLoader.loadTexture(new File("textures/mini_diffus.tga")));
            m.setNormalMap(TextureLoader.loadTexture(new File("textures/mini_normal.tga")));
            m.setLit(false);
            GameObject gc = new AnimatedGameObject(m);
            gc.transform.position = new Vector3f(0,0,-2);
            gc.transform.scale= new Vector3f(0.01f,0.01f,0.01f);

            addGameObject(gc);

            Model sun = ModelLoader.loadModel(mainProgram, new File("models/sun.obj"));
            sun.setTexture(TextureLoader.loadTexture(new File("textures/sun.png")));
            GameObject sunGC = new StaticGameObject(sun);
            sunGC.transform.position = new Vector3f(2,2,-8);
            sunGC.transform.scale = new Vector3f(3);
            addGameObject(sunGC);

            this.camera.setPosition(new Vector3f(3,3,3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw() {
        //Render GameObjects
        sceneRenderer.beforeDraw();
        gameObjectList.forEach(x -> {if(!x.getLit())sceneRenderer.draw(x, camera);});

        //Render GameObjects
        noLightRenderer.beforeDraw();
        gameObjectList.forEach(x -> noLightRenderer.draw(x, camera));

        //Make godrays
      //  depthTextureRenderer.beforeDraw();
      //  gameObjectList.forEach(x -> depthTextureRenderer.draw(x, camera));


        square.setTexture(noLightRenderer.getTexture());
        square.setFxMap(sceneRenderer.getTexture());
        godRayRenderer.beforeDraw();
        godRayRenderer.draw(square, camera);

        //Render FBO result
        square.setTexture(godRayRenderer.getTexture());
        textureRenderer.draw(square, camera);
    }
}
