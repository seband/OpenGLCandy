package engine;

public abstract class ObjectScene extends AbstractScene {
    protected int mainProgram;

    /**
     * Scene with default object drawing
     * @param camera main camera
     */
    public ObjectScene(Camera camera) {
        super(camera);
        try {
            mainProgram = utils.Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
        }catch (Exception e){
            System.out.println("Program creation failed");
        }
        initScene();
    }

    protected abstract void initScene();

    public void draw(){
        for(GameObject go: this.gameObjectList)
            go.draw(mainProgram, this.camera);
    }

}
