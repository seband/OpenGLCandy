package utils;

import engine.model.Texture;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
public class TextureLoader {
    /**
     * Texture loader based on the works by SilverTiger
     * Source: https://github.com/SilverTiger/lwjgl3-tutorial
     */
    public static class TextureLoadException extends Exception{}
    public static Texture loadTexture(File file) throws TextureLoadException {
        ByteBuffer buffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(false);
            buffer = stbi_load(file.getPath(), w, h, channels, 4);
            if (buffer == null)
                throw new TextureLoadException();

            width = w.get();
            height = h.get();

        }catch (Exception e){
            throw new TextureLoadException();
        }
        return new Texture(width, height, buffer);
    }
}
