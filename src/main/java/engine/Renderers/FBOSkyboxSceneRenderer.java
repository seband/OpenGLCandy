package engine.Renderers;

import engine.Camera;
import engine.GameObject;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class FBOSkyboxSceneRenderer extends FBOSceneRenderer{

    public FBOSkyboxSceneRenderer(int program, int width, int height) {
        super(program, width, height);
    }


    public void drawWithSkyBox(GameObject skyBox, List<GameObject> gc, Camera camera){
        init(camera);
        beforeDraw();
        renderSkybox(skyBox, camera);
        gc.forEach(o -> render(o, camera));
    }

    private void renderSkybox(GameObject gc, Camera camera){
        glDisable(GL_DEPTH_TEST);
        gc.draw(program, camera);
        glEnable(GL_DEPTH_TEST);
    }
}
