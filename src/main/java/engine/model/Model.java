package engine.model;

import engine.Camera;
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

   protected float[] vertices;
    private float[] normals;
    protected float[] textureCoords;
    private float[] tangents;
    private float[] bitangents;
   protected int[] indicies;


    private Texture texture, normalMap, fxMap;
    private DepthTexture depthTexture;
    private Material material = new Material(1,1,1,1, true);

    public Model(float[] vertices, float[] normals, float[] textureCoords, float[] tangents, float[] bitangents, int[] indicies){
        this.vertices = vertices;
        this.normals = normals;
        this.textureCoords = textureCoords;
        this.indicies = indicies;
        this.tangents = tangents;
        this.bitangents = bitangents;
    }

    public Model(){

    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    public void setNormalMap(Texture texture) {
        this.normalMap = texture;
    }
    public void setLit(boolean lit){
        this.material.lit = lit;
    }

    public void setFxMap(Texture fxMap) {
        this.fxMap = fxMap;
    }

    public boolean getLit(){
        return this.material.lit;
    }
    public void setDepthTexture(DepthTexture depthTexture) {
        this.depthTexture = depthTexture;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getTextureId(){
        return texture.getId();
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void draw(int program, Camera camera, Transform transform) {
        GL20.glUseProgram(program);
        GL30.glBindVertexArray(VAO);

        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_INDEX);
        Matrix4f modelView = new Matrix4f().translate(transform.position).mul(new Matrix4f().scale(transform.scale).mul(transform.rot));
        utils.BufferUtils.setUniform(program,"modelView", modelView);
        utils.BufferUtils.setUniform(program,"shadowMatrix", Sun.sun.shadowMatrix);
        utils.BufferUtils.setUniform(program,"cameraMatrix", camera.getViewMatrix());
        utils.BufferUtils.setUniform(program,"projection", camera.getProjectionMatrix());
        utils.BufferUtils.setUniform(program, "isLit", material.lit);
        utils.BufferUtils.setUniform(program, "lightPosition", Sun.sun.gc.transform.position);
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

        GL20.glUseProgram(0);
    }


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
