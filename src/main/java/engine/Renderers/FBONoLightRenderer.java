package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.model.Texture;
import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class FBONoLightRenderer extends FBOSceneRenderer{

    public FBONoLightRenderer(int program, int width, int height){
        super(program, width, height);
        tex = new Texture(width, height);
        this.FBO = BufferUtils.create_FBO(tex);
    }

    @Override
    protected void beforeDraw(){
        BufferUtils.bindFBO(FBO);
        glClearColor(0.0f, 0.0f, 0.0f, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
    }

}
