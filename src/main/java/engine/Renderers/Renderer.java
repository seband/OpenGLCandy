package engine.Renderers;

import engine.Camera;
import engine.GameObject;

public abstract class Renderer {
    protected int program;

    public Renderer(int program){
        this.program = program;
    }

    public abstract void draw(GameObject gc, Camera camera);
}
