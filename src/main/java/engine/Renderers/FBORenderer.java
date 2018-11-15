package engine.Renderers;

import engine.model.Texture;

import javax.xml.soap.Text;

public abstract class FBORenderer extends Renderer{
    Texture tex;
    protected int FBO;
    public FBORenderer(int program, int width, int height){
        super(program);
        tex = new Texture(width, height);
    }

    public Texture getTexture(){
        return tex;
    }
}
