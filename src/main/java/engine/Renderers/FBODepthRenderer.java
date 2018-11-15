package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.model.DepthTexture;
import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class FBODepthRenderer extends FBORenderer{
    public FBODepthRenderer(int program, int width, int height){
        super(program, width, height);
        tex = new DepthTexture(width, height);
        this.FBO = BufferUtils.create_depth_FBO((DepthTexture) tex);
    }

    @Override
    public void draw(GameObject gc, Camera camera) {
        BufferUtils.bindFBO(FBO);
        glClearColor(0.0f, 0.0f, 0.0f, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        gc.draw(program, camera);
    }

    public DepthTexture getDtex() {
        return (DepthTexture) tex;
    }
}
