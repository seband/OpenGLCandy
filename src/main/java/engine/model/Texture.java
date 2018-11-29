package engine.model;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.w3c.dom.Text;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class Texture {
    protected int width;
    protected int height;
    protected int id;

    /**
     * Texture
     * @param width of texture
     * @param height of texture
     * @param data to upload
     */
    public Texture(int width, int height, ByteBuffer data){
        this.width = width;
        this.height = height;
        setupTexture(data);
    }

    /**
     * Texture
     * @param width of texture
     * @param height of texture
     */
    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        setupTexture();
    }

    /**
     * Get current height
     * @return current height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get current weight
     * @return current weight
     */
    public int getWidth() {
        return width;
    }

    /**
     * Generate a new texture
     * @return the new texture's id
     */
    int generateTexture(){
        return GL20.glGenTextures();
    }

    /**
     * Set parameter for texture
     * @param parameter
     * @param value
     */
    void setParameter(int parameter, int value){
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, parameter, value);
    }

    /**
     * Create and uploads texture
     * @param data
     */
    private void setupTexture(ByteBuffer data){
        setupTexture();
        GL20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA8, width, height, 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, data);
    }

    /**
     * Setup the texture
     */
    protected void setupTexture(){
        this.id = generateTexture();
        bindTexure();
        setParameter(GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_BORDER);
        setParameter(GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_BORDER);
        setParameter(GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        setParameter(GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_GENERATE_MIPMAP,GL20.GL_TRUE);
        GL20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA8, width, height, 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, (ByteBuffer) null);
    }

    /**
     * Get texture ID
     * @return id of texture
     */
    public int getId() {
        return id;
    }

    /**
     * Set location in shader program
     * @param program shader program used
     * @param location to upload to
     * @param texUnit to use
     */
    public void setLocation(int program, String location, int texUnit){
        glActiveTexture(GL_TEXTURE0 + texUnit);
        bindTexure();
        glUniform1i(GL20.glGetUniformLocation(program, location), texUnit);
    }
    /**
     * Bind the texture
     */
    void bindTexure(){
        GL20.glBindTexture(GL20.GL_TEXTURE_2D, id);
    }

    /**
     * Delete texture
     */
    public void deleteTexture(){
        GL20.glDeleteTextures(id);
    }
}
