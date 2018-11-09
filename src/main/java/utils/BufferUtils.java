package utils;

import engine.model.Face;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


public class BufferUtils {
    /**
     * Generate floatbuffer object from vector list
     * @param vectors
     * @return buffer for upload
     */
    public static FloatBuffer getFloatBuffer(ArrayList<Vector3f> vectors){
        FloatBuffer nb = org.lwjgl.BufferUtils.createFloatBuffer(vectors.size()*3);
        float[] nba = new float[vectors.size()*3];
        for(int i=0; i< vectors.size(); i++) {
            nba[i*3] = vectors.get(i).x;
            nba[i*3+1] = vectors.get(i).y;
            nba[i*3+2] = vectors.get(i).z;
        }
        nb.put(nba);
        nb.flip();
        return nb;
    }

    /**
     * Generate intbuffer object from faces list
     * @param faces
     * @return buffer for upload
     */
    public static IntBuffer getIndicesBuffer(ArrayList<Face> faces) {
        IntBuffer indicesBuffer = org.lwjgl.BufferUtils.createIntBuffer(faces.size()*3);
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

    /**
     * Set uniform in shader program
     * @param program active program
     * @param name name for location
     * @param value value of uniform
     */
    public static void setUniform(int program, String name, Matrix4f value) {
        int uniformLocation = GL20.glGetUniformLocation(program, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            GL20.glUniformMatrix4fv(uniformLocation, false, fb);
        }
    }

    /**
     * Create VBO with provided data
     * @param program active shader program
     * @param name name for location
     * @param buffer data to upload
     * @return VBO identifier
     */
    public static int create_VBO(int program, String name, FloatBuffer buffer){
        int VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(GL20.glGetAttribLocation(program, name), 3, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(GL20.glGetAttribLocation(program, name));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return VBO;
    }


    /**
     * Create VBO with provided data
     * @param program active shader program
     * @param buffer data to upload
     * @return VBO identifier
     */
    public static int create_VBO(int program, IntBuffer buffer){
        int VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        return VBO;
    }

}
