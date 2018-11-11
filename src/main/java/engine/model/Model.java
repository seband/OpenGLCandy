package engine.model;

import engine.Camera;
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
    int VAO, VBO_VERTEX,VBO_INDEX, VBO_NORMAL,VBO_TEXTURE;

   private float[] vertices, normals, textureCoords;
   private int[] indicies;


    private Texture texture;
    private Material material;


    public Model(float[] vertices, float[] normals, float[] textureCoords, int[] indicies){
        this.vertices = vertices;
        this.normals = normals;
        this.textureCoords = textureCoords;
        this.indicies = indicies;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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
        utils.BufferUtils.setUniform(program,"projection", camera.getProjectionMatrix());
        if(textureCoords.length>0 && texture != null){
            texture.bindTexure();
            texture.setLocation(program, "texUnit", 0);
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
        FloatBuffer normalBuffer = utils.BufferUtils.getFloatBuffer(normals);
        FloatBuffer textureCoordBuffer = utils.BufferUtils.getFloatBuffer(textureCoords);
        IntBuffer  indicesBuffer = utils.BufferUtils.getIntBuffer(indicies);

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);
        VBO_VERTEX = utils.BufferUtils.create_VBO(program, "in_Position", verticesBuffer,3);
        if(normals.length>0)
            VBO_NORMAL = utils.BufferUtils.create_VBO(program, "in_Normal", normalBuffer,3);
        if(textureCoords.length>0)
            VBO_TEXTURE = utils.BufferUtils.create_VBO(program, "in_TexCoord", textureCoordBuffer,2);
        VBO_INDEX = utils.BufferUtils.create_VBO(program, indicesBuffer);

        GL30.glBindVertexArray(0);

    }
}
