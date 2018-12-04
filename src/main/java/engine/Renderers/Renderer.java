package engine.Renderers;

import engine.Camera;
import engine.GameObject;
import engine.RenderSettings;
import engine.Sun;
import org.lwjgl.opengl.GL20;

import java.util.List;

public abstract class Renderer {
    protected int program, width, height;

    public Renderer(int program, int width, int height){
        this.width = width;
        this.height = height;
        this.program = program;
    }

    public void draw(List<GameObject> gc, Camera camera){
        init(camera);
        beforeDraw();
        gc.forEach(o ->{render(o, camera);});
        afterDraw();
    }
    protected abstract void render(GameObject gc, Camera camera);
    protected abstract void beforeDraw();
    protected void afterDraw(){
        GL20.glUseProgram(0);
    }
    protected void init(Camera camera){
        GL20.glUseProgram(program);
        utils.BufferUtils.setUniform(program,"shadowMatrix", Sun.sun.shadowMatrix);
        utils.BufferUtils.setUniform(program,"cameraMatrix", camera.getViewMatrix());
        utils.BufferUtils.setUniform(program,"projection", camera.getProjectionMatrix());
        utils.BufferUtils.setUniform(program,"renderLight", RenderSettings.getSetting(RenderSettings.RenderSetting.LIGHT));
        utils.BufferUtils.setUniform(program,"renderShadows", RenderSettings.getSetting(RenderSettings.RenderSetting.SHADOWS));
        utils.BufferUtils.setUniform(program,"renderSSAO", RenderSettings.getSetting(RenderSettings.RenderSetting.SSAO));
        utils.BufferUtils.setUniform(program,"renderShadows", RenderSettings.getSetting(RenderSettings.RenderSetting.SHADOWS));
        utils.BufferUtils.setUniform(program,"renderGodrays", RenderSettings.getSetting(RenderSettings.RenderSetting.GODRAYS));
        utils.BufferUtils.setUniform(program,"renderLut", RenderSettings.getSetting(RenderSettings.RenderSetting.LUT));
        utils.BufferUtils.setUniform(program, "lightPosition", Sun.sun.gc.transform.position);
        RenderSettings.getActiveLUT().setLocation(program, "LUT", 4);
    }
}
