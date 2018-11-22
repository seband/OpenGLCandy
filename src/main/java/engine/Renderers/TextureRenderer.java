package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.model.Texture;
import org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL30;

public class TextureRenderer extends Renderer{
    public TextureRenderer(int program){
        super(program);
    }

    @Override
    protected void beforeDraw() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glClearColor(0.9f, 0.9f, 1.0f, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void render(GameObject gc, Camera camera) {
        gc.draw(this.program, camera);
    }
}
