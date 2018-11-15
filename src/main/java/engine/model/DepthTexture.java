package engine.model;

import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

public class DepthTexture extends Texture{
    public DepthTexture(int width, int height) {
        super(width, height);
        setupTexture();
    }

    @Override
    protected void setupTexture(){
        this.id = generateTexture();
        bindTexure();
        setParameter(GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_BORDER);
        setParameter(GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_BORDER);
        setParameter(GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        setParameter(GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_GENERATE_MIPMAP,GL20.GL_TRUE);
        GL20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, width, height, 0, GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_BYTE, (ByteBuffer) null);
    }
}
