package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import org.lwjgl.opengl.GL20;
import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class FBOSceneRenderer extends FBORenderer{

    public FBOSceneRenderer(int program, int width, int height){
        super(program, width, height);
        this.FBO = BufferUtils.create_FBO(tex);
    }

    @Override
    public void draw(GameObject gc, Camera camera) {
        BufferUtils.bindFBO(FBO);
        glClearColor(0.0f, 0.0f, 0.0f, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        gc.draw(program, camera);
    }
}
