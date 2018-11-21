package game;


import engine.*;
import engine.Renderers.*;
import engine.model.Model;
import engine.model.SquareModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import utils.ModelLoader;
import utils.TextureLoader;

import java.io.File;
import java.util.ArrayList;


/**
 * Main scene for testing
 */
public class MainScene extends AbstractScene {
    int mainProgram, textureProgram, radialBlurProgram, depthTextureProgram, noLightProgram, SSAOProgram;
    FBORenderer sceneRenderer, noLightRenderer,godRayRenderer, SSAORenderer;
    TextureRenderer textureRenderer;
    FBODepthRenderer depthTextureRenderer;
    GameObject square;
    ArrayList<GameObject> fxObjectList = new ArrayList<>();
    public MainScene(Camera camera) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
            noLightProgram = utils.Program.createProgram("shaders/noLightShader.vert", "shaders/noLightShader.frag");
            textureProgram = utils.Program.createProgram("shaders/textureShader.vert", "shaders/textureShader.frag");
            radialBlurProgram = utils.Program.createProgram("shaders/radialBlurShader.vert", "shaders/radialBlurShader.frag");
            depthTextureProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
            SSAOProgram = utils.Program.createProgram("shaders/SSAOShader.vert", "shaders/SSAOShader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        sceneRenderer = new FBOSceneRenderer(mainProgram, 600, 600);
        noLightRenderer = new FBOSceneRenderer(noLightProgram, 600, 600);
        godRayRenderer = new FBOGodRayRenderer(radialBlurProgram, 600, 600);
        depthTextureRenderer = new FBODepthRenderer(depthTextureProgram, 600, 600);
        SSAORenderer = new FBOSSAORenderer(SSAOProgram, 600, 600);
        textureRenderer = new TextureRenderer(textureProgram);

        square  = new StaticGameObject(new SquareModel(textureProgram));
        square.transform.position = new Vector3f(0,0,-2);
        fxObjectList.add(square);
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
            gc.transform.position = new Vector3f(0,1,-2);
            gc.transform.scale= new Vector3f(0.01f,0.01f,0.01f);

            addGameObject(gc);

            GameObject gc2 = new AnimatedGameObject(m);
            gc2.transform.position = new Vector3f(0.5f,1,-3);
            gc2.transform.scale= new Vector3f(0.01f,0.01f,0.01f);
            addGameObject(gc2);

            Model sun = ModelLoader.loadModel(mainProgram, new File("models/sun.obj"));
            GameObject sunGC = new GameObject(sun) {
                boolean right=true;
                @Override
                public void update() {
                    if(this.transform.position.x > 8)
                        right = false;
                    else if(this.transform.position.x <-8)
                        right = true;
                    this.transform.position.x = right ? this.transform.position.x+0.01f : this.transform.position.x-0.01f;
                }
            };
            sunGC.transform.position = new Vector3f(8,3,-40);
            sunGC.transform.scale = new Vector3f(3);
            addGameObject(sunGC);
            new Sun(sunGC);
            this.camera.setPosition(new Vector3f(0,0,0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw() {
        //Render GameObjects
        sceneRenderer.draw(gameObjectList, camera);

        //gameObjectList.forEach(x -> {if(!x.getLit())sceneRenderer.draw(x, camera);});

        //Render GameObjects
        noLightRenderer.draw(gameObjectList, camera);


        square.setTexture(noLightRenderer.getTexture());
        square.setFxMap(sceneRenderer.getTexture());
        godRayRenderer.draw(fxObjectList, camera);

        //Render FBO result
        square.setTexture(godRayRenderer.getTexture());

        SSAORenderer.draw(fxObjectList, camera);
        depthTextureRenderer.draw(gameObjectList, camera);
        square.setDepthTexture(depthTextureRenderer.getDtex());
        square.setTexture(SSAORenderer.getTexture());

        textureRenderer.draw(fxObjectList, camera);

    }
}
