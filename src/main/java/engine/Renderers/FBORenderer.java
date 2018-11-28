package engine.Renderers;

import engine.model.Texture;

public abstract class FBORenderer extends Renderer{
    Texture tex;
    protected int FBO;
    int width, height;
    public FBORenderer(int program, int width, int height){
        super(program);
        this.width = width;
        this.height = height;
    }

    public Texture getTexture(){
        return tex;
    }
}
