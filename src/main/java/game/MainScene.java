package game;


import engine.*;
import engine.Renderers.*;
import engine.model.Model;
import engine.model.SquareModel;
import engine.model.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import utils.ModelLoader;
import utils.TextureLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Main scene for testing
 */
public class MainScene extends AbstractScene {
    int mainProgram, textureProgram, radialBlurProgram, depthTextureProgram, noLightProgram, SSAOProgram;
    FBORenderer noLightRenderer,godRayRenderer, SSAORenderer;
    FBOSkyboxSceneRenderer sceneRenderer;
    TextureRenderer textureRenderer;
    FBODepthRenderer depthTextureRenderer;
    GameObject square, skybox;
    ArrayList<GameObject> fxObjectList = new ArrayList<>();
    List<GameObject> noSunObjectList = new ArrayList<>();
    List<GameObject> skyBoxList = new ArrayList<>();


    public MainScene(Camera camera, int width, int height) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
            noLightProgram = utils.Program.createProgram("shaders/noLightShader.vert", "shaders/noLightShader.frag");
            textureProgram = utils.Program.createProgram("shaders/textureShader.vert", "shaders/textureShader.frag");
            radialBlurProgram = utils.Program.createProgram("shaders/radialBlurShader.vert", "shaders/radialBlurShader.frag");
            depthTextureProgram = utils.Program.createProgram("shaders/depthShader.vert", "shaders/depthShader.frag");
            SSAOProgram = utils.Program.createProgram("shaders/SSAOShader.vert", "shaders/SSAOShader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        sceneRenderer = new FBOSkyboxSceneRenderer(mainProgram, width, height);
        noLightRenderer = new FBONoLightRenderer(noLightProgram, width, height);
        godRayRenderer = new FBOGodRayRenderer(radialBlurProgram, width, height);
        depthTextureRenderer = new FBODepthRenderer(depthTextureProgram, 2048, 2048);
        SSAORenderer = new FBOSSAORenderer(SSAOProgram, width, height);
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
            loadCartoonScene();

        } catch (TextureLoader.TextureLoadException e) {
            e.printStackTrace();
        }

        Model sun = ModelLoader.loadModel(mainProgram, new File("models/sun.obj"));
        GameObject sunGC = new GameObject(sun) {
                @Override
                public void update() {
                    this.transform.position = this.transform.position.rotateY(0.005f);
                }
            };
        sunGC.transform.position = new Vector3f(0,30,30);
        sunGC.transform.scale = new Vector3f(3);
        addGameObject(sunGC);
        noSunObjectList = gameObjectList.stream().filter(gc -> gc != sunGC).collect(Collectors.toList());

        new Sun(sunGC);
        this.camera.setPosition(new Vector3f(0,0,0));

        skybox = new GameObject(utils.ModelLoader.loadModel(mainProgram, new File("models/skybox_normals.obj"))) {
            @Override
            public void update() {
                skybox.transform.position = camera.getPosition();
            }
        };
        try {
            skybox.setTexture(TextureLoader.loadTexture(new File("textures/skybox_1.png")));
        }catch (Exception e){}
        skybox.transform.scale = new Vector3f(3);
        skyBoxList.add(skybox);
        System.out.println("Ready");
    }

    /**
     * Loads cartoon scene (Manual setup)
     * @throws TextureLoader.TextureLoadException
     */
    private void loadCartoonScene() throws TextureLoader.TextureLoadException {
        System.out.println("Loading Textures...");
        Texture tree1 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_01.png"));
        Texture tree2 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_02.png"));
        Texture tree3 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_03.png"));
        Texture tree4 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_04.png"));
        Texture tree5 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_05.png"));
        Texture tree6 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_06.png"));
        Texture tree7 = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_07.png"));
        Texture tree10_H = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_10_House.png"));
        Texture tree10_T = TextureLoader.loadTexture(new File("textures/modelTextures/Tree_10_Tree.png"));
        Texture backgroundTree = TextureLoader.loadTexture(new File("textures/modelTextures/BackGround_Tree.png"));

        Texture tree1Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_01_normal.png"));
        Texture tree2Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_02_normal.png"));
        Texture tree3Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_03_normal.png"));
        Texture tree4Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_04_normal.png"));
        Texture tree5Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_05_normal.png"));
        Texture tree6Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_06_normal.png"));
        Texture tree7Normal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_07_normal.png"));
        Texture tree10_HNormal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_10_House_normal.png"));
        Texture tree10_TNormal = TextureLoader.loadTexture(new File("textures/modelNormals/Tree_10_Tree_normal.png"));
        Texture backgroundTreeNormal = TextureLoader.loadTexture(new File("textures/modelNormals/BackGround_Tree_normal.png"));
        System.out.println("Loading Models...");

        int i=1;
        for(Model model : ModelLoader.loadModels(mainProgram, new File("models/cartoon.obj"))){
            try {
                switch (i){
                    case 1:
                    case 17:
                    case 13:
                    case 14:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                        model.setTexture(tree1);
                        model.setNormalMap(tree1Normal);
                        break;
                    case 2:
                        model.setTexture(tree2);
                        model.setNormalMap(tree2Normal);
                        break;
                    case 3:
                    case 16:
                        model.setTexture(tree3);
                        model.setNormalMap(tree3Normal);
                        break;
                    case 4:
                    case 8:
                    case 7:
                        model.setTexture(tree4);
                        model.setNormalMap(tree4Normal);
                        break;
                    case 5:
                        model.setTexture(tree6);
                        model.setNormalMap(tree6Normal);
                        break;
                    case 6:
                    case 11:
                    case 12:
                    case 18:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        model.setTexture(tree7);
                        model.setNormalMap(tree7Normal);
                        break;
                    case 9:
                        model.setTexture(tree10_T);
                        model.setNormalMap(tree10_TNormal);
                        break;
                    case 10:
                        model.setTexture(tree10_H);
                        model.setNormalMap(tree10_HNormal);
                        break;
                    case 15:
                        model.setTexture(tree5);
                        model.setNormalMap(tree5Normal);
                        break;
                    case 19:
                    case 20: //WATER
                        model.setTexture(backgroundTree);
                        model.setNormalMap(backgroundTreeNormal);
                        break;

                }
                model.setLit(false);
                GameObject gc = new StaticGameObject(model);
                gc.transform.scale = new Vector3f(0.04f,0.04f,0.04f);

                addGameObject(gc);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void update(){
        if(InputHandler.keyDown(GLFW.GLFW_KEY_1))
            RenderSettings.setSetting(RenderSettings.RenderSetting.LIGHT, !InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT));
        if(InputHandler.keyDown(GLFW.GLFW_KEY_2))
            RenderSettings.setSetting(RenderSettings.RenderSetting.SSAO, !InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT));
        if(InputHandler.keyDown(GLFW.GLFW_KEY_3))
            RenderSettings.setSetting(RenderSettings.RenderSetting.SHADOWS, !InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT));
        if(InputHandler.keyDown(GLFW.GLFW_KEY_4))
            RenderSettings.setSetting(RenderSettings.RenderSetting.GODRAYS, !InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT));
        skybox.update();
    }
    private Matrix4f viewMatrixCache;
    private Matrix4f projectionMatrixCache;
    private Vector3f cameraPostitionCache;
    @Override
    public void render() {
        viewMatrixCache = camera.getViewMatrix();
        projectionMatrixCache = camera.getProjectionMatrix();
        cameraPostitionCache = camera.getPosition();
        camera.setPosition(Sun.sun.gc.transform.position);
        camera.setLookAt(new Vector3f(0,0,0));
        camera.setProjectionMatrix(camera.getOrtho());

        Sun.sun.shadowMatrix = camera.getOrtho().mul(camera.getViewMatrix());
        //Render GameObjects with Shadows
        depthTextureRenderer.draw(noSunObjectList, camera);
        gameObjectList.forEach(gc -> gc.setDepthTexture(depthTextureRenderer.getDtex()));
        square.setDepthTexture(depthTextureRenderer.getDtex());
        camera.setPosition(cameraPostitionCache);
        camera.setViewMatrix(viewMatrixCache);

        camera.setProjectionMatrix(projectionMatrixCache);
        sceneRenderer.drawWithSkyBox(skybox, noSunObjectList, camera);

        //-------[GOD RAY]-------
        noLightRenderer.draw(gameObjectList, camera);
        square.setTexture(noLightRenderer.getTexture());
        square.setFxMap(sceneRenderer.getTexture());
        godRayRenderer.draw(fxObjectList, camera);
        square.setTexture(godRayRenderer.getTexture());

        //-------[SSAO]-------
        depthTextureRenderer.draw(noSunObjectList, camera);
        square.setDepthTexture(depthTextureRenderer.getDtex());
        SSAORenderer.draw(fxObjectList, camera);

        square.setTexture(SSAORenderer.getTexture());

        textureRenderer.draw(fxObjectList, camera);

    }
}
