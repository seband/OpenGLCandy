package engine.model;

import engine.Camera;
import engine.Transform;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import utils.OBJLoader;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Model {
    Transform transform = new Transform();
    int VAO, VBO;
    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector3f> normals, ArrayList<Face> faces){
        this.vertices = vertices;
        this.normals = normals;
        this.faces = faces;
    }
    public Model(){

    }

    public void draw(Camera camera, Transform transform) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL30.glBindVertexArray(VAO);
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO);
        GL11.glDrawElements(GL11.GL_TRIANGLES, faces.size()*3, GL11.GL_UNSIGNED_BYTE, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public FloatBuffer getVerticesBuffer(){
        FloatBuffer vb = BufferUtils.createFloatBuffer(vertices.size()*3);
        float[] vba = new float[vertices.size()*3];
        for(int i=0; i< vertices.size(); i++) {
            vba[i] = vertices.get(i).x;
            vba[i+1] = vertices.get(i).y;
            vba[i+2] = vertices.get(i).z;
        }
        vb.put(vba);
        vb.flip();
        return vb;
    }

    public FloatBuffer getIndicesBuffer(){
        FloatBuffer vb = BufferUtils.createFloatBuffer(faces.size()*3);
        float[] vba = new float[faces.size()*3];
        for(int i=0; i< vertices.size(); i++) {
            vba[i] = faces.get(i).vertices.x;
            vba[i+1] = faces.get(i).vertices.y;
            vba[i+2] = faces.get(i).vertices.z;
        }
        vb.put(vba);
        vb.flip();
        return vb;
    }

    public void GenerateBuffers() {
        //Create float buffers
        FloatBuffer verticesBuffer = getVerticesBuffer();
        FloatBuffer indicesBuffer = getIndicesBuffer();

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);
        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        /*//Generate and bind VAO & VBO
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);

        //Upload data to VBO and unbind
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);


        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
*/

    }
}
