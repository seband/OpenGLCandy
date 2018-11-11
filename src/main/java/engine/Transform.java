package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    public Vector3f position = new Vector3f();
    public Vector3f scale = new Vector3f(1,1,1);
    public Matrix4f rot = new Matrix4f().identity();
}
