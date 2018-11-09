package engine.model;

import engine.Camera;
import engine.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Model {
    Transform transform = new Transform();
    int VAO, VBO_VERTEX,VBO_INDEX, VBO_NORMAL;
    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector3f> normals, ArrayList<Face> faces){
        this.vertices = vertices;
        this.normals = normals;
        this.faces = faces;
    }

    public Model(){}

    public void draw(int program, Camera camera, Transform transform) {
        GL20.glUseProgram(program);
        GL30.glBindVertexArray(VAO);

        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_INDEX);
        Matrix4f modelView = new Matrix4f().translate(transform.position).mul(new Matrix4f().scale(transform.scale).mul(transform.rot));
        utils.BufferUtils.setUniform(program,"modelView", modelView);
        utils.BufferUtils.setUniform(program,"projection", camera.getProjectionMatrix());
        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, faces.size()*3, GL11.GL_UNSIGNED_INT, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);
    }


    public void GenerateBuffers(int program) {
        FloatBuffer verticesBuffer = utils.BufferUtils.getFloatBuffer(vertices);
        FloatBuffer normalBuffer = utils.BufferUtils.getFloatBuffer(normals);
        IntBuffer  indicesBuffer = utils.BufferUtils.getIndicesBuffer(faces);

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);
        VBO_VERTEX = utils.BufferUtils.create_VBO(program, "in_Position", verticesBuffer);
        if(normals.size()>0)
            VBO_NORMAL = utils.BufferUtils.create_VBO(program, "in_Normal", normalBuffer);
        VBO_INDEX = utils.BufferUtils.create_VBO(program, indicesBuffer);

        GL30.glBindVertexArray(0);

    }
}
