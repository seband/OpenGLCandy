package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class InputHandler extends GLFWKeyCallback {
    private static boolean[] keys = new boolean[65536];
    private static Vector2f mousePos = new Vector2f();
    private static Vector2f mousePosDelta = new Vector2f();
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public static boolean keyDown(int key){
        return keys[key];
    }
    public static void setMousePos(float x, float y){
        Vector2f newPos = new Vector2f(x,y);
        mousePosDelta = newPos.sub(mousePos);
        mousePos.x = x;
        mousePos.y = y;
    }
    public static Vector2f getMousePos(){
        return mousePosDelta;
    }
}
