package engine.Renderers;

import engine.model.Texture;

public abstract class FBORenderer extends Renderer{
    Texture tex;
    protected int FBO;
    public FBORenderer(int program, int width, int height){
        super(program);
    }

    public abstract void beforeDraw();
    public Texture getTexture(){
        return tex;
    }
}
