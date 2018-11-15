package utils;

import engine.model.Face;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class BufferUtils {

    /**
     * Create FloatBuffer from array
     * @param array data
     * @return buffer
     */
    public static FloatBuffer getFloatBuffer(float[] array){
        FloatBuffer nb = org.lwjgl.BufferUtils.createFloatBuffer(array.length);
        nb.put(array);
        nb.flip();
        return nb;
    }

    /**
     * Create IntBuffer from array
     * @param array data
     * @return buffer
     */
    public static IntBuffer getIntBuffer(int[] array){
        IntBuffer nb = org.lwjgl.BufferUtils.createIntBuffer(array.length);
        nb.put(array);
        nb.flip();
        return nb;
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
    public static int create_VBO(int program, String name, FloatBuffer buffer, int length){
        int VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(GL20.glGetAttribLocation(program, name), length, GL11.GL_FLOAT, false, 0, 0);
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
