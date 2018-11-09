package engine.model;

import engine.Camera;
import engine.Program;
import engine.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import utils.OBJLoader;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Model {
    Transform transform = new Transform();
    int VAO, VBO_VERTEX,VBO_INDEX, VBO_NORMAL, program;
    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector3f> normals, ArrayList<Face> faces){
        this.vertices = vertices;
        this.normals = normals;
        this.faces = faces;
        try {
            program = Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Model(){
        try {
            program = Program.createProgram("shaders/shader.vert", "shaders/shader.frag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);;

    public void draw(Camera camera, Transform transform) {

        GL20.glUseProgram(program);
        GL30.glBindVertexArray(VAO);

        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_INDEX);

        setUniform("rotation", transform.rot);
        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, faces.size()*3, GL11.GL_UNSIGNED_INT, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);
    }
    public void setUniform(String name, Matrix4f value) {
        int rotationLocation = GL20.glGetUniformLocation(program, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            GL20.glUniformMatrix4fv(rotationLocation, false, fb);
        }
    }

    public FloatBuffer getVerticesBuffer(){
        FloatBuffer vb = BufferUtils.createFloatBuffer(vertices.size()*3);
        float[] vba = new float[vertices.size()*3];
        for(int i=0; i< vertices.size(); i++) {
            vba[i*3] = vertices.get(i).x;
            vba[i*3+1] = vertices.get(i).y;
            vba[i*3+2] = vertices.get(i).z;
        }
        vb.put(vba);
        vb.flip();
        return vb;
    }

    public FloatBuffer getNormalsBuffer(){
        FloatBuffer nb = BufferUtils.createFloatBuffer(normals.size()*3);
        float[] nba = new float[normals.size()*3];
        for(int i=0; i< normals.size(); i++) {
            nba[i*3] = normals.get(i).x;
            nba[i*3+1] = normals.get(i).y;
            nba[i*3+2] = normals.get(i).z;
        }
        nb.put(nba);
        nb.flip();
        return nb;
    }

    public IntBuffer getIndicesBuffer() {
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(faces.size()*3);
        int[] ib = new int[faces.size()*3];
        for(int i=0; i< faces.size(); i++) {
            ib[i*3] = (faces.get(i).vertices.x);
            ib[i*3+1] = faces.get(i).vertices.y;
            ib[i*3+2] = faces.get(i).vertices.z;
        }
        indicesBuffer.put(ib);
        indicesBuffer.flip();
        return indicesBuffer;
    }

    public FloatBuffer getSimpleVerticesBuffer(){
        float[] vertices = {
                -0.5f, 0.5f, 0f,    // Left top         ID: 0
                -0.5f, -0.5f, 0f,   // Left bottom      ID: 1
                0.5f, -0.5f, 0f,    // Right bottom     ID: 2
                0.5f, 0.5f, 0f      // Right left       ID: 3
        };
        // Sending data to OpenGL requires the usage of (flipped) byte buffers
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        return  verticesBuffer;
    }

    public ByteBuffer getSimpleIndicesBuffer() {
        byte[] indices = {
                // Left bottom triangle
                0, 1, 2,
                // Right top triangle
                2, 3, 0
        };
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        return indicesBuffer;
    }

    public void GenerateBuffers() {
        FloatBuffer verticesBuffer = getVerticesBuffer();
        FloatBuffer normalBuffer = getNormalsBuffer();
        IntBuffer  indicesBuffer = getIndicesBuffer();


        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        VBO_VERTEX = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_VERTEX);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(GL20.glGetAttribLocation(program, "in_Position"), 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // VBO for normal data
        /*glBindBuffer(GL_ARRAY_BUFFER, bunnyNormalBufferObjID);
        glBufferData(GL_ARRAY_BUFFER, m->numVertices*3*sizeof(GLfloat), m->normalArray, GL_STATIC_DRAW);
        glVertexAttribPointer(glGetAttribLocation(program, "in_Normal"), 3, GL_FLOAT, GL_FALSE, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(program, "in_Normal"));
        */

        VBO_NORMAL = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_NORMAL);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(GL20.glGetAttribLocation(program, "in_Normal"), 3, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(GL20.glGetAttribLocation(program, "in_Normal"));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        VBO_INDEX = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_INDEX);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
