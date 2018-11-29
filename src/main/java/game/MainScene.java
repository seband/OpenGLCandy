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
import org.w3c.dom.Text;
import utils.ModelLoader;
import utils.TextureLoader;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    Texture brightSky, darkSky;

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
            if(InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
                loadHagridScene();
            }
            else {
                loadCartoonScene();
            }

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
        sunGC.transform.position = new Vector3f(0,20,30);
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
            brightSky = TextureLoader.loadTexture(new File("textures/Daylight Box UV.png"));
            darkSky = TextureLoader.loadTexture(new File("textures/skybox_1.png"));
            skybox.setTexture(brightSky);
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

        RenderSettings.setSetting(RenderSettings.RenderSetting.DISPLACEMENT, false);
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
    /**
     * Loads cartoon scene (Manual setup)
     * @throws TextureLoader.TextureLoadException
     */
    private void loadHagridScene() throws TextureLoader.TextureLoadException {
        System.out.println("Loading Textures...");
        Map<String, Texture> textures = new HashMap<>();
        Map<String, Texture> normalmaps = new HashMap<>();

        textures.put("Alumox.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Alumox.jpg")));
        textures.put("ban.png", TextureLoader.loadTexture(new File("textures/hagrid/ban.png")));
        textures.put("Bellows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Bellows.jpg")));
        textures.put("BepLua.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BepLua.jpg")));
        textures.put("BigHut.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BigHut.jpg")));
        textures.put("BigWindows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BigWindows.jpg")));
        textures.put("BiNgo.png", TextureLoader.loadTexture(new File("textures/hagrid/BiNgo.png")));
        textures.put("CaiAm.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiAm.jpg")));
        textures.put("SB046.JPG", TextureLoader.loadTexture(new File("textures/hagrid/SB046.JPG")));
        textures.put("CaiChoc.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiChoc.jpg")));
        textures.put("CaiQue.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiQue.jpg")));
        textures.put("CaiVac.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiVac.jpg")));
        textures.put("CaiXaBeng.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiXaBeng.jpg")));
        textures.put("CaiXoong.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiXoong.jpg")));
        textures.put("Da.png", TextureLoader.loadTexture(new File("textures/hagrid/Da.png")));
        textures.put("DayThung.jpg", TextureLoader.loadTexture(new File("textures/hagrid/DayThung.jpg")));
        textures.put("Den.png", TextureLoader.loadTexture(new File("textures/hagrid/Den.png")));
        textures.put("Door2.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Door2.jpg")));
        textures.put("Door.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Door.jpg")));
        textures.put("DSC01897.jpg", TextureLoader.loadTexture(new File("textures/hagrid/DSC01897.jpg")));
        textures.put("ghe.png", TextureLoader.loadTexture(new File("textures/hagrid/ghe.png")));
        textures.put("GioLan.png", TextureLoader.loadTexture(new File("textures/hagrid/GioLan.png")));
        textures.put("GioMay.png", TextureLoader.loadTexture(new File("textures/hagrid/GioMay.png")));
        textures.put("Ground.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Ground.jpg")));
        textures.put("hop_do.jpg", TextureLoader.loadTexture(new File("textures/hagrid/hop_do.jpg")));
        textures.put("HopGo.jpg", TextureLoader.loadTexture(new File("textures/hagrid/HopGo.jpg")));
        textures.put("Hop_nhieu_ngan.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Hop_nhieu_ngan.jpg")));
        textures.put("indt52L.jpg", TextureLoader.loadTexture(new File("textures/hagrid/indt52L.jpg")));
        textures.put("Long_dung_nam.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Long_dung_nam.jpg")));
        textures.put("Lon.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Lon.jpg")));
        textures.put("Muoi.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Muoi.jpg")));
        textures.put("RockSmooth0031_1_M.jpg", TextureLoader.loadTexture(new File("textures/hagrid/RockSmooth0031_1_M.jpg")));
        textures.put("rockwall.jpg", TextureLoader.loadTexture(new File("textures/hagrid/rockwall.jpg")));
        textures.put("SB046.JPG", TextureLoader.loadTexture(new File("textures/hagrid/SB046.JPG")));
        textures.put("SmallHut.jpg", TextureLoader.loadTexture(new File("textures/hagrid/SmallHut.jpg")));
        textures.put("SmallWindows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/SmallWindows.jpg")));
        textures.put("So_pha.jpg", TextureLoader.loadTexture(new File("textures/hagrid/So_pha.jpg")));
        textures.put("Tham.png", TextureLoader.loadTexture(new File("textures/hagrid/Tham.png")));
        textures.put("ThungGoTron.png", TextureLoader.loadTexture(new File("textures/hagrid/ThungGoTron.png")));
        textures.put("ThungGoVuong.png", TextureLoader.loadTexture(new File("textures/hagrid/ThungGoVuong.png")));
        textures.put("Trung_Nen.png", TextureLoader.loadTexture(new File("textures/hagrid/Trung_Nen.png")));
        textures.put("Vali.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Vali.jpg")));
        textures.put("XeKutKit.png", TextureLoader.loadTexture(new File("textures/hagrid/XeKutKit.png")));
        textures.put("Xeng.png", TextureLoader.loadTexture(new File("textures/hagrid/Xeng.png")));
        System.out.println("Loading normal maps");
        normalmaps.put("SB046.JPG", TextureLoader.loadTexture(new File("textures/hagrid/SB046_normal.JPG")));
        normalmaps.put("Alumox.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Alumox_normal.jpg")));
        normalmaps.put("ban.png", TextureLoader.loadTexture(new File("textures/hagrid/ban_normal.png")));
        normalmaps.put("Bellows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Bellows_normal.jpg")));
        normalmaps.put("BepLua.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BepLua_normal.jpg")));
        normalmaps.put("BigHut.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BigHut_normal.jpg")));
        normalmaps.put("BigWindows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/BigWindows_normal.jpg")));
        normalmaps.put("BiNgo.png", TextureLoader.loadTexture(new File("textures/hagrid/BiNgo_normal.png")));
        normalmaps.put("CaiAm.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiAm_normal.jpg")));
        normalmaps.put("CaiChoc.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiChoc_normal.jpg")));
        normalmaps.put("CaiQue.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiQue_normal.jpg")));
        normalmaps.put("CaiVac.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiVac_normal.jpg")));
        normalmaps.put("CaiXaBeng.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiXaBeng_normal.jpg")));
        normalmaps.put("CaiXoong.jpg", TextureLoader.loadTexture(new File("textures/hagrid/CaiXoong_normal.jpg")));
        normalmaps.put("Da.png", TextureLoader.loadTexture(new File("textures/hagrid/Da_normal.png")));
        normalmaps.put("DayThung.jpg", TextureLoader.loadTexture(new File("textures/hagrid/DayThung_normal.jpg")));
        normalmaps.put("Den.png", TextureLoader.loadTexture(new File("textures/hagrid/Den_normal.png")));
        normalmaps.put("Door2.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Door2_normal.jpg")));
        normalmaps.put("Door.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Door_normal.jpg")));
        normalmaps.put("DSC01897.jpg", TextureLoader.loadTexture(new File("textures/hagrid/DSC01897_normal.jpg")));
        normalmaps.put("ghe.png", TextureLoader.loadTexture(new File("textures/hagrid/ghe_normal.png")));
        normalmaps.put("GioLan.png", TextureLoader.loadTexture(new File("textures/hagrid/GioLan_normal.png")));
        normalmaps.put("GioMay.png", TextureLoader.loadTexture(new File("textures/hagrid/GioMay_normal.png")));
        normalmaps.put("Ground.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Ground_normal.jpg")));
        normalmaps.put("hop_do.jpg", TextureLoader.loadTexture(new File("textures/hagrid/hop_do_normal.jpg")));
        normalmaps.put("HopGo.jpg", TextureLoader.loadTexture(new File("textures/hagrid/HopGo_normal.jpg")));
        normalmaps.put("Hop_nhieu_ngan.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Hop_nhieu_ngan_normal.jpg")));
        normalmaps.put("indt52L.jpg", TextureLoader.loadTexture(new File("textures/hagrid/indt52L_normal.jpg")));
        normalmaps.put("Long_dung_nam.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Long_dung_nam_normal.jpg")));
        normalmaps.put("Lon.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Lon_normal.jpg")));
        normalmaps.put("Muoi.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Muoi_normal.jpg")));
        normalmaps.put("RockSmooth0031_1_M.jpg", TextureLoader.loadTexture(new File("textures/hagrid/RockSmooth0031_1_M_normal.jpg")));
        normalmaps.put("rockwall.jpg", TextureLoader.loadTexture(new File("textures/hagrid/rockwall_normal.jpg")));
        normalmaps.put("SB046.JPG", TextureLoader.loadTexture(new File("textures/hagrid/SB046_normal.JPG")));
        normalmaps.put("SmallHut.jpg", TextureLoader.loadTexture(new File("textures/hagrid/SmallHut_normal.jpg")));
        normalmaps.put("SmallWindows.jpg", TextureLoader.loadTexture(new File("textures/hagrid/SmallWindows_normal.jpg")));
        normalmaps.put("So_pha.jpg", TextureLoader.loadTexture(new File("textures/hagrid/So_pha_normal.jpg")));
        normalmaps.put("Tham.png", TextureLoader.loadTexture(new File("textures/hagrid/Tham_normal.png")));
        normalmaps.put("ThungGoTron.png", TextureLoader.loadTexture(new File("textures/hagrid/ThungGoTron_normal.png")));
        normalmaps.put("ThungGoVuong.png", TextureLoader.loadTexture(new File("textures/hagrid/ThungGoVuong_normal.png")));
        normalmaps.put("Trung_Nen.png", TextureLoader.loadTexture(new File("textures/hagrid/Trung_Nen_normal.png")));
        normalmaps.put("Vali.jpg", TextureLoader.loadTexture(new File("textures/hagrid/Vali_normal.jpg")));
        normalmaps.put("XeKutKit.png", TextureLoader.loadTexture(new File("textures/hagrid/XeKutKit_normal.png")));
        normalmaps.put("Xeng.png", TextureLoader.loadTexture(new File("textures/hagrid/Xeng_normal.png")));


        for(Model model : ModelLoader.loadModels(mainProgram, new File("models/hagrid_named.obj"))){

            try {
                String pattern = "\\.[a-z]{3}(.*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(model.name);
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), ""));
                }
                m.appendTail(sb); // append the rest of the contents
                System.out.println(sb.toString());
                model.setTexture(textures.get(sb.toString()));
                model.setNormalMap(normalmaps.get(sb.toString()));



                model.setLit(false);
                GameObject gc = new StaticGameObject(model);
                gc.transform.scale = new Vector3f(0.04f,0.04f,0.04f);
                if(model.getTexture() != null)
                    addGameObject(gc);

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
        if(InputHandler.keyDown(GLFW.GLFW_KEY_F1)){
            RenderSettings.setSetting(RenderSettings.RenderSetting.LUT, !InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT));
        }
        if(InputHandler.keyDown(GLFW.GLFW_KEY_F2)){
            skybox.setTexture(brightSky);
            RenderSettings.setLut(RenderSettings.LUT.SUNNY);
        }
        if(InputHandler.keyDown(GLFW.GLFW_KEY_F3)){
            skybox.setTexture(darkSky);
            RenderSettings.setLut(RenderSettings.LUT.DARK);
        }
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
