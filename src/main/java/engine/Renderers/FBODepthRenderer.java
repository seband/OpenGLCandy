package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.model.DepthTexture;
import org.lwjgl.opengl.GL20;
import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class FBODepthRenderer extends FBORenderer{

    
    public FBODepthRenderer(int program, int width, int height){
        super(program, width, height);
        tex = new DepthTexture(width, height);
        this.FBO = BufferUtils.create_depth_FBO((DepthTexture) tex);
    }

    @Override
    protected void beforeDraw() {
        BufferUtils.bindFBO(FBO);
        glClearColor(0.0f, 0.0f, 0.0f, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

    }

    @Override
    protected void render(GameObject gc, Camera camera) {
        gc.draw(program, camera);
    }

    public DepthTexture getDtex() {
        return (DepthTexture) tex;
    }
}
