package engine.model;

import engine.Camera;
import engine.RenderSettings;
import engine.Sun;
import engine.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Model {
    int VAO, VBO_VERTEX,VBO_INDEX, VBO_NORMAL,VBO_TEXTURE, VBO_TANGENT, VBO_BITANGENT;
    public String name = "";
    float[] vertices;
    float[] textureCoords;
    int[] indicies;
    private float[] normals;
    private float[] tangents;
    private float[] bitangents;


    private Texture texture, normalMap, fxMap;
    private DepthTexture depthTexture;
    private Material material = new Material(1,1,1,1, true);

    /**
     * Acts as a wrapper for mesh data
     * @param vertices vertices describing the mesh
     * @param normals normal vectors for the mesh
     * @param textureCoords texture coordinates for the mesh
     * @param tangents for tangent-space manipulations
     * @param bitangents for tangent-space manipulations
     * @param indicies describes the order of vertices, normal and texture coordinates
     */
    public Model(float[] vertices, float[] normals, float[] textureCoords, float[] tangents, float[] bitangents, int[] indicies){
        this.vertices = vertices;
        this.normals = normals;
        this.textureCoords = textureCoords;
        this.indicies = indicies;
        this.tangents = tangents;
        this.bitangents = bitangents;
    }

    /**
     * Empty model
     */
    Model(){

    }

    /**
     * Sets diffuse map
     * @param texture containing diffuse map
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Sets normal map
     * @param texture containing normal map
     */
    public void setNormalMap(Texture texture) {
        this.normalMap = texture;
    }

    /**
     * Sets if the material reacts to light
     * @param lit
     */
    public void setLit(boolean lit){
        this.material.lit = lit;
    }

    /**
     * Set secondary map that can be used for shading effects
     * @param fxMap map containing fx-data
     */
    public void setFxMap(Texture fxMap) {
        this.fxMap = fxMap;
    }

    /**
     * Does the material react to light
     * @return boolean describing the light reaction
     */
    public boolean getLit(){
        return this.material.lit;
    }

    /**
     * Set depth texture that is used for e.g. shadow-mapping
     * @param depthTexture containing depth data
     */
    public void setDepthTexture(DepthTexture depthTexture) {
        this.depthTexture = depthTexture;
    }

    /**
     * Get diffuse texture
     * @return diffuse texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get identifier for diffuse texture
     * @return texture identifier
     */
    public int getTextureId(){
        return texture.getId();
    }

    /**
     * Set material for model
     * @param material new material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Get current material
     * @return current material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Draw the model on screen
     * @param program shader program to use
     * @param camera to visualize the model
     * @param transform position and rotation of parent game object
     */
    public void draw(int program, Camera camera, Transform transform) {

        GL30.glBindVertexArray(VAO);

        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_INDEX);
        Matrix4f modelView = new Matrix4f().translate(transform.position).mul(new Matrix4f().scale(transform.scale).mul(transform.rot));
        utils.BufferUtils.setUniform(program,"modelView", modelView);
        utils.BufferUtils.setUniform(program, "isLit", material.lit);

        if(textureCoords.length>0 && texture != null){
            texture.setLocation(program, "texUnit", 0);
        }
        if(textureCoords.length>0 && normalMap != null){
            normalMap.setLocation(program, "normalMap", 1);
        }
        if(depthTexture != null){
            depthTexture.setLocation(program, "depthMap", 2);
        }
        if(fxMap != null){
            fxMap.setLocation(program, "fxMap", 3);
        }
        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicies.length, GL11.GL_UNSIGNED_INT, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);


    }

    /**
     * Generate buffers based on provided data
     * @param program used for reference
     */
    public void GenerateBuffers(int program) {
        FloatBuffer verticesBuffer = utils.BufferUtils.getFloatBuffer(vertices);
        FloatBuffer normalBuffer = null;
        if(normals != null)
            normalBuffer = utils.BufferUtils.getFloatBuffer(normals);
        FloatBuffer tangentsBuffer = null;
        if(tangents != null)
            tangentsBuffer = utils.BufferUtils.getFloatBuffer(tangents);
        FloatBuffer bitangentsBuffer= null;
        if(bitangents != null)
            bitangentsBuffer = utils.BufferUtils.getFloatBuffer(bitangents);
        FloatBuffer textureCoordBuffer = utils.BufferUtils.getFloatBuffer(textureCoords);
        IntBuffer  indicesBuffer = utils.BufferUtils.getIntBuffer(indicies);

        //Setup VAO
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        //Setup VBOs
        VBO_VERTEX = utils.BufferUtils.create_VBO(program, "in_Position", verticesBuffer,3);
        if(tangentsBuffer != null)
            VBO_TANGENT = utils.BufferUtils.create_VBO(program, "in_Tangent", tangentsBuffer,3);
        if(bitangentsBuffer != null)
            VBO_BITANGENT = utils.BufferUtils.create_VBO(program, "in_Bitangent", bitangentsBuffer,3);
        if(normalBuffer != null)
            VBO_NORMAL = utils.BufferUtils.create_VBO(program, "in_Normal", normalBuffer,3);
        if(textureCoordBuffer != null)
            VBO_TEXTURE = utils.BufferUtils.create_VBO(program, "in_TexCoord", textureCoordBuffer,2);
        VBO_INDEX = utils.BufferUtils.create_VBO(program, indicesBuffer);

        GL30.glBindVertexArray(0);
    }


}
