package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.model.Texture;
import org.lwjgl.opengl.GL20;
import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class FBOSceneRenderer extends FBORenderer{

    public FBOSceneRenderer(int program, int width, int height){
        super(program, width, height);
        tex = new Texture(width, height);
        this.FBO = BufferUtils.create_FBO(tex);
    }

    @Override
    protected void beforeDraw(){
        BufferUtils.bindFBO(FBO);
        glClearColor(0.5f, 0.5f, 1.0f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
    }

    @Override
    protected void render(GameObject gc, Camera camera) {
        gc.draw(program, camera);
    }
}
