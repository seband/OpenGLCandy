package engine.Renderers;

import engine.Camera;
import engine.GameObject;

import java.util.List;

public abstract class Renderer {
    protected int program;

    public Renderer(int program){
        this.program = program;
    }

    public void draw(List<GameObject> gc, Camera camera){
        beforeDraw();
        gc.forEach(o ->{render(o, camera);});
        afterDraw();
    }
    protected abstract void render(GameObject gc, Camera camera);
    protected abstract void beforeDraw();
    protected void afterDraw(){}
}
